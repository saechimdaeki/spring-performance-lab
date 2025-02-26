package saechim.board.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import saechim.board.common.snowflake.Snowflake;
import saechim.board.like.entity.ArticleLike;
import saechim.board.like.repository.ArticleLikeRepository;
import saechim.board.like.service.response.ArticleLikeResponse;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

	private final Snowflake snowflake = new Snowflake();

	private final ArticleLikeRepository articleLikeRepository;

	public ArticleLikeResponse read(Long articleLikeId, Long userId) {
		return articleLikeRepository.findByArticleIdAndUserId(articleLikeId, userId)
			.map(ArticleLikeResponse::from)
			.orElseThrow();
	}

	@Transactional
	public void like(Long articleId, Long userId) {
		articleLikeRepository.save(
			ArticleLike.create(
				snowflake.nextId(),
				articleId,
				userId
			)
		);
	}

	@Transactional
	public void unlike(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLikeRepository::delete);
	}
}
