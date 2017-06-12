package principal.backtracking;

public class Candidat {

	Posicio despl[] = { new Posicio(01, 0), new Posicio(0, 01), new Posicio(0, -1), new Posicio(-1, 0) };
	private Posicio ant;
	private int iCan;

	public Candidat() {
		iCan = 0;
		ant = new Posicio(0, 0);
	}

	public Candidat(Posicio pos, int sx, int sy) {
		iCan = 0;
		ant = pos;

		if (sx < pos.x() && sy < pos.y()) {
			despl[0] = new Posicio(-1, 0);
			despl[1] = new Posicio(0, -1);
			despl[2] = new Posicio(1, 0);
			despl[3] = new Posicio(0, 1);
		} else if (sx > pos.x() && sy < pos.y()) {
			despl[0] = new Posicio(1, 0);
			despl[1] = new Posicio(0, -1);
			despl[2] = new Posicio(-1, 0);
			despl[3] = new Posicio(0, 1);
		} else if (sx < pos.x() && sy > pos.y()) {
			despl[0] = new Posicio(-1, 0);
			despl[1] = new Posicio(0, 1);
			despl[2] = new Posicio(1, 0);
			despl[3] = new Posicio(0, -1);
		} else { // (sx>pos.x() && sy>pos.y())
			despl[0] = new Posicio(1, 0);
			despl[1] = new Posicio(0, 1);
			despl[2] = new Posicio(-1, 0);
			despl[3] = new Posicio(0, -1);
		}
	}

	public final boolean esFi() {
		return iCan >= 4;
	}

	public final Posicio actual() {
		if (esFi())
			System.err.println("Candidat: fi!");
		return ant.suma(despl[iCan]);
	}

	public void seguent() {
		if (esFi())
			System.err.println("Candidat: fi!");
		iCan++;
	}
}
