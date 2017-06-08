package com.tof.app;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void compareTo() {
        // Given
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("2", 2017002, 2, 1000));
        orders.add(new Order("1", 2017001, 8, 2000));

        // When
        Collections.sort(orders);

        // Then
        assertThat(orders.get(0).getId()).isEqualTo("1");
        assertThat(orders.get(1).getId()).isEqualTo("2");
    }

    @Test
    public void toCsvLine() {
        // Given
        Order order = new Order("2", 2017002, 2, 1000);

        // When
        String csvLine = order.toCsvLine();

        // Then
        assertThat(csvLine).isEqualTo("2;2017002;2;1000.0");
    }

    @Test
    public void contructorOrderFromCsvLine() {
        // Given
        String csvLine = "2;2017002;5;1000.0";

        // When
        Order order = new Order(csvLine);

        // Then
        assertThat(order.getId()).isEqualTo("2");
        assertThat(order.getStart()).isEqualTo(2017002);
        assertThat(order.getDuration()).isEqualTo(5);
        assertThat(order.getPrice()).isEqualTo(1000.0);
    }
}