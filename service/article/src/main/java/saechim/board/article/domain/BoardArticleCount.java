package saechim.board.article.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "board_article_count")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardArticleCount {

	@Id
	private Long boardId; // shard key

	private Long articleCount;

	public static BoardArticleCount init(Long boardId, Long articleCount) {
		BoardArticleCount boardArticleCount = new BoardArticleCount();
		boardArticleCount.boardId = boardId;
		boardArticleCount.articleCount = articleCount;
		return boardArticleCount;
	}
}
