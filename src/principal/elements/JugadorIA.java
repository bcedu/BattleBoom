package principal.elements;

import java.awt.Color;

import principal.backtracking.Solucio;
import principal.backtracking.Solucionador;
import principal.mapas.Casella;
import principal.mapas.Mapa;
import principal.sprites.Sprite;

/**
 * \brief Jugador controlat per l'ordinador. Hereta de Jugador.
 * 
 * Es tracte de un Jugador controlat per l'ordinador. Es mou aleatoriament fins
 * que detecta un enemic a distancia<7, llavors va cap a ell. Si detecta un
 * enemic a distancia<3 llan�a una bomba. Si detecta un Jugador en la direccio
 * en la que esta mirant i no hi ha cap Item entre els dos, el dispara. Si ha
 * llan�at la seva bomba es queda quiet fins que aquesta exploti. Si no s'esta
 * movent sempre mira al jugador més proxim.Si es troba dins el rang d'explosio
 * de alguna ArmaExplosiva intentara sortir-ne. Si es vol moure cap una Casella
 * pero aquesta esta� dins el rang d'explosio d'alguna ArmaExplosiva no hi
 * anira.
 * 
 */

public class JugadorIA extends Jugador {
	/** true si s'esta movent cap a una Casella */
	private boolean moventCasella = false;

	/**
	 * utilitzat per saber quans píxels falten per moures a la següent Casella
	 */
	private int contadorMoviment = 0;

	/** true si s'ha calculat la posicio objectiu */
	private boolean calculantPosObj = false;

	/**
	 * utilitzat per saber les actualitzacions que han passat des de l'ultim cop
	 * que s'ha calculat la posicio objectiu
	 */
	private int contB = 0;

	/**
	 * Jugador enemic que es pren com a referencia per determinar el
	 * comportament del JugadorIA
	 */
	private Jugador objectiu;

	/** posicio dins el vector enemics del Jugador objectiu */
	private int idOj = 0;

	/** utilitzat per saber si em comprobat 4 direccions al calcular el moviment */
	private int intent = 0;

	/**
	 * classe que guarda el recorregut a seguir per arrivar a la posicio
	 * objectiu
	 */
	private Solucio camiObjectiu;

	public JugadorIA(int posX, int posY, Mapa m, String ruta, int th, int tv, int mh, int mv, Color c, int sx, int sy) {
		/**
		 * @pre ruta condueix a una FullaSprites valida.
		 * 
		 * @post JugadorIA creat amb dades entrades.
		 */
		super(posX, posY, m, ruta, th, tv, mh, mv, c, sx, sy);

	}

