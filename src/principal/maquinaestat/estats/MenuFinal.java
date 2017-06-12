package principal.maquinaestat.estats;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import principal.GestorPrincipal;
import principal.eines.CarregadorRecursos;

/**
 * \brief Clase menu final. Hereta de MenuInicial.
 * 
 * Menu de final del joc on es mostren els credits i es felicita al jugador per
 * haver completat el joc
 * 
 */
public class MenuFinal extends MenuInicial {

	/**
	 * incica l'opcio escollida dins el menu aixi com les imatges que es
	 * dibuixen en cada moment
	 */
	protected int opcio = 0;

	/** controla les actulitzacions dels botons del teclat */
	protected int contador = 1;

	/** indica que es fara al sortir del menu */
	protected int codiFinalitzacio = 0;

	/** imatge de fons del menu */
	protected BufferedImage menu;

	/** imatge lletres felicitacio */
	protected BufferedImage f1;

	/** imatge lletres credits */
	protected BufferedImage credits;

	public MenuFinal() {
		/**
		 * @pre --
		 * 
		 * @post imatges menu, f1 i credits carregades
		 */
		menu = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuInicial/MenuFons.png");
		f1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuFinal/congrats.png");
		credits = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuFinal/credits.png");
	}

	public int actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post actualitza el menu final cambiant d'opcio quan es neccessari i
		 *       retorna un int que indicara el seguent pas a seguir un cop fora
		 *       del menu
		 */
		codiFinalitzacio = 0;

		if (GestorPrincipal.getTeclat().getEnter()) {
			opcio = 1;
		}
		if (opcio == 1) {
			if (contador <= 600)
				contador++;
		}
		if (contador > 600)
			codiFinalitzacio = -1;

		if (GestorPrincipal.getTeclat().getPausa())
			codiFinalitzacio = -1;
		return codiFinalitzacio;
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post menu Final mostrant-se per pantalla
		 */
		GestorPrincipal.getSD().dibuixarMenuFinal(g, this);
	}

	public void inicialitzar() {
		/**
		 * @pre --
		 * 
		 * @post inicialitza opcio i contador a 0
		 */
		contador = 0;
		opcio = 0;

	}

	public BufferedImage getF1() {
		/**
		 * @pre --
		 * 
		 * @post retorna f1
		 */
		return f1;
	}

	public BufferedImage getCredits() {
		/**
		 * @pre --
		 * 
		 * @post retorna credits
		 */
		return credits;
	}

	public BufferedImage getMenu() {
		/**
		 * @pre --
		 * 
		 * @post retorna menu
		 */
		return menu;
	}

	public int getOpcio() {
		/**
		 * @pre --
		 * 
		 * @post retorna opcio
		 */
		return opcio;
	}
}
