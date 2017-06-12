package principal.mapas;

import java.awt.Rectangle;

import principal.sprites.Sprite;

/**
 * \brief Classe que te unes coordenades i forma part de un Mapa
 * 
 * Conte la informacio de la Casella: si es solida, si es pot destruir (deixa de
 * ser solida), coordenades x i y, sprite normal, sprite destruit.
 *
 */

public class Casella {
	/** sprite principal de la Casella */
	private Sprite sprite;
	/** sprite que te la Casella al ser destruida */
	private Sprite spriteDestruit;
	/** coordenada horitzontal dins del Mapa en el que es troba */
	private final int x;
	/** coordenada vertical dins del Mapa en el que es troba */
	private final int y;
	/**
	 * true si es solida, es a dir que no s'hi pot passar perque es produeix una
	 * col.lisio
	 */
	private boolean solid;
	/** true si la casella pot ser destruida */
	private boolean destructible;
	/** true si la casella esta destruida */
	private boolean destruida = false;

	public Casella(final Sprite s, final int ix, final int iy) {
		/**
		 * @pre --
		 * 
		 * @post Casella creada amb sprite i coordenades entrats. No es solida
		 *       ni destructible.
		 */
		sprite = s;
		x = ix;
		y = iy;
		solid = false;
		destructible = false;
	}

	public Casella(final Sprite s, final int ix, final int iy, final boolean so, final boolean d) {
		/**
		 * @pre --
		 * 
		 * @post Casella creada amb sprite i coordenades entrats. solida=so i
		 *       destructible=d.
		 */
		sprite = s;
		x = ix;
		y = iy;
		solid = so;
		destructible = d;
	}

	public Casella(final Sprite s, Sprite s2, final int ix, final int iy, final boolean so, final boolean d) {
		/**
		 * @pre --
		 * 
		 * @post Casella creada amb sprite, spriteDestruit i coordenades
		 *       entrats. solida=so i destructible=d.
		 */
		sprite = s;
		spriteDestruit = s2;
		x = ix;
		y = iy;
		solid = so;
		destructible = d;
	}

	public Sprite getSprite() {
		/**
		 * @pre --
		 * 
		 * @post retorna sprite
		 */
		return sprite;
	}

	public int getX() {
		/**
		 * @pre --
		 * 
		 * @post retorna x
		 */
		return x;
	}

	public int getY() {
		/**
		 * @pre --
		 * 
		 * @post retorna y
		 */
		return y;
	}

	public void establirSolid(boolean so) {
		/**
		 * @pre --
		 * 
		 * @post solid=so
		 */
		solid = so;
	}

	public boolean esSolid() {
		/**
		 * @pre --
		 * 
		 * @post true si la casella es solida
		 */
		return solid;
	}

	public boolean esDestructible() {
		/**
		 * @pre --
		 * 
		 * @post true si es pot destruir
		 */
		return destructible;
	}

	public boolean destruida() {
		/**
		 * @pre --
		 * 
		 * @post true si esta destruida
		 */
		return destruida;
	}

	public void destruir() {
		/**
		 * @pre --
		 * 
		 * @post destruida=true, destructible=false, sprite=spriteDestruit i
		 *       solit=false.
		 */
		if (!destruida) {
			destruida = true;
			destructible = false;
			Sprite aux = sprite;
			sprite = spriteDestruit;
			spriteDestruit = aux;
			solid = false;
		}
	}

	public Rectangle getLimits() {
		/**
		 * @pre --
		 * 
		 * @post retorna el rectangle de col.lisions de la Casella
		 */
		return new Rectangle(x * Sprite.getTamany(), y * Sprite.getTamany(), Sprite.getTamany(), Sprite.getTamany());
	}

	public int distancia(Casella c) {
		/**
		 * @pre --
		 * 
		 * @post retorna la distancia (en nombre de caselles) que hi ha des de c
		 *       a Casella actual
		 */
		int auxX = x - c.x;
		if (auxX < 0)
			auxX = auxX * -1;
		int auxY = y - c.y;
		if (auxY < 0)
			auxY = auxY * -1;
		return auxX + auxY;
	}
}
