package gui;

import java.awt.Dimension;
import java.io.File;
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
	
}
