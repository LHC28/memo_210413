package com.memo.post.bo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	private static final int POST_MAX_SIZE = 3;
	
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId){
		// 게시글 번호	10 9 8 | 7 6 5 | 4 3 2 | 1
		// 1) 다음 : 가장 작은 수(오른쪽 값) => netxIdParam 쿼리 : nextIdParam보다 작은 3개(Limit)을 가져온다.
		// 2) 이전 : 가장 큰 수(왼쪽 값) => privIdParam 쿼리 : preIdParam 보다 큰 3개(Limit)를 가져온다. 순서가 뒤집히므로 코드에서 정렬을 뒤집는다.
		
		String direction = null;
		Integer standardId = null;
		if(prevId != null) {
			// 이전 버튼 클릭
			direction = "prev";
			standardId = prevId;
			
			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(postList);
			return postList;
		}else if(nextId != null) {
			direction = "next";
			standardId = nextId;
		}
		
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	// 가장 오른쪽 페이지인가?
	public boolean isLastPage(int userId, int nextId) {
		//1
		return nextId == postDAO.selectPostIdByUserIdAndSort(userId, "ASC");
	}
	
	public boolean isFirstPage(int userId, int prevId) {
		//10
		return prevId == postDAO.selectPostIdByUserIdAndSort(userId, "DESC");
	}
	
	public Post getPostByPostIdAndUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdAndUserId(postId, userId);
	}
	
	public int createPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		// file을 가지고 image URL로 구성하고 DB에 넣는다.
		String imagePath = null;
		if(file!=null) {
			try {
				// 컴퓨터(서버에 파일 업로드 후 웹으로 접근할 수 있는 image URL을 얻어낸다.
				imagePath = fileManagerService.saveFile(userLoginId, file);
			} catch (IOException e) {
				log.error("[파일업로드]"+e.getMessage()); // 간략한 에러 사항.
				
			}
		}
		log.info("###### 주소 : "+imagePath);
		return postDAO.insertPost(userId, subject, content, imagePath);
	}
	
	public int updatePost(int postId, int userId, String userLoginId, String subject, String content, MultipartFile file) {
		String imagePath = null;
		if(file!=null) {
			try {
				imagePath = fileManagerService.saveFile(userLoginId, file);
			} catch (IOException e) {
				log.error("[파일업로드]"+e.getMessage()); // 간략한 에러 사항.
			}
		}
		if(imagePath!=null) {
			Post post = postDAO.selectPostByPostIdAndUserId(postId, userId);
			String oldImagePath = post.getImagePath();
			try {
				fileManagerService.deleteFile(oldImagePath);
			} catch (IOException e) {
				log.error("[파일삭제] 삭제 중 에러 : "+postId+" "+ oldImagePath);
			}
		}
		
		return postDAO.updatePost(postId, userId, subject, content, imagePath);
	}
	
	public void postDelete(int userId, int postId, String userLoginId) {
		Post post = postDAO.selectPostByPostIdAndUserId(postId, userId);
		String imagePath = null;
		imagePath = post.getImagePath(); // url가져오기
		if(imagePath!=null) {
			try {
				fileManagerService.deleteFile(imagePath);;
			} catch (IOException e) {
				log.error("[파일삭제] 삭제 중 에러 : "+postId+" "+ imagePath);
			}
		}
		postDAO.postDelete(userId, postId);
	}
	
	
}
