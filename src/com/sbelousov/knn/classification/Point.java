package com.sbelousov.knn.classification;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private final List<Double> coordinates;
    private String klass;

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public String getKlass() {
        return klass;
    }

    public Point(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Point(List<Double> coordinates, String klass) {
        this.coordinates = coordinates;
        this.klass = klass;
    }

    public double getEuclideanDistance(Point point) {
        List<Double> items = new ArrayList<>();

        if (this.getCoordinates().size() != point.getCoordinates().size()) {
            throw new IllegalArgumentException("Dimensions of two points are unequal");
        }
        int dimensionsNumber = this.getCoordinates().size();

        for (int i = 0; i < dimensionsNumber; i++) {
            double oneCoordinate = this.getCoordinates().get(i);
            double anotherCoordinate = point.getCoordinates().get(i);
            items.add(Math.pow(oneCoordinate - anotherCoordinate, 2));
        }
        double sum = items.stream().mapToDouble(Double::doubleValue).sum();
        return Math.sqrt(sum);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Point(");
        for (Double coordinate : coordinates) {
            builder.append(coordinate).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        return builder.toString();
    }
}
