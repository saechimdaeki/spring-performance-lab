package saechim.board.like.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "article_like")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleLike {

	@Id
	private Long articleLikeId;

	private Long articleId; // shard key

	private Long userId;
	private LocalDateTime createdAt;

	public static ArticleLike create(Long articleLikeId, Long articleId, Long userId) {
		ArticleLike articleLike = new ArticleLike();
		articleLike.articleLikeId = articleLikeId;
		articleLike.articleId = articleId;
		articleLike.userId = userId;
		articleLike.createdAt = LocalDateTime.now();
		return articleLike;
	}
}
