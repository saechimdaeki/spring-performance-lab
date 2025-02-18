package saechim.board.comment.service.request;

import lombok.Getter;

@Getter
public class CommentCreateRequest {
	private Long articleId;
	private String content;
	private Long parentCommentId;
	private Long writerId;
}
