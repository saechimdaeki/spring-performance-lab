package saechim.board.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import saechim.board.comment.domain.ArticleCommentCount;

public interface ArticleCommentCountRepository extends JpaRepository<ArticleCommentCount, Long> {

	@Query(
		value = "update article_comment_count set comment_count = comment_count + 1 where article_id = :articleId"
		, nativeQuery = true
	)
	@Modifying
	int increase(@Param("articleId") Long articleId);

	@Query(
		value = "update article_comment_count set comment_count = comment_count - 1 where article_id = :articleId"
		, nativeQuery = true
	)
	@Modifying
	int decrease(@Param("articleId") Long articleId);
}
