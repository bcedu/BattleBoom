package principal.grafics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import principal.GestorPrincipal;
import principal.elements.ArmaExplosiva;
import principal.elements.Item;
import principal.elements.Jugador;
import principal.mapas.Mapa;
import principal.maquinaestat.GestorEstats;
import principal.maquinaestat.estats.EstatJoc;
import principal.maquinaestat.estats.MenuFinal;
import principal.maquinaestat.estats.MenuInicial;
import principal.maquinaestat.estats.MenuV;
import principal.maquinaestat.estats.MenuVersus;
import principal.sprites.Sprite;

/**
 * \brief Classe que implementa Canvas.
 * 
 * La SuperficieDibuix es la classe encarregada de dibuixar tots els Items i
 * Menus del joc.
 */

public class SuperficieDibuix extends Canvas {

	private static final long serialVersionUID = 6257538465563164340L;

	/** tamany horitzontal en píxels */
	private static double amplada;

	/** tamany vertical en píxels */
	private static double altura;

	/** escalat horitzontal que s'aplicar a l'amplada */
	private double escalatAmplada;

	/** escalat vertical que s'aplicar a la altura */
	private double escalatAltura;

	public SuperficieDibuix(final double am, final double al) {
		/**
		 * @pre --
		 * 
		 * @post SuperficieDibuix creada amb amplada i altura entrats.
		 */
		amplada = am;
		altura = al;

		escalatAmplada = 1.1;
		escalatAltura = 1.1;

		setIgnoreRepaint(true);
		setPreferredSize(new Dimension((int) (amplada * escalatAmplada), (int) (altura * escalatAltura)));
		addKeyListener(GestorPrincipal.getTeclat());
		setFocusable(true);
		requestFocus();
	}

