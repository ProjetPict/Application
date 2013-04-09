package localDatabase;

public class StatsWordsRow {

	/**
	 * Permet de cr�er un tableau pour la base de donn�es locale, identique � celle sur MySQL afin de juste avoir � la recopier
	 * Cette classe g�re l'ensemble des statistiques actuelles propres � un mot donn�
	 * Word = le mot
	 * proposalServer = nombre de fois que le mot a �t� propos� par le serveur depuis la derni�re sauvegarde
	 * proposalAccepted = nombre de fois que le mot a �t� choisi par les dessinateurs depuis la derni�re sauvegarde
	 * proposalPlayed = nombre de joueurs qui ont du trouver le mot depuis la derni�re sauvegarde
	 * proposalFound = nombre de joueurs qui ont trouv� le mot depuis la derni�re sauvegarde
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
