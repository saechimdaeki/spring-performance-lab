package saechim.board.comment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import saechim.board.comment.service.CommentServiceV2;
import saechim.board.comment.service.request.CommentCreateRequestV2;
import saechim.board.comment.service.response.CommentPageResponse;
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

	@GetMapping("/v2/comments")
	public CommentPageResponse readAll(@RequestParam Long articleId,
		@RequestParam("page") Long page,
		@RequestParam("pageSize") Long pageSize) {
		return commentService.readAll(articleId, page, pageSize);
	}

	@GetMapping("/v2/comments/infinite-scroll")
	public List<CommentResponse> readAllInfiniteScroll(
		@RequestParam("articleId") Long articleId,
		@RequestParam(value = "lastPath", required = false) String lastPath,
		@RequestParam("pageSize") Long pageSize
	) {
		return commentService.readAllInfiniteScroll(articleId, lastPath, pageSize);
	}

	@GetMapping("/v2/comments/articles/{articleId}/count")
	public Long count(@PathVariable final Long articleId) {
		return commentService.count(articleId);
	}
}


