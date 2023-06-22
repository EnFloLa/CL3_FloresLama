package com.cibertec.edu.services;

import java.io.InputStream;
import java.util.List;

import com.cibertec.edu.models.Producto;

public interface ProductoService {

	public List<Producto> getAllProducts();
	
	public InputStream getReportProducts() throws Exception;
}
