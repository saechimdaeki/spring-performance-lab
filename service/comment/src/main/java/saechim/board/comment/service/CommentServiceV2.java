package saechim.board.comment.service;

import static java.util.function.Predicate.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import saechim.board.comment.domain.ArticleCommentCount;
import saechim.board.comment.domain.CommentPath;
import saechim.board.comment.domain.CommentV2;
import saechim.board.comment.repository.ArticleCommentCountRepository;
import saechim.board.comment.repository.CommentRepositoryV2;
import saechim.board.comment.service.request.CommentCreateRequestV2;
import saechim.board.comment.service.response.CommentPageResponse;
import saechim.board.comment.service.response.CommentResponse;
import saechim.board.common.event.EventType;
import saechim.board.common.event.payload.CommentCreatedEventPayload;
import saechim.board.common.event.payload.CommentDeletedEventPayload;
import saechim.board.common.outboxmessagerelay.OutboxEventPublisher;
import saechim.board.common.snowflake.Snowflake;

@Service
@RequiredArgsConstructor
public class CommentServiceV2 {
	private final Snowflake snowflake = new Snowflake();
	private final CommentRepositoryV2 commentRepository;
	private final ArticleCommentCountRepository articleCommentCountRepository;
	private final OutboxEventPublisher outboxEventPublisher;

	@Transactional
	public CommentResponse create(CommentCreateRequestV2 request) {
		CommentV2 parent = findParent(request);
		CommentPath parentCommentPath = parent == null ? CommentPath.create("") : parent.getCommentPath();

		CommentV2 comment = commentRepository.save(
			CommentV2.create(
				snowflake.nextId(),
				request.getContent(),
				request.getArticleId(),
				request.getWriterId(),
				parentCommentPath.createChildCommentPath(
					commentRepository.findDescendantsTopPath(request.getArticleId(), parentCommentPath.getPath())
						.orElse(null)
				)
			)
		);

		int result = articleCommentCountRepository.increase(request.getArticleId());
		if (result == 0) {
			articleCommentCountRepository.save(
				ArticleCommentCount.init(request.getArticleId(), 1L)
			);
		}

		outboxEventPublisher.publish(
			EventType.COMMENT_CREATED,
			CommentCreatedEventPayload.builder()
				.commentId(comment.getCommentId())
				.content(comment.getContent())
				.articleId(comment.getArticleId())
				.writerId(comment.getWriterId())
				.deleted(comment.getDeleted())
				.createdAt(comment.getCreatedAt())
				.articleCommentCount(count(comment.getArticleId()))
				.build(),
			comment.getArticleId()
		);

		return CommentResponse.from(comment);
	}

	private CommentV2 findParent(CommentCreateRequestV2 request) {
		String parentPath = request.getParentPath();
		if (parentPath == null) {
			return null;
		}

		return commentRepository.findByPath(parentPath)
			.filter(not(CommentV2::getDeleted))
			.orElseThrow();
	}

	public CommentResponse read(Long commentId) {
		return CommentResponse.from(
			commentRepository.findById(commentId).orElseThrow()
		);
	}

	@Transactional
	public void delete(Long commentId) {
		commentRepository.findById(commentId)
			.filter(not(CommentV2::getDeleted))
			.ifPresent(comment -> {
				if (hasChildren(comment)) {
					comment.delete();
				} else {
					delete(comment);
				}
				outboxEventPublisher.publish(
					EventType.COMMENT_DELETED,
					CommentDeletedEventPayload.builder()
						.commentId(comment.getCommentId())
						.content(comment.getContent())
						.articleId(comment.getArticleId())
						.writerId(comment.getWriterId())
						.deleted(comment.getDeleted())
						.createdAt(comment.getCreatedAt())
						.articleCommentCount(count(comment.getArticleId()))
						.build(),
					comment.getArticleId()
				);
			});
	}

	private boolean hasChildren(CommentV2 comment) {
		return commentRepository.findDescendantsTopPath(
			comment.getArticleId(),
			comment.getCommentPath().getPath()
		).isPresent();
	}

	private void delete(final CommentV2 comment) {
		commentRepository.delete(comment);
		articleCommentCountRepository.decrease(comment.getArticleId());
		if (!comment.isRoot()) {
			commentRepository.findByPath(comment.getCommentPath().getParentPath())
				.filter(CommentV2::getDeleted)
				.filter(not(this::hasChildren))
				.ifPresent(this::delete);
		}
	}

	public CommentPageResponse readAll(Long articleId, Long page, Long pageSize) {
		return CommentPageResponse.of(
			commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize)
				.stream()
				.map(CommentResponse::from)
				.toList(),
			commentRepository.count(articleId, PageLimitCalculator.calculatePageLimit(page, pageSize, 10L))
		);
	}

	public List<CommentResponse> readAllInfiniteScroll(Long articleId, String lastPath, Long pageSize) {
		List<CommentV2> comments = lastPath == null ?
			commentRepository.findAllInfiniteScroll(articleId, pageSize) :
			commentRepository.findAllInfiniteScroll(articleId, lastPath, pageSize);

		return comments.stream()
			.map(CommentResponse::from)
			.toList();
	}

	public Long count(Long articleId) {
		return articleCommentCountRepository.findById(articleId)
			.map(ArticleCommentCount::getCommentCount)
			.orElse(0L);
	}
}
