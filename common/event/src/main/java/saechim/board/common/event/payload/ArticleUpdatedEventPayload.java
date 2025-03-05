package saechim.board.common.event.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saechim.board.common.event.EventPayload;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ArticleUpdatedEventPayload implements EventPayload {
	private Long articleId;
	private String title;
	private String content;
	private Long boardId;
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
