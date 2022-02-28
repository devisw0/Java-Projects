/*************************************************************************
 *  Compilation:  javac Sierpinski.java
 *  Execution:    java Sierpinski
 *
 *  @author: Devan Patel
 * dbp119@scarletmail.rutgers.edu
 * 193005866
 *************************************************************************/

public class Sierpinski {

    // Height of an equilateral triangle whose sides are of the specified length. 
    public static double height(double length) {

	// WRITE YOUR CODE HERE

    return (length/2)*Math.pow(3,.5);

    }

    // Draws a filled equilateral triangle whose bottom vertex is (x, y) 
    // of the specified side length.
    public static void filledTriangle(double x, double y, double length) {

	// WRITE YOUR CODE HERE
    double[] xPoints = {x, x - length/2, x + length/2};

    double[] yPoints = {y, y + height(length), y + height(length)};

    StdDraw.filledPolygon(xPoints, yPoints);

    }

    // Draws a Sierpinski triangle of order n, such that the largest filled 
    // triangle has bottom vertex (x, y) and sides of the specified length. 
    public static void sierpinski(int n, double x, double y, double length) {

	// WRITE YOUR CODE HERE

        if (n <= 0) {

            return;
        }
        n--;


        filledTriangle(x, y, length);
        sierpinski(n, x - length/2, y, length/2);
        sierpinski(n, x + length/2, y, length/2);
        sierpinski(n, x, y + height(length), length/2);
    }

    // Takes an integer command-line argument n; 
    // draws the outline of an equilateral triangle (pointed upwards) of length 1; 
    // whose bottom-left vertex is (0, 0) and bottom-right vertex is (1, 0); and 
    // draws a Sierpinski triangle of order n that fits snugly inside the outline. 
    public static void main(String[] args) {

	// WRITE YOUR CODE HERE 
    int n = Integer.parseInt(args[0]);

    double[] x = {0, 1, .5};
    double[] y = {0, 0, height(1)};
    
    StdDraw.polygon(x,y);

    sierpinski(n, 0.5, 0, 0.5);
   
    }


    
}
