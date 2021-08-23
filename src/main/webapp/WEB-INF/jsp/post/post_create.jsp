<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center my-4">
	<div class="post-box">
		<h1>글쓰기</h1>
		<input type="text" class="form-control mb-2" name="subject" placeholder="제목을 입력해주세요.">
		<textarea class="form-control mb-2" rows="15" cols="100" name="content" placeholder="내용을 입력해주세요."></textarea>
		
		<div>
		<%-- 여러 파일을 업로드할 경우에는 multiple 프로퍼티를 추가한다. --%>
			<input type="file" class="form-control" name="image" accept=".jpg,.jpeg,.png,.gif">
		</div>
		
		<div class="mt-3 clearfix">
		<%-- float 정렬을 하게 되면 block태그도 옆에 계속 붙게 된다. --%>
			<button type="button" id="postListBtn" class="btn btn-dark float-left">목록</button>
			
			<div class="float-right">
				<button id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button id="saveBtn" class="btn btn-info">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function(){
		//목록 버튼 클릭
		$('#postListBtn').on('click',function(e){
			location.href="/post/post_list_view";
		});
		
		// 모두 지우기 버튼 클릭
		$('#clearBtn').on('click',function(e){
			// 제목과 textarea, image 파일 영역을 빈칸으로 채운다.
			$('input[name=subject]').val('');
			$('textarea[name=content]').val('');
			$('input[name=image]').val('');
		});
		
		// 저장 버튼 클릭 -> DB 저장
		$('#saveBtn').on('click', function(e){
			//validation
			let subject = $('input[name=subject]').val().trim();
			if(subject==''){
				alert("제목을 입력해주세요.");
				return;
			}
			
			let content = $('textarea[name=content]').val();
			if(content==''){
				alert("내용을 입력해주세요.");
				return;
			}
			
			// 파일이 업로드된 경우에만 확장자 체크
			let file = $('input[name=image]').val();
			//console.log(file);
			if(file!=''){
				// C:\fakepath\린스컴1.jpg
				let ext = file.split('.').pop().toLowerCase(); // .을 기준으로 나누고 확장자가 있는 마지막 배열칸을 가져오고 소문자로 모두 변경
				if($.inArray(ext, ['gif','jpg','jpeg','png'])==-1){ // -1인 경우 없는 것.
					alert("gif,png,jpg,jpeg 파일만 업로드 할 수 있습니다.");
				$('input[name=image]').val('');
					return;
				}
			}
			//--- validation 끝
			
			// 서버에 보내기
			
			// form 태그를 자바스크립트에서 만든다.
			let formData = new FormData();
			formData.append("subject",subject);
			formData.append("content",content);
			// $('input[name=image]')[0]) => 첫번째 input file 태그를 의미
			// .files[0] => 업로드 된 파일 중 첫번째를 의미
			formData.append("file", $('input[name=image]')[0].files[0]);
			
			// AJAX로 form 태그에 있는 데이터를 서버에 전송
			$.ajax({
				url:'/post/create'
				, method: 'post'
				, data: formData
				// 필수 파라미터 (파일 업로드시)
				, processData: false       // 기본은 true, json 또는 쿼리스트링(key=value&key1=value1) => String / 파일을 통으로 올리기 위함
				, contentType: false
				, enctype: 'multipart/form-data'
				
				,success: function(data){
					if(data.result=="success"){
						alert("메모가 저장되었습니다.");
						location.href="/post/post_list_view";
					}
				},error: function(e){
					alert("메모 저장에 실패했습니다. 관리자에게 문의해주세요.");
				}
			});
		});
	});
</script>