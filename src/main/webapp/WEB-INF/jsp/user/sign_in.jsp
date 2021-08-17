<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="login-box">
		<h1>로그인</h1>
		
		<form id="loginForm" method="post" action="/user/sign_in">
			<div class="input-group mb-3"><!-- bootstrap -->
				<%-- input-group-pretend : input 앞에 ID 부분을 회색으로 붙인다. --%>
				<div class="input-group-pretend">
					<span class="input-group-text">ID</span>
				</div>
				<input type="text" class="form-control" id="loginId" name="loginId">
			</div>
			<div class="input-group mb-3"><!-- bootstrap -->
				<%-- input-group-pretend : input 앞에 ID 부분을 회색으로 붙인다. --%>
				<div class="input-group-pretend">
					<span class="input-group-text">PW</span>
				</div>
				<input type="password" class="form-control" id="password" name="password">
			</div>
			<input type="submit" class="btn btn-primary btn-block" value="로그인"> <%-- submit인 경우 엔터에 반응 --%>
			<a href="/user/sign_up_view" class="btn btn-dark btn-block">회원가입</a>
		</form>
	</div>
</div>

<script>
	$(document).ready(function(){
		$('#loginForm').submit(function(e){
			e.preventDefault(); // 바로 submit 방지
			
			//validation
			let login = $('input[name=loginId]').val().trim();
			if(login==''){
				alert("아이디를 입력해주세요.");
				return;
			}
			let password = $('#password').val();
			if(password==''){
				alert("비밀번호를 입력해주세요.");
				return;
			}
			
			// AJAX로 submit
			let url = $(this).attr('action'); // 폼태그에 있는 속성 중 action값을 가져온다.
			let params = $(this).serialize();
			
			$.post(url, params).done(function(data){
				if(data.result == 'success'){
					location.href="/post/post_list_view";
				}else{
					alert("로그인에 실패했습니다. 다시 시도해주세요.");
				}
			});
			
		});
	});
</script>



