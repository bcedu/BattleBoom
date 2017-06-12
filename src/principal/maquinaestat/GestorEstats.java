package principal.maquinaestat;

import java.awt.Graphics;

import principal.maquinaestat.estats.EstatJoc;
import principal.maquinaestat.estats.MenuArcade;
import principal.maquinaestat.estats.MenuD;
import principal.maquinaestat.estats.MenuFinal;
import principal.maquinaestat.estats.MenuInicial;
import principal.maquinaestat.estats.MenuP;
import principal.maquinaestat.estats.MenuV;
import principal.maquinaestat.estats.MenuVersus;

/**
 * \brief Classe encarregada de controlar tots els estats del joc
 * 
 * El joc es divideix en diversos estats i mitjantzant aquesta classe es
 * controla quan entrem dins d'un estat i quan en sortim
 *
 */

public class GestorEstats {

	/** array d'estats on es guarden tots els estats del joc */
	private Estat[] estats;

	/** estat que el joc esta executant */
	private Estat estatActual;

	/** posicio de l'estat actual al vetor d'estats */
	private int idActual;

	/** nombre d'estats total */
	private static final int NOMBRE_ESTATS = 8;

	public GestorEstats() {
		/**
		 * @pre --
		 * 
		 * @post gestor d'estats creat amb tots els estats inicialitzats al
		 *       vector d'estats
		 */
		iniciarEstats();
		iniciarEstatActual();
	}

	private void iniciarEstatActual() {
		/**
		 * @pre --
		 * 
		 * @post estat 0 definit com a estat actual(primer estat del
		 *       joc,menuInicial)
		 */
		estatActual = estats[0];
		idActual = 0;
	}

	private void iniciarEstats() {
		/**
		 * @pre --
		 * 
		 * @post creat el vector d'estats i inicialitzat cada estat del vector
		 */
		estats = new Estat[NOMBRE_ESTATS];
		estats[0] = new MenuInicial();
		estats[1] = new MenuArcade();
		estats[2] = new MenuVersus();
		estats[4] = new MenuP();
		estats[5] = new MenuV();
		estats[6] = new MenuD();
		estats[7] = new MenuFinal();
	}

	public void actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post estat actual actualitzat segons el codi retornat per les
		 *       funcions de gestio de cada estat
		 */
		int codi = estatActual.actualitzar();
		if (idActual == 0)
			gestioMenuInicial(codi);
		else if (idActual == 1)
			gestioMenuArcade(codi);
		else if (idActual == 2)
			gestioMenuVersus(codi);
		else if (idActual == 3)
			gestioEstatJoc(codi);
		else if (idActual == 4)
			gestioMenuP(codi);
		else if (idActual == 5)
			gestioMenuV(codi);
		else if (idActual == 6)
			gestioMenuD(codi);
		else if (idActual == 7)
			gestioMenuFinal(codi);
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post estat actual mostran-se per pantalla
		 */
		estatActual.dibuixar(g);
	}

	private void cambiarEstatActual(final int nou) {
		/**
		 * @pre 0<=nou<NOMBRE_ESTATS
		 * 
		 * @post s'ha cambiat l'estat actual per l'esta nou i s'ha inicialitzat
		 */
		estatActual = estats[nou];
		idActual = nou;
		estatActual.inicialitzar();
	}

	private void gestioMenuInicial(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat menu inicial depenent del valor de c, c
		 *       indica a quin estat s'ha de cambiar
		 */
		if (c == -1)
			System.exit(0);
		else if (c == 1) {
			cambiarEstatActual(1);
		} else if (c == 2) {
			cambiarEstatActual(2);

		}

	}

	private void gestioMenuArcade(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat menu Arcade depenent del valor de c, c
		 *       indica a quin estat s'ha de cambiar
		 */
		if (c == -1)
			cambiarEstatActual(0);
		else if (c == 1) {
			estats[3] = new EstatJoc("/text/Nivell1-Indv");
			cambiarEstatActual(3);
		} else if (c == 2) {
			estats[3] = new EstatJoc("/text/Nivell1-Coop");
			cambiarEstatActual(3);
		}
	}

	private void gestioMenuVersus(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat menu Versus depenent del valor de c, c
		 *       indica a quin estat s'ha de cambiar
		 */
		if (c == -1)
			cambiarEstatActual(0);
		else if (c == 1) {
			estats[3] = new EstatJoc(MenuVersus.getInfo());
			cambiarEstatActual(3);
		}
	}

	private void gestioEstatJoc(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat Joc depenent del valor de c, c indica a
		 *       quin estat s'ha de cambiar
		 */
		if (c == 2)
			cambiarEstatActual(4);
		else if ((c == 1 || c == -1) && EstatJoc.getFitxer() == "vs") {
			cambiarEstatActual(2);
		} else if (c == 1 && getNivell(EstatJoc.getFitxer()) != "fi") {
			cambiarEstatActual(5);
		} else if (c == 1 && getNivell(EstatJoc.getFitxer()) == "fi") {
			cambiarEstatActual(7);
		} else if (c == -1) {
			cambiarEstatActual(6);
		}
	}

	private void gestioMenuP(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat del menuP(menu de pausa, c indica a quin
		 *       estat s'ha de cambiar
		 */
		if (c == -1)
			cambiarEstatActual(0);
		else if (c == 1)
			cambiarEstatActual(3);
		else if (c == 2) {
			if (EstatJoc.getFitxer() == "vs")
				estats[3] = new EstatJoc(MenuVersus.getInfo());
			else
				estats[3] = new EstatJoc(EstatJoc.getFitxer());
			cambiarEstatActual(3);
		}
	}

	private void gestioMenuV(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat del menu Victoria depenent del valor de c,
		 *       c indica a quin estat s'ha de cambiar
		 */
		if (c == -1)
			cambiarEstatActual(0);
		else if (c == 1) {
			if (getNivell(EstatJoc.getFitxer()) == "fi")
				cambiarEstatActual(7);
			else {
				estats[3] = new EstatJoc(getNivell(EstatJoc.getFitxer()));
				cambiarEstatActual(3);
			}
		}
	}

	private void gestioMenuD(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat del menu Derrota depenent del valor de c, c
		 *       indica a quin estat s'ha de cambiar
		 */
		if (c == -1)
			cambiarEstatActual(0);
		if (c == 1) {
			estats[3] = new EstatJoc(EstatJoc.getFitxer());
			cambiarEstatActual(3);

		}
	}

	private void gestioMenuFinal(int c) {
		/**
		 * @pre --
		 * 
		 * @post es gestiona l'estat del menu Final depenent del valor de c, c
		 *       indica a quin estat s'ha de cambiar
		 */
		if (c == -1) {
			cambiarEstatActual(0);
		}
	}

	private String getNivell(String s) {
		/**
		 * @pre --
		 * 
		 * @post retorna un string amb la ruta del fitxer de text amb la
		 *       informacio del nivell seguent(en el mode versus)
		 */
		String x;
		if (s == "/text/Nivell1-Indv") {
			x = "/text/Nivell2-Indv";
		} else if (s == "/text/Nivell1-Coop") {
			x = "/text/Nivell2-Coop";
		} else if (s == "/text/Nivell2-Indv") {
			x = "/text/Nivell3-Indv";
		} else if (s == "/text/Nivell2-Coop") {
			x = "/text/Nivell3-Coop";
		} else {
			x = "fi";
		}
		return x;
	}

}
