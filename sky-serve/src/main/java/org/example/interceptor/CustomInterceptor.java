package org.example.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.JwtContext;
import org.example.config.Result;
import org.example.mapper.EmployeMapper;
import org.example.util.JwtUtil;
import org.example.util.LimitIp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CustomInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CustomInterceptor.class);
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtContext jwtContext;
    @Autowired
    EmployeMapper  employeMapper;
    @Autowired
    LimitIp limitIp;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取客户端IP
//        String ip = request.getRemoteAddr();
//        log.info("ip地址"+ip);
//        if (limitIp.limitIp(ip)){
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.setContentType("application/json;charset=UTF-8");
//            Result<Object> limitRequest = Result.error("limit request");
//            ObjectMapper mapper = new ObjectMapper();
//            String s = mapper.writeValueAsString(limitRequest);
//            response.getWriter().write(s);
//            return false;
//
//
//        }
        // 仅处理正常的 REQUEST 调用，跳过 ERROR/FORWARD 等分派
        // 返回true允许请求继续执行
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)  {

    }






}
