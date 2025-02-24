package saechim.board.comment.service.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import saechim.board.comment.service.response.CommentPageResponse;
import saechim.board.comment.service.response.CommentResponse;

@Slf4j
public class CommentApiV2Test {

	RestClient restClient = RestClient.create("http://localhost:9001");

	@Test
	void create() {
		CommentResponse response1 = create(new CommentCreateRequestV2(1L, "my Comment1", null, 1L));
		CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my Comment2", response1.getPath(), 1L));
		CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my Comment3", response2.getPath(), 1L));

		log.info("response1.getCommentId = {}", response1.getCommentId());
		log.info("\t response2.getCommentId = {}", response2.getCommentId());
		log.info("\t response3.getCommentId = {}", response3.getCommentId());
	}

	CommentResponse create(CommentCreateRequestV2 request) {
		return restClient.post()
			.uri("/v2/comments")
			.body(request)
			.retrieve()
			.body(CommentResponse.class);
	}

	@Test
	void read() {
		CommentResponse response = restClient.get()
			.uri("/v2/comments/{id}", 12413688684527064L)
			.retrieve()
			.body(CommentResponse.class);
		log.info("response = {}", response);
	}

	@Test
	void delete() {
		restClient.delete()
			.uri("/v2/comments/{id}", 12413688684527064L)
			.retrieve();
	}

	@Test
	void readAll() {
		CommentPageResponse response = restClient.get()
			.uri("/v2/comments?articleId=1&pageSize=1&page=1")
			.retrieve()
			.body(CommentPageResponse.class);

		for (CommentResponse comment : response.getComments()) {
			log.info("comment.commentId = {}", comment.getCommentId());
		}
	}

	@Test
	void readAllInfiniteScroll() {
		List<CommentResponse> response1 = restClient.get()
			.uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});

		for (CommentResponse commentResponse : response1) {
			log.info("comment.commentId = {}", commentResponse.getCommentId());
		}

		String lastPath = response1.getLast().getPath();

		List<CommentResponse> response2 = restClient.get()
			.uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath=" + lastPath)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});

		for (CommentResponse commentResponse : response2) {
			log.info("comment.commentId = {}", commentResponse.getCommentId());
		}
	}

	@Getter
	@AllArgsConstructor
	static class CommentCreateRequestV2 {
		private Long articleId;
		private String content;
		private String parentPath;
		private Long writerId;
	}

}
