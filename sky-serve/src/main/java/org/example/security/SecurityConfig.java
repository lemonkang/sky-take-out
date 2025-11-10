package org.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig  {
    @Autowired
    JwtAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF（跨站请求伪造）
                // 因为 Token 是前端自行管理，无需后端 session，所以可以关闭
                .csrf(csrf -> csrf.disable())
                // 3. 配置哪些接口可以不登录访问
                .authorizeHttpRequests(auth -> auth
                        // 登录接口允许匿名访问，不用携带 token
                        .requestMatchers("/login","/login/**","/register", "/register/**", "/error","/download/**","/oss/**","/ws/**","/socket/**").permitAll()

                        // 其他任何请求都必须携带 token
                        .anyRequest().authenticated()
                );

        // 4. 在 UsernamePasswordAuthenticationFilter 之前添加我们的 Token 过滤器
        // 默认登录方式的过滤器在这里，我们的 Token 校验必须在它之前执行
        http.addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
    /**
     * AuthenticationManager 用来处理用户认证
     * Spring Security 新版本需要手动暴露该 Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {

        // 自动从 Spring Security 的配置中获取 AuthenticationManager
        return authenticationConfiguration.getAuthenticationManager();
    }
}
