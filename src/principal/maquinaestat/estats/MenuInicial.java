package principal.maquinaestat.estats;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import principal.GestorPrincipal;
import principal.eines.CarregadorRecursos;
import principal.maquinaestat.Estat;

/**
 * \brief Classe menu inicial. Implementa la interficie estat
 * 
 * Mostra la pantalla inicial del joc on s'escull el mode que es vol jugar o si
 * es vol sortir del joc. Les opcions s'escullen a traves del teclat
 *
 */

public class MenuInicial implements Estat {

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

	/** imatge botons amb primer seleccionat */
	protected BufferedImage b1;

	/** imatge botons amb segon seleccionat */
	protected BufferedImage b2;

	/** imatge botons amb tercer seleccionat */
	protected BufferedImage b3;

	public MenuInicial() {
		/**
		 * @pre --
		 * 
		 * @post imatges menu, b1, b2 i b3 carregades
		 */
		menu = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuInicial/MenuFons.png");
		b1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuInicial/Botons1.png");
		b2 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuInicial/Botons2.png");
		b3 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuInicial/Botons3.png");
	}

	public int actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post actualitza el menu inicial cambiant d'opcio quan es neccessari
		 *       i retorna un int que indicara el seguent pas a seguir un cop
		 *       fora del menu
		 */
		codiFinalitzacio = 0;
		if (contador == 0) {
			if (GestorPrincipal.getTeclat().getAmunt(1)) {
				opcio--;
				contador++;
			} else if (GestorPrincipal.getTeclat().getAvall(1)) {
				opcio++;
				contador++;
			}
			if (GestorPrincipal.getTeclat().getEnter()) {
				if (opcio == 0) {
					codiFinalitzacio = 1;
				} else if (opcio == 1) {
					codiFinalitzacio = 2;
				} else if (opcio == 2) {
					codiFinalitzacio = -1;
				}
			}
			if (opcio >= 3)
				opcio = 0;
			if (opcio < 0)
				opcio = 2;
		} else if (contador < 15) {
			contador++;
		} else
			contador = 0;
		return codiFinalitzacio;
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post mostra el menu Inicial per pantalla
		 */
		GestorPrincipal.getSD().dibuixarMenu(g, this);
	}

	public void inicialitzar() {
		/**
		 * @pre --
		 * 
		 * @post inicialitza contador a 1
		 */
		contador = 1;

	}

	public BufferedImage getB1() {
		return b1;
	}

	public BufferedImage getB2() {
		return b2;
	}

	public BufferedImage getB3() {
		return b3;
	}

	public BufferedImage getMenu() {
		return menu;
	}

	public int getOpcio() {
		return opcio;
	}

}