	public void actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post Armes, ArmaExplosiva, objectiu i atributs de JugadorIA
		 *       actualitzats.
		 */
		if (numArmes > 0)
			actualitzarArma();
		actualitzarArmaExplosiva();
		if (!eliminat) {
			actualitzarSeleccioObjectiu();
			if (!atacant)
				actualitzarMoviment();
			comprovarSalut();
		}
		contB++;
	}

	protected boolean comprovarAtacar() {
		/**
		 * @pre --
		 * 
		 * @post true si hi ha algun Jugador de enemics en la direccio que estem
		 *       mirant i no hi ha cap Item !eliminat i cap Casella solida entre
		 *       ells.
		 */
		boolean apuntant = false;
		if (direccio == 'n') {
			for (int i = 0; i < numEnemics; i++) {
				if ((enemics[i].getX() - 15) <= x && (enemics[i].getX() + 15) >= x && enemics[i].getY() < y
						&& enemics[i].getSaut() > 0) {
					apuntant = camiLliure(enemics[i].getX(), enemics[i].getY(), direccio);
				}
			}
		} else if (direccio == 's') {
			for (int i = 0; i < numEnemics; i++) {
				if ((enemics[i].getX() - 15) <= x && (enemics[i].getX() + 15) >= x && enemics[i].getY() > y
						&& enemics[i].getSaut() > 0) {
					apuntant = camiLliure(enemics[i].getX(), enemics[i].getY(), direccio);
				}
			}
		} else if (direccio == 'e') {
			for (int i = 0; i < numEnemics; i++) {
				if ((enemics[i].getY() - 15) <= y && (enemics[i].getY() + 15) >= y && enemics[i].getX() > x
						&& enemics[i].getSaut() > 0) {
					apuntant = camiLliure(enemics[i].getX(), enemics[i].getY(), direccio);
				}
			}
		} else {
			for (int i = 0; i < numEnemics; i++) {
				if ((enemics[i].getY() - 15) <= y && (enemics[i].getY() + 15) >= y && enemics[i].getX() < x
						&& enemics[i].getSaut() > 0) {
					apuntant = camiLliure(enemics[i].getX(), enemics[i].getY(), direccio);
				}
			}
		}

		return apuntant;
	}

	private boolean camiLliure(int px, int py, char d) {
		/**
		 * @pre dir =='n','s','e' o 'w'; px i py dins el mapa
		 * 
		 * @post true si no hi ha cap Item !eliminat i cap Casella solida la
		 *       posicio actual i la posicio px,py.
		 */
		if (bomba.recentLlancada())
			return false;
		boolean colisio = false;
		int cx = getCasellaX();
		int cy = getCasellaY();
		int cx2 = (px) / Sprite.getTamany();
		int cy2 = (py) / Sprite.getTamany();

		if (d == 'n') {
			while (cy > cy2) {
				if (mapa.getCasella(cx, cy).esSolid()) {
					colisio = true;
					cy = cy2;
				} else if (hiHaBombes(cx, cy)) {
					colisio = true;
					cy = cy2;
				} else {
					cy--;
				}
			}
		} else if (d == 's') {
			while (cy < cy2) {
				if (mapa.getCasella(cx, cy).esSolid()) {
					colisio = true;
					cy = cy2;
				} else if (hiHaBombes(cx, cy)) {
					colisio = true;
					cy = cy2;
				} else
					cy++;
			}
		} else if (d == 'w') {
			while (cx > cx2) {
				if (mapa.getCasella(cx, cy).esSolid()) {
					colisio = true;
					cx = cx2;
				} else if (hiHaBombes(cx, cy)) {
					colisio = true;
					cx = cx2;
				} else {
					cx--;
				}
			}
		} else { // if(d=='e')
			while (cx < cx2) {
				if (mapa.getCasella(cx, cy).esSolid()) {
					colisio = true;
					cx = cx2;
				} else if (hiHaBombes(cx, cy)) {
					colisio = true;
					cx = cx2;
				} else
					cx++;
			}
		}
		return !colisio;
	}

	private boolean hiHaBombes(int cx, int cy) {
		/**
		 * @pre cx i cy dins el mapa
		 * 
		 * @post true si hi ha alguna ArmaExplosiva en la Casella de la posicio
		 *       cx,cy.
		 */
		if (!bomba.estaEliminat() && bomba.getCasellaX() == cx && bomba.getCasellaY() == cy && !bomba.recentLlancada())
			return true;
		else {
			for (int i = 0; i < numEnemics; i++) {
				if (!enemics[i].getBomba().estaEliminat() && enemics[i].getBomba().getCasellaX() == cx
						&& enemics[i].getBomba().getCasellaY() == cy)
					return true;
			}
			for (int i = 0; i < numAliats; i++) {
				if (!aliats[i].getBomba().estaEliminat() && aliats[i].getBomba().getCasellaX() == cx
						&& aliats[i].getBomba().getCasellaY() == cy)
					return true;
			}
		}
		return false;
	}

	protected boolean comprovarLlancar() {
		/**
		 * @pre --
		 * 
		 * @post true hi ha un enemic a distancia<3
		 */
		Casella aux = mapa.getCasella(getCasellaX(), getCasellaY());
		for (int i = 0; i < numEnemics; i++) {
			if (!enemics[i].estaEliminat()) {
				Casella auxE = mapa.getCasella(enemics[i].getCasellaX(), enemics[i].getCasellaY());
				if (aux.distancia(auxE) < 3)
					return true;
			}
		}
		return false;
	}

	private void actualitzarSeleccioObjectiu() {
		/**
		 * @pre --
		 * 
		 * @post Jugador objectiu es el Jugador enemic !eliminat mes pròxim
		 */
		if (numEnemics > 0) {
			idOj = enemicMesProxim();
			objectiu = enemics[idOj];
		}
	}

	private int enemicMesProxim() {
		/**
		 * @pre --
		 * 
		 * @post retorna la posicio dins el vector enemics del Jugador més
		 *       proxim
		 */
		int aux = 0;
		for (int i = 1; i < numEnemics; i++) {
			if (enemics[aux].estaEliminat())
				aux = i;
			if (distancia(enemics[i]) <= distancia(enemics[aux]) && !enemics[i].estaEliminat())
				aux = i;
		}
		return aux;
	}

	protected void calcularMoviment() {
		/**
		 * @pre !eliminat i !atacant
		 * 
		 * @post despX i despY actualitzats.
		 */
		if (!foraRangExplosions(0, 0) && !moventCasella) {
			sortirRangsExplosions();
			calculantPosObj = false;
		} else if (foraRangExplosions(0, 0) && !moventCasella) {
			if (distancia(objectiu) > 2 && distancia(objectiu) < 7 && (!calculantPosObj || contB >= 300)) {
				calcularPosicioObjectiu();
			}
			if (calculantPosObj) {
				moureCasellaBack();
			} else {
				movimentAleatori();
			}
		}
		actualitzarMovimentCasella();
	}

	private void movimentAleatori() {
		/**
		 * @pre Jugador objectiu a distancia>=10, !fentBack, !moventCasella,
		 *      !eliminat i !atacant
		 * 
		 * @post despX, despY i direccio actualitzats per anar a la primera
		 *       Casella adjaçent lliure que trovi.
		 */

		if (comprovarDireccio() != 'f') {
			direccio = comprovarDireccio();
			int dx = 0;
			int dy = 0;
			if (direccio == 'n') {
				dy = -Sprite.getTamany();
			} else if (direccio == 's') {
				dy = Sprite.getTamany();
			} else if (direccio == 'e') {
				dx = Sprite.getTamany();
			} else {
				dx = -Sprite.getTamany();
			}
			moventCasella = distancia(objectiu) > 2 && foraRangExplosions(dx, dy) && bomba.estaEliminat();
			if (!moventCasella)
				direccio = direccioObjectiu();
		}
	}

	private char direccioObjectiu() {
		/**
		 * @pre --
		 * 
		 * @post retorna la direccio en que es troba el Jugador objectiu.
		 */
		int distY = (objectiu.getCasellaY() < getCasellaY()) ? getCasellaY() - objectiu.getCasellaY() : objectiu
				.getCasellaY() - getCasellaY();
		if (distY < 1)
			return (objectiu.getX() < x) ? 'w' : 'e';
		else
			return (objectiu.getY() < y) ? 'n' : 's';
	}

	private void moureCasellaBack() {
		/**
		 * @pre !eliminat, !atacant, !moventCasella i camiObjectiu!=null
		 * 
		 * @post despX, despY i direccio actualitzats per anar a la següent
		 *       Casella del recorregut de camiObjectiu.
		 */
		if (camiObjectiu.seguentCasella(getCasellaX(), getCasellaY()) != 'f') {
			direccio = camiObjectiu.seguentCasella(getCasellaX(), getCasellaY());
			int dx = 0;
			int dy = 0;
			if (direccio == 'n') {
				dy = -Sprite.getTamany();
			} else if (direccio == 's') {
				dy = Sprite.getTamany();
			} else if (direccio == 'e') {
				dx = Sprite.getTamany();
			} else {
				dx = -Sprite.getTamany();
			}
			moventCasella = distancia(objectiu) > 2 && foraRangExplosions(dx, dy) && bomba.estaEliminat();
			if (!moventCasella)
				direccio = direccioObjectiu();
		} else
			calculantPosObj = false;
	}

	private void calcularPosicioObjectiu() {
		/**
		 * @pre !moventCasella i !atacant i !eliminat. Distancia amb objectiu<8
		 * 
		 * @post camiObjectiu conte el recorregut a seguir per arribar a la
		 *       posicio del Jugador Objectiu
		 */
		contB = 0;
		camiObjectiu = new Solucio(mapa, objectiu, this);
		calculantPosObj = true;
		Solucionador algBack = new Solucionador();
		camiObjectiu = algBack.solucionar(camiObjectiu, getCasellaX(), getCasellaY());
	}

	private void sortirRangsExplosions() {
		/**
		 * @pre !moventCasella i !atacant i !eliminat
		 * 
		 * @post despX, despY i direccio actualitzats per anar a la Casella
		 *       adjacent que no estigui al ran d'explosio de cap ArmaExplosiva.
		 *       Si no n'hi ha cap es va a Casella aleatoria.
		 */
		if (!foraRangExplosions(0, 0)) {
			if (calcularDireccioForaRang() != 'f') {
				direccio = calcularDireccioForaRang();
				moventCasella = true;
			} else if (comprovarDireccio() != 'f') {
				direccio = comprovarDireccio();
				moventCasella = true;
			}
		}
	}

	private void actualitzarMovimentCasella() {
		/**
		 * @pre !atacant i !eliminat
		 * 
		 * @post contadorMoviment actualitzat. Si s'ha arribat a la Casella a la
		 *       que es movia, moventCasella=false.
		 */
		if (moventCasella && contadorMoviment < Sprite.getTamany() && camiCasellaLliure(direccio)) {
			if (direccio == 'n') {
				despY = -velocitat;
			} else if (direccio == 's') {
				despY = velocitat;
			} else if (direccio == 'w') {
				despX = -velocitat;
			} else {
				despX = velocitat;
			}
			contadorMoviment += velocitat;
			;
		} else if (moventCasella && contadorMoviment >= Sprite.getTamany()) {
			contadorMoviment = 0;
			moventCasella = false;
		}
	}

	private boolean foraRangExplosions(int dX, int dY) {
		/**
		 * @pre --
		 * 
		 * @post true si a la posicio x+dx,y+dy no ens trobem al rang d'explosio
		 *       de cap ArmaExplosiva.
		 */
		boolean fora = true;
		x += dX;
		y += dY;
		reestablirLimits();
		if (!bomba.estaEliminat() && bomba.dinsRangExplosio(this))
			fora = false;
		for (int i = 0; i < numEnemics; i++) {
			if (!enemics[i].getBomba().estaEliminat() && enemics[i].getBomba().dinsRangExplosio(this))
				fora = false;

		}
		for (int i = 0; i < numAliats; i++) {
			if (!aliats[i].getBomba().estaEliminat() && aliats[i].getBomba().dinsRangExplosio(this))
				fora = false;

		}
		x -= dX;
		y -= dY;
		reestablirLimits();
		return fora;
	}

	private char calcularDireccioForaRang() {
		/**
		 * @pre --
		 * 
		 * @post retorna la direccio en la que hi ha una Casella fora el rang
		 *       d'explosio de totes les ArmesExplosives. Si no n'hi ha retorna
		 *       'f'.
		 */
		if (foraRangExplosions(Sprite.getTamany(), 0) && camiCasellaLliure('e'))
			return 'e';

		if (foraRangExplosions(0, Sprite.getTamany()) && camiCasellaLliure('s'))
			return 's';

		if (foraRangExplosions(-Sprite.getTamany(), 0) && camiCasellaLliure('w'))
			return 'w';

		if (foraRangExplosions(0, -Sprite.getTamany()) && camiCasellaLliure('n'))
			return 'n';

		return 'f';
	}

	private char comprovarDireccio() {
		/**
		 * @pre --
		 * 
		 * @post retorna la la primera direccio en la que ens podem moure sense
		 *       col.lisionar amb cap Item i Casella. Si no n'hi ha retorna 'f'.
		 */
		if (direccio == 'e') {
			if (camiCasellaLliure('e')) {
				intent = 0;
				return 'e';
			} else if (intent < 4) {
				direccio = 's';
				intent++;
				comprovarDireccio();
			} else {
				intent = 0;
				return 'f';
			}
		} else if (direccio == 's') {
			if (camiCasellaLliure('s')) {
				intent = 0;
				return 's';
			} else if (intent < 2) {
				direccio = 'w';
				intent++;
				comprovarDireccio();
			} else if (intent >= 2) {
				direccio = 'e';
				intent++;
				comprovarDireccio();
			} else {
				intent = 0;
				return 'f';
			}
		} else if (direccio == 'w') {
			if (camiCasellaLliure('w')) {
				intent = 0;
				return 'w';
			} else if (intent < 4) {
				direccio = 'n';
				intent++;
				comprovarDireccio();
			} else {
				intent = 0;
				return 'f';
			}
		} else if (direccio == 'n') {
			if (camiCasellaLliure('n')) {
				intent = 0;
				return 'n';
			} else if (intent < 2) {
				direccio = 'w';
				intent++;
				comprovarDireccio();
			} else if (intent >= 2) {
				direccio = 'e';
				intent++;
				comprovarDireccio();
			} else {
				intent = 0;
				return 'f';
			}
		}
		return 'f';
	}

	private boolean camiCasellaLliure(char d) {
		/**
		 * @pre --
		 * 
		 * @post retorna true si el cami cap a la Casella que esta en la
		 *       direccio d està lliure
		 */
		boolean lliure = true;
		int i = 0;
		if (d == 'n') {
			while (i < (Sprite.getTamany() - contadorMoviment) && lliure) {
				y -= 1;
				reestablirLimits();
				lliure = !colisioMapa(0, 0) && !colisioEnemics() && (!colisioItem(bomba) || bomba.recentLlancada());
				i++;
			}
			y += i;
			reestablirLimits();
			return lliure;
		} else if (d == 's') {
			while (i < (Sprite.getTamany() - contadorMoviment) && lliure) {
				y += 1;
				reestablirLimits();
				lliure = !colisioMapa(0, 0) && !colisioEnemics() && (!colisioItem(bomba) || bomba.recentLlancada());
				i++;
			}
			y -= i;
			reestablirLimits();
			return lliure;
		} else if (d == 'e') {
			while (i < (Sprite.getTamany() - contadorMoviment) && lliure) {
				x += 1;
				reestablirLimits();
				lliure = !colisioMapa(0, 0) && !colisioEnemics() && (!colisioItem(bomba) || bomba.recentLlancada());
				i++;
			}
			x -= i;
			reestablirLimits();
			return lliure;
		} else {
			while (i < (Sprite.getTamany() - contadorMoviment) && lliure) {
				x -= 1;
				reestablirLimits();
				lliure = !colisioMapa(0, 0) && !colisioEnemics() && (!colisioItem(bomba) || bomba.recentLlancada());
				i++;
			}
			x += i;
			reestablirLimits();
			return lliure;
		}
	}

}
