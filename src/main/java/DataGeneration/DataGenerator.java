package DataGeneration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class DataGenerator {
    private int numberOfCases = 100;
    private int lowerRandomRange;
    private int upperRandomRange;
    private int numberOfRandomConstants = 100;
    private int numberOfVariables = 1;
    private float domainLowerBound;
    private float domainUpperBound;
    StringBuilder data;
    double result;

    void generateData(int funcNumber) {
        float step = domainLowerBound < 0 ? (domainUpperBound + domainLowerBound) / numberOfCases : (domainUpperBound - domainLowerBound) / numberOfCases;
        data = new StringBuilder();
        data.append(String.format("%d %d %d %d %d\n", numberOfVariables, numberOfRandomConstants, lowerRandomRange, upperRandomRange, numberOfCases));
        for (float x = domainLowerBound; x <= domainUpperBound; x += step) {
            if (funcNumber == 1) {
                result = 5 * Math.pow(x, 3) - Math.pow(2 * x, 2) + 3 * x - 17;
            } else if (funcNumber == 2) {
                result = Math.sin(x) + Math.cos(x);
            } else if (funcNumber == 3) {
                result = 2 * Math.log(x + 1);
            }
//                    x + 2*y;
//                    Math.sin(x/2) + 2*Math.cos(x);
//                    Math.pow(x, 2) + 3*x*y - 7*y + 1
            data.append(x + " " + result + "\n");
        }
    }
}
