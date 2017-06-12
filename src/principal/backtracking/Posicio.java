package principal.backtracking;

public class Posicio {
	int x;
	int y;

	public Posicio() {
		x = 0;
		y = 0;
	}

	public Posicio(int x2, int y2) {
		x = x2;
		y = y2;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public Posicio suma(Posicio p) {
		return new Posicio(x + p.x, y + p.y);
	}

	public boolean esValida(int mx, int my) {
		return (x < mx && y < my && x >= 0 && y >= 0);
	}
}
