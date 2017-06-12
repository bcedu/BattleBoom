package principal.elements;

import java.awt.Rectangle;

import principal.mapas.Mapa;

/**
 * \brief Aram a distancia que pot tenir un Jugador. Hereta de Item.
 * 
 * Arma que es dispara quan un Jugador ataca. Es tracta de un projectil que
 * abança en linia recta. Pot col.lisionar amb ArmaExplosiva, Casella solida i
 * amb Jugador. Si col.lisiona amb Jugador li resta salut. Si col.lisiona amb
 * ArmaExplosiva la fa explotar.
 */

public class Arma extends Item {

	/** pot ser 'n','s','e','w', indica cap on es mou */
	private int direccio;

	/** indica si s'acava de col.lisionar amb un Item */
	private boolean explotant = false;

	/**
	 * utilitzar per mostrar durant un temps l'Sprite d'explosio al col.lisionar
	 */
	private int contadorExp = 0;

	/** numero de pixels que s'avancen per actualitzacio */
	private int velocitat = 6;

	/**
	 * pixels es sumen a x (posicio 0) i a y (posicio 1) al disparar en vertical
	 */
	private final int[] v = new int[2];

	/**
	 * pixels es sumen a x (posicio 0) i a y (posicio 1) al disparar en
	 * horitzontal
	 */
	private final int[] h = new int[2];

	public Arma(int posX, int posY, Mapa m, String ruta, int vx, int vy, int hx, int hy, int mh, int mv, int th, int tv) {
		/**
		 * @pre ruta condueix a una FullaSprites valida.
		 * 
		 * @post Arma creada amb dades entrades.
		 */
		super(posX, posY, m, ruta);
		eliminat = true;
		margeH = mh;
		tamanyH = th;
		margeV = mv;
		tamanyV = tv;
		v[0] = vx;
		v[1] = vy;
		h[0] = hx;
		h[1] = hy;
		sprite = fulla.getSprite(0);
	}

	public void actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post atributs de la Arma actualitzats.
		 */
		if (colisioMapa() && !explotant)
			finalitzarAtac();
		else if (!colisioMapa() && !explotant)
			moure();
		else if (explotant && contadorExp < 50)
			contadorExp++;
		else if (explotant && contadorExp >= 50) {
			explotant = false;
			eliminat = true;
		}
	}

	public void llancarAtac(int px, int py, char dir) {
		/**
		 * @pre dir =='n','s','e' o 'w'
		 * 
		 * @post arma disparada cap a la direccio i des de la posicio entrada,
		 *       eliminat=false.
		 */
		x = px;
		y = py;
		direccio = dir;
		eliminat = false;
		if (direccio == 'n') {
			sprite = fulla.getSprite(3, 0);
			x += v[0];// 7;
			y += v[1];// 0;
		} else if (direccio == 's') {
			sprite = fulla.getSprite(1, 0);
			x -= v[0];// 6;
			y += v[1];// 6;
		} else if (direccio == 'w') {
			sprite = fulla.getSprite(2, 0);
			x -= h[0];// 4;
			y += h[1];// 3;
		} else {// direccio=='e'
			sprite = fulla.getSprite(0, 0);
			x += h[0];// 4;
			y += h[1];// 3;
		}
	}

	public void finalitzarAtac() {
		/**
		 * @pre !eliminat
		 * 
		 * @post sprite canviat, explotant=true i eliminat=false.
		 */
		sprite = fulla.getSprite(4, 0);
		explotant = true;
		eliminat = false;
		contadorExp = 0;
	}

	private void moure() {
		/**
		 * @pre !eliminat i !explotant
		 * 
		 * @post la arma es mou,posicio x i y modificades segons la direccio i
		 *       la velocitat.
		 */
		if (direccio == 'e')
			x += velocitat;
		else if (direccio == 'w')
			x -= velocitat;
		else if (direccio == 's')
			y += velocitat;
		else if (direccio == 'n')
			y -= velocitat;
	}

	public boolean colisioJugador(Jugador e) {
		/**
		 * @pre e!=NULL
		 * 
		 * @post true si la Arma col.lisiona amb el Jugador.
		 */
		Rectangle aux = e.getTotsLimits();
		boolean colisio = this.getLimits().intersects(aux);

		return colisio;
	}

	public boolean getExplotant() {
		/**
		 * @pre --
		 * 
		 * @post retorna explotant.
		 */
		return explotant;
	}

	public void assignarVelocitat(int v) {
		/**
		 * @pre --
		 * 
		 * @post velocitat=v
		 */
		velocitat = v;
	}

}
