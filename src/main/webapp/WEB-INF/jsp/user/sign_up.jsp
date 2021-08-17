<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<form id="signUpForm" method="post" action="/user/sign_up_for_submit">
<div class="d-flex align-items-center justify-content-center inputBox">
	<table class="text-center">
		<tr>
			<th>* 아이디</th>
			<td>
				<div class="d-flex">
					<div class="col-7">
						<input type="text" id="loginId" name="loginId" class="form-control">
						<div class="duplicatedId d-flex align-items-start">
							<div id="idCheckLength" class="small text-danger d-none">ID를
								4자 이상 입력해주세요.</div>
							<div id="idCheckDuplicated" class="small text-danger d-none">이미
								사용중인 ID입니다.</div>
							<div id="idCheckOk" class="small text-success d-none">사용
								가능한 ID입니다.</div>
						</div>
					</div>
					<div class="col-4 ml-3">
						<button type="button" id="loginIdCheckBtn"
							class="duplicatedBtn btn btn-success form-control color-white">중복확인</button>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th>* 비밀번호</th>
			<td>
				<div class="col-7">
					<input type="password" id="password" name="password" name="password"
						class="form-control">
				</div>
			</td>
		</tr>
		<tr>
			<th>* 비밀번호 확인</th>
			<td>
				<div class="col-7">
					<input type="password" id="confirmPassword" class="form-control">
				</div>
			</td>
		</tr>
		<tr>
			<th>* 이름</th>
			<td>
				<div class="col-7">
					<input type="text" id="name" name="name" class="form-control"
						plaecholder="이름을 입력하세요.">
				</div>
			</td>
		</tr>
		<tr>
			<th>* 이메일 주소</th>
			<td>
				<div class="col-10">
					<input type="text" id="email" name="email" class="form-control"
						plaecholder="이메일 주소를 입력하세요.">
				</div>
			</td>
		</tr>
	</table>
</div>
<div class="d-flex justify-content-center">
	<input type="submit" id="signUpBtn" class="btn btn-primary"
		value="회원가입">
</div>
</form>
<script>
$(document).ready(function(e) {

	// 아이디 중복 확인
	$('#loginIdCheckBtn').on('click', function(e) {
		let loginId = $('#loginId').val().trim();
		// 만약 id가 4자 이상이 아니면 경고 문구 노출
		if (loginId.length < 4) {
			// idCheckLength -> d-none remove
			// idCheckDuplicated -> d-none add
			// idCheckOk -> d-none add
			$('#idCheckLength').removeClass('d-none'); // 4자 이상 입력 경고문구 노출
			$('#idCheckDuplicated').addClass('d-none'); // 숨김
			$('#idCheckOk').addClass('d-none'); // 숨김
			return;
		}

		// 중복 확인 여부는 DB를 조회해야 하므로 서버에 묻는다.
		// 화면을 이동시키지 않고 AJAX 통신으로 중복 여부를 확인하고 동적으로 문구 노출
		$.ajax({
			url : '/user/is_duplicated_id',
			type : 'get',
			data : {
				'loginId' : loginId
			},
			success : function(data) {
				if (data.result === true) { // 중복인 경우
					$('#idCheckLength').addClass('d-none'); //  숨김
					$('#idCheckDuplicated').removeClass('d-none'); // 중복 경고 노출						$('#idCheckOk').addClass('d-none'); // 숨김
				} else { // 사용 가능한 경우
					$('#idCheckOk').removeClass('d-none'); // 사용 가능 문구 노출
					$('#idCheckLength').addClass('d-none'); //  숨김
					$('#idCheckDuplicated').addClass('d-none'); // 숨김
				}
			},
			error : function(e) {
				alert("아이디 중복확인에 실패했습니다. 관리자에게 문의해주세요.");
			}
		});
	});

	// 회원가입 버튼 동작
	$('#signUpForm').submit(function(e){
		e.preventDefault(); // submit의 기본동작을 중단
		
		//validation
		let loginId = $('#loginId').val().trim();
		if(loginId==''){
			alert("아이디를 입력하세요.");
			return;
		}
		
		let password = $('#password').val();
		let confirmPassword = $('#confirmPassword').val();
		if(password=='' || confirmPassword==''){
			alert("비밀번호를 입력하세요.");
			return;
		}
		//비밀번호 확인 일치 여부
		if(password != confirmPassword){
			alert("비밀번호가 일치하지 않습니다.");
			
			// 텍스트 초기화
			$('#password').val('');
			$('#confirmPassword').val('');
			return;
		}
		
		let name = $('#name').val().trim();
		if(name==''){
			alert("이름을 입력하세요.");
			return;
		}
		
		let email = $('#email').val().trim();
		if(email==''){
			alert("이메일을 입력하세요.");
			return;
		}
		
		// 아이디 중복확인이 완료됐는지 확인
		// -- idCheckOk -> d-none이 없으면 사용가능한 아이디라고 가정한다.
		if($('#idCheckOk').hasClass('d-none')){
			alert("아이디 중복확인을 해주세요.");
			return;
		}
		
		// 서버로 보내는 방법
		// --1) submit
		// --2) ajax
		
		// 1) submit : name 속성에 있는 값들이 서버로 넘어간다.(request param)
		// $(this)[0].submit(); // 서브밋은 서브밋 후 전체 화면을 이동시키는 경우 사용한다.
		
		// 2) AJAX (form태그시에만)
		let url='/user/sign_up_for_ajax';
		let data = $(this).serialize(); // 폼태그에 들어있는 name input(request param)이 구성된다. 만약 이걸 사용하지 않으면, data json을 임의로 구성해야 한다.
		
		$.post(url, data).done(function(data){
			if(data.result=="success"){
				alert("회원가입을 환영합니다. 로그인을 해주세요.");
				location.href="/user/sign_in_view";
			}else{
				alert("가입에 실패했습니다. 다시 시도해주세요.");
			}
		});
		
	});

});
</script>