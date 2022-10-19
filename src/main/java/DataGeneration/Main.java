package DataGeneration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DataGenerator dataGenerator = new DataGenerator();

        //ustawianie wartosci
        dataGenerator.setNumberOfVariables(2);
        dataGenerator.setNumberOfRandomConstants(100);
        dataGenerator.setLowerRandomRange(-5);
        dataGenerator.setUpperRandomRange(5);
        dataGenerator.setNumberOfCases(101);

        //dziedzina
        dataGenerator.setDomainLowerBound(-10);
        dataGenerator.setDomainUpperBound(10);
        //generowanie danych
        dataGenerator.generateData(6);

        DataToFileWriter dataToFileWriter = new DataToFileWriter();
        dataToFileWriter.setFilename("6_-10_10.dat");
        dataToFileWriter.setData(dataGenerator.getData().toString());
        dataToFileWriter.writeToFile();
    }

}
