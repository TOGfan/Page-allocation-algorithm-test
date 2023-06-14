package algorithmTest;

import static java.lang.Math.round;

public class Proportional {
    public static int[] allocate(int[][]processes, int pageSize){
        int arr[] = new int[processes.length];
        int sum=0;
        for(int i=0;i<processes.length;i++){
            arr[i]=processes[i].length;
            sum+=arr[i];
        }
        int balance=pageSize;
        for(int i=0;i<arr.length;i++){
            arr[i]=arr[i]*pageSize;
            //rem=arr[i]%sum;
            arr[i]=round((float)arr[i]/sum);
            balance-=arr[i];
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==0){
                arr[i]++;
                balance--;
            }
        }
        for (int i = 0; i < arr.length&&balance<0; i++) {
            if(arr[i]>1){
                balance++;
                arr[i]--;
            }
        }
        return arr;
    }
}
