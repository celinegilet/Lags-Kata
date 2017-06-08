package com.tof.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LagsServiceTest {

    @Mock
    FileHandler fileHandler;

    @Mock
    Console console;

    @Test
    public void initOrdersFromFile_shouldCreateFile_whenFileDoesNotExists() throws IOException {
        // Given
        when(fileHandler.readLines()).thenThrow(new IOException());

        // When
        LagsService lagsService = new LagsService(fileHandler, console);

        // Then
        verify(fileHandler).writeLines(emptyList());
        assertThat(lagsService.getOrders()).isEmpty();
    }

    @Test
    public void initOrdersFromFile_shouldCreateListOrders_whenFileExists() throws IOException {
        // Given
        List<String> lines = asList("01;2015251;019;065622.00", "02;2015009;095;065936.00");
        when(fileHandler.readLines()).thenReturn(lines);

        // When
        LagsService lagsService = new LagsService(fileHandler, console);

        // Then
        assertThat(lagsService.getOrders()).hasSize(2);
    }

    @Test
    public void calculateSales_whenDebugIsEnabled() throws IOException {
        // Given
        Order order1 = new Order("DONALD", 2015001, 6, 10000.00);
        Order order2 = new Order("DAISY", 2015003, 2, 4000.00);
        Order order3 = new Order("PICSOU", 2015007, 7, 8000.00);
        Order order4 = new Order("MICKEY", 2015008, 7, 9000.00);
        when(fileHandler.readLines()).thenReturn(asList(order1.toCsvLine(), order2.toCsvLine(), order3.toCsvLine(), order4.toCsvLine()));
        LagsService lagsService = new LagsService(fileHandler, console);

        // When
        double sales = lagsService.calculateSales(true);

        // Then
        assertThat(sales).isEqualTo(19_000.00);
    }

    @Test
    public void calculateSales_whenDebugIsDisabled() throws IOException {
        // Given
        Order order1 = new Order("DONALD", 2015001, 6, 10000.00);
        Order order2 = new Order("DAISY", 2015003, 2, 4000.00);
        Order order3 = new Order("PICSOU", 2015007, 7, 8000.00);
        Order order4 = new Order("MICKEY", 2015008, 7, 9000.00);
        when(fileHandler.readLines()).thenReturn(asList(order1.toCsvLine(), order2.toCsvLine(), order3.toCsvLine(), order4.toCsvLine()));
        LagsService lagsService = new LagsService(fileHandler, console);

        // When
        double sales = lagsService.calculateSales(false);

        // Then
        assertThat(sales).isEqualTo(19_000.00);
    }

    @Test
    public void displayOrders() throws IOException {
        // Given
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order("DONALD", 2015001, 6, 10000.00);
        Order order2 = new Order("DAISY", 2015003, 2, 4000.00);
        when(fileHandler.readLines()).thenReturn(asList(order1.toCsvLine(), order2.toCsvLine()));
        LagsService lagsService = new LagsService(fileHandler, console);

        // When
        lagsService.displayOrders();

        // Then
        verify(console).println("LISTE DES ORDRES\n");
        verify(console).format("%8s %8s %5s %13s", "ID", "DEBUT", "DUREE", "PRIX\n");
        verify(console, times(2)).format("%8s %8s %5s %13s","--------", "-------", "-----", "----------" + "\n");
        verify(console).format("%8s %8d %5d %10.2f" + "\n", "DONALD", 2015001, 6, 10000.0d);
        verify(console).format("%8s %8d %5d %10.2f" + "\n", "DAISY", 2015003, 2, 4000.0d);
    }

    @Test
    public void addOrder() throws IOException {
        // Given
        when(console.readLine()).thenReturn("DONALD;2015001;6;10000.00");
        when(fileHandler.readLines()).thenReturn(emptyList());
        LagsService lagsService = new LagsService(fileHandler, console);

        // When
        lagsService.addOrder();

        // Then
        verify(console).println("AJOUTER UN ORDRE" + "\n" + "FORMAT = ID;DEBUT;FIN;PRIX");
        verify(fileHandler).writeLines(anyList());
        assertThat(lagsService.getOrders()).hasSize(1);
    }

    @Test
    public void deleteOrder() throws IOException {
        // Given
        when(console.readLine()).thenReturn("DONALD");
        when(fileHandler.readLines()).thenReturn(asList("DONALD;2015001;6;10000.00"));
        LagsService lagsService = new LagsService(fileHandler, console);

        // When
        lagsService.deleteOrder();

        // Then
        verify(console).println("SUPPRIMER UN ORDRE");
        verify(console).println("ID:");
        verify(fileHandler).writeLines(anyList());
        assertThat(lagsService.getOrders()).hasSize(0);
    }
}