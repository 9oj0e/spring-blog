package shop.mtcoding.blog.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final HttpSession session;
    private final ReplyRepository replyRepository;
    @PostMapping("/reply/save") // 댓글 쓰기
    public String write(ReplyRequest.SaveDTO requestDTO){
        System.out.println(requestDTO);
        // 1. 인증 안되면 나가
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) { // 401
            return "redirect:/loginForm";
        }
        // 2. 유효성 검사 (알아서 하세요)
        // 3. 모델 위임
        replyRepository.save(requestDTO, sessionUser.getId());
        return "redirect:/board/" +requestDTO.getBoardId();
    }
    // todo 댓글 수정
    // todo 댓글 삭제
    // todo 댓글 목록보기
}
