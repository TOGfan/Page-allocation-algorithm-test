package algorithmTest;

public class ALRU {
	
	public static int findOldest(int age[]) {
		int oldest=0;
		for(int i=0;i<age.length;i++) {
			if(age[i]>age[oldest]) {
				oldest=i;
			}
		}
		return oldest;
	}
	public static void increaseAge(int age[]){
		for(int i=0;i<age.length;i++) {
			age[i]++;
		}
	}	
	public static int simulate(int length, int requests[]){
		int faults=0;
		int[] page = new int[length];
		for(int i=0;i<length;i++) {
			page[i]=-1; //-1 means empty
		}
		boolean[] bitref = new boolean[length];
		for(int i=0;i<length;i++) {
			bitref[i]=true;
		}
		int[] age = new int[length];
		int temp;
		for(int i=0;i<requests.length;i++) {
			temp=Main.findOnPage(page, requests[i]);
			if(temp==-1) {
				temp=Main.findOnPage(page, -1);
				while(temp==-1) {
					temp=findOldest(age);
					if(bitref[temp]==true) {
						increaseAge(age);
						age[temp]=0;
						bitref[temp]=false;
						temp=-1;
					}
				}
				age[temp]=0;
				page[temp]=requests[i];
				faults++;
			}
			increaseAge(age);
		}
		return faults;
	}
}
