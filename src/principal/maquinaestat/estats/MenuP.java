package principal.maquinaestat.estats;

import principal.eines.CarregadorRecursos;

/**
 * \brief Classe menu de Pausa. Hereta de MenuInicial
 * 
 * Pausa l'estat de joc i dona les opcions de continuar, resetajar o tornar al
 * menuInicial
 *
 */

public class MenuP extends MenuInicial {

	public MenuP() {
		/**
		 * @pre --
		 * 
		 * @post imatges b1, b2, b3 i menu carregades
		 */
		menu = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuPausa/menuFons.png");
		b1 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuPausa/Botons1.png");
		b2 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuPausa/Botons2.png");
		b3 = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/MenuPausa/Botons3.png");
	}
}
