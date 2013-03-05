import model.Model;

public class Main {
//Test
// TEST 2 COMMIT
	public static void main(String[] argc)
	{
		//if(argc.length == 2){
			
			Model model = new Model("localhost","christopher");
			//System.out.print("host = "+argc[0]+" user = "+argc[1]);
			model.run();
		//}
		
	}
	
}
