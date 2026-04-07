package ucr.algoritmos.tarea01.model;

public class ConversorDolares extends Conversor {
    private static final double TIPO_DE_CAMBIO = 520.0;

    @Override
    public double convertir() {
        return valorDeEntrada * TIPO_DE_CAMBIO;
    }
}
