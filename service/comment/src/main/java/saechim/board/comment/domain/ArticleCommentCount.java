package saechim.board.comment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_comment_count")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCommentCount {

	@Id
	private Long articleId; //shard key

	private Long commentCount;

	public static ArticleCommentCount init(Long articleId, Long commentCount) {
		ArticleCommentCount articleCommentCount = new ArticleCommentCount();
		articleCommentCount.articleId = articleId;
		articleCommentCount.commentCount = commentCount;
		return articleCommentCount;
	}
}
