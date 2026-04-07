package ucr.algoritmos.tarea01.model;

public class ConversorKilometros extends Conversor {
    private static final double KILOMETROS_A_METROS = 1000.0;

    @Override
    public double convertir() {
        return valorDeEntrada * KILOMETROS_A_METROS;
    }
}
