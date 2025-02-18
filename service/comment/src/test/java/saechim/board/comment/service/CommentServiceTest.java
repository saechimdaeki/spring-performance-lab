package saechim.board.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import saechim.board.comment.entity.Comment;
import saechim.board.comment.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	CommentService commentService;

	@Mock
	CommentRepository commentRepository;

	@Test
	@DisplayName("삭제할 댓글이 자식이 있으면 삭제 표시만 한다")
	void deleteShouldMarkDeletedIfHasChildren() {
		// given
		final Long articleId = 1L;
		final Long commentId = 2L;
		final Comment comment = createComment(articleId, commentId);
		given(commentRepository.findById(commentId))
			.willReturn(Optional.of(comment));
		given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(2L);
		// when

		commentService.delete(commentId);

		// then
		verify(comment).delete();
	}

	@Test
	@DisplayName("하위 댓글이 삭제되고, 삭제되지 않은 부모면, 하위 댓글만 삭제한다")
	void deleteShouldDeleteChildOnlyIfNotDeletedParent() {
		// given
		final Long articleId = 1L;
		final Long commentId = 2L;
		final Long parentCommentId = 1L;

		final Comment comment = createComment(articleId, commentId, parentCommentId);
		given(comment.isRoot()).willReturn(false);

		final Comment parentComment = mock(Comment.class);

		given(parentComment.getDeleted()).willReturn(false);

		given(commentRepository.findById(commentId))
			.willReturn(Optional.of(comment));

		given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(1L);

		given(commentRepository.findById(parentCommentId))
			.willReturn(Optional.of(parentComment));

		// when
		commentService.delete(commentId);

		// then

		verify(commentRepository).delete(comment);
		verify(commentRepository, never()).delete(parentComment);

	}

	@Test
	@DisplayName("하위 댓글이 삭제되고, 삭제된 부모면, 재귀적으로 모두 삭제한다")
	void deleteShouldDeleteAllRecursivelyIfDeletedParent() {
		// given
		final Long articleId = 1L;
		final Long commentId = 2L;
		final Long parentCommentId = 1L;

		final Comment comment = createComment(articleId, commentId, parentCommentId);
		given(comment.isRoot()).willReturn(false);

		final Comment parentComment = createComment(articleId, parentCommentId);
		given(parentComment.isRoot()).willReturn(true);
		given(parentComment.getDeleted()).willReturn(true);

		given(commentRepository.findById(commentId))
			.willReturn(Optional.of(comment));

		given(commentRepository.countBy(articleId, commentId, 2L)).willReturn(1L);

		given(commentRepository.findById(parentCommentId))
			.willReturn(Optional.of(parentComment));
		given(commentRepository.countBy(articleId, parentCommentId, 2L)).willReturn(1L);

		// when
		commentService.delete(commentId);

		// then

		verify(commentRepository).delete(comment);
		verify(commentRepository).delete(parentComment);

	}

	private Comment createComment(final Long articleId, final Long commentId) {
		final Comment comment = mock(Comment.class);
		given(comment.getArticleId()).willReturn(articleId);
		given(comment.getCommentId()).willReturn(commentId);
		return comment;
	}

	private Comment createComment(final Long articleId, final Long commentId, final Long parentCommentId) {
		final Comment comment = createComment(articleId, commentId);
		given(comment.getParentCommentId()).willReturn(parentCommentId);
		return comment;
	}
}