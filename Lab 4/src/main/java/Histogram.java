
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;

class Histogram {
    private final int bufferSize;

    public Histogram(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void printValues() throws IOException {
        var dataset = new DefaultCategoryDataset();
        int x;
        if (bufferSize == 10000)
            x = 100;
        else
            x = 1000;
        for (int i = 0; i < (bufferSize / 2 + 1) / x; i++) {
            int tmp = 0;
            for (int j = i * x; j < (i + 1) * x; j++)
                tmp += Main.producersAccesses.get(j);
            dataset.addValue((Number) tmp, i + 1, "Buffer Size");
        }

        String title;
        if (bufferSize == 10000)
            title = "PRODUCERS: Number of accesses dependant on portion size (the " +
                    "buckets are 1-100, 101-200, ...)";
        else
            title = "PRODUCERS: Number of accesses dependant on portion size (the " +
                    "buckets are 1-1000, 1001-2000, ...)";
        JFreeChart histogramx = ChartFactory.createBarChart(title,
                "Size of portion", "Buffer accesses", dataset, PlotOrientation.VERTICAL, false, false, false);

        var dataset2 = new DefaultCategoryDataset();
        for (int i = 0; i < (bufferSize / 2 + 1) / x; i++) {
            int tmp = 0;
            for (int j = i * x; j < (i + 1) * x; j++)
                tmp += Main.consumersAccesses.get(j);
            dataset2.addValue((Number) tmp, i + 1, "Buffer Size");
        }

        String title2;
        if (bufferSize == 10000)
            title2 = "CONSUMERS: Number of accesses dependant on portion size (the " +
                    "buckets are 1-100, 101-200, ...)";
        else
            title2 = "CONSUMERS: Number of accesses dependant on portion size (the " +
                    "buckets are 1-1000, 1001-2000, ...)";
        JFreeChart histogramx2 = ChartFactory.createBarChart(title2,
                "Size of portion", "Buffer accesses", dataset2, PlotOrientation.VERTICAL, false, false, false);

        String text = "Buffer size: " + bufferSize;
        TextTitle legendText = new TextTitle(text);
        legendText.setPosition(RectangleEdge.BOTTOM);
        histogramx.addSubtitle(legendText);
        histogramx2.addSubtitle(legendText);

        if (Main.producersNumber == 100) {
            text = "PC Config: 100P + 100C";
        } else {
            text = "PC Config: 1000P + 1000C";
        }
        TextTitle legendText2 = new TextTitle(text);
        legendText2.setPosition(RectangleEdge.BOTTOM);
        histogramx.addSubtitle(legendText2);
        histogramx2.addSubtitle(legendText2);

        if (Main.isFair) {
            text = "Is buffer fair: yes";
        } else {
            text = "Is buffer fair: no";
        }
        TextTitle legendText3 = new TextTitle(text);
        legendText3.setPosition(RectangleEdge.BOTTOM);
        histogramx.addSubtitle(legendText3);
        histogramx2.addSubtitle(legendText3);

        if (Main.sameProb) {
            text = "Same portion probability: yes";
        } else {
            text = "Same portion probability: no";
        }
        TextTitle legendText4 = new TextTitle(text);
        legendText4.setPosition(RectangleEdge.BOTTOM);
        histogramx.addSubtitle(legendText4);
        histogramx2.addSubtitle(legendText4);

        ChartUtils.saveChartAsPNG(new File("producersAccesses.png"), histogramx, 1920, 1080);
        ChartUtils.saveChartAsPNG(new File("consumersAccesses.png"), histogramx2, 1920, 1080);
    }
}
