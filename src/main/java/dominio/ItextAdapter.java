package dominio;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import tds.BubbleText;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;

public class ItextAdapter implements IAdaptadorPDF {

	private static final String FILE_PATH = "premium.pdf";
	// Fuentes
	private static final Font fuenteTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static final Font fuenteSubTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	private static final Font fuenteTexto = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static final Font fuenteMensaje = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

	@Override
	public void exportarMensajes(Usuario usuario, Contacto contacto) throws Exception {
		Document document = new Document();
		try {
			// Crear el escritor de PDF
			PdfWriter.getInstance(document, new FileOutputStream(FILE_PATH));
			document.open();

			// Crear contenido del PDF
			setTile(document);
			setUserInfo(document, usuario);
			setContactInfo(document, contacto);
			setMessages(document, contacto);

		} catch (DocumentException | IOException e) {
			System.err.println("Error al generar el PDF: " + e.getMessage());
		} finally {
			document.close();
		}
	}

	private void setTile(Document document) throws DocumentException {
		Paragraph title = new Paragraph("Intercambio de Mensajes", fuenteTitulo);
		title.setAlignment(Element.ALIGN_CENTER); // Centrar el texto
		title.setSpacingAfter(20); // Añadir espacio después del título
		document.add(title);
	}

	private void setUserInfo(Document document, Usuario usuario) throws DocumentException {
		document.add(new Paragraph("\nInformación del Usuario:", fuenteSubTitulo));
		Paragraph userInfo = new Paragraph(
				"Usuario: " + usuario.getNombre() + " " + usuario.getApellidos() + "\nTelefono: " + usuario.getMovil(),
				fuenteTexto);
		userInfo.setSpacingAfter(10);
		document.add(userInfo);
	}

	private void setContactInfo(Document document, Contacto contacto) throws DocumentException {
		document.add(new Paragraph("\nInformación del Contacto:", fuenteSubTitulo));
		if (contacto.isGroup()) {
			Paragraph groupInfo = new Paragraph("Grupo: " + contacto.getNombre(), fuenteSubTitulo);
			groupInfo.setSpacingAfter(10);
			document.add(groupInfo);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);

			// Encabezado estilizado
			PdfPCell header1 = new PdfPCell(new Phrase("Miembro", fuenteSubTitulo));
			header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header1.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell header2 = new PdfPCell(new Phrase("Teléfono", fuenteSubTitulo));
			header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header1);
			table.addCell(header2);

			// Filas con alternancia de colores
			boolean isOddRow = true;
			for (ContactoIndividual miembro : ((Grupo) contacto).getMiembros()) {
				BaseColor rowColor = isOddRow ? BaseColor.WHITE : new BaseColor(230, 230, 230);
				PdfPCell cell1 = new PdfPCell(new Phrase(miembro.getNombre(), fuenteTexto));
				cell1.setBackgroundColor(rowColor);
				PdfPCell cell2 = new PdfPCell(new Phrase(miembro.getMovil(), fuenteTexto));
				cell2.setBackgroundColor(rowColor);
				table.addCell(cell1);
				table.addCell(cell2);
				isOddRow = !isOddRow;
			}
			document.add(table);
		} else {
			Paragraph contactInfo = new Paragraph(
					"Nombre: " + contacto.getNombre() + "\nTelefono: " + ((ContactoIndividual) contacto).getMovil(),
					fuenteTexto);
			contactInfo.setSpacingAfter(10);
			document.add(contactInfo);
		}
	}

	private void setMessages(Document document, Contacto contacto) throws DocumentException {
		document.add(new Paragraph("\nMensajes Intercambiados:", fuenteSubTitulo));
		// Formateador para la fecha y hora
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		for (Mensaje mensaje : contacto.getListaMensajes()) {
			// Crear un recuadro para el mensaje
			PdfPTable mensajeTable = new PdfPTable(1);
			mensajeTable.setWidthPercentage(100);
			mensajeTable.setSpacingBefore(10);

			// Encabezado del mensaje
			String tipoMensaje = mensaje.getTipo() == TipoMensaje.SENT ? "Enviado" : "Recibido";
			String direccion = mensaje.getTipo() == TipoMensaje.SENT ? "Para" : "De";
			String headerText = tipoMensaje + " - " + direccion + ": " + mensaje.getContacto().getNombre()
					+ " - Fecha y Hora: " + mensaje.getHora().format(dateFormat);
			PdfPCell headerCell = new PdfPCell(new Phrase(headerText, fuenteTexto));
			headerCell.setBackgroundColor(new BaseColor(200, 221, 242));
			headerCell.setPadding(10);
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mensajeTable.addCell(headerCell);

			if (mensaje.isEmoticon()) {
				// Manejo de emojis
				try {
					ImageIcon emojiIcon = BubbleText.getEmoji(mensaje.getEmoticon()); // Obtiene el
																						// ImageIcon del
																						// emoji

					Image emojiImage = Image.getInstance(emojiIcon.getImage(), null); // Convierte el ImageIcon a iText
																						// Image
					emojiImage.scaleToFit(50, 50); // Escala la imagen del emoji
					PdfPCell emojiCell = new PdfPCell(emojiImage);
					emojiCell.setPadding(10);
					emojiCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					mensajeTable.addCell(emojiCell);
				} catch (Exception e) {
					System.err.println("Error al agregar el emoji: " + e.getMessage());
				}
			} else {
				// Contenido del mensaje (texto normal)
				PdfPCell mensajeCell = new PdfPCell(new Phrase(mensaje.getTexto(), fuenteMensaje));
				mensajeCell.setPadding(10);
				mensajeTable.addCell(mensajeCell);
			}

			document.add(mensajeTable);
		}
	}
}
