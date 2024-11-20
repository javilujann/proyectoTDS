package gui;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
