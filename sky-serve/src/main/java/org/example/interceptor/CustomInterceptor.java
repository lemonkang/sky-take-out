package org.example.interceptor;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.DispatcherType;
import org.example.config.JwtContext;
import org.example.entity.EmployeEntity;
import org.example.mapper.EmployeMapper;
import org.example.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CustomInterceptor.class);
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtContext jwtContext;
    @Autowired
    EmployeMapper  employeMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 仅处理正常的 REQUEST 调用，跳过 ERROR/FORWARD 等分派
        if (request.getDispatcherType() != DispatcherType.REQUEST) {
            return true;
        }
        String authorization = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        String pathInfo = request.getPathInfo();
        log.info("pathInfo"+pathInfo);
        log.info("authorization"+authorization+"uri"+uri);
        // 白名单路径直接放行（支持前缀）
        if (uri.equals("/login") || uri.startsWith("/login/") || uri.equals("/register") || uri.startsWith("/register/")||uri.equals("/oss") || uri.startsWith("/oss/")) {
            return true;
        }
        log.info("权限验证逻辑");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String subject = jwtUtil.parseToken(token).getSubject();
            EmployeEntity employeEntity = employeMapper.selectOne(new QueryWrapper<EmployeEntity>().eq("employe_name", subject));
            jwtContext.setJwtContext(employeEntity);
            return true;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401, \"msg\":\"Unauthorized\"}");
        return false;
    }





}
