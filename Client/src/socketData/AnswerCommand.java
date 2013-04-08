package socketData;

/**
 * Cette classe hérite de Command et est utilisé pour envoyer une réponse au serveur
 *
 */
public class AnswerCommand extends Command {


	private static final long serialVersionUID = 1L;
	public long nbPixels;

	/**
	 * Constructeur de AnswerCommand
	 * @param answer La réponse à envoyer
	 * @param nbPixels Le nombre de pixels du dessin lors de l'envoi de la réponse
	 */
	public AnswerCommand(String answer, long nbPixels) {
		command = answer;
		this.nbPixels = nbPixels;
	}
}
