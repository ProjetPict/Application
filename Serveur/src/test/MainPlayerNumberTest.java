package test;

/**
 * Test de montée en charge du serveur. En cas de surcharge, le serveur print une exception.
 * monte à 2400 sur ma machine
 * 
 */
public class MainPlayerNumberTest {

	//nombre de connections voulues
	static int CONNECTTEST = 2000;

	public static void main(String[] args)  {
		Boolean res = true;
		Integer i;
		PlayerNumberTest[] pNT = new PlayerNumberTest[CONNECTTEST];
		
		for(i=0; i < CONNECTTEST ; i++){
			pNT[i] = new PlayerNumberTest();
			res = pNT[i].connect(i.toString(), "");
			
			if(!res)
				break;
		}

		System.out.print(i.toString() + " connections réussies, res : " + res);
		
		try {
			Thread.currentThread().sleep(10000)	;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
