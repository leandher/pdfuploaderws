package br.com.pdfUploaderWS.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.springframework.web.multipart.MultipartFile;

public class UploadArquivos {

	public String uploadAnexo(MultipartFile file, String hash, HttpServletRequest request) {

		String path = "";
		try {
			byte[] bytes = file.getBytes();

			// Criando diretório para salvar o arquivo
			String uploadsDir = "/public/upload/";
			String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
			File dir = new File(realPathtoUploads);
			if (!dir.exists())
				dir.mkdirs();

			// Criando o arquivo no servidor
			File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			
			//Alterando arquivo para conter a hash
			printHash(serverFile, hash, serverFile);
			
			path = serverFile.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return path;
	}

	  public void printHash( File file, String message, File  outfile ) throws IOException
	    {
	        try (PDDocument doc = PDDocument.load(file))
	        {
	            PDFont font = PDType1Font.HELVETICA_BOLD;
	            float fontSize = 36.0f;

	            for( PDPage page : doc.getPages() )
	            {
	                PDRectangle pageSize = page.getMediaBox();
	                float stringWidth = font.getStringWidth( message )*fontSize/1000f;
	                // calculate to center of the page
	                int rotation = page.getRotation();
	                boolean rotate = rotation == 90 || rotation == 270;
	                float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
	                float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
	                float centerX = rotate ? pageHeight/2f : (pageWidth - stringWidth)/2f;
	                float centerY = rotate ? (pageWidth - stringWidth)/2f : pageHeight/2f;

	                // append the content to the existing stream
	                try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true, true))
	                {
	                    contentStream.beginText();
	                    // set font and font size
	                    contentStream.setFont( font, fontSize );
	                    // set text color to red
	                    contentStream.setNonStrokingColor(255, 0, 0);
	                    if (rotate)
	                    {
	                        // rotate the text according to the page rotation
	                        contentStream.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, centerX, centerY));
	                    }
	                    else
	                    {
	                        contentStream.setTextMatrix(Matrix.getTranslateInstance(centerX, centerY));
	                    }
	                    contentStream.showText(message);
	                    contentStream.endText();
	                }
	            }

	            doc.save( outfile );
	        }
	    }
}
