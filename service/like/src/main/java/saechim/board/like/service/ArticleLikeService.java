package saechim.board.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import saechim.board.common.snowflake.Snowflake;
import saechim.board.like.domain.ArticleLike;
import saechim.board.like.domain.ArticleLikeCount;
import saechim.board.like.repository.ArticleLikeCountRepository;
import saechim.board.like.repository.ArticleLikeRepository;
import saechim.board.like.service.response.ArticleLikeResponse;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

	private final Snowflake snowflake = new Snowflake();

	private final ArticleLikeRepository articleLikeRepository;
	private final ArticleLikeCountRepository articleLikeCountRepository;

	public ArticleLikeResponse read(Long articleLikeId, Long userId) {
		return articleLikeRepository.findByArticleIdAndUserId(articleLikeId, userId)
			.map(ArticleLikeResponse::from)
			.orElseThrow();
	}

	@Transactional
	public void likePessimisticLock1(Long articleId, Long userId) {
		articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);
		int result = articleLikeCountRepository.increase(articleId);
		if (result == 0) {
			// 최초 요청 시에는 update되는 레코드가 없으므로 1로추가
			// 트래픽 순식간 몰릴시 유실될 수 있음, 게시글 생성 시점에 따라 0으로 초기화할 수도 있음
			articleLikeCountRepository.save(
				ArticleLikeCount.init(articleId, 1L)
			);
		}
	}

	@Transactional
	public void unlikePessimisticLock1(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(
				articleLike -> {
					articleLikeRepository.delete(articleLike);
					articleLikeCountRepository.decrease(articleId);
				}
			);
	}

	// select ... for update + update
	@Transactional
	public void likePessimisticLock2(Long articleId, Long userId) {
		articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);
		ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId)
			.orElseGet(() -> ArticleLikeCount.init(articleId, 0L));

		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	}

	@Transactional
	public void unlikePessimisticLock2(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLike -> {
				articleLikeRepository.delete(articleLike);
				ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId)
					.orElseThrow();
				articleLikeCount.decrease();
			});
	}

	@Transactional
	public void likeOptimisticLock(Long articleId, Long userId) {
		articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);

		ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
			.orElseGet(() -> ArticleLikeCount.init(articleId, 0L));

		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	}

	@Transactional
	public void unlikeOptimisticLock(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLike -> {
				articleLikeRepository.delete(articleLike);
				ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId).orElseThrow();
				articleLikeCount.decrease();
			});
	}

	public Long count(Long articleId) {
		return articleLikeCountRepository.findById(articleId)
			.map(ArticleLikeCount::getLikeCount)
			.orElse(0L);
	}
}
