package principal;

import principal.control.Teclat;
import principal.grafics.Finestra;
import principal.grafics.SuperficieDibuix;
import principal.maquinaestat.GestorEstats;

/**
 * \brief Classe "principal" des de on es crea i inicialitza tot el necessari
 * per funcionar.
 * 
 * Conté la Finestra des de on es juga, la SuperficieDibuix. el Teclat i el
 * GestorEstats. A mes de informacio del joc com el nom, les mides, ... Tambe
 * controla les actualitzacions per segon que es fan (60) i els FPS.
 */
public class GestorPrincipal {
	/** nom del joc */
	private final String NOM;

	/** tamany horitzontal */
	private final int AMPLADA;

	/** tamany vertical */
	private final int ALTURA;

	/** Classe on es dibuixa */
	private static SuperficieDibuix sd;

	/** finestra del joc */
	private static Finestra finestra;

	/** classe que gestiona l'estat actual del joc */
	private GestorEstats ge;

	/** classe que implementa KeyListener */
	private static final Teclat teclat = new Teclat();

	private GestorPrincipal(int am, int al, String n) {
		/**
		 * @pre --
		 * 
		 * @post GestorPrincipal creat amb nom i mides entrades.
		 */
		NOM = n;
		AMPLADA = am;
		ALTURA = al;
	}

	public static void main(String[] args) {
		GestorPrincipal gp = new GestorPrincipal(768, 480, "BattleBoom!!");

		gp.inicialitzar();
		gp.iniciarBuclePrincipal();
	}

	private void inicialitzar() {
		/**
		 * @pre --
		 * 
		 * @post SuperficieDibuix, Finestra i GestorEstats creats.
		 */
		sd = new SuperficieDibuix(AMPLADA, ALTURA);
		finestra = new Finestra(NOM, sd);
		ge = new GestorEstats();
	}

	private void iniciarBuclePrincipal() {
		/**
		 * @pre SuperficieDibuix, Finestra i GestorEstats creats.
		 * 
		 * @post GestorPrincipal es troba dins un bucle que actualitza i dibuixa
		 *       el joc.
		 */
		final int NS_PER_SEG = 1000000000;
		final byte APS_MAX = 60;
		final double NS_PER_ACTUALITZACIO = NS_PER_SEG / APS_MAX;

		long referenciaActualitzacio = System.nanoTime();
		long rc = System.nanoTime();

		double tempsTranscorregut;
		double delta = 0;

		while (true) {
			final long iniciBucle = System.nanoTime();

			tempsTranscorregut = iniciBucle - referenciaActualitzacio;
			referenciaActualitzacio = iniciBucle;

			delta += tempsTranscorregut / NS_PER_ACTUALITZACIO;

			while (delta >= 1) {
				actualitzar();
				delta--;
			}
			dibuixar();
			if (System.nanoTime() - rc > NS_PER_SEG) {
				rc = System.nanoTime();
			}
		}
	}

	private void dibuixar() {
		/**
		 * @pre sd != null.
		 * 
		 * @post GestorEstats dibuixat.
		 */
		sd.dibuixar(ge);

	}

	private void actualitzar() {
		/**
		 * @pre teclat i ge != null .
		 * 
		 * @post teclat i ge actualitzats.
		 */
		teclat.actualizar();
		ge.actualitzar();
	}

	public static Teclat getTeclat() {
		/**
		 * @pre --
		 * 
		 * @post retorna teclat.
		 */
		return teclat;
	}

	public static SuperficieDibuix getSD() {
		/**
		 * @pre --
		 * 
		 * @post retorna sd.
		 */
		return sd;
	}
}
