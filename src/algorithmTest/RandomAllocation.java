package algorithmTest;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class RandomAllocation {
    public static int[] allocate(int[][]processes,int pageSize){
        int arr[] = new int[processes.length];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < processes.length; i++) {
            arr[i]=1;
        }
        for (int i = 0; i < pageSize-arr.length; i++) {
            arr[abs(random.nextInt()%arr.length)]++;
        }/*
        for(int i=0;i<processes.length-1;i++){
            arr[i]=random.nextInt()%(round(pageSize/processes.length));
            if(arr[i]<0)arr[i]*=-1;
            arr[i]++;
            sum+=arr[i];
        }
        arr[arr.length-1]=pageSize-sum;
        */
        return arr;
    }
}
