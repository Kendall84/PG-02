package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Moneda {
    private final SimpleStringProperty denominacion;
    private final SimpleIntegerProperty cantidad;
    private final SimpleDoubleProperty monto;
    private final SimpleDoubleProperty restante;

    public Moneda(String denominacion, int cantidad, double monto, double restante) {
        this.denominacion = new SimpleStringProperty(denominacion);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.monto = new SimpleDoubleProperty(monto);
        this.restante = new SimpleDoubleProperty(restante);
    }

    public String getDenominacion() {
        return denominacion.get();
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public double getMonto() {
        return monto.get();
    }

    public double getRestante() {
        return restante.get();
    }

    public SimpleStringProperty denominacionProperty() {
        return denominacion;
    }

    public SimpleIntegerProperty cantidadProperty() {
        return cantidad;
    }

    public SimpleDoubleProperty montoProperty() {
        return monto;
    }

    public SimpleDoubleProperty restanteProperty() {
        return restante;
    }
}
