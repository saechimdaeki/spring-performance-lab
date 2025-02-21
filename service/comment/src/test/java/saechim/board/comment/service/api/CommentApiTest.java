package saechim.board.comment.service.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import saechim.board.comment.service.response.CommentPageResponse;
import saechim.board.comment.service.response.CommentResponse;

@Slf4j
public class CommentApiTest {

    RestClient restClient = RestClient.create("http://localhost:9091");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment3", response1.getCommentId(), 1L));

        log.info("commentId1 = {}", response1.getCommentId());
        log.info("commentId2 = {}", response2.getCommentId());
        log.info("commentId3 = {}", response3.getCommentId());

    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v1/comments/{commentId}", 1L /*여긴 나중에 랜덤으로 */)
                .retrieve()
                .body(CommentResponse.class);

        log.info("response = {}", response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v1/comments/{commentId}", 1L /*여긴 나중에 랜덤으로 */)
                .retrieve();
    }

    @Test
    void readAll() {

        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId=1&pageSize=10")
                .retrieve()
                .body(CommentPageResponse.class);

        log.info("response = {}", response);
        for (CommentResponse comment : response.getComments()) {
            if (!comment.getCommentId().equals(comment.getParentCommentId()))
                log.info("\t");
            log.info("comment.getCommentId() = {}", comment.getCommentId());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }
}
