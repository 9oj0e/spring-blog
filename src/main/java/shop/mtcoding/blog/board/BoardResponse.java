package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class DetailDTO {
        private int id;
        private String title;
        private String content;
        private int userId;
        private String username;
    }
    @Data
    public static class MainDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Timestamp createdAt;
        private Boolean pageOwner;

        private List<ReplyDTO> replies = new ArrayList<>();
        public void addReply(ReplyDTO reply){ // 이 부분 참고
            replies.add(reply);
        }

        public MainDTO(Integer id, String title, String content, Integer userId, String username, Timestamp createdAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.userId = userId;
            this.username = username;
            this.createdAt = createdAt;
        }
    }
    @AllArgsConstructor // 풀생성자 만드는 이유?
    @Data
    public static class ReplyDTO {
        private Integer uId;
        private Integer rUserId;
        private String rUsername;
        private String rComment;
        private Boolean rOwner;
    }
}
