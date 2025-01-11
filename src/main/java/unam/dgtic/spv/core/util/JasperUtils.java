package unam.dgtic.spv.core.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.List;
import java.util.Map;

public class JasperUtils {

    /**
     * Compila un archivo JRXML para obtener el JasperReport.
     * @param jrxmlPath path del archivo JRXML.
     * @return JasperReport compilado.
     * @throws JRException si ocurre un error durante la compilación.
     */
    public static JasperReport compileReport(String jrxmlPath) throws JRException {
        return JasperCompileManager.compileReport(jrxmlPath);
    }

    /**
     * Llena el reporte compilado con datos.
     * @param report reporte compilado.
     * @param parameters parámetros para el reporte.
     * @param beanList lista de beans para el datasource.
     * @return JasperPrint para ser exportado a PDF.
     * @throws JRException si ocurre un error al llenar el reporte.
     */
    public static JasperPrint fillReport(JasperReport report, Map<String, Object> parameters, List<?> beanList) throws JRException {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(beanList);
        return JasperFillManager.fillReport(report, parameters, dataSource);
    }

    /**
     * Exporta un JasperPrint a un archivo PDF.
     * @param print JasperPrint que será exportado.
     * @param destinationPath path donde se guardará el archivo PDF.
     * @throws JRException si ocurre un error durante la exportación.
     */
    public static void exportReportToPdf(JasperPrint print, String destinationPath) throws JRException {
        JasperExportManager.exportReportToPdfFile(print, destinationPath);
    }
}
