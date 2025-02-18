package saechim.board.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import saechim.board.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query(
		value = "select count(*) from (" +
			"   select comment_id from comment " +
			"   where article_id = :articleId and parent_comment_id = :parentCommentId " +
			"   limit :limit" +
			") t",
		nativeQuery = true
	)
	Long countBy(
		@Param("articleId") Long articleId,
		@Param("parentCommentId") Long parentCommentId,
		@Param("limit") Long limit
	);
}
