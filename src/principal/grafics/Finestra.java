package principal.grafics;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import principal.eines.CarregadorRecursos;

/**
 * \brief Classe que implementa JFrame.
 * 
 * Finestra des de on es juga, conte la SuperficieDibuix on es dibuixa tot el
 * joc.
 */

public class Finestra extends JFrame {

	private static final long serialVersionUID = 6340077381690638525L;

	/** icona del joc */
	private final ImageIcon icona;

	/** nom del joc */
	private final String NOM;

	public Finestra(final String n, final SuperficieDibuix s) {
		/**
		 * @pre --
		 * 
		 * @post Finestra creada amb nom n i SuperficieDibiux d.
		 */
		NOM = n;
		BufferedImage imatge = CarregadorRecursos.carregarImatgeCompatibleTranslucida("/IMG/Icones/icona.png");
		icona = new ImageIcon(imatge);
		configurarFinestra(s);

	}

	private void configurarFinestra(final SuperficieDibuix s) {
		/**
		 * @pre --
		 * 
		 * @post parametres de la Finestra establerts, SuperficieDibuix s
		 *       afegida.
		 */
		setTitle(NOM);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setIconImage(icona.getImage());
		setLayout(new BorderLayout());
		add(s, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
