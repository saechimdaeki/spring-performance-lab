package saechim.board.hotarticle.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import saechim.board.hotarticle.service.HotArticleService;
import saechim.board.hotarticle.service.response.HotArticleResponse;

@RestControllerAdvice
@RequiredArgsConstructor
public class HotArticleController {
	private final HotArticleService hotArticleService;

	@GetMapping("/v1/hot-articles/articles/date/{dateStr}")
	public List<HotArticleResponse> readAll(
		@PathVariable String dateStr
	) {
		return hotArticleService.readAll(dateStr);
	}

}
