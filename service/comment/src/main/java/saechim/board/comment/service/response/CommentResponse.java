package saechim.board.comment.service.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;
import saechim.board.comment.domain.Comment;
import saechim.board.comment.domain.CommentV2;

@Getter
@ToString
public class CommentResponse {
	private Long commentId;
	private String content;
	private Long parentCommentId;
	private Long articleId;
	private Long writerId;
	private Boolean deleted;
	private String path;
	private LocalDateTime createdAt;

	public static CommentResponse from(Comment comment) {
		CommentResponse response = new CommentResponse();
		response.commentId = comment.getCommentId();
		response.content = comment.getContent();
		response.parentCommentId = comment.getParentCommentId();
		response.articleId = comment.getArticleId();
		response.writerId = comment.getWriterId();
		response.deleted = comment.getDeleted();
		response.createdAt = comment.getCreatedAt();

		return response;
	}

	public static CommentResponse from(CommentV2 comment) {
		CommentResponse response = new CommentResponse();
		response.commentId = comment.getCommentId();
		response.content = comment.getContent();
		response.path = comment.getCommentPath().getPath();
		response.articleId = comment.getArticleId();
		response.writerId = comment.getWriterId();
		response.deleted = comment.getDeleted();
		response.createdAt = comment.getCreatedAt();

		return response;
	}
}
