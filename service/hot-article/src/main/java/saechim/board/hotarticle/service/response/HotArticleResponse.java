package saechim.board.hotarticle.service.response;

import java.time.LocalDateTime;

import lombok.Getter;
import saechim.board.hotarticle.client.ArticleClient;

@Getter
public class HotArticleResponse {

	private Long articleId;
	private String title;
	private LocalDateTime createdAt;

	public static HotArticleResponse from(ArticleClient.ArticleResponse articleResponse) {
		HotArticleResponse hotArticleResponse = new HotArticleResponse();
		hotArticleResponse.articleId = articleResponse.getArticleId();
		hotArticleResponse.title = articleResponse.getTitle();
		hotArticleResponse.createdAt = articleResponse.getCreatedAt();
		return hotArticleResponse;
	}
}
