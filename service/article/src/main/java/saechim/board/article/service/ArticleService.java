package saechim.board.article.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import saechim.board.article.domain.Article;
import saechim.board.article.domain.BoardArticleCount;
import saechim.board.article.repository.ArticleRepository;
import saechim.board.article.repository.BoardArticleCountRepository;
import saechim.board.article.service.request.ArticleCreateRequest;
import saechim.board.article.service.request.ArticleUpdateRequest;
import saechim.board.article.service.response.ArticlePageResponse;
import saechim.board.article.service.response.ArticleResponse;
import saechim.board.common.snowflake.Snowflake;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
	private final Snowflake snowflake = new Snowflake();
	private final ArticleRepository articleRepository;
	private final BoardArticleCountRepository boardArticleCountRepository;

	@Transactional
	public ArticleResponse create(final ArticleCreateRequest request) {
		final Article article = articleRepository.save(
			Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(),
				request.getWriterId())
		);
		int result = boardArticleCountRepository.increase(request.getBoardId());
		if (result == 0) {
			boardArticleCountRepository.save(
				BoardArticleCount.init(request.getBoardId(), 1L)
			);
		}

		return ArticleResponse.from(article);
	}

	@Transactional
	public ArticleResponse update(final Long articleId, final ArticleUpdateRequest request) {
		final Article article = articleRepository.findById(articleId).orElseThrow();
		article.update(request.getTitle(), request.getContent());
		return ArticleResponse.from(article);
	}

	public ArticleResponse read(final Long articleId) {
		return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
	}

	@Transactional
	public void delete(final Long articleId) {
		Article article = articleRepository.findById(articleId).orElseThrow();
		articleRepository.delete(article);
		boardArticleCountRepository.decrease(article.getBoardId());
	}

	public ArticlePageResponse readAll(final Long boardId, final Long page, final Long pageSize) {
		return ArticlePageResponse.of(
			articleRepository.findAll(boardId, (page - 1) * pageSize, pageSize).stream()
				.map(ArticleResponse::from)
				.toList(),
			articleRepository.count(
				boardId,
				PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
			)
		);
	}

	public List<ArticleResponse> readAllInfiniteScroll(final Long boardId, final Long pageSize,
		final Long lastArticleId) {
		final List<Article> articles = lastArticleId == null ?
			articleRepository.findAllInfiniteScroll(boardId, pageSize) :
			articleRepository.findAllInfiniteScroll(boardId, pageSize, lastArticleId);
		return articles.stream().map(ArticleResponse::from).toList();
	}

	public Long count(Long boardId) {
		return boardArticleCountRepository.findById(boardId)
			.map(BoardArticleCount::getArticleCount)
			.orElse(0L);
	}
}
