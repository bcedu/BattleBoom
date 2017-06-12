package principal.elements;

import java.awt.Graphics;
import java.awt.Rectangle;

import principal.GestorPrincipal;
import principal.mapas.Casella;
import principal.mapas.Mapa;
import principal.sprites.FullaSprites;
import principal.sprites.Sprite;

/**
 * \brief Element que es troba dins un mapa.
 * 
 * Es tracte de un "element" que es troba en un mapa, te com a minim un Sprite i
 * pot ser dibuixat. El seu tamany pot ser com a maxim el tamany de un Sprite.
 * Aquesta classe mai s'utilitza directament, sino que s'utilitza perque altres
 * classes heretin de aquesta.
 * 
 */
public abstract class Item {
	/** posicio horitzontal (en pixels) */
	protected int x;
	/** posicio vertical (en pixels) */
	protected int y;
	/** Mapa en que es troba el Item */
	protected Mapa mapa;
	/** lloc de on s'extreuen tots els Sprites d'aquest Item */
	protected FullaSprites fulla;
	/** Sprite actual, es el que es dibuixara */
	protected Sprite sprite;
	/** pixels de marge horitzontal des del inici del Sprite al inici del Item */
	protected int margeH = 0;
	/** tamany horitzontal en pixels del Item */
	protected int tamanyH = 32;
	/** pixels de marge vertical des del inici del Sprite fins al inici del Item */
	protected int margeV = 0;
	/** tamany vertical en pixels del Item */
	protected int tamanyV = 32;
	/** indica si el Item esta eliminat o no */
	protected boolean eliminat = false;

	public Item(int posX, int posY, Mapa m, String ruta) {
		/**
		 * @pre ruta condueix a una FullaSprites valida.
		 * 
		 * @post Item creat amb dades entrades.
		 */
		x = posX;
		y = posY;
		mapa = m;
		fulla = new FullaSprites(ruta, Sprite.getTamany());
		sprite = fulla.getSprite(0);
	}

	public Item(int posX, int posY, Mapa m, String ruta, int mH, int mV, int tH, int tV) {
		/**
		 * @pre ruta condueix a una FullaSprites valida.
		 * 
		 * @post Item creat amb dades entrades.
		 */
		x = posX;
		y = posY;
		mapa = m;
		fulla = new FullaSprites(ruta, Sprite.getTamany());
		sprite = fulla.getSprite(0);
		margeV = mV;
		tamanyV = tV;
		margeH = mH;
		tamanyH = tH;
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post Item dibuixat a la posicio x,y.
		 */
		GestorPrincipal.getSD().dibuixarItem(g, this);
	}

	public boolean estaEliminat() {
		/**
		 * @pre --
		 * 
		 * @post retorna eliminat.
		 */
		return eliminat;
	}

	public int getX() {
		/**
		 * @pre --
		 * 
		 * @post retorna x.
		 */
		return x;
	}

	public int getY() {
		/**
		 * @pre --
		 * 
		 * @post retorna y.
		 */
		return y;
	}

	public int getCasellaX() {
		/**
		 * @pre --
		 * 
		 * @post retorna la posicio horitzaontal en les Caselles del mapa.
		 */
		return (x + margeH) / Sprite.getTamany();
	}

	public int getCasellaY() {
		/**
		 * @pre --
		 * 
		 * @post retorna la posicio vertical en les Caselles del mapa.
		 */
		return (y + margeV) / Sprite.getTamany();
	}

	public Rectangle getLimits() {
		/**
		 * @pre --
		 * 
		 * @post retorna un Rectangle amb les mides del Item.
		 */
		return new Rectangle(x + margeH, y + margeV, tamanyH, tamanyV);
	}

	public boolean colisioItem(Item i) {
		/**
		 * @pre i!=Null.
		 * 
		 * @post retorna true si i col.lisiona amb Item.
		 */
		if (i.estaEliminat())
			return false;
		else
			return this.getLimits().intersects(i.getLimits());
	}

	public boolean colisioMapa() {
		/**
		 * @pre --
		 * 
		 * @post retorna true si a la posicio actual col.lisionem amb alguna
		 *       Casella solida del mapa.
		 */
		int cx = getCasellaX();
		int cy = getCasellaY();
		int cx2 = cx + tamanyH;
		int cy2 = cy + tamanyV;

		return colisioCasella(cx, cy) || colisioCasella(cx2 / Sprite.getTamany(), cy2 / Sprite.getTamany());
	}

	public boolean colisioCasella(int cx, int cy) {
		/**
		 * @pre --
		 * 
		 * @post retorna true si es col.lisiona amb la Casella de les
		 *       coordenades entrades.
		 */
		boolean colisio = false;
		Casella aux1 = null;
		aux1 = mapa.getCasella(cx, cy);
		if (aux1 == null)
			colisio = true;
		else if (aux1.esSolid()) {
			Rectangle auxR = aux1.getLimits();
			if (getLimits().intersects(auxR)) {
				colisio = true;
			}
		}
		return colisio;
	}

	public int distancia(Item i) {
		/**
		 * @pre i!=Null.
		 * 
		 * @post retorna les Caselles que hi ha de Item a i.
		 */
		Casella cObj = mapa.getCasella(i.getCasellaX(), i.getCasellaY());
		Casella c = mapa.getCasella(getCasellaX(), getCasellaY());
		return c.distancia(cObj);
	}

	public Sprite getSprite() {
		/**
		 * @pre --
		 * 
		 * @post retorna sprite.
		 */
		return sprite;
	}

}
