package br.com.pdfUploaderWS.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import br.com.pdfUploaderWS.service.HomeService;

@Controller
public class HomeController {

	@Autowired
	private HomeService homeService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<String> salvar(@RequestParam("file") MultipartFile file, @RequestParam("cpf") String cpf,
			HttpServletRequest request) {
		try {
			String ipCliente = request.getRemoteAddr();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
			String data = simpleDateFormat.format(new Date());
			
			StringBuilder sb = new StringBuilder();
			sb.append(cpf);
			sb.append("-");
			sb.append(ipCliente);
			sb.append("-");
			sb.append(data);

			System.err.println(sb.toString());

			return new ResponseEntity<String>(homeService.saveFile(file, request), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> test() {
		return new ResponseEntity<String>("Servidor Rodando", HttpStatus.OK);
	}
}
