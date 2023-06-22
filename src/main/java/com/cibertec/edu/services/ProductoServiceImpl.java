package com.cibertec.edu.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cibertec.edu.models.Producto;
import com.cibertec.edu.models.ProductoReport;
import com.cibertec.edu.repositories.ProductoDao;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;

public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoDao productoRepository;

	@Override
	public List<Producto> getAllProducts() {
		// TODO Auto-generated method stub
		return this.productoRepository.findAll();
	}

	@Override
	public InputStream getReportProducts() throws Exception {
		try {
			List<Producto> listaProducto = this.getAllProducts();
			List<ProductoReport> listaData = new ArrayList<ProductoReport>();
			listaData.add(new ProductoReport());
			listaData.get(0).setProductosList(listaProducto);
			JRBeanCollectionDataSource dts = new JRBeanCollectionDataSource(listaData);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("IMAGE_PATH", "https://www.logolynx.com/images/logolynx/2d/2d0872912b6970a81b093aaf1bd027c7.jpeg");
			
			JasperReport jasperReportObj = getJasperReportCompiled();
			JasperPrint jPrint = JasperFillManager.fillReport(jasperReportObj, parameters, dts);
			InputStream result = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jPrint));
			return result;
		} catch(JRException ex){
			throw ex;
		}
	}
	
	private JasperReport getJasperReportCompiled() {
		try {
			InputStream productoReportStream = getClass().getResourceAsStream("/jasper/products_report.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(productoReportStream);
			JRSaver.saveObject(jasperReport, "students_report.jasper");
			return jasperReport;
		} catch(Exception e){
			return null;
		}
	}
	
	
}
