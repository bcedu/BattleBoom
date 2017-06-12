package principal.maquinaestat.estats;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import principal.GestorPrincipal;
import principal.eines.CarregadorRecursos;
import principal.elements.Arma;
import principal.elements.Jugador;
import principal.elements.JugadorIA;
import principal.mapas.Mapa;
import principal.maquinaestat.Estat;

/**
 * \brief Classe que gestiona una partida del joc.
 * 
 * Conte tots els Jugadors de la partida i el Mapa en el que estan. tambe
 * comprova l'estat de la partida i detecta les victories i derrotes.
 *
 */
public class EstatJoc implements Estat {

	/** Mapa en el que es juga la partida */
	private Mapa mapa;
	/** guarda el logotip del joc */
	private BufferedImage logo;
	/**
	 * array amb tots els jugadors del la partida, en la posicio 0 sempre hi ha
	 * el Jugador principla
	 */
	private Jugador[] jugadors; // jugador[0] es sempre el principal
	/** numero de Jugadors de la partida */
	private int numJ;
	/** varia segons l'estat de la partida */
	private int codiFinalitzacio = 0;
	/** utilitzat per esperarse un temps al acavar la partida */
	private int contadorFinal = 0;
	/** guarda el resultat (victoria o derrota) */
	private int resultat = 0;
	/** ruta al fitxer que conte la informacio de la partida amb la qual es crea */
	private static String fitxer;

	public EstatJoc(String[] info) {
		/**
		 * @pre info conté tota la informacio de la partida (Jugadors, Mapa,...)
		 * @post fitxer=vs, Jugadors guardats al array jugadors, mapa creat,
		 *       numJ conte el numero de jugadors
		 */

		fitxer = "vs";
		extreureInformacio(info);
	}

	public EstatJoc(String ruta) {
		/**
		 * @pre ruta condueix a un fitxer amb tota la informacio de la partida
		 * @post Jugadors guardats al array jugadors, mapa creat, numJ conte el
		 *       numero de jugadors
		 */
		fitxer = ruta;
		String[] info = CarregadorRecursos.llegirArxiuText(ruta).split("#");
		extreureInformacio(info);
	}

	private void extreureInformacio(String[] info) {
		/**
		 * @pre info conté tota la informacio de la partida (Jugadors, Mapa,...)
		 * @post Jugadors guardats al array jugadors, mapa creat, numJ conte el
		 *       numero de jugadors
		 */
		mapa = new Mapa(info[0]);
		logo = CarregadorRecursos.carregarImatgeCompatibleTranslucida(info[1]);
		int numA = Integer.parseInt(info[2]);
		int numE = Integer.parseInt(info[3]);
		numJ = numA + numE;
		jugadors = new Jugador[numJ];
		Jugador jsa[] = crearJugadors(info[4], numA);
		Jugador jse[] = crearJugadors(info[5], numE);

		for (int i = 0; i < jsa.length; i++) {
			jugadors[i] = jsa[i];
			for (int j = 0; j < jsa.length; j++) {
				if (i != j)
					jugadors[i].assignarAliat(jsa[j]);
			}
			for (int j = 0; j < jse.length; j++) {
				jugadors[i].assignarEnemic(jse[j]);
			}
		}
		for (int i = 0; i < jse.length; i++) {
			jugadors[i + jsa.length] = jse[i];
			for (int j = 0; j < jse.length; j++) {
				if (i + jsa.length - jsa.length != j)
					jugadors[i].assignarAliat(jse[j]);
			}
			for (int j = 0; j < jsa.length; j++) {
				jugadors[i + jsa.length].assignarEnemic(jsa[j]);
			}
		}

	}

	public int actualitzar() {
		/**
		 * @pre --
		 * @post s'actualitzaen tots els jugadors,retorna codiFinalitzacio que
		 *       actualitza el seu valor: -1 (derrota), 0 (continuatar jugant),
		 *       1 (victoria) , 2 (pausar el joc)
		 */
		if (codiFinalitzacio != 3) {
			contadorFinal = 0;
			codiFinalitzacio = 0;
			for (int i = 0; i < numJ; i++)
				jugadors[i].actualitzar();

			if (GestorPrincipal.getTeclat().getPausa()) {
				codiFinalitzacio = 2;
			}

			comprovarVictoria();
			comprovarDerrota();
		} else if (contadorFinal > 160) {
			codiFinalitzacio = resultat;
		} else
			contadorFinal++;
		return codiFinalitzacio;
	}

	private void comprovarVictoria() {
		/**
		 * @pre --
		 * @post si tots els enemics de jugador[0] tenen salut<=0,
		 *       codiFonalitzacio=3 i resultat=1
		 */
		boolean victoria = true;
		for (int i = 0; i < jugadors[0].getNumEnemics(); i++) {
			if (jugadors[0].getEnemic(i).getSaut() > 0)
				victoria = false;
		}
		if (victoria) {
			codiFinalitzacio = 3;
			resultat = 1;
		}
	}

