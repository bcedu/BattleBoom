package principal.backtracking;

public class Solucionador {
	private Solucio act;
	private boolean encertat = false;

	public Solucionador() {

	}

	public Solucio solucionar(Solucio inicial, int x, int y) {
		act = inicial;
		Posicio inici = new Posicio(x - act.getIniX(), y - act.getIniY());
		act.anotar(inici);
		trobarSolucio(inici);
		return act;
	}

	public boolean teSolucio() {
		return encertat;
	}

	private void trobarSolucio(Posicio ant) {
		Candidat iCan = act.inicialitzarCandidats(ant);
		while (!iCan.esFi() && !encertat) {
			if (act.acceptable(iCan.actual())) {
				act.anotar(iCan.actual());
				if (!act.solCompleta(iCan.actual())) {
					trobarSolucio(iCan.actual());
					if (!encertat)
						act.desanotar(iCan.actual());
				} else {
					encertat = true;
				}
			}
			iCan.seguent();
		}
	}
}
