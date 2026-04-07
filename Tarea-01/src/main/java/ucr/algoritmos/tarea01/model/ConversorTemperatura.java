package ucr.algoritmos.tarea01.model;

public class ConversorTemperatura extends Conversor {
    @Override
    public double convertir() {
        return (valorDeEntrada * 9.0 / 5.0) + 32.0;
    }
}
