class Solution {
    public int[][] merge(int[][] intervals) {
        int len = intervals.length;
        int mergeCount = 0;
        for(int i = 0; i< len; i++) {
            for(int j = i + 1; j<len; j++) {
                if(merge(intervals, i, j)) {
                    mergeCount++;
                    break;
                };
            }
        }
        
        int[][] res = new int[len - mergeCount][2];

        int index = 0;
        for(int i = 0; i< len; i++) {
           if(intervals[i][0] ==  1 && intervals[i][1]== -1) {
               continue;
           }
           res[index][0] =  intervals[i][0];
           res[index][1] =  intervals[i][1];
           index ++;
        }
        return res;

    }

    public boolean merge(int[][] intervals, int k, int j) {
        if(j <=  k) {
            return false;
        }
        if(intervals[k][1] == -1 && intervals[k][0] == 1) {
            return false;
        }
        if(intervals[j][1] == -1 && intervals[j][0] == 1) {
            return false;
        }
        if(intervals[k][0] < intervals[j][0] && intervals[k][1] < intervals[j][0]) {
            return false;
        }

        if(intervals[j][0] < intervals[k][0] && intervals[j][1] < intervals[k][0]) {
            return false;
        }

           int left = Math.min(intervals[k][0], intervals[j][0]);
           int right =  Math.max(intervals[k][1], intervals[j][1]);
           intervals[j][0] = left;
           intervals[j][1] = right;
           intervals[k][0] = 1;
           intervals[k][1] = -1;
           return true;


    }
}