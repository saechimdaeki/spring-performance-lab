package saechim.board.like.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import saechim.board.like.entity.ArticleLike;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
	Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);
}
