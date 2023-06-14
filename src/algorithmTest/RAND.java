package algorithmTest;

import java.util.Random;

public class RAND {
	public static int randomizePage(int length) {
		Random random = new Random();
		int result = random.nextInt()%length;
		if(result<0)result*=-1;
		return result;
	}	
	public static int simulate(int length, int requests[]){
		int faults=0;
		int[] page = new int[length];
		for(int i=0;i<length;i++) {
			page[i]=-1; //-1 means empty
		}
		int temp;
		for(int i=0;i<requests.length;i++) {
			temp=Main.findOnPage(page, requests[i]);
			if(temp==-1) {
				temp=Main.findOnPage(page, -1);
				if(temp==-1) {
					temp=randomizePage(length);
				}
				page[temp]=requests[i];
				faults++;
			}
		}
		return faults;
	}
}
