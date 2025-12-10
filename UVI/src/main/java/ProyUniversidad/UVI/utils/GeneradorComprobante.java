package ProyUniversidad.UVI.utils;

import ProyUniversidad.UVI.models.matricula.models.Matricula;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class GeneradorComprobante {

    public byte[] generarPDF(Matricula matricula) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Comprobante de Matrícula", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" ")); // Espacio

            document.add(new Paragraph("Código Matrícula: " + matricula.getCodigoComprobante()));
            document.add(new Paragraph("Alumno ID: " + matricula.getAlumnoId()));
            document.add(new Paragraph("Fecha: " + matricula.getFechaMatricula().toString()));
            document.add(new Paragraph("Total Créditos: " + matricula.getTotalCreditos()));
            document.add(new Paragraph("Monto a Pagar: S/ " + matricula.getCostoTotal()));
            document.add(new Paragraph("Estado: " + matricula.getEstado()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4); 
            table.setWidthPercentage(100);
            
            table.addCell(crearCeldaHeader("Código"));
            table.addCell(crearCeldaHeader("Curso"));
            table.addCell(crearCeldaHeader("Sección"));
            table.addCell(crearCeldaHeader("Créditos"));

            for (Matricula.DetalleCursoMatricula curso : matricula.getCursos()) {
                table.addCell(curso.getCursoCodigo());
                table.addCell(curso.getCursoNombre());
                table.addCell(curso.getSeccionCodigo());
                table.addCell(String.valueOf(curso.getCreditos()));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF del comprobante", e);
        }

        return out.toByteArray();
    }

    private PdfPCell crearCeldaHeader(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}