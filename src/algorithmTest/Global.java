package algorithmTest;

public class Global {

    public static int findOnPageThis(int page[], int index, int[] owner, int currentProcess) {
        for (int i = 0; i < page.length; i++) {
            if (page[i] == index&&owner[i]==currentProcess) {
                return i;
            }
        }
        return -1;
    }

    public static int FIFO(int length, int requests[][]){
        int[] owner = new int[length];
        for (int i = 0; i < length; i++) {
            owner[i]=-1;
        }
        int faults=0;
        int lengths[] = Proportional.allocate(requests,length);
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
                    temp = findOnPageThis(page, requests[j][i],owner,j);
                    if (temp == -1) {
                        temp = Main.findOnPage(page,-1);
                        if (temp == -1) {
                            temp = FIFO.findOldest(age);
                        }
                        owner[temp]=j;
                        age[temp] = 0;
                        page[temp] = requests[j][i];
                        faults++;
                    }
                }
                currentBegin+=lengths[j];
            }
            FIFO.increaseAge(age);
        }
        return faults;
    }



    public static int findOptimalThis(int requests[], int step,int page[], int[] owner, int currentProcess) {
        int optimal=-1;
        int result=-1;
        int temp;
        for(int i=0;i<page.length;i++) {
            if(owner[i]==currentProcess) {
                temp = OPT.findNextUse(requests, step, page[i]);
                if (temp > optimal) {
                    optimal = temp;
                    result = i;
                }
            }
        }
        return result;
    }
    public static int OPT(int length, int requests[][]){
        int[] owner = new int[length];
        for (int i = 0; i < length; i++) {
            owner[i]=-1;
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
            for (int j = 0; j < requests.length; j++) {
                if (i < requests[j].length) {
                    temp = findOnPageThis(page, requests[j][i],owner,j);
                    if (temp == -1) {
                        temp = Main.findOnPage(page, -1);
                        if (temp == -1) {
                            temp = findOptimalThis(requests[j], i, page,owner,j);
                        }
                        owner[temp]=j;
                        page[temp] = requests[j][i];
                        faults++;
                    }
                }
            }
        }
        return faults;
    }



    public static int LRU(int length, int requests[][]){
        int[] owner = new int[length];
        for (int i = 0; i < length; i++) {
            owner[i]=-1;
        }
        int lengths[] = Proportional.allocate(requests,length);
        int faults=0;
        int[] page = new int[length];
        for(int i=0;i<length;i++) {
            page[i]=-1; //-1 means empty
        }
        int[] lastUse = new int[length];
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
                    temp = findOnPageThis(page, requests[j][i],owner,j);
                    if (temp == -1) {
                        temp = Main.findOnPage(page, -1);
                        if (temp == -1) {
                            temp = LRU.findLRU(lastUse);
                        }
                        owner[temp]=j;
                        page[temp] = requests[j][i];
                        faults++;
                    }
                    lastUse[temp] = 0;
                    LRU.increaseTime(lastUse);
                    currentBegin+=lengths[j];
                }
            }
        }
        return faults;
    }



    public static int ALRU(int length, int requests[][]){
        int[] owner = new int[length];
        for (int i = 0; i < length; i++) {
            owner[i]=-1;
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

                    temp = findOnPageThis(page, requests[j][i],owner,j);
                    if (temp == -1) {
                        temp = Main.findOnPage(page, -1);
                        while (temp == -1) {
                            temp = ALRU.findOldest(age);
                            if (bitref[temp] == 1) {
                                ALRU.increaseAge(age);
                                age[temp] = 0;
                                bitref[temp] = 0;
                                temp = -1;
                            }
                        }
                        owner[temp]=j;
                        age[temp] = 0;
                        page[temp] = requests[j][i];
                        faults++;
                    }
                    ALRU.increaseAge(age);
                    currentBegin+=lengths[j];
                }
            }
        }
        return faults;
    }

    public static int RAND(int length, int requests[][]){
        int[] owner = new int[length];
        for (int i = 0; i < length; i++) {
            owner[i]=-1;
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

                    temp = findOnPageThis(page, requests[j][i], owner,j);
                    if (temp == -1) {
                        temp = Main.findOnPage(page, -1);
                        if (temp == -1) {
                            temp = RAND.randomizePage(length);
                        }
                        owner[temp]=j;
                        page[temp] = requests[j][i];
                        faults++;
                    }
                    currentBegin += lengths[j];
                }
            }
        }
        return faults;
    }

}
