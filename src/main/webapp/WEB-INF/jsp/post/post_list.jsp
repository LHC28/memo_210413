<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="d-flex justify-content-center">
	<div class="post-box">
		<h1>글 목록</h1>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>No.</th>
					<th>제목</th>
					<th>작성날짜</th>
					<th>수정날짜</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="post" items="${postList }">
				<tr>
					<th>${post.id }</th>
					<th><a href="/post/post_detail_view?postId=${post.id }">${post.subject }</a></th>
					<th>
						<%--${post.createdAt } --%>
						<fmt:formatDate value="${post.createdAt }" pattern="yyyy-MM-dd HH:mm:ss" var="createdAt" />
						${createdAt }
					</th>
					<th>
						<%--${post.updatedAt } --%>
						<fmt:formatDate value="${post.updatedAt }" pattern="yyyy-MM-dd HH:mm:ss" />
					</th>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="d-flex justify-content-end">
			<a href="/post/post_create_view" class="btn btn-primary">글쓰기</a>
		</div>
	</div>
</div>