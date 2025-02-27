package saechim.board.article.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import saechim.board.article.service.ArticleService;
import saechim.board.article.service.request.ArticleCreateRequest;
import saechim.board.article.service.request.ArticleUpdateRequest;
import saechim.board.article.service.response.ArticlePageResponse;
import saechim.board.article.service.response.ArticleResponse;

@RestController
@RequiredArgsConstructor
public class ArticleController {
	private final ArticleService articleService;

	@GetMapping("/v1/articles/{articleId}")
	public ArticleResponse read(@PathVariable final Long articleId) {
		return articleService.read(articleId);
	}

	@GetMapping("/v1/articles")
	public ArticlePageResponse readAll(
		@RequestParam("boardId") final Long boardId,
		@RequestParam("page") final Long page,
		@RequestParam("pageSize") final Long pageSize
	) {
		return articleService.readAll(boardId, page, pageSize);
	}

	@GetMapping("/v1/articles/infinite-scroll")
	public List<ArticleResponse> readAllInfiniteScroll(
		@RequestParam("boardId") Long boardId,
		@RequestParam("pageSize") Long pageSize,
		@RequestParam(value = "lastArticleId", required = false) Long lastArticleId
	) {
		return articleService.readAllInfiniteScroll(boardId, pageSize, lastArticleId);
	}

	@PostMapping("/v1/articles")
	public ArticleResponse create(@RequestBody final ArticleCreateRequest articleCreateRequest) {
		return articleService.create(articleCreateRequest);
	}

	@PutMapping("/v1/articles/{articleId}")
	public ArticleResponse update(@PathVariable final Long articleId,
		@RequestBody final ArticleUpdateRequest articleUpdateRequest) {

		return articleService.update(articleId, articleUpdateRequest);
	}

	@DeleteMapping("/v1/articles/{articleId}")
	public void delete(@PathVariable final Long articleId) {
		articleService.delete(articleId);
	}

	@GetMapping("/v1/articles/boards/{boardId}/count")
	public Long count(@PathVariable final Long boardId) {
		return articleService.count(boardId);
	}
}
