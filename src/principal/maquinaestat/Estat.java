package principal.maquinaestat;

import java.awt.Graphics;

/**
 * \brief Interficie estat
 * 
 * Estructura que ha de tenir un estat
 *
 */

public interface Estat {
	/**
	 * @pre --
	 * 
	 * @post actualitza l'estat
	 */
	int actualitzar();

	/**
	 * @pre --
	 * 
	 * @post dibuixa l'estat
	 */
	void dibuixar(Graphics g);

	/**
	 * @pre --
	 * 
	 * @post inicialitza l'estat
	 */
	void inicialitzar();

}
