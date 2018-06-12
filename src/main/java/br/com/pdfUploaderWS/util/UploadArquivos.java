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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.springframework.web.multipart.MultipartFile;

public class UploadArquivos {

	public String uploadAnexo(MultipartFile file, String hash, HttpServletRequest request) throws IOException {

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

			// Alterando arquivo para conter a hash
			printHash(serverFile, hash, serverFile);

			path = serverFile.getAbsolutePath();
		} catch (IOException e) {
			throw new IOException();
		} catch (Exception e) {
			throw new RuntimeException();
		}

		return path;
	}

	/**
	 * Imprime a hash no arquivo
	 *
	 * @param file
	 *            Arquivo de entrada.
	 * @param message
	 *            Hash que será escrita.
	 * @param outfile
	 *            Arquivo de saída.
	 *
	 * @throws IOException
	 *             Se ocorrer um erro ao escrever o arquivo e deleta o mesmo do diretório.
	 */
	public void printHash(File file, String message, File outfile) throws IOException {
		try (PDDocument doc = PDDocument.load(file)) {
			PDFont font = PDType1Font.HELVETICA_BOLD;
			float fontSize = 7.0f;

			for (PDPage page : doc.getPages()) {
				// Verifica se o arquivo está rotacionado
				int rotation = page.getRotation();
				boolean rotate = rotation == 90 || rotation == 270;

				// adiciona o conteudo ao arquivo
				try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true,
						true)) {

					contentStream.beginText();
					contentStream.setFont(font, fontSize);
					contentStream.setNonStrokingColor(0, 0, 0);
					if (rotate) {
						// rotaciona o texto de acordo com a rotação da página
						contentStream.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, 2, 1));
					} else {
						contentStream.setTextMatrix(Matrix.getTranslateInstance(2, 1));
					}
					contentStream.showText(message);
					contentStream.endText();
				}
			}

			doc.save(outfile);
		} catch (IOException e) {
			file.delete();
			throw new IOException();
		}
	}
}
