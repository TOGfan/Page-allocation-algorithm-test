package algorithmTest;

public class WorkingSet_wrong {

    private static void insertAtFrom(int arr[], int at, int from) {
        if (at > from) {
            int temp = arr[from];
            for (int i = from; i < at; i++) {
                arr[i] = arr[i + 1];
            }
            arr[at] = temp;
        } else {
            int temp = arr[from];
            for (int i = from; i > at; i--) {
                arr[i] = arr[i - 1];
            }
            arr[at] = temp;
        }
    }
    public static int findOnPageBounded(int page[], int index, int begin, int end) {
        for (int i = begin; i < end; i++) {
            if (page[i] == index) {
                return i;
            }
        }
        return -1;
    }
    public static int findOldestBounded(int age[], int begin, int end) {
        int oldest=0;
        for(int i=begin;i<end;i++) {
            if(age[i]>age[oldest]) {
                oldest=i;
            }
        }
        return oldest;
    }
    private static void adjustLengths(int[]lengths,int[] page,int[] lastUse, int[]addiArr){
        int min=Integer.MAX_VALUE;
        int max=LRU.findLRU(lastUse);
        int index=0;
        int minProcess=0;
        int maxProcess=-1;
        for (int i = 0; i < lengths.length; i++) {
            int tempMax=index;
            for (int j = 0; j < lengths[i]; j++) {
                if(lastUse[j+index]>tempMax){
                    tempMax=j+index;
                }
            }
            if(lastUse[tempMax]<lastUse[min]){
                min=tempMax;
                minProcess=i;
            }
            if(maxProcess==-1&&index>max){
                maxProcess=i-1;
            }
            index+=lengths[i];
        }
        if(lengths[maxProcess]>1) {
            lengths[minProcess]--;
            lengths[maxProcess]++;
            insertAtFrom(page,max,min);
            insertAtFrom(lastUse,max,min);
            if(addiArr!=null){
                insertAtFrom(addiArr,max,min);
            }
        }

    }
    public static int FIFO(int length, int requests[][]){
        int[] lastUse = new int[length];
        for (int i = 0; i < lastUse.length; i++) {
            lastUse[i]=Integer.MAX_VALUE;
        }
        int faults=0;
        int lengths[] = Proportional.allocate(requests,length);
        int[]faultsArr = new int[requests.length];
        for (int i = 0; i < faultsArr.length; i++) {
            faultsArr[i]=0;
        }
        int[] page = new int[length];
        for(int i=0;i<length;i++) {
            page[i]=-1; //-1 means empty
        }
        int[] age = new int[length];
        int temp;
        int maxLength=0;
        for (int i = 0; i < requests.length; i++) {
            if(requests[i].length>maxLength){
                maxLength=requests[i].length;
            }
        }
        for(int i=0;i<maxLength;i++) {
            int currentBegin=0;
            for (int j = 0; j < requests.length; j++) {
                if(i<requests[j].length){
                    temp = findOnPageBounded(page, requests[j][i],currentBegin,currentBegin+lengths[j]);
                    if (temp == -1) {
                        temp = findOnPageBounded(page, -1,currentBegin,currentBegin+lengths[j]);
                        if (temp == -1) {
                            temp = findOldestBounded(age,currentBegin,currentBegin+lengths[j]);
                        }
                        age[temp] = 0;
                        page[temp] = requests[j][i];
                        faultsArr[j]++;
                        faults++;
                    }
                    lastUse[temp] = 0;
                    LRU.increaseTime(lastUse);
                }
                currentBegin+=lengths[j];
            }
            FIFO.increaseAge(age);
            adjustLengths(lengths,page,lastUse, age);
        }
        return faults;
    }



    public static int findOptimalBounded(int requests[], int step,int page[], int begin, int end) {
        int optimal=OPT.findNextUse(requests,step,page[begin]);
        int result=0;
        int temp;
        for(int i=begin+1;i<end;i++) {
            temp=OPT.findNextUse(requests,step,page[i]);
            if(temp>optimal) {
                optimal=temp;
                result=i;
            }
        }
        return result;
    }


    public static int OPT(int length, int requests[][]){
        int[] lastUse = new int[length];
        for (int i = 0; i < lastUse.length; i++) {
            lastUse[i]=Integer.MAX_VALUE;
        }
        int[]faultsArr = new int[requests.length];
        for (int i = 0; i < faultsArr.length; i++) {
            faultsArr[i]=0;
        }
        int lengths[] = Proportional.allocate(requests,length);
        int faults=0;
        int[] page = new int[length];
        for(int i=0;i<length;i++) {
            page[i]=-1; //-1 means empty
        }
        int temp;
        int maxLength=0;
        for (int i = 0; i < requests.length; i++) {
            if(requests[i].length>maxLength){
                maxLength=requests[i].length;
            }
        }
        for(int i=0;i<maxLength;i++) {
            int currentBegin=0;
            for (int j = 0; j < requests.length; j++) {
                if (i < requests[j].length) {
                    temp = findOnPageBounded(page, requests[j][i],currentBegin,currentBegin+lengths[j]);
                    if (temp == -1) {
                        temp = findOnPageBounded(page, -1,currentBegin,currentBegin+lengths[j]);
                        if (temp == -1) {
                            temp = findOptimalBounded(requests[j], i, page,currentBegin,currentBegin+lengths[j]);
                        }
                        page[temp] = requests[j][i];
                        faults++;
                        faultsArr[j]++;
                    }
                    lastUse[temp] = 0;
                    LRU.increaseTime(lastUse);
                }
                currentBegin+=lengths[j];
            }
            adjustLengths(lengths,page,lastUse, null);
        }
        return faults;
    }

