package principal.elements;

import java.awt.Graphics;
import java.awt.Rectangle;

import principal.GestorPrincipal;
import principal.mapas.Casella;
import principal.mapas.Mapa;
import principal.sprites.Sprite;

/**
 * \brief Bomba que tenen tots els Jugadors. Hereta de Item.
 * 
 * ArmaExplosiva que explota al cap de un temps al ser llançada. Tambe explota
 * si col.lisiona amb una Arma o una altre ArmaExplosiva que esta explotant.
 * Totes les Caselles destructibles dins el seu rang son destruides al explotar.
 * Si la explosio col.lisiona amb un Jugador, aquest perd tota la vida
 */
public class ArmaExplosiva extends Item {
	/** temporitzador per explotar */
	private int contador = 0;
	/** numero de caselles a les que arriba la explosio en cada direccio */
	private int rang = 2;
	/** indica sie s'esta explotant */
	private boolean explotant;
	/** indica si la ArmaExplosiva acava de ser llançada per un Jugador */
	private boolean recentLlancada = false;
	/** vector amb les Casella que son dins el rang de explosio */
	private Casella casellesRang[];
	/** Jugador propietari de la ArmaExplosiva */
	private Jugador j;
	/** valor al que ha de arribar el contador perque la bomba exploti */
	private int tempsExplosio = 300;

	public ArmaExplosiva(int posX, int posY, Mapa m, String ruta, Jugador jugador) {
		/**
		 * @pre ruta condueix a una FullaSprites valida.
		 * 
		 * @post ArmaExplosiva creada amb dades entrades.
		 */
		super(posX, posY, m, ruta);
		eliminat = true;
		explotant = false;
		margeH = 8;
		tamanyH = 16;
		margeV = 18;
		tamanyV = 14;
		j = jugador;
	}

	public void actualitzar() {
		/**
		 * @pre --
		 * 
		 * @post atributs de la ArmaExplosiva actualitzats.
		 */
		if (!eliminat) {
			contador++;
			if (contador < tempsExplosio) {
				animacioBomba();

				if (colisioArmes() || colisioArmesExplosives()) {
					contador = tempsExplosio;
					explotar();
				}
			} else if (contador == tempsExplosio) {
				explotar();
			} else if (contador > tempsExplosio && contador < tempsExplosio + 30) {
				// esperar, esta explotant
			} else if (contador >= tempsExplosio + 30 && contador < tempsExplosio + 45) {
				sprite = fulla.getSprite(3);
				// esperar, esta explotant
			} else {
				explotant = false;
				eliminat = true;
				contador = 0;
			}
			if (recentLlancada && !colisioJugador(j))
				recentLlancada = false;
		}
	}

	private void animacioBomba() {
		/**
		 * @pre !explotant i !eliminat
		 * 
		 * @post Sprite de la bomba canviat segons el contador.
		 */
		if (contador % 16 < 7)
			sprite = fulla.getSprite(0);
		else
			sprite = fulla.getSprite(1);
	}

	public void llancar(int px, int py) {
		/**
		 * @pre eliminat=true
		 * 
		 * @post ArmaExplosiva llançada a la posicio entrada, casellesRang
		 *       creades, eliminat=false i recentLlançada=true.
		 */
		x = px;
		y = py;
		eliminat = false;
		contador = 0;
		recentLlancada = true;
		crearCasellesRang();
	}

	private void explotar() {
		/**
		 * @pre eliminat=false.
		 * 
		 * @post ArmaExplosiva explota, Caselles de casellesRang destruides (si
		 *       eren destructibles), explotant=true i Sprite canviat.
		 */
		explotant = true;
		sprite = fulla.getSprite(2);
		for (int i = 0; i < casellesRang.length; i++) {
			if (casellesRang[i] != null && casellesRang[i].esDestructible())
				casellesRang[i].destruir();
		}

	}

