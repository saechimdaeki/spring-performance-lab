package saechim.board.articleread.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentClient {
	private final RestClient restClient;

	@Value("${endpoints.saechim-board-comment-service.url}")
	private String commentServiceUrl;

	@PostConstruct
	public void initRestClient() {
		restClient = RestClient.create(commentServiceUrl);
	}

	public long count(Long articleId) {
		try {
			return restClient.get()
				.uri("/v2/comments/articles/{articleId}/count", articleId)
				.retrieve()
				.body(Long.class);
		} catch (Exception e) {
			log.error("[ArticleClient.read] articleId = {}", articleId, e);
			return 0;
		}
	}

}
