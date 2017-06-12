package principal.maquinaestat.estats;

import principal.eines.CarregadorRecursos;

/**
 * \brief Classe Menu derrota. Hereda de menu Victoria
 * 
 * Mostra la pantalla de derrota a l'usuari i li dona la opcio de tornar a jugar
 * o tornar al menu inicial
 * 
 */

public class MenuD extends MenuV {

	public MenuD() {
		/**
		 * @pre --
		 *
		 * @post imatges menu, b1 i b3 carregades
		 */
		menu = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuDArcade/menuFons.png");
		b1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuDArcade/Botons1.png");
		b2 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuDArcade/Botons2.png");
	}
}