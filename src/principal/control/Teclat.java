package principal.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * \brief Classe Teclat. Implementa KeyListener.
 * 
 * Controla totes les tecles que es teclejent i pren accions comforme
 *
 */

public final class Teclat implements KeyListener {

	/** numero maxim de tecles */
	private final static int NUMERO_TECLAS = 256;

	/** vector de booleans amb mida la mida del maxim de tecles d'un teclat */
	private boolean[] tecles = new boolean[NUMERO_TECLAS];

	/**
	 * booleans per controlar el personatge i els menus, es posen a true quan es
	 * premen les tecles determinades
	 */
	private boolean amunt;
	private boolean avall;
	private boolean dreta;
	private boolean esquerra;
	private boolean bomba;
	private boolean atacar;

	private boolean reprendre;
	private boolean pausa;
	private boolean amunt2;
	private boolean avall2;
	private boolean dreta2;
	private boolean esquerra2;
	private boolean bomba2;
	private boolean atacar2;

	public void actualizar() {
		/**
		 * @pre --
		 * 
		 * @post booleans actualitzats segons les tecles preses al teclat
		 */
		amunt = tecles[KeyEvent.VK_W];
		avall = tecles[KeyEvent.VK_S];
		esquerra = tecles[KeyEvent.VK_A];
		dreta = tecles[KeyEvent.VK_D];

		bomba = tecles[KeyEvent.VK_SPACE];
		atacar = tecles[KeyEvent.VK_V];
		atacar2 = tecles[KeyEvent.VK_COMMA];
		amunt2 = tecles[KeyEvent.VK_UP];
		avall2 = tecles[KeyEvent.VK_DOWN];
		esquerra2 = tecles[KeyEvent.VK_LEFT];
		dreta2 = tecles[KeyEvent.VK_RIGHT];
		bomba2 = tecles[KeyEvent.VK_M];
		reprendre = tecles[KeyEvent.VK_ENTER];
		pausa = tecles[KeyEvent.VK_ESCAPE];
	}

	@Override
	public void keyTyped(KeyEvent e) {
		/**
		 * metode de la interficie KeyListener no s'utilitza
		 */
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/**
		 * @pre --
		 * 
		 * @post posicio del vector de booleans corresponent a la tecla premuda
		 *       possat a true
		 */
		tecles[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/**
		 * @pre --
		 * 
		 * @post posicio del vector de booleans corresponent a la tecla premuda
		 *       possat a false
		 */
		tecles[e.getKeyCode()] = false;
	}

	public boolean getAmunt(int ct) {
		/**
		 * @pre ct=0 o 1
		 * 
		 * @post retorna amunt si ct=0, amunt2 altrament
		 */
		if (ct == 0)
			return amunt;
		else
			return amunt2;
	}

	public boolean getAvall(int ct) {
		/**
		 * @pre ct=0 o 1
		 * 
		 * @post retorna avall si ct=0, avall2 altrament
		 */
		if (ct == 0)
			return avall;
		else
			return avall2;
	}

	public boolean getDreta(int ct) {
		/**
		 * @pre ct=0 o 1
		 * 
		 * @post retorna dreta si ct=0, dreta2 altrament
		 */
		if (ct == 0)
			return dreta;
		else
			return dreta2;
	}

	public boolean getEsquerra(int ct) {
		/**
		 * @pre ct=0 o 1
		 * 
		 * @post retorna esquerra si ct=0, esquerra2 altrament
		 */
		if (ct == 0)
			return esquerra;
		else
			return esquerra2;
	}

	public boolean getBomba(int ct) {
		/**
		 * @pre ct=0 o 1
		 * 
		 * @post retorna bomba si ct=0, bomba2 altrament
		 */
		if (ct == 0)
			return bomba;
		else
			return bomba2;
	}

	public boolean getAtacar(int ct) {
		/**
		 * @pre ct=0 o 1
		 * 
		 * @post retorna atacar si ct=0, atacar2 altrament
		 */
		if (ct == 0)
			return atacar;
		else
			return atacar2;
	}

	public boolean getEnter() {
		/**
		 * @pre --
		 * 
		 * @post retorna reprendre
		 */
		return reprendre;
	}

	public boolean getPausa() {
		/**
		 * @pre --
		 * 
		 * @post retorna pausa
		 */
		return pausa;
	}

}