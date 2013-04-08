package socketData;

/**
 * Cette commande est utilisée pour créer ou rejoindre une partie
 *
 */
public class CreateJoinCommand extends Command {

	private static final long serialVersionUID = 1L;
	public String name;
	public String password;
	public int pMax;
	public int turns;
	public int difficulty;


	/**
	 * Constructeur pour rejoindre une partie
	 * @param name Le nom de la partie
	 * @param password Le mot de passe (peut être null)
	 */
	public CreateJoinCommand(String name, String password)
	{
		command = "joingame";
		this.name = name;
		setPassword(password);
		pMax = 0;
		turns = 0;
	}

	/**
	 * Constructeur pour créer une partie
	 * @param name Le nom de la partie
	 * @param password Le mot de passe (peut être null)
	 * @param pMax Le nombre de joueurs maximum (0 = par défaut)
	 * @param turns Le nombre de tours (0 = par défaut)
	 */
	public CreateJoinCommand(String name, String password, int pMax, int turns, int difficulty) {
		command = "creategame";
		this.name = name;
		setPassword(password);
		this.pMax = pMax;
		this.turns = turns;
		this.difficulty = difficulty;
	}

	/**
	 * Si le mot de passe n'est pas null et est >= 4 en taille, alors on l'assigne
	 * au champ correspondant
	 * @param password
	 */
	private void setPassword(String password)
	{
		this.password = null;
		if(password != null)
		{
			if(password.length() >= 4)
				this.password = password;
		}
	}
}
