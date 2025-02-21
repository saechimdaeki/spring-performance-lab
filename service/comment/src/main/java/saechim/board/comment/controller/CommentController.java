package saechim.board.comment.controller;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import saechim.board.comment.service.CommentService;
import saechim.board.comment.service.request.CommentCreateRequest;
import saechim.board.comment.service.response.CommentPageResponse;
import saechim.board.comment.service.response.CommentResponse;

import java.util.List;

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

    @GetMapping("/v1/comments")
    public CommentPageResponse readAll(@RequestParam("articleId") Long articleId,
                                       @RequestParam("page") Long page,
                                       @RequestParam("pageSize") Long pageSize) {
        return commentService.readAll(articleId, page, pageSize);
    }

    @GetMapping("/v1/comments/infinite-scroll")
    public List<CommentResponse> readAll(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
            @RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
            @RequestParam("pageSize") Long pageSize
    ) {
        return commentService.readAll(articleId, lastParentCommentId, lastCommentId, pageSize);
    }
}
