import java.util.Arrays;

public class Project2 {
    final static int[] X = {0, 10, 0};
    final static int[] Y = {0, 0, 10};
    //final static int[] X = {0, 1, 2, 3, 4, 5, 6};
    //final static int[] Y = {0, 0, 0, 0, 0, 0, 0};
    static int sol[] = {-1, -1, -1};
    //finding the Median for the L1 metric
    public static int findMedian(int[] arr){
        Arrays.sort(arr);
        int median = 0;
        if(arr.length % 2 == 0){
            median = ((int)arr[arr.length/2] + (int)arr[arr.length/2 - 1]);
        }
        else{
            median = (int)arr[arr.length/2];
        }
        return median;
    }
    //function to calculate the L1 metric
    public static int calcL1(int[] xPts, int[] yPts){
        int midX = findMedian(xPts);
        int midY = findMedian(yPts);
        int l1 = 0;
        for(int i = 0; i < X.length; i++){
            l1 += Math.abs(midX - X[i]) + Math.abs(midY - Y[i]);
        }
        return l1;
    }

    // Utility function for calulating the L1 metric
    public static double calcL2(int inputX, int inputY) {
        double l2 = 0;

        for (int i = 0; i < X.length; i++) {
            l2 += Math.sqrt(Math.pow(inputX - X[i], 2) + Math.pow(inputY - Y[i], 2));
        }
        //System.out.println("(" + inputX + ", " + inputY + "): " + l1);
        return l2;
    }

    // Utility function to calculate the plausible area to search through
    public static int[] calcBox(int[] inputX, int[] inputY) {

        int x_Max = Integer.MIN_VALUE;
        int y_Max = Integer.MIN_VALUE;

        int x_Min = Integer.MAX_VALUE;
        int y_Min = Integer.MAX_VALUE;

        for (int i = 0; i < inputX.length; i++) {
            if (x_Max < inputX[i]) {
                x_Max = inputX[i];
            }
            if (x_Min > inputX[i]) {
                x_Min = inputX[i];
            }
        }

        for (int i = 0; i < inputY.length; i++) {
            if (y_Max < inputY[i]) {
                y_Max = inputY[i];
            }
            if (y_Min > inputY[i]) {
                y_Min = inputY[i];
            }
        }

        /*
         * 0: X minimum 
         * 1: X maximum 
         * 2: Y minimum 
         * 3: Y maximum
         */

        int[] box = { x_Min, x_Max, y_Min, y_Max };
        return box;
    }

    // Utility function to check whether the next move is within the contraints
    public static boolean isSafe(int[] box, int x, int y) {
        return (x >= box[0] && x <= box[1] && y >= box[2] && y <= box[3]);
    }

    public static int[] recursiveStart(int[] box) {
        double currMin = calcL2(1, 1);
        sol[0] = 1;
        sol[1] = 1;
        //sol[2] = currMin;
        
        recursiveSearch(box, 1, 1, currMin);
        return sol;
    }
    
    //Main recursive function that starts in the middle and checks all neighbors.
    //It moves towards the most minimum L1 metric.
    public static void recursiveSearch(int[] box, int x, int y, double currMin) {
        // Possible combinations of movement for search
        int[] possibleX = { 0, 1, 1, 1, 0, -1, -1, -1 };
        int[] possibleY = { 1, 1, 0, -1, -1, -1, 0, 1 };

        double temp;
        int tempX, tempY;
        double nextMin = calcL2(x + possibleX[0], y + possibleY[0]);

        int nextX = x;
        int nextY = y;

        // Traverse the inner graph
        for (int k = 0; k < 8; k++) {
            tempX = x + possibleX[k];
            tempY = y + possibleY[k];
            if (isSafe(box, tempX, tempY)) {
                temp = calcL2(tempX, tempY);
                System.out.println("(" + tempX + ", " + tempY + "): " + temp); //For debugging purposes
                if(temp < nextMin) {
                    System.out.println("Grabbing: (" + tempX + ", " + tempY + "): " + temp);
                    nextMin = temp;
                    nextX = tempX;
                    nextY = tempY;
                }
            }
        }
        System.out.println(currMin + " vs. " + nextMin);
        if (nextMin < currMin) {
            sol[0] = nextX;
            sol[1] = nextY;
            //sol[2] = nextMin;
            recursiveSearch(box, nextX, nextY, nextMin);
        }
    }
    public static void main(String[] args) {
        int[] output = recursiveStart(calcBox(X, Y));
        System.out.println("Using L1: ("+ findMedian(X) + ", " + findMedian(Y) + ") " + calcL1(X, Y));
        System.out.println("Using L2: (" + output[0] + ", " + output[1] + ")" + output[2]);
    }
}
