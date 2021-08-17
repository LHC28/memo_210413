package com.memo.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/post")
@Controller
public class PostController {

	@RequestMapping("/post_list_view")
	public String postListView(Model model) {
		
		model.addAttribute("viewName", "post/post_list");
		return "template/layout";
	}
}
