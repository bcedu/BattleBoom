package principal.sprites;

import java.awt.image.BufferedImage;

import principal.eines.CarregadorRecursos;

/**
 * \brief Classe que guarda molts Sprites.
 * 
 * Es tracte de una classe que extreu molts Sprites de una imatge i els guarda
 * en un array.
 */
public class FullaSprites {

	/** amplada en pixels de la Imatge que conte els Sprites */
	final private int ampladaFullaPix;

	/** alture en pixels de la Imatge que conte els Sprites */
	final private int alturaFullaPix;

	/** Numero de Sprites que hi ha en horitzontal */
	final private int ampladaFullaSpr;

	/** Numero de Sprites que hi ha en vertical */
	final private int alturaFullaSpr;

	/** array amb tots els Sprites que s'han extret */
	final private Sprite[] sprites;

	public FullaSprites(final String ruta, final int tamanySpr) {
		/**
		 * @pre ruta condueix a una Imatge valida.
		 * @post Sprites de la imatge a la que conduia ruta s'han extret i
		 *       guardat a sprites[].
		 */

		final BufferedImage imatge;

		imatge = CarregadorRecursos.carregarImatgeCompatibleTranslucida(ruta);

		ampladaFullaPix = imatge.getWidth();
		alturaFullaPix = imatge.getHeight();
		ampladaFullaSpr = ampladaFullaPix / tamanySpr;
		alturaFullaSpr = alturaFullaPix / tamanySpr;

		sprites = new Sprite[ampladaFullaSpr * alturaFullaSpr];
		extreureSprites(imatge);
	}

	private void extreureSprites(final BufferedImage imatge) {
		/**
		 * @pre --
		 * @post Sprites de la imatge a la que conduia ruta s'han extret i
		 *       guardat a sprites[].
		 */
		for (int y = 0; y < alturaFullaSpr; y++) {
			for (int x = 0; x < ampladaFullaSpr; x++) {
				final int posX = x * Sprite.getTamany();
				final int posY = y * Sprite.getTamany();
				sprites[x + y * ampladaFullaSpr] = new Sprite(imatge.getSubimage(posX, posY, Sprite.getTamany(),
						Sprite.getTamany()));
			}
		}
	}

	public Sprite getSprite(final int id) {
		/**
		 * @pre id >= 0 && id < ampladaSpr*alturaSpr
		 * @post retorna el Sprite de la posicio id en l'array sprites.
		 */
		return sprites[id];
	}

	public Sprite getSprite(final int x, final int y) {
		/**
		 * @pre x >= 0 && x < ampladaSpr && y>=0 && y<alturaSpr
		 * @post retorna el Sprite de la posicio x+y*ampladaSpr en l'array
		 *       sprites.
		 */
		return sprites[x + y * ampladaFullaSpr];
	}

}
