package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<Board> findAll() {
        Query query = em.createNativeQuery("select * from board_tb order by id desc", Board.class);
        return query.getResultList();
    }

    public Board findById(int id) {
        Query query = em.createNativeQuery("select * from board_tb where id = ?", Board.class);
        query.setParameter(1, id);

        Board board = (Board) query.getSingleResult();
        return board;
    }

    public BoardResponse.MainDTO findByIdWithUsername(int idx) {
        String q = """
                select bt.id, bt.title, bt.content, bt.user_id, but.username, bt.created_at, rt.id r_id, rt.user_id r_user_id, rut.username, rt.comment
                from board_tb bt
                left outer join reply_tb rt on bt.id = rt.board_id
                inner join user_tb but on bt.user_id = but.id
                left outer join user_tb rut on rt.user_id = rut.id where bt.id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, idx);
        List<Object[]> rows = (List<Object[]>) query.getResultList();
        // 0번지의 값만 가지고 오는 이유 : board 내용이 중복되기에, 그 이상의 값은 가져올 필요 x
        Integer boardId = (Integer) rows.get(0)[0];
        String boardTitle = (String) rows.get(0)[1];
        String boardContent = (String) rows.get(0)[2];
        Integer boardUserId = (Integer) rows.get(0)[3];
        String boardUsername = (String) rows.get(0)[4];
        Timestamp boardCreatedAt = (Timestamp) rows.get(0)[5];
        BoardResponse.MainDTO responseDTO = new BoardResponse.MainDTO(boardId, boardTitle, boardContent, boardUserId, boardUsername, boardCreatedAt);
        // reply 가져오기 (참조)
        for (int i = 0; i < rows.size(); i++) {
            Integer rId = (Integer) rows.get(i)[6];
            Integer rUserId = (Integer) rows.get(i)[7];
            String rUsername = (String) rows.get(i)[8];
            String rComment = (String) rows.get(i)[9];
            BoardResponse.ReplyDTO replyDTO = new BoardResponse.ReplyDTO(
                    rId, rUserId, rUsername, rComment, false
            );
            if(rId != null) responseDTO.addReply(replyDTO);
        }
        return responseDTO;
    }

    public BoardResponse.DetailDTO findByIdWithUser(int idx) {
        Query query = em.createNativeQuery("select b.id, b.title, b.content, b.user_id, u.username from board_tb b inner join user_tb u on b.user_id = u.id where b.id = ?");
        query.setParameter(1, idx);

        Object[] row = (Object[]) query.getSingleResult();

        Integer id = (Integer) row[0];
        String title = (String) row[1];
        String content = (String) row[2];
        int userId = (Integer) row[3];
        String username = (String) row[4];
        System.out.println("id : " + id);
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        System.out.println("userId : " + userId);
        System.out.println("username : " + username);

        BoardResponse.DetailDTO responseDTO = new BoardResponse.DetailDTO();
        responseDTO.setId(id);
        responseDTO.setTitle(title);
        responseDTO.setContent(content);
        responseDTO.setUserId(userId);
        responseDTO.setUsername(username);

        return responseDTO;
    }

    @Transactional
    public void save(BoardRequest.SaveDTO requestDTO, int userId) {
        Query query = em.createNativeQuery("insert into board_tb(title, content, user_id, created_at) values(?,?,?, now())");
        query.setParameter(1, requestDTO.getTitle());
        query.setParameter(2, requestDTO.getContent());
        query.setParameter(3, userId);

        query.executeUpdate();
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }

    @Transactional
    public void update(BoardRequest.UpdateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("update board_tb set title=?, content=? where id = ?");
        query.setParameter(1, requestDTO.getTitle());
        query.setParameter(2, requestDTO.getContent());
        query.setParameter(3, id);

        query.executeUpdate();
    }
}
