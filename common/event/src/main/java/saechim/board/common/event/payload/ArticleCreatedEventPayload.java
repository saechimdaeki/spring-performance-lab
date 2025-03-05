package saechim.board.common.event.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saechim.board.common.event.EventPayload;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleCreatedEventPayload implements EventPayload {
	private Long articleId;
	private String title;
	private String content;
	private Long boardId;
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long boardArticleCount;
}
