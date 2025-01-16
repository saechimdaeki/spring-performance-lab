package saechim.board.article.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import saechim.board.article.domain.Article;
import saechim.board.article.repository.ArticleRepository;
import saechim.board.article.service.request.ArticleCreateRequest;
import saechim.board.article.service.request.ArticleUpdateRequest;
import saechim.board.article.service.response.ArticleResponse;
import saechim.board.common.snowflake.Snowflake;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
	private final Snowflake snowflake = new Snowflake();
	private final ArticleRepository articleRepository;

	@Transactional
	public ArticleResponse create(final ArticleCreateRequest request) {
		final Article article = articleRepository.save(
			Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(),
				request.getWriterId())
		);

		return ArticleResponse.from(article);
	}

	@Transactional
	public ArticleResponse update(final Long articleId, final ArticleUpdateRequest request) {
		final Article article = articleRepository.findById(articleId).orElseThrow();
		article.update(request.getTitle(), request.getContent());
		return ArticleResponse.from(article);
	}

	public ArticleResponse read(final Long articleId) {
		return ArticleResponse.from(articleRepository.findById(articleId).orElseThrow());
	}

	@Transactional
	public void delete(final Long articleId) {
		articleRepository.deleteById(articleId);
	}
}
