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
        dataGenerator.setDomainLowerBound(0);
        dataGenerator.setDomainUpperBound(7);
        //generowanie danych
        dataGenerator.generateData(5);

        DataToFileWriter dataToFileWriter = new DataToFileWriter();
        dataToFileWriter.setFilename("5_0_7.dat");
        dataToFileWriter.setData(dataGenerator.getData().toString());
        dataToFileWriter.writeToFile();
    }

}
