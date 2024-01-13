/*
 *  Copyright 2023 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package dev.morling.onebrc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class CalculateAverage_marceloserpa {

    private static final String FILE = "./measurements.txt";

    private static Map<String, Double> preComputedDoubleConvertion = new HashMap<>();

    private static class Measurement {

        public Measurement(String location, double initialMetric) {
            this.location = location;
            this.min = initialMetric;
            this.max = initialMetric;
            this.accumulator = initialMetric;
            this.count = 1;
        }

        private String location;
        private double min;
        private double max;
        private double accumulator;
        private double count;

        public void addMetric(double metric) {
            if (metric < this.min) {
                this.min = metric;
            }
            else if (metric > this.max) {
                this.max = metric;
            }
            this.accumulator += metric;
            this.count++;
        }

        public double avg() {
            return accumulator / count;
        }

        public String getLocation() {
            return this.location;
        }

    }

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        Map<String, Measurement> metrics = new HashMap<>();

        try (FileInputStream fileInputStream = new FileInputStream(FILE);
                InputStreamReader reader = new InputStreamReader(fileInputStream);
                BufferedReader buffReader = new BufferedReader(reader)) {

            String line;
            String[] segments = new String[2];
            while ((line = buffReader.readLine()) != null) {
                segments = line.split(";");

                Measurement measurement = metrics.get(segments[0]);
                if (measurement == null) {
                    measurement = new Measurement(segments[0], stringToDouble(segments[1]));
                }
                else {
                    measurement.addMetric(stringToDouble(segments[1]));
                }
                metrics.put(segments[0], measurement);

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(metrics);

        System.out.printf("Calculated measurements in %s ms%n", System.currentTimeMillis() - start);
    }

    private static double stringToDouble(String stringDouble) {
        Double cache = preComputedDoubleConvertion.get(stringDouble);
        if (cache == null) {
            cache = Double.parseDouble(stringDouble);
            preComputedDoubleConvertion.put(stringDouble, cache);
        }
        return cache;
    }
}
