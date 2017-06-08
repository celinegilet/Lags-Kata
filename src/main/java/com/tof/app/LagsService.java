package com.tof.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

class LagsService {

    private FileHandler fileHandler;
    private Console console;

    private List<Order> orders = new ArrayList<>();

    public static final double ZERO = 0.0;

    public static final String NEW_LINE = "\n";
    public static final String ORDER_FORMAT_PATTERN = "%8s %8s %5s %13s";

    public LagsService(FileHandler fileHandler, Console console) {
        this.fileHandler = fileHandler;
        this.console = console;
        initOrdersFromFile();
    }

    public void displayOrders() {
        console.println("LISTE DES ORDRES" + NEW_LINE);

        console.format(ORDER_FORMAT_PATTERN, "ID", "DEBUT", "DUREE", "PRIX" + NEW_LINE);
        console.format(ORDER_FORMAT_PATTERN, "--------", "-------", "-----", "----------" + NEW_LINE);

        orders.stream().forEach(
                order -> console.format("%8s %8d %5d %10.2f" + NEW_LINE,
                        order.getId(), order.getStart(), order.getDuration(), order.getPrice())
        );

        console.format(ORDER_FORMAT_PATTERN, "--------", "-------", "-----", "----------" + NEW_LINE);
    }

    public void addOrder() {
        console.println("AJOUTER UN ORDRE" + NEW_LINE + "FORMAT = ID;DEBUT;FIN;PRIX");
        orders.add(new Order(console.readLine()));
        writeOrdres();
    }

    public void deleteOrder() {
        console.println("SUPPRIMER UN ORDRE");
        console.println("ID:");
        String idOrderToRemove = console.readLine();
        orders.removeIf(order -> idOrderToRemove.equals(order.getId()));
        writeOrdres();
    }

    public double calculateSales(boolean debug) {
        return calculateSales(orders, debug);
    }

    private void initOrdersFromFile() {
        try {
            orders = fileHandler.readLines().stream().map(Order::new).collect(toList());
            Collections.sort(orders);
        } catch (IOException e) {

            writeOrdres();
        }
    }

    private void writeOrdres() {
        fileHandler.writeLines(orders.stream().map(Order::toCsvLine).collect(toList()));
    }

    private double calculateSales(List<Order> orders, boolean debug) {
        if (noMoreOrders(orders)) {
            return ZERO;
        }
        Order currentOrder = orders.get(0);
        double salesWithCurrentOrder = currentOrder.getPrice() + calculateSales(buildOrdersRemainingByStart(currentOrder), debug);
        double salesWithoutCurrentOrder = calculateSales(buildOrdersWithoutCurrentOrder(orders), debug);
        double salesBest = Math.max(salesWithCurrentOrder, salesWithoutCurrentOrder);
        printSales(debug, salesBest);
        return salesBest;
    }

    private boolean noMoreOrders(List<Order> orders) {
        return orders.size() == 0;
    }

    private List<Order> buildOrdersWithoutCurrentOrder(List<Order> orders) {
        List<Order> ordersWithoutCurrentOrder = new ArrayList<>(orders);
        ordersWithoutCurrentOrder.remove(0);
        return ordersWithoutCurrentOrder;
    }

    private List<Order> buildOrdersRemainingByStart(Order currentOrder) {
        List<Order> ordersByStart = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStart() >= currentOrder.getStart() + currentOrder.getDuration()) {
                ordersByStart.add(order);
            }
        }
        return ordersByStart;
    }

    private void printSales(boolean debug, double salesBest) {
        if (debug) {
            console.format("%10.2f\n", salesBest);
        } else {
            console.print(".");
        }
    }

    public List<Order> getOrders() {
        return orders;
    }
}

