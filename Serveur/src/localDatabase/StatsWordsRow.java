package localDatabase;

public class StatsWordsRow {

	/**
	 * Permet de créer un tableau pour la base de données locale, identique à celle sur MySQL afin de juste avoir à la recopier
	 * Cette classe gère l'ensemble des statistiques actuelles propres à un mot donné
	 * Word = le mot
	 * proposalServer = nombre de fois que le mot a été proposé par le serveur depuis la dernière sauvegarde
	 * proposalAccepted = nombre de fois que le mot a été choisi par les dessinateurs depuis la dernière sauvegarde
	 * proposalPlayed = nombre de joueurs qui ont du trouver le mot depuis la dernière sauvegarde
	 * proposalFound = nombre de joueurs qui ont trouvé le mot depuis la dernière sauvegarde
	 */

	private String word;
	private int proposalServer;
	private int proposalAccepted;
	private int proposalPlayed;
	private int proposalFound;

	public StatsWordsRow(String w) {
		word = w;
		proposalServer = 0;
		proposalAccepted = 0;
		proposalPlayed = 0;
		proposalFound = 0;
	}

	public void addNewProposalServer() {
		proposalServer++;
	}

	public void addNewProposalAccept(int nbPlayers, int nbFound) {
		proposalAccepted++;
		proposalPlayed += nbPlayers;
		proposalFound += nbFound;
	}

	public String getWord() {
		return word;
	}

	public void resetStatsWord() {
		proposalServer = 0;
		proposalAccepted = 0;
		proposalPlayed = 0;
		proposalFound = 0;
	}

	public String returnSQLRequest(String index) {
		return "INSERT INTO words_statistiques VALUES (null,'"+index
				+"','"+proposalServer+"','"+proposalAccepted+"','"+proposalPlayed+"','"+proposalFound+"');";
	}
}
