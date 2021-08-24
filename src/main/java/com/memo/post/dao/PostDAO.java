package com.memo.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {

	public List<Post> selectPostListByUserId(
			@Param("userId") int userId
			,@Param("direction") String direction
			,@Param("standardId") Integer standardId
			,@Param("limit") int limit);
	
	public Post selectPostByPostIdAndUserId(
			@Param("postId") int postId
			,@Param("userId") int userId);
	
	public int selectPostIdByUserIdAndSort(
			@Param("userId") int userId
			,@Param("sort") String sort
			);
	
	public int insertPost(
			@Param("userId")int userId
			,@Param("subject") String subject
			,@Param("content") String content
			,@Param("imagePath") String imagePath);
	
	public int updatePost(
			@Param("postId") int postId,
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	public void postDelete(
			@Param("userId") int userId
			,@Param("postId") int postId);
}
