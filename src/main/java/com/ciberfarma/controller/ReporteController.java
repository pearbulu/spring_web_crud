package com.ciberfarma.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ciberfarma.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("reporte")
public class ReporteController {

	@Autowired
	private ReporteService reporteService;

	@GetMapping("boleta")
	public void boleta(@RequestParam Integer numBol, HttpServletResponse response) throws Exception {
		// Ruta del reporte (en resources/reportes)
		String reportPath = "/reporte/boleta.jrxml";

		// Parámetros
		Map<String, Object> params = new HashMap<>();
		params.put("pNumBoleta", numBol);
		
		//Get JasperPrint
		JasperPrint jasperPrint = reporteService.getJasperPrint(params, reportPath);

		// Configuración de respuesta HTTP
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", String.format("inline; filename=boleta-nro-%s.pdf", numBol));

		// Exportar a PDF
		OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

		outputStream.flush();
		outputStream.close();
	}
}
