package principal.maquinaestat.estats;

import principal.eines.CarregadorRecursos;

/**
 * \brief Classe menu Arcade. Hereta de MenuInicial
 * 
 * Menu del mode Arcade on es trien les opcions per jugar aquest mode
 *
 */

public class MenuArcade extends MenuInicial {

	public MenuArcade() {
		/**
		 * @pre --
		 * 
		 * @post imatges b1, b2 i b3 carregades
		 */
		b1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuArcade/Botons1.png");
		b2 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuArcade/Botons2.png");
		b3 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuArcade/Botons3.png");
	}
}
