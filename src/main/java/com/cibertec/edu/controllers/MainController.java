package com.cibertec.edu.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.repositories.ProductoDao;
import com.cibertec.edu.services.ProductoServiceImpl;

@Controller
public class MainController {

	@Autowired
	private ProductoDao productoRepository;
	private ProductoServiceImpl productoService;
	
	@GetMapping("/")
	public String login() {
		return "login";
	}
	
	@GetMapping("/registro")
	public String registro(@ModelAttribute("producto")Producto producto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			return "registro";
		} else {
			return "acceso_denegado";
		}
	}
	
	@PostMapping("/registro")
	public String registrarProducto(@Validated @ModelAttribute("producto")Producto producto, BindingResult bindingResult) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			if(bindingResult.hasErrors()) {
			return "registro";
			}
			
			productoRepository.save(producto);
			
			return "redirect:/";
			
		} else {
			return "acceso_denegado";
		}
	}
	
	@GetMapping(value = "/reporte", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> reporteProductos(HttpServletRequest request,
			HttpServletResponse response ) throws Exception	 {
		try {
			InputStream report = this.productoService.getReportProducts();
			byte[] data = report.readAllBytes();
			report.close();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_PDF);
			header.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"reporte_productos.pdf\"");
			header.setContentLength(data.length);
			return new ResponseEntity<byte[]>(data, header, HttpStatus.CREATED);
			
		}catch(IOException ex) {
			throw new RuntimeException("IOError retornando archivo");
		}
	}	
		
	}
	

