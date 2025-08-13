package ajedrez;

import ajedrez.componentes.CrearTablero;

import javax.swing.*;

public class JuegoAjedrez {

    public static void main(String[] args) {

        CrearTablero tablero = new CrearTablero();

        // Crear ventana
        JFrame ventana = new JFrame("Ajedrez");

        // Configurar el tamaño de la ventana
        ventana.setSize(800,800);

        // Hacer que se cierre al presionar X
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        ventana.add(tablero.crearCuadricula());

        // Hacer visible la ventana
        ventana.setVisible(true);

    }

}
