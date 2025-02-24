package saechim.board.comment.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import saechim.board.comment.service.CommentServiceV2;
import saechim.board.comment.service.request.CommentCreateRequestV2;
import saechim.board.comment.service.response.CommentResponse;

@RestController
@RequiredArgsConstructor
public class CommentControllerV2 {

	private final CommentServiceV2 commentService;

	@GetMapping("/v2/comments/{commentId}")
	public CommentResponse read(@PathVariable final Long commentId) {
		return commentService.read(commentId);
	}

	@PostMapping("/v2/comments")
	public CommentResponse create(@RequestBody final CommentCreateRequestV2 request) {
		return commentService.create(request);
	}

	@DeleteMapping("/v2/comments/{commentId}")
	public void delete(@PathVariable final Long commentId) {
		commentService.delete(commentId);
	}

}
