package br.com.pdfUploaderWS.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class UploadArquivos {

	public String uploadAnexo(MultipartFile file, String pasta, HttpServletRequest request) {

		String path = "";
		try {
			byte[] bytes = file.getBytes();

			// Creating the directory to store file
			String uploadsDir = "/public/" + pasta + "/";
			String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
			File dir = new File(realPathtoUploads);
			if (!dir.exists())
				dir.mkdirs();

			// Create the file on server
			File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();

			System.out.println("Server File Location=" + serverFile.getAbsolutePath());
			path = serverFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return path;
	}

}
