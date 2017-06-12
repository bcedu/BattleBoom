package principal.maquinaestat.estats;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import principal.GestorPrincipal;
import principal.eines.CarregadorRecursos;

/**
 * \brief Classe del menu mode versus(humaVShuma)
 * 
 * El menu perment triar un personatge per cada huma(jugador 1 i jugador 2)
 * perque puguin jugar un contra l'altre amb els personatges escollits I
 * estableix un mapa aleatori per el combat
 */

public class MenuVersus extends MenuInicial {

	private int p = 0;

	/** guarda l'informacio dels personatges triats i del mapa establert */
	private static String info;

	/** imatge de fons per jugador 1 */
	private BufferedImage menuP1;

	/** imatge de fons per jugador 2 */
	private BufferedImage menuP2;

	/** imatge botons amb quart seleccionat */
	private BufferedImage b4;

	/** imatge botons amb cinque seleccionat */
	private BufferedImage b5;

	/** numero del mapa escollit de manera aleatoria */
	int nMapa;

	public MenuVersus() {
		/**
		 * @pre --
		 * 
		 * @post imatge menuP1, menuP2, b1, b2,b3,b4,b5 carregades
		 */
		menuP1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVersus/MenuFons0.png");
		menuP2 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVersus/MenuFons1.png");
		b1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVersus/Sel1.png");
		b2 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVersus/Sel2.png");
		b3 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVersus/Sel3.png");
		b4 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVersus/Sel4.png");
		b5 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVersus/Tornar.png");
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
		if (contador == 0) {
			if (GestorPrincipal.getTeclat().getDreta(1)) {
				opcio++;
				contador++;
			} else if (GestorPrincipal.getTeclat().getEsquerra(1)) {
				opcio--;
				contador++;
			}
			if (GestorPrincipal.getTeclat().getEnter()) {
				if (opcio == 0) {
					codiFinalitzacio = 1;
					contador = 1;
					info = info + "/text/T1";
					if (p == 0) {
						info = info + "#";
						info = info + "672_64_520_32_1_";
					}
					p++;
				} else if (opcio == 1) {
					codiFinalitzacio = 1;
					contador = 1;
					info = info + "/text/E2";
					if (p == 0) {
						info = info + "#";
						info = info + "672_64_520_32_1_";
					}
					p++;
				} else if (opcio == 2) {
					codiFinalitzacio = 1;
					contador = 1;
					info = info + "/text/E1";
					if (p == 0) {
						info = info + "#";
						info = info + "672_64_520_32_1_";
					}
					p++;
				} else if (opcio == 3) {
					codiFinalitzacio = 1;
					contador = 1;
					info = info + "/text/E3";
					if (p == 0) {
						info = info + "#";
						info = info + "672_64_520_32_1_";
					}
					p++;
				} else if (opcio == 4) {
					codiFinalitzacio = -1;
				}
				if (codiFinalitzacio == 1 && p <= 1) {
					codiFinalitzacio = 0;
				}
			}
			if (opcio > 4)
				opcio = 0;
			if (opcio < 0)
				opcio = 4;
		} else if (contador < 15) {
			contador++;
		} else
			contador = 0;
		return codiFinalitzacio;
	}

	public void inicialitzar() {
		/**
		 * @pre --
		 * 
		 * @post inicialitza contador a 1, crea un random pel mapa i guarda la
		 *       informacio del joc a info
		 */
		contador = 1;
		p = 0;
		Random r = new Random();
		nMapa = r.nextInt((3 - 1) + 1) + 1;
		info = "/text/mapa" + nMapa + "#" + "/IMG/Icones/iconaP.png" + "#" + "1" + "#" + "1" + "#" + "64_64_32_32_0_";
	}

	public static String[] getInfo() {
		/**
		 * @pre --
		 * 
		 * @post return info
		 */
		return info.split("#");
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post mostra el menu versus per pantalla
		 */
		GestorPrincipal.getSD().dibuixarMenuVersus(g, this);
	}

	public BufferedImage getB4() {
		/**
		 * @pre --
		 * 
		 * @post retorna b4
		 */
		return b4;
	}

	public BufferedImage getB5() {
		/**
		 * @pre --
		 * 
		 * @post retorna b5
		 */
		return b5;
	}

	public BufferedImage getMP1() {
		/**
		 * @pre --
		 * 
		 * @post retorna menuP1
		 */
		return menuP1;
	}

	public BufferedImage getMP2() {
		/**
		 * @pre --
		 * 
		 * @post retorna menuP2
		 */
		return menuP2;
	}

	public int getP() {
		/**
		 * @pre --
		 * 
		 * @post retorna p
		 */
		return p;
	}
}
