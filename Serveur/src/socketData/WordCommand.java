package socketData;


/**
 * Cette classe sert à envoyer des mots à faire choisir (côté serveur) 
 * ou à envoyer le mot choisi (côté client)
 *
 */
public class WordCommand extends Command {

	private static final long serialVersionUID = 1L;

	public String word1;
	public String word2;
	public String word3;
	public int difficulty;

	/**
	 * Constructeur de WordCommand
	 * @param word1 Le premier mot à faire deviner
	 * @param word2 Le deuxième mot à faire deviner
	 * @param word3 Le troisième mot à faire deviner
	 * @param difficulty La difficulté de ces mots
	 */
	public WordCommand(String word1, String word2, String word3, int difficulty) {
		this.command = "";
		this.word1 = word1;
		this.word2 = word2;
		this.word3 = word3;
		this.difficulty = difficulty;
	}
}
