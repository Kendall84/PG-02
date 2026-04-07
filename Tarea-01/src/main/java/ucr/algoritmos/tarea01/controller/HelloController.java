package ucr.algoritmos.tarea01.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador para la vista inicial
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("¡Welcome to JavaFX Application!");
    }
}
