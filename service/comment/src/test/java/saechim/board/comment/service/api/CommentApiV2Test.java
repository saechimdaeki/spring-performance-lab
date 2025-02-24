package saechim.board.comment.service.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

	@Getter
	@AllArgsConstructor
	static class CommentCreateRequestV2 {
		private Long articleId;
		private String content;
		private String parentPath;
		private Long writerId;
	}

}
