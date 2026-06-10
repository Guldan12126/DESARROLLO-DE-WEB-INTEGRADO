package apf3.ChifaXinYan.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import apf3.ChifaXinYan.Model.Venta;

@Service
public class ReporteService {

    @Autowired
    private VentaService ventaService;

    public ByteArrayInputStream exportarVentasExcel() {
        List<Venta> ventas = ventaService.listarTodas();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Ventas");

            // Cabecera
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Comprobante", "Fecha", "Monto", "Método Pago"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Datos
            int rowIdx = 1;
            for (Venta venta : ventas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(venta.getId());
                row.createCell(1).setCellValue(venta.getNumeroComprobante());
                row.createCell(2).setCellValue(venta.getFecha().toString());
                row.createCell(3).setCellValue(venta.getMonto());
                row.createCell(4).setCellValue(venta.getMetodoPago().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportarVentasPdf() {
        List<Venta> ventas = ventaService.listarTodas();
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            document.add(new Paragraph("Reporte de Ventas - Chifa Xin Yan", font));
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(5);
            table.addCell("ID"); table.addCell("Comprobante"); table.addCell("Fecha"); table.addCell("Monto"); table.addCell("Pago");
            for (Venta v : ventas) {
                table.addCell(v.getId().toString()); table.addCell(v.getNumeroComprobante());
                table.addCell(v.getFecha().toLocalDate().toString()); table.addCell(String.valueOf(v.getMonto()));
                table.addCell(v.getMetodoPago().toString());
            }
            document.add(table);
            document.close();
        } catch (Exception e) { throw new RuntimeException("Error al generar PDF", e); }
        return new ByteArrayInputStream(out.toByteArray());
    }
}