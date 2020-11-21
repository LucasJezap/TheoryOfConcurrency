package tw.lab5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static final boolean demo = true;
    static final boolean isSynchronized = true;
    static final int boundsX = 0;
    static final int boundsY = 0;
    static final int boundsWidth = 2000;
    static final int boundsHeight = 2000;
    static final int MAX_ITER = 6000;
    static final double ZOOM = 300;
    static final int tasksPerThread = 10;
    static final List<Integer> threadNumbers = new ArrayList<>(Arrays.asList(1, 3, 5, 10, 20, 50, 100, 200, 500, 1000));

    public static void main(String[] args) {
        if (demo) {
            ThreadedMandelbrot x = new ThreadedMandelbrot(threadNumbers.get(0), tasksPerThread);
            x.createSet();
            x.setVisible(true);
        } else {
            for (Integer threadNumber : threadNumbers) {
                ThreadedMandelbrot x = new ThreadedMandelbrot(threadNumber, tasksPerThread);
                x.createSet();
            }
        }
    }
}
