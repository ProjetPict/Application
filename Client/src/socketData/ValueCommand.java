package socketData;


/**
 * Cette classe sert � envoyer une valeur pour accompagner une Command
 *
 */
public class ValueCommand extends Command {

	private static final long serialVersionUID = 1L;

	public int value;

	/**
	 * Constructeur de ValueCommand
	 * @param command La commande � envoyer
	 * @param value La valeur � associer
	 */
	public ValueCommand(String command, int value) {
		super(command);
		this.value = value;
	}
}
