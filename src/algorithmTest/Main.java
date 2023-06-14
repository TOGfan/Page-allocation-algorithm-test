package algorithmTest;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
/*
4.Frame allocation algorithms for multiple processes -simulation of algorithms: random, proportional, page fault frequency, working set. Compare their numbers of generated page faults.

Task 4:
- the application should process multiple processes in parallel, where each process contains multiple requests (as opposed to task 3 where there was one process and multiple requests),
- for parallelization you should test the allocation of processes - equal and proportional,
- additionally check the global and local replacement methods,
- it is worth knowing what the working set is and still the principle of locality is necessary to meet,
- simulations should be carried out for the algorithms from task 3,
- the application should print the simulation assumptions on the screen and allow to change them by the user if possible (number of processes, requests, pages, frames - possibly ranges),
- please do not set fixed values in the application code but parameterize them so that the user can easily change them,
How do frame allocation strategies affect performance (number of page faults - globally, for each process)?
GLOBAL REPLACEMENT
*/

public class Main {
	public static int[] generateProcessesLength(int numOfProcesses, int maxLength){
		Random random = new  Random();
		int[] arr = new int[numOfProcesses];
		for (int i=0;i<numOfProcesses;i++){
			arr[i]=random.nextInt()%maxLength;
			if(arr[i]<0)arr[i]*=-1;
		}
		return arr;
	}
	public static int[] generateRequests(int length, int range) {
		int[] arr = new int[length];
		Random random = new Random();
		int prev=0;
		for (int i = 0; i < length/2; i++) {
			arr[i] = (int) ((double)((random.nextGaussian()/10.0+0.5)*range+0.5)+prev);
			while(arr[i]<0||arr[i]>range) {
				arr[i] = (int) ((double)((random.nextGaussian()/10.0+0.5)*range+0.5)+prev);
			}
			prev=arr[i]-range/2;
		}
		for (int i = length-1; i >= length/2; i--) {
			arr[i] = (int) ((double)((random.nextGaussian()/10.0+0.5)*range+0.5)+arr[length-i-1]-range/2);
			while(arr[i]<0||arr[i]>range) {
				arr[i] = (int) ((double)((random.nextGaussian()/10.0+0.5)*range+0.5)+arr[length-i-1]-range/2);
			}
		}
		return arr;
	}
	public static int[][] generateFinal(int numOfProcesses, int maxLength, int range){
		int[]lengths = generateProcessesLength(numOfProcesses,maxLength);
		int[][] requests = new int[lengths.length][];
		for(int i=0;i<lengths.length;i++){
				requests[i]=generateRequests(lengths[i],range);
		}
		return requests;
	}
	public static int findOnPage(int page[], int index) {
		for (int i = 0; i < page.length; i++) {
			if (page[i] == index) {
				return i;
			}
		}
		return -1;
	}

	public static void displayArr2D(int[][] page) {
		for (int i = 0; i < page.length; i++) {
			for (int j = 0; j <page[i].length ; j++) {
				if (page[i][j] == -1) {
					System.out.print("_ ");
				} else {
					System.out.print(page[i][j] + " ");
				}
			}
			System.out.println();
		}

	}

	public static void displayArr(int[] page) {
		for (int i = 0; i < page.length; i++) {

					System.out.print(page[i] + " ");
				}

			System.out.println();
		}
	public static int[] TwoDTo1D(int[][] arr2D){
		int totalLength=0;
		for (int i = 0; i < arr2D.length; i++) {
			totalLength+=arr2D[i].length;
		}
		int[] arr1D=new int[totalLength];
		totalLength=0;
		for (int i = 0; i < arr2D.length; i++) {
			for (int j = 0; j < arr2D[i].length; j++) {
				arr1D[totalLength+j]=arr2D[i][j];
			}
			totalLength+=arr2D[i].length;
		}
		return arr1D;
	}



	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter total number of memory slots:");
		int pageLength = scanner.nextInt();
		System.out.println("Enter maximum process length:");
		int testLength = scanner.nextInt();
		System.out.println("Enter range of requests:");
		int range = scanner.nextInt();
		System.out.println("Enter number of processes:");
		int processNum = scanner.nextInt();
		int[][] requests = generateFinal(processNum,testLength,range);
		int[] requests1D = TwoDTo1D(requests);
		int temp;
		//System.out.println("Enter initial pages (enter -1 to start empty):");

