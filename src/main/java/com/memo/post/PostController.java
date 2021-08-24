package com.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;

	@RequestMapping("/post_list_view")
	public String postListView(
			@RequestParam(value = "prevId", required = false) Integer prevIdParam
			,@RequestParam(value = "nextId", required = false) Integer nextIdParam
			,Model model
			,HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId"); // 없을 가능성도 가져가기 위해.
		if(userId==null) {
			// 세션 정보에 로그인 아이디가 없으면 => 로그인 페이지로 리다이렉트
			return "redirect:/user/sign_in_view";
		}
		
		
		// 게시글 번호	10 9 8 | 7 6 5 | 4 3 2 | 1
		// 1) 다음 : 가장 작은 수(오른쪽 값) => netxIdParam 쿼리 : nextIdParam보다 작은 3개(Limit)을 가져온다.
		// 2) 이전 : 가장 큰 수(왼쪽 값) => privIdParam 쿼리 : preIdParam 보다 큰 3개(Limit)를 가져온다. 순서가 뒤집히므로 코드에서 정렬을 뒤집는다.
		
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		int prevId = 0;
		int nextId = 0;
		if(postList.isEmpty()==false) {
			prevId = postList.get(0).getId();
			nextId = postList.get(postList.size()-1).getId();
			
			// 마지막 페이지 => nextId를 0으로 세팅한다.
			if(postBO.isLastPage(prevId, nextId)) {
				nextId = 0;
			}
			
			// 첫페이지
			if(postBO.isFirstPage(nextId, prevId)) {
				prevId = 0;
			}
		}
		
		
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/post_list");
		model.addAttribute("prevId", prevId); // 리스트 줌 가장 앞쪽(제일 큰) id
		model.addAttribute("nextId", nextId); // 리스트 중 가장 뒷쪽(제일 작은) id
		return "template/layout";
	}
	
	@RequestMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/post_create");
		return "template/layout";
	}
	
	@RequestMapping("/post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId
			,Model model
			,HttpServletRequest request
			) {
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId");
		if(userId==null) {
			//세션에 로그인 아이디가 없다면 로그인이 안 된 것으로 로그인 페이지로 redirect한다.
			return "redirect:/user/sign_in_view";
		}
		
		Post post = postBO.getPostByPostIdAndUserId(postId, userId);
		
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/post_detail");
		return "template/layout";
	}
	
}
