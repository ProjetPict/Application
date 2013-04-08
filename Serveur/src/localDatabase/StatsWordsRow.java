package localDatabase;

public class StatsWordsRow {

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
		return "INSERT INTO words_statistiques VALUES ('null','"+index+"','"+proposalServer+"','"+proposalAccepted+"','"+proposalPlayed+"','"+proposalFound+"');";
	}
	
}
