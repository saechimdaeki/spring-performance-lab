package saechim.board.comment.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import saechim.board.comment.service.CommentService;
import saechim.board.comment.service.request.CommentCreateRequest;
import saechim.board.comment.service.response.CommentResponse;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping("/v1/comments/{commentId}")
	public CommentResponse read(@PathVariable final Long commentId) {
		return commentService.read(commentId);
	}

	@PostMapping("/v1/comments")
	public CommentResponse create(@RequestBody final CommentCreateRequest request) {
		return commentService.create(request);
	}

	@DeleteMapping("/v1/comments/{commentId}")
	public void delete(@PathVariable final Long commentId) {
		commentService.delete(commentId);
	}
}