	private void crearCasellesRang() {
		/**
		 * @pre !eliminat
		 * 
		 * @post casellesRang omplert amb les Caselles de mapa que estiguessin a
		 *       una distancia<rang i que no tinguessin Caselles destructibles a
		 *       la posicio anterior.
		 */
		casellesRang = new Casella[(rang * 4) + 1];
		int auxX = x + margeH + tamanyH / 2;
		int auxY = y + margeV + tamanyV / 2;
		for (int i = 0; i < rang; i++) {
			casellesRang[0 + (i * 4)] = mapa.getCasella((auxX) / Sprite.getTamany(),
					(auxY + (Sprite.getTamany() * (i + 1))) / Sprite.getTamany());
			casellesRang[1 + (i * 4)] = mapa.getCasella((auxX + (Sprite.getTamany() * (i + 1))) / Sprite.getTamany(),
					(auxY) / Sprite.getTamany());
			casellesRang[2 + (i * 4)] = mapa.getCasella((auxX - (Sprite.getTamany() * (i + 1))) / Sprite.getTamany(),
					(auxY) / Sprite.getTamany());
			casellesRang[3 + (i * 4)] = mapa.getCasella((auxX) / Sprite.getTamany(),
					(auxY - (Sprite.getTamany() * (i + 1))) / Sprite.getTamany());

		}
		casellesRang[rang * 4] = mapa.getCasella(auxX / Sprite.getTamany(), auxY / Sprite.getTamany());

		for (int i = 0; i < casellesRang.length - 1; i++) {
			if (i - 4 >= 0 && (casellesRang[i - 4] == null || casellesRang[i - 4].esDestructible()))
				casellesRang[i] = null;
			else if (casellesRang[i] != null && casellesRang[i].esSolid() && !casellesRang[i].esDestructible())
				casellesRang[i] = null;
		}
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post ArmaExplosiva dibuixada.
		 */
		if (explotant) {
			GestorPrincipal.getSD().dibuixarExplosio(g, this);
		} else
			GestorPrincipal.getSD().dibuixarItem(g, this);
	}

	public boolean dinsRangExplosio(Jugador it) {
		/**
		 * @pre it!=Null
		 * 
		 * @post si Jugador it esta a una Casella de casellerRang retorna true.
		 */
		boolean dins = false;
		for (int i = 0; i < casellesRang.length; i++) {
			if (casellesRang[i] != null) {
				if (it.getTotsLimits().intersects(casellesRang[i].getLimits()))
					dins = true;
			}
		}
		return dins;
	}

	public boolean dinsRangExplosio(Item it) {
		/**
		 * @pre it!=Null
		 * 
		 * @post si el Item it esta a una Casella de casellerRang retorna true.
		 */
		boolean dins = false;
		for (int i = 0; i < casellesRang.length; i++) {
			if (casellesRang[i] != null) {
				if (it.getLimits().intersects(casellesRang[i].getLimits()))
					dins = true;
			}
		}
		return dins;
	}

	private boolean colisioArmes() {
		/**
		 * @pre !eliminat
		 * 
		 * @post true si alguna Arma de j o dels aliats de j o dels enemics de j
		 *       col.lisiona amb ArmaExplosiva
		 */
		for (int i = 0; i < j.getNumArmas(); i++) {
			if (j.getArma(i).colisioItem(this) && !j.getArma(i).estaEliminat())
				return true;
		}
		for (int i = 0; i < j.getNumAliats(); i++) {
			for (int p = 0; p < j.getAliat(i).getNumArmas(); p++) {
				if (j.getAliat(i).getArma(p).colisioItem(this) && !j.getAliat(i).getArma(p).estaEliminat())
					return true;
			}
		}
		for (int i = 0; i < j.getNumEnemics(); i++) {
			for (int p = 0; p < j.getEnemic(i).getNumArmas(); p++) {
				if (j.getEnemic(i).getArma(p).colisioItem(this) && !j.getEnemic(i).getArma(p).estaEliminat())
					return true;
			}
		}
		return false;
	}

	private boolean colisioArmesExplosives() {
		/**
		 * @pre !eliminat
		 * 
		 * @post true si alguna ArmaExplosiva explotant dels aliats de j o dels
		 *       enemics de j col.lisiona amb ArmaExplosiva
		 */
		for (int i = 0; i < j.getNumAliats(); i++) {
			if (!j.getAliat(i).getBomba().estaEliminat() && j.getAliat(i).getBomba().dinsRangExplosio(this)
					&& j.getAliat(i).getBomba().estaExplotant())
				return true;

		}
		for (int i = 0; i < j.getNumEnemics(); i++) {
			if (!j.getEnemic(i).getBomba().estaEliminat() && j.getEnemic(i).getBomba().dinsRangExplosio(this)
					&& j.getEnemic(i).getBomba().estaExplotant())
				return true;
		}
		return false;
	}

	public boolean colisioJugador(Jugador e) {
		/**
		 * @pre e!=NULL
		 * 
		 * @post true e col.lisiona amb ArmaExplosiva
		 */
		Rectangle aux = e.getTotsLimits();
		boolean colisio = this.getLimits().intersects(aux);

		return colisio;
	}

	public boolean recentLlancada() {
		/**
		 * @pre --
		 * 
		 * @post retorna recentLlancada
		 */
		return recentLlancada;
	}

	public Casella[] getCasellesRang() {
		/**
		 * @pre --
		 * 
		 * @post casellesRang
		 */
		return casellesRang;
	}

	public boolean estaExplotant() {
		/**
		 * @pre --
		 * 
		 * @post retorna explotant
		 */
		return explotant;
	}

	public int getRang() {
		/**
		 * @pre --
		 * 
		 * @post retorna rang
		 */
		return rang;
	}

	public void setRang(int r) {
		/**
		 * @pre --
		 * 
		 * @post rang=r
		 */
		rang = r;
	}

}
