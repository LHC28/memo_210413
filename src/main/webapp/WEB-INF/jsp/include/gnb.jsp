<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex">
	<div class="logo">
		<h1 class="text-white p-4 font-weight-bold">메모 게시판</h1>
	</div>
	<div class="login-info d-flex justify-content-end">
		
		<%-- 로그인이 된 경우 --%>
		<c:if test="${not empty userName}">
			<div class="mt-5 mr-4">
				<span class="text-white"><b>${userName}님 안녕하세요.</b></span>
				<a href="/user/sign_out" class="text-white font-weight-bold ml-3">로그아웃</a>
			</div>
		</c:if>
		<%-- 로그인이 안 된 경우 --%>
		<c:if test="${empty userName}">
			<div class="mt-5 mr-4">
				<a href="/user/sign_in_view" class="text-white font-weight-bold ml-3">로그인</a>
			</div>
		</c:if>
	</div>
</div>
