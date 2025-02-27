package saechim.board.view.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_view_count")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleViewCount {

	@Id
	private Long articleId; // shard key

	private Long viewCount;

	public static ArticleViewCount init(Long articleId, Long viewCount) {
		ArticleViewCount articleViewCount = new ArticleViewCount();
		articleViewCount.articleId = articleId;
		articleViewCount.viewCount = viewCount;
		return articleViewCount;
	}
}
