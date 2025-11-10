package org.example.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Result;
import org.example.entity.EmployeEntity;
import org.example.mapper.EmployeMapper;
import org.example.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    EmployeMapper employeMapper;
    @Autowired
    ObjectMapper objectMapper;
    
    // 允许匿名访问的路径列表（与SecurityConfig中的permitAll保持一致）
    private static final List<String> PERMIT_ALL_PATHS = Arrays.asList(
        "/login", "/register", "/error", "/download", "/oss","/ws","/socket"
    );
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        log.info("JwtAuthFilter处理请求: {}", requestPath);
        
        // 放行 WebSocket 握手请求
        String upgradeHeader = request.getHeader("Upgrade");
        if (upgradeHeader != null && "websocket".equalsIgnoreCase(upgradeHeader)) {
            log.info("检测到 WebSocket 升级请求，直接放行");
            filterChain.doFilter(request, response);
            return;
        }
        
        boolean isWhitePath = PERMIT_ALL_PATHS.stream().anyMatch(item -> requestPath.startsWith(item));
        if (isWhitePath) {
            log.info("请求路径 {} 在白名单中，直接放行", requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        
        // 如果没有token，返回错误
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            NoAuth(response, "未提供认证令牌");
            return;
        }
        
        String token = authHeader.substring(7);
        if (token == null || token.isEmpty()) {
            NoAuth(response, "认证令牌为空");
            return;
        }
        
        try {
            // 验证token是否过期或无效
            if (jwtUtil.validateToken(token)) {
                NoAuth(response, "认证令牌已过期");
                return;
            }
            
            // 解析token并设置认证信息
            String subject = jwtUtil.parseToken(token).getSubject();
            EmployeEntity employeEntity = employeMapper.selectOne(new QueryWrapper<EmployeEntity>().eq("employe_name", subject));
            if (employeEntity == null) {
                NoAuth(response, "用户不存在");
                return;
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(employeEntity, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            // token解析失败（格式错误、签名错误等）
            NoAuth(response, "认证令牌无效");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    public void NoAuth(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        // 使用Result类统一返回格式
        Result<Object> result = Result.error(message);
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }
}
