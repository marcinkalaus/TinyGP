package DataGeneration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DataGenerator dataGenerator = new DataGenerator();

        //ustawianie wartosci
        dataGenerator.setNumberOfVariables(1);
        dataGenerator.setNumberOfRandomConstants(100);
        dataGenerator.setLowerRandomRange(-5);
        dataGenerator.setUpperRandomRange(5);
        dataGenerator.setNumberOfCases(100);

        //dziedzina
        dataGenerator.setDomainLowerBound(0);
        dataGenerator.setDomainUpperBound(100);
        //generowanie danych
        dataGenerator.generateData(1);

        DataToFileWriter dataToFileWriter = new DataToFileWriter();
        dataToFileWriter.setFilename("1.dat");
        dataToFileWriter.setData(dataGenerator.getData().toString());
        dataToFileWriter.writeToFile();
    }

}
