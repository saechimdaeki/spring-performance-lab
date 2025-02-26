package saechim.board.like.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import saechim.board.like.service.ArticleLikeService;
import saechim.board.like.service.response.ArticleLikeResponse;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {
	private final ArticleLikeService articleLikeService;

	@GetMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
	public ArticleLikeResponse read(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		return articleLikeService.read(articleId, userId);
	}

	@PostMapping("/v1/articles-likes/articles/{articleId}/users/{userId}")
	public void like(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId
	) {
		articleLikeService.like(articleId, userId);
	}

	@DeleteMapping("/v1/articles-likes/articles/{articleId}/users/{userId}")
	public void unlike(
		@PathVariable("articleId") Long articleId,
		@PathVariable("userId") Long userId) {
		articleLikeService.unlike(articleId, userId);
	}
}
