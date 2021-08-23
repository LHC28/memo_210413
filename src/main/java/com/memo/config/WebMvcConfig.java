package com.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.interceptor.PermissionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private PermissionInterceptor permissionInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(permissionInterceptor)
		.addPathPatterns("/**") // ** 아래 디렉토리까지 확인
		.excludePathPatterns("/user/sign_out", "/static/**","/error"); // 여기에 해당하는 URL은 인터셉터를 타지 않는다. 외우자.
		;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) { // 내 서버에 있는 실제 파일을 가져와서 웹주소로 가져올 수 있게 맵핑을 하는 것
		registry.addResourceHandler("/images/**") // http://localhost/images/usqq_1629441225415/린스컴1.jpg와 같이 접근 가능하게 맵핑해준다.
		.addResourceLocations("file:///C:\\springproject\\projectex\\workspaceprojectEx\\memo\\images/"); // 실제 파일 저장 위치
	}
	
}
