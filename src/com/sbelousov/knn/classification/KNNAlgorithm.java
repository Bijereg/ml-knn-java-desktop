package com.sbelousov.knn.classification;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class KNNAlgorithm {
    private int K = 0;
    private int dimensionNumber = 0;
    private final List<Point> points = new ArrayList<>();
    private final Set<String> classes = new HashSet<>();

    public void setK(String s) throws NumberFormatException {
        int k = Integer.parseInt(s);
        if (k <= 0 || k > this.getSize()) {
            throw new NumberFormatException("Incorrect number K");
        }
        this.K = k;
    }

    public int getSize() {
        return points.size();
    }

    public int getClassesCount() {
        return classes.size();
    }

    public int getDimensionNumber() {
        return dimensionNumber;
    }

    public void parseFile(File file) {
        Path path = file.toPath();
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        dimensionNumber = Integer.parseInt(lines.get(0));
        for (int i = 1; i < lines.size(); i++) {
            List<Double> coordinates = new ArrayList<>();
            String[] splitted = lines.get(i).split("\\s+");
            for (int j = 0; j < dimensionNumber; j++) {
                coordinates.add(Double.parseDouble(splitted[j]));
            }
            classes.add(splitted[dimensionNumber]);
            points.add(new Point(coordinates, splitted[dimensionNumber]));
        }
    }

    public String predictClass(Point testPoint) {
        // Step 1: Calculate distance from testPoint to each point from points list
        Map<Point, Double> distances = new HashMap<>();
        for (Point point : points) {
            double distance = testPoint.getEuclideanDistance(point);
            distances.put(point, distance);
        }

        // Step 2: Sort calculated distances in descending order
        Map<Point, Double> sortedDistances = distances.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // Step 3: Get classes from K nearest points
        Iterator<Map.Entry<Point, Double>> iterator = sortedDistances.entrySet().iterator();
        List<String> classesInKNN = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            if (iterator.hasNext()) {
                Map.Entry<Point, Double> entry = iterator.next();
                classesInKNN.add(entry.getKey().getKlass());
            }
        }

        // Step 4: Calculate occurrences for each class
        Map<String, Long> occurrences =
                classesInKNN.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        // Return the most frequent class
        return Collections.max(occurrences.entrySet(), (entry1, entry2) ->
                (int) (entry1.getValue() - entry2.getValue())).getKey();
    }
}
