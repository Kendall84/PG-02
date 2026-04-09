package model;

import java.util.ArrayList;
import java.util.List;

public class Greedy {

    // Denominaciones oficiales para el algoritmo, ahora públicas y finales
    public static final int[] MONEDAS_CR = {500, 100, 50, 25, 10, 5, 1};

    /**
     * Algoritmo Greedy para el cambio de monedas.
     * @param monto El monto total a desglosar.
     * @return Una lista de objetos Moneda con el desglose.
     */
    public static List<Moneda> coinChange(int monto) {
        List<Moneda> usadas = new ArrayList<>();
        int remaining = monto;

        if (monto <= 0) {
            return usadas;
        }

        for (int coinValue : MONEDAS_CR) {
            int quantity = remaining / coinValue;
            if (quantity > 0) {
                int amountUsed = quantity * coinValue;
                remaining -= amountUsed;
                // Creamos un objeto Moneda para este paso
                // Parámetros: denominacion (String), cantidad (int), monto (double), restante (double)
                usadas.add(new Moneda(String.valueOf(coinValue), quantity, (double)amountUsed, (double)remaining));
            }
        }
        return usadas;
    }

    public static class KnapsackResult{
        public final Item[] sortedItems;
        public final List<Item> selected;
        private final double maxValue;
        private final int maxWeight;
        private final int capacity;
        public final long nanoTime;

        public KnapsackResult(Item[] sortedItems, List<Item> selected, double maxValue, int maxWeight, int capacity, long nanoTime) {
            this.sortedItems = sortedItems;
            this.selected = selected;
            this.maxValue = maxValue;
            this.maxWeight = maxWeight;
            this.capacity = capacity;
            this.nanoTime = nanoTime;
        }
    }

    public static KnapsackResult knapsackResult(Item[] items, int capacity){
        long t1= System.nanoTime();
        Item[] sortedItems = items.clone();
        bubbleSort(sortedItems);
        List<Item> selected = new ArrayList<>();
        double totalValue = 0;
        int totalWeight = 0;
        int remainingCapacity = capacity;

        for (Item item : sortedItems){
            if (remainingCapacity <= 0) break;
            if (item.getWeight() <=remainingCapacity){
                selected.add(new Item(item.getName(), item.getWeight(), item.getValue()));
                totalValue += item.getValue();
                totalWeight += item.getWeight();
                remainingCapacity -= item.getWeight();
            }
        }

        long nanos = System.nanoTime() - t1;
        return new KnapsackResult(sortedItems,selected,totalValue,totalWeight,capacity,nanos);
    }

    public static void bubbleSort(Item[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getRadio() < arr[j + 1].getRadio()) {
                    Item temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
}
