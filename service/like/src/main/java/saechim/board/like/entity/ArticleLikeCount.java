package saechim.board.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "article_like_count")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLikeCount {

	@Id
	private Long articleId; // shard key

	private Long likeCount;

	@Version
	private Long version;

	public static ArticleLikeCount init(Long articleLikeId, Long articleId) {
		ArticleLikeCount articleLikeCount = new ArticleLikeCount();
		articleLikeCount.articleId = articleId;
		articleLikeCount.likeCount = articleLikeId;
		articleLikeCount.version = 0L;
		return articleLikeCount;
	}

	public void increase() {
		this.likeCount++;
	}

	public void decrease() {
		this.likeCount--;
	}
}
