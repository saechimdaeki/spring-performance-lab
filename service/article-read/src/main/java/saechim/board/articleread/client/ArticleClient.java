package saechim.board.articleread.client;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ArticleClient {
	private final RestClient restClient;

	@Value("${endpoints.saechim-board-article-service.url}")
	private String articleServiceUrl;

	@PostConstruct
	public void initRestClient() {
		restClient = RestClient.create(articleServiceUrl);
	}

	public Optional<ArticleResponse> read(Long articleId) {
		try {
			ArticleResponse articleResponse = restClient.get()
				.uri("/v1/articles/{articleId}", articleId)
				.retrieve()
				.body(ArticleResponse.class);
			return Optional.of(articleResponse);
		} catch (Exception e) {
			log.error("[ArticleClient.read] articleId = {}", articleId, e);
			return Optional.empty();
		}
	}

	@Getter
	public static class ArticleResponse {
		private Long articleId;
		private String title;
		private String content;
		private Long boardId;
		private Long writerId;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
	}
}