    public static int findLRUBounded(int age[],int begin, int end) {
        int oldest=0;
        for(int i=0;i<age.length;i++) {
            if(age[i]>age[oldest]) {
                oldest=i;
            }
        }
        return oldest;
    }

    public static int LRU(int length, int requests[][]){
        int[]faultsArr = new int[requests.length];
        for (int i = 0; i < faultsArr.length; i++) {
            faultsArr[i]=0;
        }
        int lengths[] = Proportional.allocate(requests,length);
        int faults=0;
        int[] page = new int[length];
        for(int i=0;i<length;i++) {
            page[i]=-1; //-1 means empty
        }
        int[] lastUse = new int[length];
        for (int i = 0; i < lastUse.length; i++) {
            lastUse[i]=Integer.MAX_VALUE;
        }
        int temp;
        int maxLength=0;
        for (int i = 0; i < requests.length; i++) {
            if(requests[i].length>maxLength){
                maxLength=requests[i].length;
            }
        }
        for(int i=0;i<maxLength;i++) {
            int currentBegin=0;
            for (int j = 0; j < requests.length; j++) {
                if (i < requests[j].length) {
                    temp = findOnPageBounded(page, requests[j][i],currentBegin,currentBegin+lengths[j]);
                    if (temp == -1) {
                        temp = findOnPageBounded(page, -1,currentBegin,currentBegin+lengths[j]);
                        if (temp == -1) {
                            temp = findLRUBounded(lastUse,currentBegin,currentBegin+lengths[j]);
                        }
                        page[temp] = requests[j][i];
                        faults++;
                        faultsArr[j]++;
                    }
                    lastUse[temp] = 0;
                    LRU.increaseTime(lastUse);
                    currentBegin+=lengths[j];
                }
            }
            adjustLengths(lengths,page,lastUse,null);
        }
        return faults;
    }



    public static int ALRU(int length, int requests[][]){
        int[] lastUse = new int[length];
        for (int i = 0; i < lastUse.length; i++) {
            lastUse[i]=Integer.MAX_VALUE;
        }
        int[]faultsArr = new int[requests.length];
        for (int i = 0; i < faultsArr.length; i++) {
            faultsArr[i]=0;
        }
        int lengths[] = Proportional.allocate(requests,length);
        int faults=0;
        int[] page = new int[length];
        for(int i=0;i<length;i++) {
            page[i]=-1; //-1 means empty
        }
        int[] bitref = new int[length];
        for(int i=0;i<length;i++) {
            bitref[i]=1;
        }
        int[] age = new int[length];
        int temp;
        int maxLength=0;
        for (int i = 0; i < requests.length; i++) {
            if(requests[i].length>maxLength){
                maxLength=requests[i].length;
            }
        }
        for(int i=0;i<maxLength;i++) {
            int currentBegin=0;
            for (int j = 0; j < requests.length; j++) {
                if (i < requests[j].length) {

                    temp = findOnPageBounded(page, requests[j][i],currentBegin,currentBegin+lengths[j]);
                    if (temp == -1) {
                        temp = findOnPageBounded(page, -1,currentBegin,currentBegin+lengths[j]);
                        while (temp == -1) {
                            temp = findOldestBounded(age,currentBegin,currentBegin+lengths[j]);
                            if (bitref[temp] == 1) {
                                ALRU.increaseAge(age);
                                age[temp] = 0;
                                bitref[temp] = 0;
                                temp = -1;
                            }
                        }
                        age[temp] = 0;
                        page[temp] = requests[j][i];
                        faults++;
                        faultsArr[j]++;
                    }
                    lastUse[temp] = 0;
                    LRU.increaseTime(lastUse);
                    ALRU.increaseAge(age);
                    currentBegin+=lengths[j];
                }
            }
            adjustLengths(lengths,page,lastUse,age);
        }
        return faults;
    }

    public static int RAND(int length, int requests[][]){
        int[] lastUse = new int[length];
        for (int i = 0; i < lastUse.length; i++) {
            lastUse[i]=Integer.MAX_VALUE;
        }
        int[]faultsArr = new int[requests.length];
        for (int i = 0; i < faultsArr.length; i++) {
            faultsArr[i]=0;
        }
        int lengths[] = Proportional.allocate(requests,length);
        int faults=0;
        int[] page = new int[length];
        for(int i=0;i<length;i++) {
            page[i]=-1; //-1 means empty
        }
        int temp;
        int maxLength=0;
        for (int i = 0; i < requests.length; i++) {
            if(requests[i].length>maxLength){
                maxLength=requests[i].length;
            }
        }
        for(int i=0;i<maxLength;i++) {
            int currentBegin = 0;
            for (int j = 0; j < requests.length; j++) {
                if (i < requests[j].length) {

                    temp = findOnPageBounded(page, requests[j][i], currentBegin, currentBegin + lengths[j]);
                    if (temp == -1) {
                        temp = findOnPageBounded(page, -1, currentBegin, currentBegin + lengths[j]);
                        if (temp == -1) {
                            temp = currentBegin + RAND.randomizePage(lengths[j]);
                        }
                        page[temp] = requests[j][i];
                        faults++;
                    }
                    lastUse[temp] = 0;
                    LRU.increaseTime(lastUse);
                    currentBegin += lengths[j];
                }
            }
            adjustLengths(lengths,page,lastUse,null);
        }
        return faults;
    }

}
