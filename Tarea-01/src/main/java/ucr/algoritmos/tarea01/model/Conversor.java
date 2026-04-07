package ucr.algoritmos.tarea01.model;

/**
 * Clase base para la lógica de los conversores.
 */
public abstract class Conversor {
    protected double valorDeEntrada;

    public void setValorDeEntrada(double valorDeEntrada) {
        this.valorDeEntrada = valorDeEntrada;
    }

    public abstract double convertir();
}
