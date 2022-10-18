package agh.genetyczne;

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

    void generateData(int funcNumber) {
        float step = domainLowerBound < 0 ? (domainUpperBound + domainLowerBound)/numberOfCases: (domainUpperBound - domainLowerBound)/numberOfCases;
        for (float x = domainLowerBound; x <= domainUpperBound; x += step) {
//            switch (funcNumber) {
//                case 1:
//                    5 * Math.pow(x, 3) - Math.pow(2*x, 2) + 3*x -17;
//                    Math.sin(x) + Math.cos(x);
//                    2*Math.log(x+1);
//                    x + 2*y;
//                    Math.sin(x/2) + 2*Math.cos(x);
//                    Math.pow(x, 2) + 3*x*y - 7*y + 1;

            }
        }
    }

