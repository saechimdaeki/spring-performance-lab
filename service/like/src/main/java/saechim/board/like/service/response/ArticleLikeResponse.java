package saechim.board.like.service.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;
import saechim.board.like.domain.ArticleLike;

@Getter
@ToString
public class ArticleLikeResponse {
	private Long articleLikeId;
	private Long articleId;
	private Long userId;
	private LocalDateTime createdAt;

	public static ArticleLikeResponse from(ArticleLike articleLike) {
		ArticleLikeResponse response = new ArticleLikeResponse();
		response.articleId = articleLike.getArticleId();
		response.articleLikeId = articleLike.getArticleLikeId();
		response.createdAt = articleLike.getCreatedAt();
		response.userId = articleLike.getUserId();
		return response;
	}
}
