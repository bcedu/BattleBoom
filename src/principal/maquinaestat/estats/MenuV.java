package principal.maquinaestat.estats;

import java.awt.Graphics;

import principal.GestorPrincipal;
import principal.eines.CarregadorRecursos;

/**
 * \brief Menu de Victoria. Hereta de MenuInicial
 *
 * Mostra la pantalla de victoria a l'usuari i dona les opcions per passa al
 * seguent nivell o tornar al menu Inicial
 */

public class MenuV extends MenuInicial {

	public MenuV() {
		/**
		 * @pre --
		 * 
		 * @post imatges b1, b2 i menu carregades
		 */
		menu = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVArcade/menuFons.png");
		b1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVArcade/Botons1.png");
		b2 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuVArcade/Botons2.png");
	}

	public int actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post actualitza el menu Victoria cambiant d'opcio quan es neccessari
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
					codiFinalitzacio = -1;
				}
			}
			if (opcio >= 2)
				opcio = 0;
			if (opcio < 0)
				opcio = 1;
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
		 * @post mostra per pantalla el menu victoria
		 */
		GestorPrincipal.getSD().dibuixarMenuResultat(g, this);
	}
}