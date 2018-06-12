package br.com.pdfUploaderWS.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.pdfUploaderWS.model.Hash;
import br.com.pdfUploaderWS.util.UploadArquivos;

@Service
public class HomeService {

	public Hash saveFile(MultipartFile file, String cpf, HttpServletRequest request) throws IOException{
		
		String ipCliente = request.getRemoteAddr();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
		String data = simpleDateFormat.format(new Date());
		
		StringBuilder sb = new StringBuilder();
		sb.append(cpf);
		sb.append(data);
		sb.append(ipCliente);
		
		String hash = sb.toString();
		
		String path = null;
		try {
			path = new UploadArquivos().uploadAnexo(file, hash, request);
			if(path == null || path.equals("")) {
				throw new RuntimeException();
			}
		} catch (IOException e) {
			throw new IOException();
		}
		Hash h = new Hash();
		h.setHash(hash);
		
		return h;
	}
	
}
