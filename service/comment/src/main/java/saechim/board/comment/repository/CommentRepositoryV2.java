package saechim.board.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import saechim.board.comment.entity.CommentV2;

public interface CommentRepositoryV2 extends JpaRepository<CommentV2, Long> {

	@Query("select c from CommentV2  c where c.commentPath = :path")
	Optional<CommentV2> findByPath(@Param("path") String path);

	@Query(
		value = "select path from comment_v2 " +
			"where article_id = :articleId and path > :pathPrefix and path like :pathPrefix " +
			"order by path desc limit 1",
		nativeQuery = true
	)
	Optional<String> findDescendantsTopPath(
		@Param("articleId") Long articleId,
		@Param("pathPrefix") String pathPrefix
	);
}
