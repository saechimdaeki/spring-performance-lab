package saechim.board.common.event.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saechim.board.common.event.EventPayload;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLikeEventPayload implements EventPayload {
	private Long articleLikeId;
	private Long articleId;
	private Long userId;
	private LocalDateTime createdAt;
	private Long articleLikeCount;
}
