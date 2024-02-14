package shop.mtcoding.blog.reply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRequest;
import shop.mtcoding.blog.board.BoardResponse;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;
    @Transactional
    public void save(ReplyRequest.SaveDTO requestDTO, int userId) { // 댓글 쓰기
        Query query = em.createNativeQuery("insert into reply_tb(comment, board_id, user_id, created_at) values(?,?,?, now())");
        query.setParameter(1, requestDTO.getComment());
        query.setParameter(2, requestDTO.getBoardId());
        query.setParameter(3, userId);

        query.executeUpdate();
    }

    @Transactional
    public void deleteById(int id) { // todo 댓글 삭제
        Query query = em.createNativeQuery("delete from reply_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }
    /*
    @Transactional
    public void update(ReplyRequest.UpdateDTO requestDTO, int id) { // todo 댓글 수정
        Query query = em.createNativeQuery("update reply_tb set title=?, content=? where id = ?");
        query.setParameter(1, requestDTO.getTitle());
        query.setParameter(2, requestDTO.getContent());
        query.setParameter(3, id);

        query.executeUpdate();
    }
    */
}
