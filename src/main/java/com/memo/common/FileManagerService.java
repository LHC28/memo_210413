package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // 일반적인 스프링빈 - 가장 포괄적인 범위
public class FileManagerService {
	
	private Logger logger = LoggerFactory.getLogger(FileManagerService.class);
	
	public static final int POST_MAX_SIZE = 3;
	
	// 실제 이미지가 (컴퓨터에) 저장될 경로
	public final static String FILE_UPLOAD_PATH = "C:\\springproject\\projectex\\workspaceprojectEx\\memo\\images/"; // 상수값이고 변경할 수 없음.
	
	// 이미지를 저장 -> URL Path 리턴
	public String saveFile(String userLoginId, MultipartFile file) throws IOException {
		// 파일을 컴퓨터에 저장
		
		// 1. 파일 디렉토리 경로 만들기(겹치지 않게) 예: marobiana_1620995857660/sun.png - 겹칠 확률이 매우 적음.
		String directoryName = userLoginId + "_" + System.currentTimeMillis()+"/"; // marobiana_1620995857660/
		String filePath = FILE_UPLOAD_PATH + directoryName;
		
		File directory = new File(filePath);
		if(directory.mkdir()==false) { // 파일을 업로드할 filePath 경로에 폴더 생성을 한다.
			logger.error("[파일업로드] 디렉토리 생성 실패" + userLoginId +", "+filePath);
			// 디렉토리 생성 실패
			return null;
		}
		// 파일 업로드 => byte 단위로 업로드 한다.
		byte[] bytes = file.getBytes();
		Path path = Paths.get(filePath + file.getOriginalFilename()); // originalFilename => input에서 올린 파일명
		Files.write(path, bytes);
		
		// 이미지 URL 만들어 리턴
		//http://localhost/images/usqq_1629441225415/린스컴1.jpg
			
		return "/images/" + directoryName + file.getOriginalFilename();
	}
	
	// 파일 삭제
	public void deleteFile(String imagePath) throws IOException {
		// C:\\springproject\\projectex\\workspaceprojectEx\\memo\\images/
		// /images/usqq_1629444033939/린스컴1.jpg
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		if(Files.exists(path)) {
			Files.delete(path); // 이미지 삭제
		}
		
		// 디렉토리 삭제
		path = path.getParent();
		if(Files.exists(path)) {
			Files.delete(path);
		}
	}
}
