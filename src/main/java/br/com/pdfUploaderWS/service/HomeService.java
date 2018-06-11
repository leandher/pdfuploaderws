package br.com.pdfUploaderWS.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.pdfUploaderWS.util.UploadArquivos;

@Service
public class HomeService {

	public String saveFile(MultipartFile file, String cpf, HttpServletRequest request){
		
		String ipCliente = request.getRemoteAddr();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
		String data = simpleDateFormat.format(new Date());
		
		StringBuilder sb = new StringBuilder();
		sb.append(cpf);
		sb.append("-");
		sb.append(ipCliente);
		sb.append("-");
		sb.append(data);
		
		String hash = sb.toString();
		
		return new UploadArquivos().uploadAnexo(file, hash, request);
	}
	
}
