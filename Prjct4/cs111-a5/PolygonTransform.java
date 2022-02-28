/*************************************************************************
 *  Compilation:  javac PolygonTransform.java
 *  Execution:    java PolygonTransform
 *
 *  @author: Devan Patel
 * email: dbp119@scarletmail.rutgers.edu
 *RUID: 193005866
 *************************************************************************/

public class PolygonTransform {


    // Returns a new array that is an exact copy of the given array. 
    // The given array is not mutated. 
    public static double[] copy(double[] array) {

	// WRITE YOUR CODE HERE

    for (int i = 0; i < array.length; i++) {
        s[i] = array [i];

        return s;
    }
    }
    
    // Scales the given polygon by the factor alpha. 
    public static void scale(double[] x, double[] y, double alpha) {

	// WRITE YOUR CODE HERE
    for (i = 0; i < y.length; i++) {
        x[i] = x[i] * alpha;
        y[i] = y[i] * alpha;
    }
    }
    
    // Translates the given polygon by (dx, dy). 
    public static void translate(double[] x, double[] y, double dx, double dy) {

	// WRITE YOUR CODE HERE
    for (i = 0; i < y.length; i++) {
        x[i] = x[i] + dx;
        y[i] = y[i] + dy;
    }
    }
    
    // Rotates the given polygon theta degrees counterclockwise, about the origin. 
    public static void rotate(double[] x, double[] y, double theta) {

	// WRITE YOUR CODE HERE
    double degrees = theta * Math.PI/180;
    double sine = Math.sin(degrees);
    double cosine = Math.cos(degrees);
    for (int i = 0; i < y.length; i++) {
        double xrot = 0, yrot = 0;
        xrot = x[i]*cosine - y[i] * sine;
        yrot = y[i]*cosine + x[i]*sine;
        x[i] = xrot;
        y[i] = yrot;
    }
    }

    // Tests each of the API methods by directly calling them. 
    public static void main(String[] args) {

	// WRITE YOUR CODE HERE
        StdDraw.setScale(-5.0, +5.0);
        double arrayX[] = {0,1,1,0};
        double arrayY[] = {0,0,2,1};
        double alpha = 2.0;
        double dx = 2.0,dy = 1.0;
        double theta = 90.0;
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.polygon(arrayX, arrayY);
        
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.polygon(arrayX, arrayY);

    }
}
