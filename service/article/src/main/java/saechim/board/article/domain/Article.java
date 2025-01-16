package saechim.board.article.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "article")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
	@Id
	private Long articleId;
	private String title;
	private String content;
	private Long boardId; // shard key
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static Article create(final Long articleId, final String title, final String content, final Long boardId,
		final Long writerId) {
		final Article article = new Article();
		article.articleId = articleId;
		article.title = title;
		article.content = content;
		article.boardId = boardId;
		article.writerId = writerId;
		article.createdAt = LocalDateTime.now();
		article.modifiedAt = LocalDateTime.now();
		return article;
	}

	public void update(final String title, final String content) {
		this.title = title;
		this.content = content;
		this.modifiedAt = LocalDateTime.now();
	}
}
