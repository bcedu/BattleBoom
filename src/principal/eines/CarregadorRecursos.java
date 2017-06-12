package principal.eines;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

/**
 * \brief Classe que carrega el recurros que s'utilitzarant
 * 
 * Carrega les imatges al buffer
 *
 */

public class CarregadorRecursos {

	public static BufferedImage carregarImatgeCompatibleTranslucida(final String ruta) {
		/**
		 * @pre ruta fa referencia a una imatge amb parts translucides
		 * 
		 * @post carrega la imatge de la ruta a buffer
		 */
		Image imatge = null;
		try {
			imatge = ImageIO.read(ClassLoader.class.getResource(ruta));
		} catch (IOException e) {
			e.printStackTrace();
		}

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		BufferedImage imatgeAccelerada = gc.createCompatibleImage(imatge.getWidth(null), imatge.getHeight(null),
				Transparency.TRANSLUCENT);
		Graphics g = imatgeAccelerada.getGraphics();
		g.drawImage(imatge, 0, 0, null);
		g.dispose();
		return imatgeAccelerada;
	}

	public static String llegirArxiuText(final String ruta) {
		/**
		 * @pre ruta fa referencia a un fitxer de text
		 * 
		 * @post llegeix el fitxer de text al qual fa referencia ruta
		 */
		String contingut = "";
		InputStream entradaBytes = ClassLoader.class.getResourceAsStream(ruta);
		BufferedReader lector = new BufferedReader(new InputStreamReader(entradaBytes));
		String linia;
		try {
			while ((linia = lector.readLine()) != null) {
				contingut += linia;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				while ((linia = lector.readLine()) != null) {
					if (entradaBytes != null)
						entradaBytes.close();
					if (lector != null)
						lector.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		return contingut;
	}

}
