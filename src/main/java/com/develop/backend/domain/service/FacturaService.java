package com.develop.backend.domain.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
@Service
public class FacturaService {

    public byte[] generarFactura(Map<String, Object> parametros) {
        try {
            InputStream jasperStream = getClass().getResourceAsStream("/reports.jasper");
            if (jasperStream == null) {
                throw new FileNotFoundException("El archivo reports.jasper no se encontró en resources.");
            }

            JasperReport report = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Error generando la factura: " + e.getMessage(), e);
        }
    }

}





