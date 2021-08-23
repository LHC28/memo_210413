<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex justify-content-center my-4">
	<div class="post-box">
		<h1>글 상태 /수정</h1>
		
		<input type="text" name="subject" class="form-control" value="${post.subject}"><br>
		<textarea name="content" class="form-control" rows="15" cols="100">${post.content}</textarea>
		
		<%-- 이미지가 있을 때에만 이미지 영역 추가 --%>
		<c:if test="${not empty post.imagePath}">
		<div class="mt-3">
			<img src="${post.imagePath}" alt="업로드된 이미지" width="300px">
		</div>
		</c:if>
		<div class="mb-3">
			<input type="file" class="form-control" name="image" accept=".jpg,.jpeg,.png,.gif">
		</div>
		<div class="mt-5 clearfix">
			<button id="postDeleteBtn" class="btn btn-secondary float-left" data-post-id="${post.id}">삭제</button>
			<div class="float-right">
				<a href="/post/post_list_view" class="btn btn-dark">목록으로</a>
				<button type="button" id="saveBtn" class="btn btn-primary" data-post-id="${post.id}">수정</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function(){
		// 수정 버튼 클릭(글 내용 저장)
		$('#saveBtn').on('click',function(e){
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
			
			// 서버에 보내기
			let formData = new FormData();
			formData.append("postId", $('#saveBtn').data('post-id'));
			formData.append("subject",subject);
			formData.append("content",content);
			// $('input[name=image]')[0]) => 첫번째 input file 태그를 의미
			// .files[0] => 업로드 된 파일 중 첫번째를 의미
			formData.append("file", $('input[name=image]')[0].files[0]);
			
			$.ajax({
				url:'/post/update'
				,type:'post'
				,data: formData
				,processData: false
				,contentType: false
				,encType: 'multipart/form-data'
				,success: function(data){
					if(data.result=='success'){
						alert("메모가 수정되었습니다.");
						location.reload();
					}
				},error: function(e){
					alert("메모 수정에 실패했습니다. "+e)
				}
			})
			
		});
	
		// 삭제
		$('#postDeleteBtn').on('click',function(e){
			//validation
			let formData = new FormData();
			formData.append("postId", $('#postDeleteBtn').data('post-id'));
			
			$.ajax({
				url: '/post/delete'
				,type: 'post'
				,data: {"postId":$('#postDeleteBtn').data('post-id')}
				,success: function(data){
					if(data.result=="success"){
						alert("삭제 완료");
						location.href="/post/post_list_view"
					}
				},error: function(e){
					alert("삭제 실패하였습니다. 문의해주세요.");
				}
			});
		});
		
	});
</script>