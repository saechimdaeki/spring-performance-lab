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
public class ViewClient {
	private final RestClient restClient;

	@Value("${endpoints.saechim-board-view-service.url}")
	private String viewServiceUrl;

	@PostConstruct
	public void initRestClient() {
		restClient = RestClient.create(viewServiceUrl);
	}

	public long count(Long articleId) {
		try {
			return restClient.get()
				.uri("/v1/article-views/articles/{articleId}/count", articleId)
				.retrieve()
				.body(Long.class);
		} catch (Exception e) {
			log.error("[ViewClient.read] articleId = {}", articleId, e);
			return 0;
		}
	}

}