		scanner.close();
		try {
			PrintWriter results = new PrintWriter("Results.txt");
			System.out.println("Generated requests:");
			displayArr2D(requests);
			results.println("Global page replacement with FIFO page faults: "+Global.FIFO(pageLength,requests));
			results.println("Global page replacement with OPT page faults: "+Global.OPT(pageLength,requests));
			results.println("Global page replacement with FIFO page faults: "+Global.LRU(pageLength,requests));
			results.println("Global page replacement with FIFO page faults: "+Global.ALRU(pageLength,requests));
			results.println("Global page replacement with FIFO page faults: "+Global.RAND(pageLength,requests));
			results.println("Simulation of local page replacement:");
			System.out.println("Simulation of equal page allocation:");
			int[]pageAllocation=Equal.allocate(requests,pageLength);
			displayArr(pageAllocation);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=FIFO.simulate(pageAllocation[i],requests[i]);
			}
			results.println("equal FIFO page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=OPT.simulate(pageAllocation[i],requests[i]);
			}
			results.println("equal OPT page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=LRU.simulate(pageAllocation[i],requests[i]);
			}
			results.println("equal LRU page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=ALRU.simulate(pageAllocation[i],requests[i]);
			}
			results.println("equal ARLU page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=RAND.simulate(pageAllocation[i],requests[i]);
			}
			results.println("equal RAND page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=FIFO.simulate(pageAllocation[i],requests[i]);
			}
			System.out.println("Simulation of proportional page allocation:");
			pageAllocation=Proportional.allocate(requests,pageLength);
			displayArr(pageAllocation);
			results.println("proportional FIFO page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=OPT.simulate(pageAllocation[i],requests[i]);
			}
			results.println("proportional OPT page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=LRU.simulate(pageAllocation[i],requests[i]);
			}
			results.println("proportional LRU page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=ALRU.simulate(pageAllocation[i],requests[i]);
			}
			results.println("proportional ARLU page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=RAND.simulate(pageAllocation[i],requests[i]);
			}
			results.println("proportional RAND page faults: " + temp);



			System.out.println("Simulation of random page allocation:");
			pageAllocation=RandomAllocation.allocate(requests,pageLength);
			displayArr(pageAllocation);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=FIFO.simulate(pageAllocation[i],requests[i]);
			}
			results.println("random FIFO page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=OPT.simulate(pageAllocation[i],requests[i]);
			}
			results.println("random OPT page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=LRU.simulate(pageAllocation[i],requests[i]);
			}
			results.println("random LRU page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=ALRU.simulate(pageAllocation[i],requests[i]);
			}
			results.println("random ARLU page faults: " + temp);
			temp=0;
			for (int i = 0; i < processNum; i++) {
				temp+=RAND.simulate(pageAllocation[i],requests[i]);
			}
			results.println("random RAND page faults: " + temp);

			results.println("PFF FIFO page faults: " + PageFaultFrequency.FIFO(pageLength,requests));
			results.println("PFF OPT page faults: " + PageFaultFrequency.OPT(pageLength,requests));
			results.println("PFF LRU page faults: " + PageFaultFrequency.LRU(pageLength,requests));
			results.println("PFF ALRU page faults: " + PageFaultFrequency.ALRU(pageLength,requests));
			results.println("PFF RAND page faults: " + PageFaultFrequency.RAND(pageLength,requests));

			results.println("Working Set page faults: " + WorkingSet.calculatePageFaults(requests,pageLength));
			results.close();

		} catch (FileNotFoundException ex) {

		}
	}
}
