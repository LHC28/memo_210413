package com.memo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class PermissionInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 세션 확인 => 있으면 로그인된 상태
		HttpSession session = request.getSession();
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		//URI - url path 확인
		String uri = request.getRequestURI();
		System.out.println("#### uri : "+uri);
		
		// 비로그인 && /post  => 로그인 페이지로 리다이렉트
		
		// 로그인 && /user => 포스트 리스트 페이지로 리다이렉트
		
		return true;
	}
}