	public void dibuixar(final GestorEstats ge) {
		/**
		 * @pre ge!=Null
		 * 
		 * @post estat actual del GestorEstats ge dibuixat.
		 */
		BufferStrategy buffer = getBufferStrategy();
		if (buffer == null) {
			createBufferStrategy(4);
			return;
		}
		Graphics2D g = (Graphics2D) buffer.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) (amplada * escalatAmplada), (int) (altura * escalatAltura));

		if (escalatAmplada != 1 && escalatAltura != 1)
			g.scale(escalatAmplada, escalatAltura);
		ge.dibuixar(g);

		g.setColor(Color.green);

		Toolkit.getDefaultToolkit().sync();

		g.dispose();

		buffer.show();
	}

	public void dibuixarItem(Graphics g, Item i) {
		/**
		 * @pre i!=Null
		 * 
		 * @post sprite actual del Item i dibuixat en la seva posicio actual.
		 */
		g.drawImage(i.getSprite().getImatge(), i.getX(), i.getY(), null);
	}

	public void dibuixarEstatJoc(Graphics g, EstatJoc ej) {
		/**
		 * @pre ej!=Null
		 * 
		 * @post mapa i Jugadors de ej dibuixats.
		 */
		ej.getMapa().dibuixar(g);
		g.drawImage(ej.getLogo(), 764 - ej.getLogo().getWidth(), 480 - ej.getLogo().getHeight(), null);
		Jugador[] jugadorsOrdenats = ej.getJugadorsOrdenats();
		for (int i = 0; i < ej.getNumJ(); i++) {
			jugadorsOrdenats[i].dibuixar(g);
		}
	}

	public void dibuixarMapa(Graphics g, Mapa m) {
		/**
		 * @pre m!=null
		 * 
		 * @post totes les Caselles de la quadricula s'han dibuixat en les seves
		 *       posicions
		 */
		for (int y = 0; y < m.getAltura(); y++) {
			for (int x = 0; x < m.getAmplada(); x++) {
				g.drawImage(m.getCasella(x, y).getSprite().getImatge(), (x * Sprite.getTamany()),
						(y * Sprite.getTamany()), null);
			}
		}
		g.setColor(Color.green);
	}

	public void dibuixarJugador(Graphics g, Jugador j) {
		/**
		 * @pre j!=Null
		 * 
		 * @post sprite actual del Jugador j dibuixat en la seva posicio actual.
		 *       Barra de salut, Armes i ArmaExplosiva de j dibuixades.
		 */
		g.setColor(j.getColor());
		g.drawRect(j.getSx(), j.getSy(), j.getSautMax() * 2, 5);
		g.fillRect(j.getSx(), j.getSy(), j.getSaut() * 2, 5);

		if (!j.getBomba().estaEliminat())
			j.getBomba().dibuixar(g);
		for (int i = 0; i < j.getNumArmas(); i++) {
			if (!j.getArma(i).estaEliminat())
				j.getArma(i).dibuixar(g);
		}

		g.drawImage(j.getSprite().getImatge(), j.getX(), j.getY(), null);

	}

	public void dibuixarExplosio(Graphics g, ArmaExplosiva a) {
		/**
		 * @pre a!=Null i explotant.
		 * 
		 * @post sprites de explosió dibuixats a totes les casellesRang de a.
		 */
		for (int i = 0; i < a.getCasellesRang().length; i++) {
			if (a.getCasellesRang()[i] != null)
				g.drawImage(a.getSprite().getImatge(), a.getCasellesRang()[i].getX() * Sprite.getTamany(),
						a.getCasellesRang()[i].getY() * Sprite.getTamany(), null);
		}
	}

	public void dibuixarMenu(Graphics g, MenuInicial m) {
		/**
		 * @pre m!=Null.
		 * 
		 * @post imatges de MenuInicial dibuixades.
		 */

		g.drawImage(m.getMenu(), 0, 0, null);
		if (m.getOpcio() == 0) {
			g.drawImage(m.getB1(), 0, 0, null);
		} else if (m.getOpcio() == 1) {
			g.drawImage(m.getB2(), 0, 0, null);
		} else if (m.getOpcio() == 2) {
			g.drawImage(m.getB3(), 0, 0, null);
		}

	}

	public void dibuixarMenuVersus(Graphics g, MenuVersus m) {
		/**
		 * @pre m!=Null.
		 * 
		 * @post imatges de MenuVersus dibuixades.
		 */

		if (m.getP() == 0) {
			g.drawImage(m.getMP1(), 0, 0, null);
		} else if (m.getP() == 1) {
			g.drawImage(m.getMP2(), 0, 0, null);
		}
		if (m.getOpcio() == 0) {
			g.drawImage(m.getB1(), 0, 0, null);
		} else if (m.getOpcio() == 1) {
			g.drawImage(m.getB2(), 0, 0, null);
		} else if (m.getOpcio() == 2) {
			g.drawImage(m.getB3(), 0, 0, null);
		} else if (m.getOpcio() == 3) {
			g.drawImage(m.getB4(), 0, 0, null);
		} else if (m.getOpcio() == 4) {
			g.drawImage(m.getB5(), 0, 0, null);
		} else if (m.getOpcio() == 5) {
			g.drawImage(m.getB5(), 0, 0, null);
		}
	}

	public void dibuixarMenuResultat(Graphics g, MenuV m) {
		/**
		 * @pre m!=Null.
		 * 
		 * @post imatges de MenuV dibuixades.
		 */

		g.drawImage(m.getMenu(), 0, 0, null);
		if (m.getOpcio() == 0) {
			g.drawImage(m.getB1(), 0, 0, null);
		} else if (m.getOpcio() == 1) {
			g.drawImage(m.getB2(), 0, 0, null);
		}

	}

	public void dibuixarMenuFinal(Graphics g, MenuFinal m) {
		/**
		 * @pre m!=Null.
		 * 
		 * @post imatges de MenuFinal dibuixades.
		 */

		g.drawImage(m.getMenu(), 0, 0, null);
		if (m.getOpcio() == 0) {
			g.drawImage(m.getF1(), 0, 0, null);
		} else if (m.getOpcio() == 1) {
			g.drawImage(m.getCredits(), 0, 0, null);
		}
	}

}
