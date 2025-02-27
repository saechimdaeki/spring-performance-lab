package saechim.board.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import saechim.board.article.domain.BoardArticleCount;

public interface BoardArticleCountRepository extends JpaRepository<BoardArticleCount, Long> {

	@Query(
		value = "update board_article_count set article_count = article_count + 1 where board_id = :articleId"
		, nativeQuery = true
	)
	@Modifying
	int increase(@Param("boardId") Long boardId);

	@Query(
		value = "update board_article_count set article_count = article_count - 1 where board_id = :boardId"
		, nativeQuery = true
	)
	@Modifying
	int decrease(@Param("boardId") Long boardId);
}
