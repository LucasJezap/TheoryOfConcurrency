import java.util.ArrayList;
import java.util.List;

class DataGenerator {

    DataGenerator() {
    }

    List<String[]> generateDataLines() {
        List<String[]> dataLines = new ArrayList<>();

        String bufferSize = String.valueOf(Main.M);
        String pkConfig;
        String isFair = String.valueOf(Main.isFair);
        String Randomization;
        if (Main.producersNumber == 100)
            pkConfig = "100+100";
        else
            pkConfig = "1000+1000";
        if (Main.sameProb)
            Randomization = "uniform";
        else
            Randomization = "irregular";

        for (int i = 0; i < Main.M / 2 + 1; i++) {
            dataLines.add(new String[]
                    {bufferSize, "producer", String.valueOf(i), pkConfig, isFair, Randomization,
                            String.valueOf(Main.producersAccesses.get(i))});
        }

        for (int i = 0; i < Main.M / 2 + 1; i++) {
            dataLines.add(new String[]
                    {bufferSize, "consumer", String.valueOf(i), pkConfig, isFair, Randomization,
                            String.valueOf(Main.consumersAccesses.get(i))});
        }

        return dataLines;
    }
}
