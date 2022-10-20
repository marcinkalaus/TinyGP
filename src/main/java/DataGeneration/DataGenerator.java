package DataGeneration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class DataGenerator {
    private int numberOfCases = 100;
    private int lowerRandomRange;
    private int upperRandomRange;
    private int numberOfRandomConstants = 100;
    private int numberOfVariables = 2;
    private double domainLowerBound;
    private double domainUpperBound;
    StringBuilder data;
    double result;
    private Random rand = new Random();

    void generateData(int funcNumber) {
        double step =  (domainUpperBound - domainLowerBound) / numberOfCases;
        double y = Double.MIN_VALUE;
        data = new StringBuilder();
        data.append(String.format("%d %d %d %d %d\n", numberOfVariables, numberOfRandomConstants, lowerRandomRange, upperRandomRange, numberOfCases));
        for (double x =domainLowerBound; x <= domainUpperBound; x += step) {
            if (funcNumber == 1) {
                result = 5 * Math.pow(x, 3) - Math.pow(2 * x, 2) + 3 * x - 17;
            } else if (funcNumber == 2) {
                result = Math.sin(x) + Math.cos(x);
            } else if (funcNumber == 3) {
                result = 2 * Math.log(x + 1);
            } else if (funcNumber == 4) {
                y =  domainLowerBound + rand.nextFloat() * (upperRandomRange - lowerRandomRange);
                result = x + 2*y;
            } else if (funcNumber == 5) {
                y = domainLowerBound + rand.nextFloat() * (upperRandomRange - lowerRandomRange);
                result = Math.sin(x/2) + 2*Math.cos(y);
            } else if (funcNumber == 6) {
                y =  domainLowerBound + rand.nextFloat() * (upperRandomRange - lowerRandomRange);
                result = Math.pow(x, 2) + 3*x*y - 7*y + 1;
            }

            if (y != Double.MIN_VALUE){
                data.append(x + " " + y + " " + result + "\n");
            } else {
                data.append(x + " " + result + "\n");
            }
        }
    }
}
