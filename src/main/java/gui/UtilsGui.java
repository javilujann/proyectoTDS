package gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JComponent;

public class UtilsGui {

	public static String getRutaResourceFromFile(File archivoImagen) {
		// Define la ruta base del proyecto que debe apuntar a "src/main/resources"
		Path rutaBase = Paths.get("src/main/resources").toAbsolutePath();

		// Obtén la ruta absoluta del archivo
		Path rutaArchivo = archivoImagen.toPath().toAbsolutePath();

		// Calcula la ruta relativa desde "src/main/resources" hasta el archivo
		Path rutaRelativa = rutaBase.relativize(rutaArchivo);

		// Devuelve la ruta en formato compatible con getResource()
		return "/" + rutaRelativa.toString().replace("\\", "/");
	}

	public static String getRutaResourceFromString (String source) {
		String target = "";
		if (source.contains("src\\main\\resources\\")) {
             target = source.substring(source.indexOf("src\\main\\resources\\") + "src\\main\\resources\\".length());
             // Cambia las barras de Windows (\) por barras de URL (/)
             target = "/" + target.replace("\\", "/");
       }
		return target;
	}
	
	public static void fixSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x,y));
		c.setMaximumSize(new Dimension(x,y));
		c.setPreferredSize(new Dimension(x,y));
	}
	
	public static BufferedImage adjustAspectRatio(BufferedImage originalImage, int aspectNumerator, int aspectDenominator, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calcular la relación de aspecto deseada
        float targetAspect = (float) aspectNumerator / aspectDenominator;
        float originalAspect = (float) originalWidth / originalHeight;

        // Variables para el recorte
        int cropWidth = originalWidth;
        int cropHeight = originalHeight;
        int cropX = 0;
        int cropY = 0;

        // Ajustar las dimensiones de recorte según la relación de aspecto
        if (originalAspect > targetAspect) {
            // Imagen más ancha que el aspecto deseado: recortar los lados
            cropWidth = (int) (originalHeight * targetAspect);
            cropX = (originalWidth - cropWidth) / 2;
        } else if (originalAspect < targetAspect) {
            // Imagen más alta que el aspecto deseado: recortar la parte superior e inferior
            cropHeight = (int) (originalWidth / targetAspect);
            cropY = (originalHeight - cropHeight) / 2;
        }

        // Recortar la imagen al nuevo aspecto
        BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, cropWidth, cropHeight);

        // Escalar la imagen recortada al tamaño objetivo
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(croppedImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return outputImage;
    }
	
	public boolean isValidImageUrl(String urlText) {
        try {
            URL url = new URL(urlText);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000); // Tiempo de espera
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();

            return responseCode == HttpURLConnection.HTTP_OK && contentType.startsWith("image/");
        } catch (Exception e) {
            return false; // Si hay un error, no es válida
        }
    }
	
}
