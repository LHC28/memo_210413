package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

/*
 * 데이터만 처리하는 API용 Controller
 * LHC28
 * 
 * */
@RequestMapping("/user")
@RestController // @Controller + @ResponseBody에 대한
public class UserRestController {
	
	@Autowired
	private UserBO userBO;
	
	@RequestMapping("/is_duplicated_id")
	public Map<String, Boolean> isDuplicatedID(
			@RequestParam("loginId") String loginId
			){
		Map<String, Boolean> result = new HashMap<>();
		User user =  userBO.getUserByloginId(loginId);
		if(user==null) {
			result.put("result", false);
		}else {
			result.put("result", true);
		}
		return result;
	}
	
	@PostMapping("/sign_up_for_ajax")
	public Map<String, String> signUpForAjax(
			@RequestParam("loginId") String loginId
			,@RequestParam("password") String password
			,@RequestParam("name") String name
			,@RequestParam("email") String email
			){
		// 암호화(해싱)
		String encryptPassword = EncryptUtils.md5(password);
		// insert DB
		userBO.addUser(loginId, encryptPassword, name, email);
		// 결과값 return
		Map<String, String> result = new HashMap<>();
		result.put("result", "success");
		return result;
	}
	
	@PostMapping("/sign_in")
	public Map<String, String> signIn(
			@RequestParam("loginId") String loginId
			,@RequestParam("password") String password
			,HttpServletRequest request
			){
		// password를 md5로 해싱한다.
		String encryptPassword = EncryptUtils.md5(password);
		// loginId, password로 user를 가져와서 있으면 로그인 성공
		User user = userBO.getUserByLoginIdAndPassword(loginId, encryptPassword);

		Map<String, String> result = new HashMap<>();
		if(user!=null) {
			// 성공 : 세션에 저장 (로그인 상태를 유지)
			HttpSession session =  request.getSession();
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			session.setAttribute("userId", user.getId());
			
			result.put("result", "success");
		}else {
			// 실패 : 에러 리턴
			result.put("result", "fail");
			result.put("message", "존재하지 않는 사용자입니다.");
		}
		return result;
	}
	
	
}
