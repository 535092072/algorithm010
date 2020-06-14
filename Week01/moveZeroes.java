class Solution {
    public void moveZeroes(int[] nums) {
        int zeroIndex = 0;
        boolean hasZero = false;
        for(int currIndex = 0; currIndex < nums.length; currIndex++){
            if(nums[currIndex] != 0){
                if(hasZero){
                    nums[zeroIndex] = nums[currIndex];
                    nums[currIndex] = 0;
                }
                zeroIndex++;
            } else {
                hasZero = true;
            }
        }
    }
}