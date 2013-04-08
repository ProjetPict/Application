package socketData;


/**
 * Cette classe sert à envoyer une valeur pour accompagner une Command
 *
 */
public class ValueCommand extends Command {

	private static final long serialVersionUID = 1L;

	public int value;

	/**
	 * Constructeur de ValueCommand
	 * @param command La commande à envoyer
	 * @param value La valeur à associer
	 */
	public ValueCommand(String command, int value) {
		super(command);
		this.value = value;
	}
}
