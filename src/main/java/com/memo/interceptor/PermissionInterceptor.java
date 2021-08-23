package com.memo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class PermissionInterceptor implements HandlerInterceptor{
	
	//private Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 세션 확인 => 있으면 로그인된 상태
		HttpSession session = request.getSession();
		String userLoginId = (String)session.getAttribute("userLoginId"); // 있으면 로그인 상태
		
		//URI - url path 확인
		String uri = request.getRequestURI(); // 도메인 뒤에 있는 path
		logger.info("#### uri:"+uri);
		
		// 비로그인 && /post/~  => 로그인 페이지로 리다이렉트
		if(userLoginId==null && uri.startsWith("/post")) {
			response.sendRedirect("/user/sign_in_view");
			return false; // Controller로 넘어가는 것을 방지
		}
		
		// 로그인 && /user => 포스트 리스트 페이지로 리다이렉트
		if(userLoginId!=null && uri.startsWith("/user")) {
			response.sendRedirect("/post/post_list_view");
			return false;
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		String uri = request.getRequestURI();
		logger.info("#### postHandle():"+uri);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		String uri = request.getRequestURI();
		logger.info("#### afterCompletion():"+uri);
	}
}
