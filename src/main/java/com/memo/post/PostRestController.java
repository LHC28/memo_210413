package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

@RequestMapping("/post")
@RestController
public class PostRestController {
	
	@Autowired
	private PostBO postBO;

	@PostMapping("/create")
	public Map<String, String> postCreate(
			@RequestParam("subject") String subject
			,@RequestParam("content") String content
			,@RequestParam(value="file",required=false) MultipartFile file
			,HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		int userId = (Integer)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		
		Map<String, String> result = new HashMap<>();
		int row = postBO.createPost(userId, userLoginId, subject, content, file);
		if(row>0) {
			result.put("result","success");
		}else {
			result.put("result", "fail");
		}
		
		return result;
	}
	
	@PostMapping("/update")
	public Map<String, String> postupdate(
			@RequestParam("postId") int postId
			,@RequestParam("subject") String subject
			,@RequestParam("content") String content
			,@RequestParam(value="file",required=false) MultipartFile file
			,HttpServletRequest request
			){
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		Map<String, String> result = new HashMap<>();
		int row = postBO.updatePost(postId, userId, userLoginId, subject, content, file);
		if(row>0) {
			result.put("result", "success");
		}else {
			result.put("result", "fail");
		}
		return result;
	}
	
	@RequestMapping("/delete")
	public Map<String, String> postDelete(
			@RequestParam("postId") int postId
			,HttpServletRequest request
			){
		HttpSession session = request.getSession();
		int userId = (int)request.getAttribute("userId");
		String userLoginId = (String)request.getAttribute("userLoginId");
		postBO.postDelete(userId, postId, userLoginId);
		Map<String, String> result = new HashMap<>();
		
		result.put("result", "success");
		return result;
	}
	
}
