package com.sbelousov.knn;

import com.sbelousov.knn.classification.KNNAlgorithm;
import com.sbelousov.knn.classification.Point;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public KNNAlgorithm knnAlgorithm = new KNNAlgorithm();

    @FXML public VBox vbox;
    @FXML public TextField newDataField;
    @FXML public Label dataInfoText;
    @FXML public TextField kField;

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void onLoadDataButtonClick() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(vbox.getScene().getWindow());
        if (file == null) {
            showAlert("Ошибка!",
                    "Файл не может быть открыт.",
                    "Проверьте наличие файла и права доступа к нему.",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            knnAlgorithm.parseFile(file);
        }
        catch (NumberFormatException e) {
            showAlert("Ошибка!",
                    "Некорректный формат файла.",
                    "Проверьте соблюдение формата входных данных.",
                    Alert.AlertType.ERROR);
            return;
        }

        newDataField.setPromptText("0 ".repeat(knnAlgorithm.getDimensionNumber()));
        dataInfoText.setText("Размерность данных: " + knnAlgorithm.getDimensionNumber() + "\n" +
                "Количество точек: " + knnAlgorithm.getSize() + "\n" +
                "Количество классов: " + knnAlgorithm.getClassesCount());
    }

    public void onAboutDataFormatButtonClick() {
        showAlert("О формате входных данных",
                "Формат входных данных",
                "Первая строка файла должна содержать количество параметров (размерность, R).\n" +
                        "Последующие строки (произвольное количество) должны содержать R-1 значений, " +
                        "записанных через пробел.\n" +
                        "Первые R значений являются параметрами точки (координатами, вещественные числа), " +
                        "последнее значение - её класс (строка без пробелов).\n" +
                        "Пример файла:\n\n" +
                        "3\n" +
                        "4 9 0.1 Class1\n" +
                        "6 -2.5 1.1 Class2\n" +
                        "-1 -0.9 6.1 Class1\n" +
                        "-0.8 1 2 Class2",
                Alert.AlertType.INFORMATION);
    }

    public void onComputeButtonClick() {
        // Set K
        try {
            knnAlgorithm.setK(kField.getText());
        }
        catch (NumberFormatException e) {
            showAlert("Ошибка!",
                    "Некорректное значение K!",
                    "Количество соседей (K) должно быть целым положительным числом " +
                            "и не превышать количество точек.",
                    Alert.AlertType.ERROR);
            return;
        }

        // Parse test-point coordinates
        String[] splitData = newDataField.getText().split("\\s+");
        if (splitData.length != knnAlgorithm.getDimensionNumber()) {
            showAlert("Ошибка!",
                    "Некорректное количество параметров!",
                    "На входе должно быть " + knnAlgorithm.getDimensionNumber() + " параметров!",
                    Alert.AlertType.ERROR);
            return;
        }

        List<Double> coordinates = new ArrayList<>();
        for (String s : splitData) {
            double coordinate;
            try {
                coordinate = Double.parseDouble(s);
            }
            catch (NumberFormatException e) {
                showAlert("Ошибка!",
                        "Некорректный параметр!",
                        "Проверьте входные данные. Некорректное значение: " + s,
                        Alert.AlertType.ERROR);
                return;
            }
            coordinates.add(coordinate);
        }

        // Predict
        Point testPoint = new Point(coordinates);
        String predictedClass = knnAlgorithm.predictClass(testPoint);
        showAlert("Результат!",
                "Указанная точка соответствует классу " + predictedClass,
                null,
                Alert.AlertType.INFORMATION);
    }
}
