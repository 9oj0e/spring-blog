package shop.mtcoding.blog._core.config.security;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;

// POST, /login, x-www-form-urlencoded, 키 값이 username, password.
@RequiredArgsConstructor
@Service
public class MyLoginService implements UserDetailsService {
    private final UserRepository userRepository;
    private final HttpSession session;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // "/login"에서 Post요청을 받으면 loadUserByUsername을 때린다.
        User user = userRepository.findByUsername(username);
        if(user == null){
        return null;
        } else {
            session.setAttribute("sessionUser", user); // header.mustache에서 쓰기위한 session
            return new MyLoginUser(user); // SecurityContextHolder 에 저장.
        }
    }
}
