module ucr.algoritmos.tarea01 {
    requires javafx.controls;
    requires javafx.fxml;

    // Abrimos el paquete principal para JavaFX
    opens ucr.algoritmos.tarea01 to javafx.fxml;
    exports ucr.algoritmos.tarea01;

    // Abrimos los controladores para que JavaFX (FXML) pueda inyectar los componentes
    opens ucr.algoritmos.tarea01.controller to javafx.fxml;
    exports ucr.algoritmos.tarea01.controller;

    // Exportamos el modelo
    opens ucr.algoritmos.tarea01.model to javafx.fxml;
    exports ucr.algoritmos.tarea01.model;
}
