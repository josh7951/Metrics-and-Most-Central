/** Programmer: Joshua Mejia
 *  COMP482
 *  Noga
 *  1 November 2020
 */
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;

public class Project2 {
    static double sol[] = {-1, -1, -1};
    //method for the input file
    public static int[] readFile(String file){
        try{
            File f = new File(file);
            Scanner s = new Scanner(f);
            int itr = 0;
            while(s.hasNextInt()) {
                itr++;
                s.nextInt();
            }
            s.close();
            int[] arr = new int[itr];
            Scanner s1 = new Scanner(f);

            for(int i = 0; i < arr.length; i++)    
                arr[i] = s1.nextInt();
                s1.close();
            return arr;
        }
        catch(Exception e) { return null; }
    }
    //finding the Median for the L1 metric
    public static int findMedian(int[] arr){
        Arrays.sort(arr);
        int median = 0;
        if(arr.length % 2 == 0){
            median = ((int)arr[arr.length/2] + (int)arr[arr.length/2 - 1])/2;
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
        for(int i = 0; i < xPts.length; i++){
            l1 += Math.abs(midX - xPts[i]) + Math.abs(midY - yPts[i]);
        }
        return l1;
    }

    // Utility function for calulating the L2 metric
    public static double calcL2(int inputX, int inputY, int[] X, int[] Y) {
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

    public static double[] recursiveStart(int[] box, int[] X, int[] Y) {
        int xAvg = 0;
        int yAvg = 0;
        for(int x = 0; x < X.length; x++){
            xAvg+=X[x]/X.length;
        }
        for(int y = 0; y < Y.length; y++){
            yAvg+=Y[y]/Y.length;
        }
        double currMin = calcL2(xAvg, yAvg, X, Y);
        sol[0] = xAvg;
        sol[1] = yAvg;
        sol[2] = currMin;
        
        recursiveSearch(box, xAvg, yAvg, currMin, X, Y);
        return sol;
    }
    
    //Main recursive function that starts in the middle and checks all neighbors.
    //It moves towards the most minimum L2 metric.
    public static void recursiveSearch(int[] box, int x, int y, double currMin, int[] X, int[] Y) {
        // Possible combinations of movement for search
        int[] possibleX = { 0, 1, 1, 1, 0, -1, -1, -1, -5, -5, 5, 5};
        int[] possibleY = { 1, 1, 0, -1, -1, -1, 0, 1, -5, 5, -5, 5};

        double temp;
        int tempX, tempY;
        double nextMin = calcL2(x + possibleX[0], y + possibleY[0], X, Y);

        int nextX = x;
        int nextY = y;

        // Traverse the inner graph
        for (int k = 0; k < 12; k++) {
            tempX = x + possibleX[k];
            tempY = y + possibleY[k];
            if (isSafe(box, tempX, tempY)) {
                temp = calcL2(tempX, tempY, X, Y);
                //System.out.println("(" + tempX + ", " + tempY + "): " + temp); //For debugging purposes
                if(temp < nextMin) {
                    //System.out.println("Grabbing: (" + tempX + ", " + tempY + "): " + temp); //For debugging purposes
                    nextMin = temp;
                    nextX = tempX;
                    nextY = tempY;
                }
            }
        }
        if (nextMin < currMin) {
            sol[0] = nextX;
            sol[1] = nextY;
            sol[2] = nextMin;
            recursiveSearch(box, nextX, nextY, nextMin, X, Y);
        }
    }
    public static void main(String[] args) {
        int[] originalArr = readFile("input2.txt");
        int length = originalArr.length/2;
        int[] X = new int[length];
        int[] Y = new int[length];
        for(int x = 0, y = 1, i = 0; x < originalArr.length-1 && y < originalArr.length; x+=2, y+=2, i++) {
            X[i] = originalArr[x];
            Y[i] = originalArr[y];
        }
        double[] l2 = recursiveStart(calcBox(X, Y), X, Y);

        System.out.println("Using L1: ("+ findMedian(X) + ", " + findMedian(Y) + ") " + calcL1(X, Y));
        System.out.println("Using L2: (" + (int)l2[0] + ", " + (int)l2[1] + ") " + l2[2]);
    }
}
