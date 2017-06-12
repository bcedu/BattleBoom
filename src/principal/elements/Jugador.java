package principal.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import principal.GestorPrincipal;
import principal.mapas.Mapa;
import principal.sprites.Sprite;

/**
 * \brief Item controlat per un Huma�. Hereta de Item.
 * 
 * Es tracte de un personatge que es pot moure, pot atacar (mitjancant Arma) i
 * llancar bombes (ArmaExplosiva). Te una salut determinada, es controla per
 * Teclat i te Jugadors aliats i enemics.
 * 
 */
public class Jugador extends Item {
	/** vida que li queda al Jugador */
	protected int salut = 100;

	/** nivell maxim i inicial de salut del Jugador. */
	protected int salutMax = 100;

	/** numero de pixels que s'avancen per actualització */
	protected int velocitat = 1;

	/** indica si el Jugador pot perdre vida */
	protected boolean invulnerable = false;

	/** pot ser 'n','s','e','w', indica cap on es mou */
	protected char direccio = 's';

	/** contador utilitzat fer les animacions de moviment */
	protected int contadorAnimacio = 0;

	/** contador utilitzat per determinar el temps que s'es invulnerable */
	protected int contadorInvulnerable = 0;

	/** contador utilitzat per determinar el temps entre entre dos atacs */
	protected int contadorAtac = 0;

	/** indica si el Jugador esta� en moviment */
	protected boolean enMoviment = false;

	/** indica si el Jugador esta� atacant */
	protected boolean atacant = false;

	/** color de la barra de salut */
	protected Color color;

	/** posicio horitzontal (pixels) de la barra de salut */
	protected int posSx;

	/** posicio vertical (pixels) de la barra de salut */
	protected int posSy;

	/** pixels a desplacar-se horitzontalment */
	protected int despX = 0;

	/** pixels a desplacar-se verticalment */
	protected int despY = 0;

	/** rectangle de col.lisions quan direccio=='n' */
	protected final Rectangle LIMIT_NORD;
	/** rectangle de col.lisions quan direccio=='s' */
	protected final Rectangle LIMIT_SUD;
	/** rectangle de col.lisions quan direccio=='e' */
	protected final Rectangle LIMIT_EST;
	/** rectangle de col.lisions quan direccio=='w' */
	protected final Rectangle LIMIT_OEST;

	/** indica quina combinacio de tecles del Teclat s'utilitzen */
	protected int combinacioTecles;

	/** ArmaExplosiva del jugador */
	protected ArmaExplosiva bomba;

	/** píxels a la que es llança la bomba */
	protected int distanciaLlancament = 0;

	/** vector amb les Armes del Jugador */
	protected Arma[] armes;

	/** vector amb els Jugadors enemics */
	protected Jugador[] enemics;

	/** vector amb els Jugadors aliats */
	protected Jugador[] aliats;

	/** indica els enemics actuals */
	protected int numEnemics = 0;

	/** indica els aliats actuals (màxim 3) */
	protected int numAliats = 0;

	/** indica les Armes actuals (màxim 3) */
	protected int numArmes = 0;

	public Jugador(int posX, int posY, Mapa m, String ruta, int mH, int mV, int tH, int tV, Color c, int sx, int sy) {
		/**
		 * @pre ruta condueix a una FullaSprites valida.
		 * 
		 * @post Jugador creat amb dades entrades.
		 */
		super(posX, posY, m, ruta, mH, mV, tH, tV);

		bomba = new ArmaExplosiva(x, y, m, "/IMG/Textures/fullaBomba.png", this);
		combinacioTecles = 0;
		enemics = new Jugador[3];
		aliats = new Jugador[3];
		armes = new Arma[2];
		color = c;
		posSx = sx;
		posSy = sy;
		LIMIT_NORD = new Rectangle((int) x + margeH, (int) y + margeV, tamanyH, 1);
		LIMIT_SUD = new Rectangle((int) x + margeH, (int) y + tamanyV + margeV, tamanyH, 1);
		LIMIT_EST = new Rectangle((int) x + margeH + tamanyH, (int) y + margeV, 1, tamanyV);
		LIMIT_OEST = new Rectangle((int) x + margeH, (int) y + margeV, 1, tamanyV);

	}

	public void assignarEnemic(Jugador e) {
		/**
		 * @pre --
		 * 
		 * @post si numEnemics < 3, e afegit al vector de enemics i
		 *       numEnemics++.
		 */
		if (numEnemics < enemics.length) {
			enemics[numEnemics] = e;
			numEnemics++;
		}
	}

