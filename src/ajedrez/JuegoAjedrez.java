package ajedrez;

import ajedrez.componentes.CrearTablero;

import javax.swing.*;
import java.awt.*;

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

        ventana.setResizable(false);

        // Hacer visible la ventana
        ventana.setVisible(true);
        JOptionPane.showMessageDialog(null, "Para reiniciar el juego presione Ctrl+R", "Instrucción", JOptionPane.INFORMATION_MESSAGE);


    }

}
