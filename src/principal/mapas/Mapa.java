package principal.mapas;

import java.awt.Graphics;

import principal.GestorPrincipal;
import principal.eines.CarregadorRecursos;
import principal.sprites.FullaSprites;
import principal.sprites.Sprite;

/**
 * \brief Mapa sobre el que els Jugadors es mouen i juguen
 * 
 * Està format per una quadricula de Caselles.
 *
 */
public class Mapa {

	/** tamany horitzontal en pixels */
	private final int amplada;

	/** tamany vertical en pixels */
	private final int altura;

	/** array amb les diferents Caselles que hi ha al mapa */
	private final Casella[] paletaCaselles;

	/** array amb els diferents Sprites que poden fer servir les Caselles */
	private final int[] spritesCaselles;

	/** matriu amb totes les Caselles que formen el mapa */
	private Casella quadricula[][];

	public Mapa(final String ruta) {
		/**
		 * @pre ruta condueix a un fitxer valid amb tota la informacio del mapa
		 * 
		 * @post quadricula conté tots els Sprites que formen el mapa
		 */
		String contingut = CarregadorRecursos.llegirArxiuText(ruta);

		String[] parts = contingut.split("\\*");
		amplada = Integer.parseInt(parts[0]);
		altura = Integer.parseInt(parts[1]);
		paletaCaselles = assignarCaselles(parts[3].split("#"), parts[2]);

		spritesCaselles = extreureSprites(parts[4].split(" "));

		quadricula = new Casella[amplada][altura];
		for (int y = 0; y < altura; y++) {
			for (int x = 0; x < amplada; x++) {
				quadricula[x][y] = new Casella(paletaCaselles[spritesCaselles[x + y * amplada]].getSprite(),
						paletaCaselles[0].getSprite(), x, y,
						paletaCaselles[spritesCaselles[x + y * amplada]].esSolid(), paletaCaselles[spritesCaselles[x
								+ y * amplada]].esDestructible());
			}
		}
	}

	private Casella[] assignarCaselles(final String[] partsPaleta, final String fullaUtilitzada) {
		/**
		 * @pre --
		 * 
		 * @post retorna un array amb les caselles ja creades
		 */
		FullaSprites fulla = new FullaSprites("/IMG/Textures/" + fullaUtilitzada + ".png", Sprite.getTamany());
		Casella[] auxC = new Casella[partsPaleta.length];

		for (int i = 0; i < partsPaleta.length; i++) {
			String[] aux = partsPaleta[i].split("-");
			boolean c = false;
			boolean d = false;
			if (Integer.parseInt(aux[2]) == 1)
				c = true;
			if (Integer.parseInt(aux[3]) == 1)
				d = true;

			auxC[Integer.parseInt(aux[0])] = new Casella(fulla.getSprite(Integer.parseInt(aux[1])), 0, 0, c, d);
		}
		return auxC;
	}

	private int[] extreureSprites(String[] s) {
		/**
		 * @pre --
		 * 
		 * @post retorna un array amb els index dels sprites a cada posicio
		 */
		int[] c = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			c[i] = Integer.parseInt(s[i]);
		}
		return c;
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post totes les Caselles de la quadricula s'han dibuixat en les seves
		 *       posicions
		 */
		GestorPrincipal.getSD().dibuixarMapa(g, this);
	}

	public Casella getCasella(int x, int y) {
		/**
		 * @pre x < amplada && y < altura && x >= 0 && y >= 0
		 * 
		 * @post retorna la casella de la posixio x-y de la quadricula
		 */
		if (x >= amplada || y >= altura || x < 0 || y < 0)
			return null;
		else {
			return quadricula[x][y];
		}
	}

	public int getAmplada() {
		/**
		 * @pre --
		 * 
		 * @post retorna amplada
		 */
		return amplada;
	}

	public int getAltura() {
		/**
		 * @pre --
		 * 
		 * @post retorna altura
		 */
		return altura;
	}

}
