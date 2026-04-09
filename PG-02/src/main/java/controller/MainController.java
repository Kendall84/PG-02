package controller;


import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import model.ArrayPainter;
import model.Greedy;
import model.Moneda; // Importar la clase Moneda
import model.SearchEngine;
import model.SearchResult;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // --- TAB MONEDAS ---
    @FXML private HBox hboxCoins;
    @FXML private TableView<Moneda> tableCoins; // Cambiado a Moneda
    @FXML private TableColumn<Moneda, String> colMoneda; // Cambiado a Moneda, String para denominacion
    @FXML private TableColumn<Moneda, Integer> colCantidad; // Cambiado a Moneda
    @FXML private TableColumn<Moneda, Double> colMonto; // Cambiado a Moneda, Double para monto
    @FXML private TableColumn<Moneda, Double> colRestante; // Cambiado a Moneda, Double para restante
    @FXML private Slider sliderCoinAmount;
    @FXML private TextField txtCointValue;
    @FXML private Button btnCalcularMonedas;
    @FXML private Button btnLimpiarMonedas;
    @FXML private ListView<String> coinList;

    @FXML private Button btnBinAnimate;
    @FXML private ListView<String> listBinSteps;
    @FXML private Button btnBinGen;
    @FXML private Slider sliderBinSize;
    @FXML private Button btnBinSearch;
    @FXML private TextField txtBinValue;
    @FXML private Canvas canvasBin;
    @FXML private Label lblBinTime;
    @FXML private Label lblBinSize;
    @FXML private Label lblBinArray;
    @FXML private Button btnBinReset;
    @FXML private Label lblBinComplexity;

    private final SearchEngine searchEngine = new SearchEngine();
    private final ArrayPainter arrayPainter = new ArrayPainter();
    private Timeline animation;
    private int[] binArray;
    private SearchResult binResult;
    @FXML private Label lblBinResult;
    @FXML private ProgressBar progressBanBin;
    @FXML private Label lblComps;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCoinTab();
        setupBinTab();
    }

    private void setupCoinTab() {
        // Enlazar columnas con las propiedades de la clase Moneda
        colMoneda.setCellValueFactory(new PropertyValueFactory<>("denominacion"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colRestante.setCellValueFactory(new PropertyValueFactory<>("restante"));

        sliderCoinAmount.setMin(1);
        sliderCoinAmount.setMax(10000);
        sliderCoinAmount.setValue(787);
        sliderCoinAmount.valueProperty().addListener((observable, oldValue, newValue) -> {
            txtCointValue.setText(String.valueOf(newValue.intValue()));
        });
        txtCointValue.setText(String.valueOf((int)sliderCoinAmount.getValue()));

        btnCalcularMonedas.setOnAction(event -> generateCoinChange());
        if (btnLimpiarMonedas != null) {
            btnLimpiarMonedas.setOnAction(event -> {
                coinList.getItems().clear();
                tableCoins.getItems().clear();
            });
        }
    }

    //esto hizo la ia

    private VBox drawCoin(Moneda m) {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);

        // StackPane para poner la cantidad (burbuja roja) sobre la moneda
        StackPane stack = new StackPane();

        // Círculo principal de la moneda
        Circle circle = new Circle(40);
        String color = "#F1C40F"; // Dorado por defecto

        // Cambiar color según denominación (basado en tu captura)
        switch (m.getDenominacion()) {
            case "500": color = "#F1C40F"; break;
            case "100": color = "#BDC3C7"; break;
            case "25": case "10": case "5": color = "#D35400"; break;
            default: color = "#95A5A6";
        }

        circle.setStyle("-fx-fill: " + color + "; -fx-stroke: #2C3E50; -fx-stroke-width: 2;");

        // Texto de la denominación (ej: ₡500)
        Label lblValue = new Label("₡" + m.getDenominacion());
        lblValue.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: black;");

        // La burbuja roja (badge) para la cantidad (x6, x4, etc.)
        Circle badgeCircle = new Circle(12, Color.RED);
        Label lblQty = new Label("x" + m.getCantidad());
        lblQty.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");

        StackPane badge = new StackPane(badgeCircle, lblQty);
        // Posicionamos la burbuja arriba a la derecha
        badge.setTranslateX(30);
        badge.setTranslateY(-30);

        stack.getChildren().addAll(circle, lblValue, badge);
        container.getChildren().add(stack);

        return container;
    }

    //esto me dijo que le actulizara un par de cosas
    private void generateCoinChange() {
        int monto;
        try {
            monto = Integer.parseInt(txtCointValue.getText().trim());
            if (monto <= 0) {
                showAlert("Monto inválido", "Por favor ingrese un valor positivo", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error de entrada", "Monto no válido", Alert.AlertType.ERROR);
            return;
        }

        tableCoins.getItems().clear();
        coinList.getItems().clear();
        hboxCoins.getChildren().clear(); // Limpiamos las monedas visuales anteriores

        List<Moneda> resultList = Greedy.coinChange(monto);
        tableCoins.setItems(FXCollections.observableArrayList(resultList));

        ObservableList<String> textSteps = FXCollections.observableArrayList();
        for (Moneda m : resultList) {
            // Solo dibujamos y listamos si la cantidad es mayor a 0
            if (m.getCantidad() > 0) {
                // Agregar el círculo visual al HBox
                hboxCoins.getChildren().add(drawCoin(m));

                String step = String.format("[%02d] %s * %d = %.0f (remaining %.0f)",
                        textSteps.size() + 1, m.getDenominacion(), m.getCantidad(), m.getMonto(), m.getRestante());
                textSteps.add(step);
            }
        }
        coinList.setItems(textSteps);
    }


    //hasta aqui
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void runSearch(boolean animate) {
        if(binArray == null){
            showError(txtBinValue, "Primero genere un arreglo");
            return;
        }

        int value;
        try {
            value = Integer.parseInt(txtBinValue.getText());
        } catch (NumberFormatException e) {
            value = binArray[new Random().nextInt(binArray.length)];
            txtBinValue.setText(String.valueOf(value));
        }

        binArray = SearchEngine.ensureContains(binArray, value);
        updateArrayLabel(lblBinArray, binArray);
        SearchResult searchResult = searchEngine.binary(binArray, value);

        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < searchResult.steps.size(); i++) {
            SearchResult.Step step = searchResult.steps.get(i);
            items.add(String.format("[%02d] %s", i + 1, step.description));
        }
        listBinSteps.setItems(items);

        updateStats(lblBinResult, lblComps, lblBinTime, lblBinComplexity, searchResult);

        if (animate) {
            animateSearch(searchResult, binArray, canvasBin, progressBanBin, listBinSteps);
        } else {
            boolean[] vis = buildVisited(searchResult.steps, binArray.length, searchResult.steps.size());
            SearchResult.Step last = searchResult.steps.isEmpty() ? null : searchResult.steps.getLast();
            arrayPainter.paint(canvasBin, binArray, last, vis, searchResult.foundIndex);
            progressBanBin.setProgress(1.0);
        }
    }

    private void animateSearch(SearchResult res, int[] arr, Canvas canvas, ProgressBar pb, ListView<String> lv) {
        stopAnimation();
        if (res.steps.isEmpty()) return;

        int total = res.steps.size();
        int delay = Math.max(180, Math.min(800, 3000 / total));

        animation = new Timeline();
        for (int i = 1; i <= total; i++) {
            final int step = i;
            animation.getKeyFrames().add(new KeyFrame(Duration.millis(step * delay), e -> {
                SearchResult.Step s = res.steps.get(step - 1);
                boolean[] vis = buildVisited(res.steps, arr.length, step - 1);
                int found = s.isHit ? s.index : (step == total ? res.foundIndex : -1);
                arrayPainter.paint(canvas, arr, s, vis, found);
                pb.setProgress((double) step / total);
                lv.scrollTo(step - 1);
                lv.getSelectionModel().select(step - 1);
            }));
        }
        animation.setOnFinished(e -> pb.setProgress(1.0));
        animation.play();
    }

    private void stopAnimation() {
        if (animation != null) { animation.stop(); animation = null; }
    }

    private void updateStats(Label lblBinResult, Label lblBinComps, Label lblBinTime, Label lblBinComplexity, SearchResult searchResult) {
        if (searchResult == null) {
            clearStats(lblBinResult, lblComps, lblBinTime, lblBinComplexity);
            return;
        }
        if (searchResult.isFound()) {
            lblBinResult.setText("Encontrado en índice: " + searchResult.foundIndex);
            lblBinResult.setStyle("-fx-text-fill: #2ECC71; -fx-font-weight: bold;");
        } else {
            lblBinResult.setText("No encontrado");
            lblBinResult.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
        }
        lblComps.setText(String.valueOf(searchResult.comparisons));
        lblComps.setStyle("-fx-text-fill: #3498DB;");
        double timeMs = searchResult.nanoTime / 1_000_000.0;
        lblBinTime.setText(String.format("%.4f ms", timeMs));
        lblBinTime.setStyle("-fx-text-fill: #9B59B6;");
        lblBinComplexity.setText(searchResult.complexityLabel());
        lblBinComplexity.setStyle("-fx-text-fill: #F39C12;");
    }

    private void showError(TextField txt, String msg) {
        txt.setStyle("-fx-border-color: #E74C3C;");
        txt.setPromptText(msg);
        txt.setText("");
    }

    private void setupBinTab() {
        configSlider(sliderBinSize, 10, 50, 20, lblBinSize);
        btnBinGen.setOnAction(event -> generateBin());
        btnBinSearch.setOnAction(actionEvent -> runSearch(false));
        btnBinAnimate.setOnAction(actionEvent -> runSearch(true));
        if (btnBinReset != null) {
            btnBinReset.setOnAction(event -> {
                binArray = null;
                lblBinArray.setText("[ Arreglo Binario ]");
                canvasBin.getGraphicsContext2D().clearRect(0, 0, canvasBin.getWidth(), canvasBin.getHeight());
                listBinSteps.getItems().clear();
                clearStats(lblBinResult, lblComps, lblBinTime, lblBinComplexity);
                progressBanBin.setProgress(0);
            });
        }
    }

    private void generateBin(){
        int size = (int) sliderBinSize.getValue();
        binArray = SearchEngine.generateSorted(size, size * 15);
        updateArrayLabel(lblBinArray, binArray);
        canvasBin.getGraphicsContext2D().clearRect(0, 0, canvasBin.getWidth(), canvasBin.getHeight());
    }

    private void clearStats(Label... labels){
        for (Label l: labels){
            l.setText("-");
            l.setStyle("");
        }
    }

    private boolean[] buildVisited(List<SearchResult.Step> steps, int n, int upTo) {
        boolean[] vis = new boolean[n];
        int limit = Math.min(upTo, steps.size());
        for (int i = 0; i < limit; i++) {
            int idx = steps.get(i).index;
            if (idx >= 0 && idx < n) vis[idx] = true;
        }
        return vis;
    }

    private void updateArrayLabel(Label lbl, int[] arr) {
        if (arr == null || arr.length == 0) { lbl.setText(""); return; }
        StringBuilder sb = new StringBuilder("[");
        int show = Math.min(arr.length, 20);
        for (int i = 0; i < show; i++) {
            sb.append(arr[i]);
            if (i < show - 1) sb.append(", ");
        }
        if (arr.length > 20) sb.append(", …");
        sb.append("]  (n=").append(arr.length).append(")");
        lbl.setText(sb.toString());
    }

    private void configSlider(Slider s, int min, int max, int val, Label lbl) {
        s.setMin(min); s.setMax(max); s.setValue(val);
        s.setMajorTickUnit(5); s.setSnapToTicks(false);
        s.valueProperty().addListener((o, ov, nv) -> lbl.setText(String.valueOf(nv.intValue())));
        lbl.setText(String.valueOf(val));
    }
}