	public void assignarAliat(Jugador e) {
		/**
		 * @pre --
		 * 
		 * @post si numAliats < 3, e afegit al vector de aliats i numAliats++.
		 */
		if (numAliats < aliats.length) {
			aliats[numAliats] = e;
			numAliats++;
		}
	}

	public void assignarArma(Arma a) {
		/**
		 * @pre --
		 * 
		 * @post si numArmes < 2, a afegit al vector armes i numArmes++.
		 */
		armes[numArmes] = a;
		numArmes++;
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post sprite del Jugador dibuixat a la posicio x,y. Barra de salut
		 *       dibuixada a la posició posSx,posSy.
		 */
		GestorPrincipal.getSD().dibuixarJugador(g, this);
	}

	public void actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post Armes, ArmaExplosiva i atributs de Jugador actualitzats.
		 */
		if (numArmes > 0)
			actualitzarArma();
		actualitzarArmaExplosiva();
		if (!eliminat) {
			if (!atacant)
				actualitzarMoviment();
			comprovarSalut();
		}
	}

	protected void actualitzarArma() {
		/**
		 * @pre --
		 * 
		 * @post armes del Jugador actualitzades.
		 */
		if (comprovarAtacar() && !armasDisparadas() && !eliminat) {
			for (int i = 0; i < numArmes; i++)
				armes[i].llancarAtac(x, y, direccio);

			atacant = true;
			animacioDisparar();
		} else if (armasDisparadas()) {
			comprovarColisionsArmas();
			for (int j = 0; j < numArmes; j++)
				if (!armes[j].estaEliminat())
					armes[j].actualitzar();
		}
		if (atacant)
			contadorAtac++;
		if (contadorAtac > 30) {
			atacant = false;
			contadorAtac = 0;
		}
	}

	protected boolean armasDisparadas() {
		/**
		 * @pre --
		 * 
		 * @post true si alguna Arma del vector armes no està eliminada.
		 */
		boolean at = false;
		for (int i = 0; i < numArmes; i++) {
			if (!armes[i].estaEliminat())
				at = true;
		}
		return at;
	}

	protected boolean comprovarAtacar() {
		/**
		 * @pre --
		 * 
		 * @post true si s'ha pitjat la tecla de atacar del Teclat.
		 */
		return GestorPrincipal.getTeclat().getAtacar(combinacioTecles);
	}

	protected void comprovarColisionsArmas() {
		/**
		 * @pre --
		 * 
		 * @post si hi ha col.lisio entre alguna Arma i algun Jugador (de aliats
		 *       o enemics), aquest rep dany i la arma s'elimina.
		 */
		for (int j = 0; j < numArmes; j++) {
			for (int i = 0; i < numEnemics; i++) {
				for (int a = 0; a < enemics[i].getNumArmas(); a++) {
					if (armes[j].colisioItem(enemics[i].getArma(a)) && !armes[j].getExplotant())
						armes[j].finalitzarAtac();
				}
				if (armes[j].colisioJugador(enemics[i]) && !armes[j].getExplotant()) {
					enemics[i].rebreDany(25);
					armes[j].finalitzarAtac();
				}
				if (armes[j].colisioItem(enemics[i].getBomba()) && !armes[j].getExplotant())
					armes[j].finalitzarAtac();
			}
			for (int i = 0; i < numAliats; i++) {
				for (int a = 0; a < enemics[i].getNumArmas(); a++) {
					if (armes[j].colisioItem(enemics[i].getArma(a)) && !armes[j].getExplotant())
						armes[j].finalitzarAtac();
				}
				if (armes[j].colisioJugador(aliats[i]) && !armes[j].getExplotant()) {
					aliats[i].rebreDany(25);
					armes[j].finalitzarAtac();
				}
				if (armes[j].colisioItem(aliats[i].getBomba()) && !armes[j].getExplotant())
					armes[j].finalitzarAtac();
			}
			if (armes[j].colisioItem(bomba) && !armes[j].getExplotant())
				armes[j].finalitzarAtac();
		}
	}

	protected void animacioDisparar() {
		/**
		 * @pre Jugador atacant
		 * 
		 * @post Sprite canviat segons direccio.
		 */
		int seleccioSpr = 0;
		if (invulnerable)
			seleccioSpr = 2;

		if (direccio == 'n')
			sprite = fulla.getSprite(3, 1 + seleccioSpr);
		else if (direccio == 's')
			sprite = fulla.getSprite(2, 1 + seleccioSpr);
		else if (direccio == 'w')
			sprite = fulla.getSprite(5, 1 + seleccioSpr);
		else if (direccio == 'e')
			sprite = fulla.getSprite(4, 1 + seleccioSpr);
	}

	protected void actualitzarArmaExplosiva() {
		/**
		 * @pre --
		 * 
		 * @post ArmaExplosiva del Jugador actualitzada.
		 */
		if (comprovarLlancar() && bomba.estaEliminat() && !eliminat) {
			llancarArmaExplosiva();
		} else if (!bomba.estaEliminat()) {
			bomba.actualitzar();
			if (bomba.estaExplotant())
				comprovarDanysArmaExplosiva();
		}
	}

	protected void llancarArmaExplosiva() {
		/**
		 * @pre bomba esta eliminada
		 * 
		 * @post bomba llançada a la posicio actual + distanciaLlancament.
		 */
		if (direccio == 'n')
			bomba.llancar(x, y - distanciaLlancament);
		else if (direccio == 's')
			bomba.llancar(x, y + distanciaLlancament);
		else if (direccio == 'w')
			bomba.llancar(x - distanciaLlancament, y);
		else
			bomba.llancar(x + distanciaLlancament, y);
	}

	protected void comprovarDanysArmaExplosiva() {
		/**
		 * @pre bomba esta explotant
		 * 
		 * @post si Jugador actual o algun Jugador enemic o aliat esta dins el
		 *       rang d'explosio, aquell jugador perd tota la seva salut
		 */
		if (bomba.dinsRangExplosio(this))
			rebreDanyArmaExplosiva(salutMax);
		for (int i = 0; i < numEnemics; i++) {
			if (bomba.dinsRangExplosio(enemics[i]))
				enemics[i].rebreDanyArmaExplosiva(enemics[i].getSautMax());
		}
		for (int i = 0; i < numAliats; i++) {
			if (bomba.dinsRangExplosio(aliats[i]))
				aliats[i].rebreDanyArmaExplosiva(aliats[i].getSautMax());
		}
	}

	protected boolean comprovarLlancar() {
		/**
		 * @pre --
		 * 
		 * @post true si s'ha pitjat la tecla de bomba del Teclat.
		 */
		return GestorPrincipal.getTeclat().getBomba(combinacioTecles);
	}

	protected void actualitzarMoviment() {
		/**
		 * @pre Jugador !eliminat i !atacant
		 * 
		 * @post posicio x i y actualitzats
		 */
		calcularMoviment();
		if (despX != 0 || despY != 0) {
			enMoviment = true;
			moure(despX, despY);
		} else
			enMoviment = false;

		animar();
		despX = 0;
		despY = 0;
	}

	protected void calcularMoviment() {
		/**
		 * @pre !eliminat i !atacant
		 * 
		 * @post despX i despY actualitzats segons tecles pitjades.
		 */
		if (GestorPrincipal.getTeclat().getAmunt(combinacioTecles))
			despY -= velocitat;
		else if (GestorPrincipal.getTeclat().getAvall(combinacioTecles))
			despY += velocitat;
		else if (GestorPrincipal.getTeclat().getDreta(combinacioTecles))
			despX += velocitat;
		else if (GestorPrincipal.getTeclat().getEsquerra(combinacioTecles))
			despX -= velocitat;
	}

	protected void moure(int despX, int despY) {
		/**
		 * @pre despX || despY > 0
		 * 
		 * @post el Jugador es mou si no es produeixen col.lisions: direccio
		 *       actualitzada segons despX i despY; x i y actualitzades.
		 */
		if (despX > 0)
			direccio = 'e';
		else if (despX < 0)
			direccio = 'w';
		else if (despY > 0)
			direccio = 's';
		else if (despY < 0)
			direccio = 'n';

		if (!colisioMapa(0, despY) && (!colisioItem(bomba) || bomba.recentLlancada()) && !colisioEnemics()) {
			y = y + despY;
		}
		if (!colisioMapa(despX, 0) && (!colisioItem(bomba) || bomba.recentLlancada()) && !colisioEnemics()) {
			x = x + despX;
		}
		reestablirLimits();
	}

	protected void comprovarSalut() {
		/**
		 * @pre !eliminat
		 * 
		 * @post s'actualitza el contador de invulnerabilitat. Si salut<0
		 *       eliminat=true.
		 */
		if (invulnerable)
			contadorInvulnerable++;
		if (contadorInvulnerable > 120) {
			invulnerable = false;
			contadorInvulnerable = 0;
		}
		if (salut <= 0) {
			eliminat = true;
			sprite = fulla.getSprite(6, 1);
		}
	}

	protected boolean colisioEnemics() {
		/**
		 * @pre --
		 * 
		 * @post true si es col.lisiona amb algun Jugador enemic o aliat o amb
		 *       alguna de les seves bombes
		 */
		boolean colisio = false;
		for (int i = 0; i < numEnemics; i++) {
			if (colisioJugador(enemics[i]) || colisioItem(enemics[i].getBomba()))
				colisio = true;
		}
		for (int i = 0; i < numAliats; i++) {
			if (colisioJugador(aliats[i]) || colisioItem(aliats[i].getBomba()))
				colisio = true;
		}
		return colisio;
	}

	public boolean colisioMapa(int dX, int dY) {
		/**
		 * @pre --
		 * 
		 * @post retorna true si a la posicio actual amb els desplaçament entrat
		 *       col.lisionem amb alguna Casella solida del mapa.
		 */
		moureLimits(dX, dY);

		boolean colisio = false;

		int coordenadaX = getLimits().x / Sprite.getTamany();
		int coordenadaX2 = (getLimits().x + getLimits().width) / Sprite.getTamany();
		int coordenadaY = getLimits().y / Sprite.getTamany();
		int coordenadaY2 = (getLimits().y + getLimits().height) / Sprite.getTamany();

		if (colisioCasella(coordenadaX, coordenadaY) || colisioCasella(coordenadaX2, coordenadaY2))
			colisio = true;

		reestablirLimits();
		return colisio;
	}

	protected void animar() {
		/**
		 * @pre !eliminat
		 * 
		 * @post sprite cambiat segons direccio, contadorAnimacio,
		 *       invulnerabilitat i enMoviment.
		 */
		if (contadorAnimacio < 32767)
			contadorAnimacio++;
		else
			contadorAnimacio = 0;

		int seleccioSpr = 0;
		if (invulnerable)
			seleccioSpr = 2;

		int residoV = contadorAnimacio % 32;
		int residoH = contadorAnimacio % 32;
		if (direccio == 'n') {
			sprite = fulla.getSprite(3, 0 + seleccioSpr);
			if (enMoviment) {
				if (residoV > (8) && residoV <= 16)
					sprite = fulla.getSprite(4, 0 + seleccioSpr);
				else if (residoV > 16 && residoV <= 24)
					sprite = fulla.getSprite(3, 0 + seleccioSpr);
				else if (residoV > 24)
					sprite = fulla.getSprite(5, 0 + seleccioSpr);
				else
					sprite = fulla.getSprite(3, 0 + seleccioSpr);
			}
		} else if (direccio == 's') {
			sprite = fulla.getSprite(0, 0 + seleccioSpr);
			if (enMoviment) {
				if (residoV > 8 && residoV <= 16)
					sprite = fulla.getSprite(1, 0 + seleccioSpr);
				else if (residoV > 16 && residoV <= 24)
					sprite = fulla.getSprite(0, 0 + seleccioSpr);
				else if (residoV > 24)
					sprite = fulla.getSprite(2, 0 + seleccioSpr);
				else
					sprite = fulla.getSprite(0, 0 + seleccioSpr);
			}

		} else if (direccio == 'w') {
			sprite = fulla.getSprite(6, 0 + seleccioSpr);
			if (enMoviment) {
				if (residoH > 8 && residoH <= 16)
					sprite = fulla.getSprite(7, 0 + seleccioSpr);
				else if (residoH > 16 && residoH <= 24)
					sprite = fulla.getSprite(6, 0 + seleccioSpr);
				else if (residoH > 24)
					sprite = fulla.getSprite(8, 0 + seleccioSpr);
				else
					sprite = fulla.getSprite(6, 0 + seleccioSpr);
			}

		} else if (direccio == 'e') {
			sprite = fulla.getSprite(9, 0 + seleccioSpr);
			if (enMoviment) {
				if (residoH > 8 && residoH <= 16)
					sprite = fulla.getSprite(0, 1 + seleccioSpr);
				else if (residoH > 16 && residoH <= 24)
					sprite = fulla.getSprite(9, 0 + seleccioSpr);
				else if (residoH > 24)
					sprite = fulla.getSprite(1, 1 + seleccioSpr);
				else
					sprite = fulla.getSprite(9, 0 + seleccioSpr);
			}
		}
	}

	protected void moureLimits(int dX, int dY) {
		/**
		 * @pre --
		 * 
		 * @post limits de col.lisions moguts dX horitzontakment i dY
		 *       verticalment
		 */
		LIMIT_NORD.x += dX;
		LIMIT_NORD.y += dY;
		LIMIT_SUD.x += dX;
		LIMIT_SUD.y += dY;
		LIMIT_EST.x += dX;
		LIMIT_EST.y += dY;
		LIMIT_OEST.x += dX;
		LIMIT_OEST.y += dY;
	}

	protected void reestablirLimits() {
		/**
		 * @pre --
		 * 
		 * @post limits de col.lisions a la posicio x, y.
		 */
		LIMIT_NORD.x = x + margeH;
		LIMIT_NORD.y = y + margeV;
		LIMIT_SUD.x = x + margeH;
		LIMIT_SUD.y = y + margeV + tamanyV;
		LIMIT_EST.x = x + margeH + tamanyH;
		LIMIT_EST.y = y + margeV;
		LIMIT_OEST.x = x + margeH;
		LIMIT_OEST.y = y + margeV;
	}

	public Rectangle getLimits() {
		/**
		 * @pre --
		 * 
		 * @post retorna un rectanlge de colisions segons la direcció.
		 */
		if (direccio == 'n')
			return LIMIT_NORD;
		else if (direccio == 's')
			return LIMIT_SUD;
		else if (direccio == 'w')
			return LIMIT_OEST;
		else
			return LIMIT_EST;// if (direccio == 'e')
	}

	public Rectangle getTotsLimits() {
		/**
		 * @pre --
		 * 
		 * @post retorna el rectangle de col.lisions de totes les direccions
		 */
		return new Rectangle(x + margeH, y + margeV, tamanyH, tamanyV);
	}

	public void rebreDany(int m) {
		/**
		 * @pre --
		 * 
		 * @post si !invulnerable es resta m a salut
		 */
		if (!invulnerable) {
			invulnerable = true;
			salut -= m;
			if (salut < 0)
				salut = 0;
		}
	}

	public void rebreDanyArmaExplosiva(int m) {
		/**
		 * @pre --
		 * 
		 * @post es resta m a salut
		 */
		if (!invulnerable) {
			invulnerable = true;
		}
		salut -= m;
		if (salut < 0)
			salut = 0;
	}

	public void assignarTeclas(int ct) {
		/**
		 * @pre ct=0 || ct=1
		 * 
		 * @post combinacioTecles=ct
		 */
		combinacioTecles = ct;
	}

	public void assignarDistancia(int d) {
		/**
		 * @pre --
		 * 
		 * @post distanciaLlançament=d
		 */
		distanciaLlancament = d;
	}

	public ArmaExplosiva getBomba() {
		/**
		 * @pre --
		 * 
		 * @post retorna bomba
		 */
		return bomba;
	}

	public Arma getArma(int i) {
		/**
		 * @pre i<2 && i>=0
		 * 
		 * @post retorna la Arma de la posicio i del vector armes
		 */
		return armes[i];
	}

	public boolean colisioJugador(Jugador e) {
		/**
		 * @pre e!=NULL
		 * 
		 * @post true si es col.lisiona amb e
		 */
		if (e.estaEliminat())
			return false;
		Rectangle aux = e.getTotsLimits();
		boolean colisio = this.getLimits().intersects(aux);

		return colisio;
	}

	public int getSautMax() {
		/**
		 * @pre --
		 * 
		 * @post retorna salutMax
		 */
		return salutMax;
	}

	public int getSaut() {
		/**
		 * @pre --
		 * 
		 * @post retorna salut
		 */
		return salut;
	}

	public int getNumArmas() {
		/**
		 * @pre --
		 * 
		 * @post retorna numArmes
		 */
		return numArmes;
	}

	public Jugador getEnemic(int i) {
		/**
		 * @pre i<3 && i>=0
		 * 
		 * @post retorna el Jugador de la posicio i de enemics
		 */
		return enemics[i];
	}

	public int getNumEnemics() {
		/**
		 * @pre --
		 * 
		 * @post retorna numEnemics
		 */
		return numEnemics;
	}

	public Jugador getAliat(int i) {
		/**
		 * @pre i<3 && i>=0
		 * 
		 * @post retorna el Jugador de la posicio i de aliats
		 */
		return aliats[i];
	}

	public int getNumAliats() {
		/**
		 * @pre --
		 * 
		 * @post retorna numAliats
		 */
		return numAliats;
	}

	public int getSx() {
		/**
		 * @pre --
		 * 
		 * @post retorna posSx
		 */
		return posSx;
	}

	public int getSy() {
		/**
		 * @pre --
		 * 
		 * @post retorna posSy
		 */
		return posSy;
	}

	public Color getColor() {
		/**
		 * @pre --
		 * 
		 * @post retorna color
		 */
		return color;
	}

	public void assignarVelocitat(int v) {
		/**
		 * @pre --
		 * 
		 * @post velocitat=v
		 */
		velocitat = v;
	}
}
