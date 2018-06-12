package br.com.pdfUploaderWS.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.pdfUploaderWS.model.Hash;
import br.com.pdfUploaderWS.service.HomeService;

@Controller
public class HomeController {

	@Autowired
	private HomeService homeService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> salvar(@RequestParam("file") MultipartFile file,
			@RequestParam("cpf") String cpf, HttpServletRequest request) {
		try {
			return new ResponseEntity<Hash>(homeService.saveFile(file, cpf,
					request), HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Arquivo enviado pertence ao formato pdf!",
					HttpStatus.BAD_REQUEST);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Erro ao salvar arquivo",
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Erro no servidor",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> test() {
		return new ResponseEntity<String>("Servidor Rodando", HttpStatus.OK);
	}
}