	private void comprovarDerrota() {
		/**
		 * @pre --
		 * @post si tots els aliats de jugadors[0] i jugadors[0] tenen salut<=0,
		 *       codiFonalitzacio=3 i resultat=-1
		 */
		boolean derrota = true;
		for (int i = 0; i < jugadors[0].getNumAliats(); i++) {
			if (jugadors[0].getAliat(i).getSaut() > 0)
				derrota = false;
		}
		if (jugadors[0].getSaut() > 0)
			derrota = false;

		if (derrota) {
			resultat = -1;
			codiFinalitzacio = 3;
		}
	}

	public void dibuixar(Graphics g) {
		/**
		 * @pre --
		 * 
		 * @post mapa i jugadors dibuixats.
		 */
		GestorPrincipal.getSD().dibuixarEstatJoc(g, this);
	}

	private Jugador[] crearJugadors(String info, int nj) {
		/**
		 * @pre info conte tota la informacio necessaria per crear nj jugadors
		 * 
		 * @post retorna un array amb nj Jugadors
		 */
		Jugador[] js = new Jugador[nj];
		String[] parts;
		String[] j = info.split("\\*");
		for (int i = 0; i < nj; i++) {
			parts = j[i].split("_");
			String[] infoJugador = CarregadorRecursos.llegirArxiuText(parts[5]).split("_");
			Color c = assignarColor(Integer.parseInt(infoJugador[5]));

			if (Integer.parseInt(parts[4]) == 0 || Integer.parseInt(parts[4]) == 1) {
				js[i] = new Jugador(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), mapa, infoJugador[0],
						Integer.parseInt(infoJugador[1]), Integer.parseInt(infoJugador[2]),
						Integer.parseInt(infoJugador[3]), Integer.parseInt(infoJugador[4]), c,
						Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				js[i].assignarTeclas(Integer.parseInt(parts[4]));
			} else {
				js[i] = new JugadorIA(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), mapa, infoJugador[0],
						Integer.parseInt(infoJugador[1]), Integer.parseInt(infoJugador[2]),
						Integer.parseInt(infoJugador[3]), Integer.parseInt(infoJugador[4]), c,
						Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
			}
			js[i].assignarDistancia(Integer.parseInt(infoJugador[6]));
			js[i].getBomba().setRang(Integer.parseInt(infoJugador[7]));
			js[i].assignarVelocitat(Integer.parseInt(infoJugador[8]));
			String[] infoArma = infoJugador[10].split("\\|");
			for (int y = 0; y < Integer.parseInt(infoJugador[9]); y++) {
				String[] arm = infoArma[y].split(" ");
				js[i].assignarArma(new Arma(js[i].getX(), js[i].getY(), mapa, arm[0], Integer.parseInt(arm[1]), Integer
						.parseInt(arm[2]), Integer.parseInt(arm[3]), Integer.parseInt(arm[4]),
						Integer.parseInt(arm[6]), Integer.parseInt(arm[7]), Integer.parseInt(arm[8]), Integer
								.parseInt(arm[9])));
				js[i].getArma(y).assignarVelocitat(Integer.parseInt(arm[5]));
			}
		}
		return js;
	}

	private Color assignarColor(int s) {
		/**
		 * @pre --
		 * 
		 * @post retorna un Color diferent segons el numero entrat
		 */
		Color c;

		if (s == 1)
			c = Color.blue;
		else if (s == 2)
			c = Color.black;
		else if (s == 3)
			c = Color.red;
		else if (s == 4)
			c = Color.green;
		else
			c = Color.yellow;
		return c;
	}

	public Mapa getMapa() {
		/**
		 * @pre --
		 * 
		 * @post retorna mapa
		 */
		return mapa;
	}

	public BufferedImage getLogo() {
		/**
		 * @pre --
		 * 
		 * @post retorna logo
		 */
		return logo;
	}

	public Jugador getJugador(int i) {
		/**
		 * @pre i>=0 && i<numJ
		 * 
		 * @post retorna el Jugador de la posicio i del array de jugadors
		 */
		return jugadors[i];
	}

	public int getNumJ() {
		/**
		 * @pre --
		 * 
		 * @post retorna numJ
		 */
		return numJ;
	}

	public Jugador[] getJugadorsOrdenats() {
		/**
		 * @pre --
		 * 
		 * @post retorna un array amb els Jugadors ordenats per coordenada y de
		 *       petit a gran
		 */
		Jugador[] jo = new Jugador[numJ];
		for (int j = 0; j < numJ; j++) {
			jo[j] = jugadors[j];
		}
		Jugador aux;
		for (int i = 1; i < numJ; i++) {
			for (int j = 0; j < numJ - 1; j++) {
				if (jo[j].getY() > jo[j + 1].getY()) {
					aux = jo[j];
					jo[j] = jo[j + 1];
					jo[j + 1] = aux;
				}
			}
		}
		return jo;
	}

	@Override
	public void inicialitzar() {
		/**
		 * @pre --
		 * 
		 * @post codiFinalitzacio=0
		 */
		contadorFinal = 0;

	}

	public static String getFitxer() {
		/**
		 * @pre --
		 * 
		 * @post retorna fitxer
		 */
		return fitxer;
	}
}
