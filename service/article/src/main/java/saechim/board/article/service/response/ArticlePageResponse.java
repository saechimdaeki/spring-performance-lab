package saechim.board.article.service.response;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArticlePageResponse {

	private List<ArticleResponse> articles;
	private Long articleCount;

	public static ArticlePageResponse of(final List<ArticleResponse> articles, final Long articleCount) {
		final ArticlePageResponse articlePageResponse = new ArticlePageResponse();
		articlePageResponse.articles = articles;
		articlePageResponse.articleCount = articleCount;
		return articlePageResponse;
	}
}
