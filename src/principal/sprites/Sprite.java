package principal.sprites;

import java.awt.image.BufferedImage;

/**
 * \brief Classe que guarda una Imatge.
 * 
 * Es tracte de una classe que guarda una Imatge i el seu tamany.
 */
public class Sprite {

	/** Imatge que es guarda */
	private final BufferedImage imatge;

	/** tamany en pixels de la imatge */
	private static final int TAMANY_SPRITES = 32;

	public Sprite(final BufferedImage i) {
		/**
		 * @pre --
		 * @post es guarda i al atribut imatge
		 */
		imatge = i;
	}

	public BufferedImage getImatge() {
		/**
		 * @pre --
		 * @post retorna imatge.
		 */
		return imatge;
	}

	public static int getTamany() {
		/**
		 * @pre --
		 * @post retorna TAMANY_SPRITES.
		 */
		return TAMANY_SPRITES;
	}

}
