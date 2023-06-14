package algorithmTest;

import java.util.ArrayList;
import java.util.HashSet;

    public class WorkingSet {

        public static int calculatePageFaults(int[][] processes, int frameNum) {
            int maxSize=0;
            for (int i = 0; i < processes.length; i++) {
                if(processes[i].length>maxSize){
                    maxSize=processes[i].length;
                }
            }
            ArrayList<HashSet<Integer>> setsOfPages = new ArrayList<>();
            int faults = 0;
            int filledFrames = 0;

            for (int i = 0; i < processes.length; i++) {
                setsOfPages.add(new HashSet<>());
            }

            for (int i = 0; i < maxSize; i++) { //number of call
                for (int j = 0; j < processes.length; j++) { //number of process
                    if(processes[j].length<=i){
                        continue;
                    }
                    ArrayList<Integer> workingSet = new ArrayList<>();

                    for (int k = 0; i - k >= 0; k++) {
                        if (!workingSet.contains(processes[j][i - k])) {
                            workingSet.add(processes[j][i - k]);
                        }
                    }

                    filledFrames -= setsOfPages.get(j).size();
                    int allocatedFrames = Math.min(frameNum - filledFrames, workingSet.size());
                    filledFrames += allocatedFrames;
                    if (!setsOfPages.get(j).contains(workingSet.get(0))) {
                        faults++;
                    }
                    HashSet<Integer> newSetOfPages = new HashSet<>();
                    for (int k = 0; k < allocatedFrames; k++) {
                        newSetOfPages.add(workingSet.get(k));
                    }

                    setsOfPages.set(j, newSetOfPages);

                }
            }

            return faults;
        }
    }

