package tw.lab5;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class ThreadedMandelbrot extends JFrame {
    BufferedImage I;
    final int threadNumber;
    final int tasksPerThread;

    public ThreadedMandelbrot(int threadNumber, int tasksPerThread) {
        super("Mandelbrot Set");
        setBounds(Main.boundsX, Main.boundsY, Main.boundsWidth, Main.boundsHeight);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        this.threadNumber = threadNumber;
        this.tasksPerThread = tasksPerThread;
    }

    public void createSet() {
        LocalDateTime before = LocalDateTime.now();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNumber);
        List<Runnable> tasks = new ArrayList<>();
        int taskCount = threadNumber * tasksPerThread;
        for (int i = 0; i < taskCount; i++) {
            int height = getHeight() / taskCount * i;
            tasks.add(() -> threadDraw(height, getHeight() / taskCount));
        }
        CompletableFuture<?>[] futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(task, executor))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        executor.shutdown();
        LocalDateTime after = LocalDateTime.now();
        long diff = ChronoUnit.MILLIS.between(before, after);
        System.out.printf("Number of threads: %-5d" +
                        " Tasks per thread: %-5d" +
                        " Bounds: (%-5d, %-5d, %-5d, %-5d)" +
                        " Max Iterations: %-5d" + " Zoom: %-5.2f" + " Time (in millis): %-5d\n",
                threadNumber, tasksPerThread, Main.boundsX, Main.boundsY,
                Main.boundsWidth, Main.boundsWidth, Main.MAX_ITER, Main.ZOOM, diff);
    }

    private void threadDraw(int height, int dy) {
        for (int y = height; y < height + dy; y++) {
            for (int x = 0; x < getWidth(); x++) {
                double zx = 0;
                double zy = 0;
                double cX = (x - 800) / Main.ZOOM;
                double cY = (y - 400) / Main.ZOOM;
                int iter = Main.MAX_ITER;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                if (Main.isSynchronized) {
                    synchronized (this) {
                        I.setRGB(x, y, iter | (iter << 8));
                    }
                } else {
                    I.setRGB(x, y, iter | (iter << 8));
                }
            }
        }
    }

    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }
}
