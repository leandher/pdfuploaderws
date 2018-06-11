package br.com.pdfUploaderWS.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.pdfUploaderWS.util.UploadArquivos;

@Service
public class HomeService {

	public String saveFile(MultipartFile file, HttpServletRequest request){
		String path = null;

		path = new UploadArquivos().uploadAnexo(file, "upload", request);
		
		return path;
	}
	
}
