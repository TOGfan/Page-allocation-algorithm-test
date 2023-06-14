package algorithmTest;

public class Equal {
    public static int[] allocate(int[][]processes, int pageSize){
        int [] result = new int[processes.length];
        for (int i = 0; i < result.length; i++) {
            result[i]=pageSize/ processes.length;
        }
        for (int i = 0; i < pageSize% processes.length; i++) {
            result[i]++;
        }
        return result;
    }
}
