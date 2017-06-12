package principal.backtracking;

import principal.elements.Jugador;
import principal.mapas.Mapa;

public class Solucio {
	private boolean laberint[][];
	private int recorregut[][];
	private int midaX, midaY, niv;
	private int sx;
	private int sy;
	private int iniX;
	private int iniY;

	public Solucio(Mapa mapa, Jugador o, Jugador ia) {
		iniX = (o.getCasellaX() < ia.getCasellaX()) ? o.getCasellaX() : ia.getCasellaX();
		iniY = (o.getCasellaY() < ia.getCasellaY()) ? o.getCasellaY() : ia.getCasellaY();
		iniX = (iniX - 1 >= 0) ? iniX - 1 : 0;
		iniY = (iniY - 1 >= 0) ? iniY - 1 : 0;

		midaX = (iniX + 8 >= mapa.getAmplada()) ? mapa.getAmplada() - iniX : 8;
		midaY = (iniY + 8 >= mapa.getAltura()) ? mapa.getAltura() - iniY : 8;

		sx = o.getCasellaX() - iniX;
		sy = o.getCasellaY() - iniY;

		laberint = new boolean[midaY][midaX];
		for (int i = 0; i < midaY; i++) {
			for (int j = 0; j < midaX; j++) {
				laberint[i][j] = !mapa.getCasella(j + iniX, i + iniY).esSolid();
			}
		}
		recorregut = new int[midaY][midaX];
		for (int i = 0; i < midaY; i++) {
			for (int j = 0; j < midaX; j++) {
				recorregut[i][j] = 0;
			}
		}
	}

	public Candidat inicialitzarCandidats(Posicio pos) {
		return new Candidat(pos, sx, sy);
	}

	public final boolean acceptable(Posicio pos) {
		return pos.esValida(midaX, midaY) && laberint[pos.y()][pos.x()] && recorregut[pos.y()][pos.x()] == 0;
	}

	public void anotar(Posicio pos) {
		niv++;
		recorregut[pos.y()][pos.x()] = niv;
	}

	public final boolean solCompleta(Posicio pos) {
		return recorregut[sy][sx] != 0;
	}

	public void desanotar(Posicio pos) {
		recorregut[pos.y()][pos.x()] = 0;
		niv--;
	}

	public final void mostrarLaberint() {
		System.out.println("LABERINT:");

		for (int i = 0; i < midaY; i++) {
			for (int j = 0; j < midaX; j++) {
				if (laberint[i][j])
					System.out.print("0  ");
				else
					System.out.print("1  ");
			}
			System.out.println("");
		}
	}

	public final void mostrarRecorregut() {
		System.out.println("RECORREGUT:");

		for (int i = 0; i < midaY; i++) {
			for (int j = 0; j < midaX; j++) {
				System.out.print(recorregut[i][j] + "  ");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public char seguentCasella(int cx, int cy) {
		int px = cx - iniX;
		int py = cy - iniY;

		if (px >= 0 && py >= 0 && px < midaX && py < midaY && py - 1 >= 0
				&& (recorregut[py][px] < recorregut[py - 1][px]))
			return 'n';
		else if (px >= 0 && py >= 0 && px < midaX && py < midaY && py + 1 < midaY
				&& (recorregut[py][px] < recorregut[py + 1][px]))
			return 's';
		else if (px >= 0 && py >= 0 && px < midaX && py < midaY && px - 1 >= 0
				&& (recorregut[py][px] < recorregut[py][px - 1]))
			return 'w';
		else if (px >= 0 && py >= 0 && px < midaX && py < midaY && px + 1 < midaX
				&& (recorregut[py][px] < recorregut[py][px + 1]))
			return 'e';
		else
			return 'f';

	}

	public int getIniX() {
		return iniX;
	}

	public int getIniY() {
		return iniY;
	}

}
