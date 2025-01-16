package saechim.board.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import saechim.board.article.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
