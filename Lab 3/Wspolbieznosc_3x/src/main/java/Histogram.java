
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class Histogram {
    private final int producersNumber;
    private final int consumersNumber;
    private final ArrayList<Integer> producersSizes;
    private final ArrayList<Integer> consumersSizes;
    private final ArrayList<Integer> producersAccesses;
    private final ArrayList<Integer> consumersAccesses;

    public Histogram(int producersNumber, int consumersNumber) {
        this.producersNumber = producersNumber;
        this.consumersNumber = consumersNumber;
        this.producersSizes = new ArrayList<>(Collections.nCopies(producersNumber, 0));
        this.consumersSizes = new ArrayList<>(Collections.nCopies(consumersNumber, 0));
        this.producersAccesses = new ArrayList<>(Collections.nCopies(producersNumber, 0));
        this.consumersAccesses = new ArrayList<>(Collections.nCopies(consumersNumber, 0));
    }

    public void setValue(boolean isProducer, int id, int size, int value) {
        if (isProducer) {
            producersSizes.set(id, size);
            producersAccesses.set(id, value);
        } else {
            consumersSizes.set(id, size);
            consumersAccesses.set(id, value);
        }
    }


    public void printValues() throws IOException {
        for (int i = 0; i < producersNumber; i++) {
            System.out.println("ProducerID = " + i + 1 +
                    " Size = " + producersSizes.get(i) + " Accesses = " + producersAccesses.get(i));
        }
        for (int i = 0; i < consumersNumber; i++) {
            System.out.println("ConsumerID = " + i + 1 +
                    " Size = " + consumersSizes.get(i) + " Accesses = " + consumersAccesses.get(i));
        }

        var dataset = new DefaultCategoryDataset();
        var used = new ArrayList<>(Collections.nCopies(producersNumber, false));
        for (int i = 0; i < producersNumber; i++) {
            int mx = 0;
            if (used.get(mx)) {
                for (int j = 1; j < producersNumber; j++) {
                    if (!used.get(j)) {
                        mx = j;
                        break;
                    }
                }
            }
            for (int j = 1; j < producersNumber; j++) {
                if (!used.get(j) && producersSizes.get(j) < producersSizes.get(mx)) {
                    mx = j;
                }
            }
            used.set(mx, true);
            dataset.addValue(producersAccesses.get(mx), producersSizes.get(mx), "Producer");
        }

        JFreeChart histogramx = ChartFactory.createBarChart("Number of accesses dependant on portion size (Producers)",
                "Size of portion", "Buffer accesses", dataset);

        ChartUtils.saveChartAsPNG(new File("histogramProducers.png"), histogramx, 1600, 900);

        var dataset2 = new DefaultCategoryDataset();
        var used2 = new ArrayList<>(Collections.nCopies(consumersNumber, false));
        for (int i = 0; i < consumersNumber; i++) {
            int mx = 0;
            if (used2.get(mx)) {
                for (int j = 1; j < consumersNumber; j++) {
                    if (!used2.get(j)) {
                        mx = j;
                        break;
                    }
                }
            }
            for (int j = 1; j < consumersNumber; j++) {
                if (!used2.get(j) && consumersSizes.get(j) < consumersSizes.get(mx)) {
                    mx = j;
                }
            }
            used2.set(mx, true);
            dataset2.addValue(consumersAccesses.get(mx), consumersSizes.get(mx), "Consumer");
        }

        JFreeChart histogramx2 = ChartFactory.createBarChart("Number of accesses dependant on portion size (Consumers)",
                "Size of portion", "Buffer accesses", dataset2);

        ChartUtils.saveChartAsPNG(new File("histogramConsumers.png"), histogramx2, 1600, 900);
    }
}
