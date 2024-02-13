package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // IoC에 띄운다.
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer ignore() {
        return w -> w.ignoring().requestMatchers("/board/*", "/static/**", "/h2-console/**");
        // 인증이 필요 없는 페이지는 여기에.
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(c -> c.disable());

        // board/1, board/1/update .. 일일이 다 적기 귀찮으니, board/**
        http.authorizeHttpRequests(a -> {
            a.requestMatchers("/user/updateForm", "board/**").authenticated()
                    .anyRequest().permitAll();
            // 인증이 필요한 페이지 여기에. 그 이외의 요청(anyRequest)은 모두 허가(permitAll)
        });
        http.formLogin(f -> {
            f.loginPage("/loginForm").loginProcessingUrl("/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/loginForm");
        });
        // loginForm으로 이동하게 하고, 로그인은 login으로, 로그인이 성공하면 /으로.
        return http.build(); // 최종 http를 리턴
    }
}
