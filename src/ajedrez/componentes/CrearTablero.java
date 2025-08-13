package ajedrez.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearTablero {

    private boolean esTurnoBlancas = true;

    private final ReglasMovimiento reglas = new ReglasMovimiento();

    private JButton casillaSeleccionada = null;
    private  Icon iconoSeleccionado = null;

    // Colores
    private final Color colorBlancas = new Color(238,238,213);
    private final Color colorNegras = new Color(125,148,93);

    // Definimos el tamaño del tablero
    private final int TAM = 8;

    // Creamos una matriz de botones, uno por cada casilla
    private final JButton[][] botones = new JButton[TAM][TAM];


    public JPanel crearCuadricula() {

        // Crear panel con layout de 8x8
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(TAM,TAM));

        // Agregar 64 casillas para simular el tablero
        for (int fila = 0; fila < TAM; fila++) {
            for (int col = 0; col < TAM; col++) {
                JButton boton = getJButton(fila, col);

                // Agregar al panel
                panelTablero.add(boton);
            }
        }

        // Colocamos las piezas
        colocarPiezasIniciales();
        return panelTablero;
    }

    public JButton getJButton(int fila, int col) {

        JButton boton = new JButton();

        // opciones para que se vea bien
        boton.setOpaque(true);  // permite ver el color de fondo
        boton.setBorderPainted(false); // sin borde exterior
        boton.setFocusPainted(false); // sin foco visual al hacer clic

        // blanco rgb(238,238,213)
        // verde rgb(125,148,93)
        // Alternar colores para simular el tablero
        if ((fila + col) % 2 == 0) {
            boton.setBackground(colorBlancas);
        } else {
            boton.setBackground(colorNegras);
        }

        // Aquí agregamos el ActionListener
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (casillaSeleccionada == null) {
                    // Primer clic: seleccionar pieza
                    if (boton.getIcon() != null) {

                        // comprobar turno: solo permitir seleccionar piezas del color correspondiente
                        String color = reglas.obtenerColorPieza(boton.getIcon()); // "white" / "black"
                        if ((esTurnoBlancas && !color.equals("white")) || (!esTurnoBlancas && !color.equals("black"))) {
                            JOptionPane.showMessageDialog(null, "No es tu turno");
                            return; // No seleccionar
                        }

                        casillaSeleccionada = boton;
                        iconoSeleccionado = boton.getIcon();
                    }
                } else {
                    // Segundo clic: mover pieza

                    int filaOrigen = getFila(casillaSeleccionada);
                    int colOrigen = getColumna(casillaSeleccionada);
                    int filaDestino = getFila(boton);
                    int colDestino = getColumna(boton);
                    Icon iconodestino = boton.getIcon();

                    String colorPieza = reglas.obtenerColorPieza(iconoSeleccionado);
                    boolean movimientoValido = false;

                    if (colorPieza.equals("white") && reglas.obtenerNombrePieza(iconoSeleccionado, "peon").equals("peon")) {
                        movimientoValido = reglas.esMovimientoValidoPeonBlanco(filaOrigen, colOrigen, filaDestino, colDestino, botones);

                    } else if (colorPieza.equals("black") && reglas.obtenerNombrePieza(iconoSeleccionado, "peon").equals("peon")) {
                        movimientoValido = reglas.esMovimientoValidoPeonNegro(filaOrigen, colOrigen, filaDestino, colDestino, botones);

                    } else if (reglas.obtenerNombrePieza(iconoSeleccionado, "torre").equals("torre") && !reglas.esMismoColor(iconoSeleccionado, iconodestino)) {
                        movimientoValido = reglas.esMovimientoValidoTorre(filaOrigen, colOrigen, filaDestino, colDestino, botones);

                    } else if (reglas.obtenerNombrePieza(iconoSeleccionado, "alfil").equals("alfil") && !reglas.esMismoColor(iconoSeleccionado, iconodestino)) {
                        movimientoValido = reglas.esMovimientoValidoAlfil(filaOrigen, colOrigen, filaDestino, colDestino, botones);

                    } else if (reglas.obtenerNombrePieza(iconoSeleccionado, "caballo").equals("caballo") && !reglas.esMismoColor(iconoSeleccionado, iconodestino)) {
                        movimientoValido = reglas.esMovimientoValidoCaballo(filaOrigen, colOrigen, filaDestino, colDestino);

                    } else if (reglas.obtenerNombrePieza(iconoSeleccionado, "reina").equals("reina") && !reglas.esMismoColor(iconoSeleccionado, iconodestino)) {
                        movimientoValido = reglas.esMovimientoValidoReina(filaOrigen, colOrigen, filaDestino, colDestino, botones);

                    } else if (reglas.obtenerNombrePieza(iconoSeleccionado, "rey").equals("rey") && !reglas.esMismoColor(iconoSeleccionado, iconodestino)) {
                        movimientoValido = reglas.esMovimientoValidoRey(filaOrigen, colOrigen, filaDestino, colDestino);

                    }

                    if (movimientoValido) {

                        // --- Simular el movimiento para ver si deja al rey del jugador en jaque ---
                        Icon iconoOrigen = casillaSeleccionada.getIcon();
                        Icon iconoDestinoPrevio = boton.getIcon();

                        // Aplicar movimiento temporal
                        casillaSeleccionada.setIcon((null));
                        boton.setIcon(iconoOrigen);

                        boolean miReyBlanco = colorPieza.equals("white");
                        boolean quedaEnJaque = reglas.estaEnJaque(miReyBlanco, TAM, botones);

                        // Revertir movimiento temporal
                        casillaSeleccionada.setIcon(iconoOrigen);
                        boton.setIcon(iconoDestinoPrevio);

                        if (quedaEnJaque) {
                            JOptionPane.showMessageDialog(null, "Movimiento inválido: deja a tu rey en jaque.");

                        } else {
                            // Confirmar movimiento real
                            // Mover la pieza
                            if (colorPieza.equals("white") && reglas.obtenerNombrePieza(iconoSeleccionado, "peon").equals("peon") && filaDestino == 0) {
                                boton.setIcon(crearIcono("src/imagenes/", "reina-white.png"));
                                casillaSeleccionada.setIcon(null);
                            } else if (colorPieza.equals("black") && reglas.obtenerNombrePieza(iconoSeleccionado, "peon").equals("peon") && filaDestino == 7) {
                                boton.setIcon(crearIcono("src/imagenes/", "reina-black.png"));
                                casillaSeleccionada.setIcon(null);
                            } else {
                                boton.setIcon(iconoSeleccionado);
                                casillaSeleccionada.setIcon(null);
                            }


                            // Cambiar Turno
                            esTurnoBlancas = !esTurnoBlancas;

                            // Verificar si el jugador que ahora le toca está en jaque o jaque mate
                            boolean turnoActualEsBlanco = esTurnoBlancas;

                            if (reglas.estaEnJaque(turnoActualEsBlanco, TAM, botones)) {
                                if (reglas.esJaqueMate(turnoActualEsBlanco, TAM, botones)) {
                                    String colorGanador = !turnoActualEsBlanco ? "Blancas" : "Negras";
                                    int opcion = JOptionPane.showOptionDialog(
                                            null,
                                            "¡Jaque mate, ganan "+ colorGanador +"! ¿Quieres reiniciar la partida?",
                                            "Fin del juego",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            new Object[]{"Sí", "No"},
                                            "Sí"
                                    );
                                    if (opcion == JOptionPane.YES_OPTION) {
                                        reiniciarJuego();
                                    }

                                }
                            } else {
                                // Si no hay jaque, comprobar tablas
                                if (reglas.esTablas(turnoActualEsBlanco, TAM, botones)) {
                                    int opcion = JOptionPane.showOptionDialog(
                                            null,
                                            "¡Tablas! ¿Quieres reiniciar la partida?",
                                            "Tablas",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            new Object[]{"Sí", "No"},
                                            "Sí"
                                    );
                                    if (opcion == JOptionPane.YES_OPTION) {
                                        reiniciarJuego();
                                    }
                                }
                            }
                        }


                    }

                    // Resetear selección
                    casillaSeleccionada = null;
                    iconoSeleccionado = null;
                }
            }
        });

        // Guardamos la posición como nombre
        boton.setName(fila + "," + col);

        // Guardamos el botón en la matriz
        botones[fila][col] = boton;

        return boton;
    }
    
    // Metodo para colocar las piezas al inicio
    public void colocarPiezasIniciales() {
        
        // Ruta base a las imagenes
        String ruta = "src/imagenes/";
        
        // Peones
        for (int col = 0; col < TAM; col++) {

            botones[1][col].setIcon(crearIcono(ruta, "peon-black.png"));

            botones[6][col].setIcon(crearIcono(ruta, "peon-white.png"));

        }

        crearIcono(ruta, "rey-white.png");

        //Torres
        botones[0][0].setIcon(crearIcono(ruta, "torre-black.png"));
        botones[0][7].setIcon(crearIcono(ruta, "torre-black.png"));
        botones[7][0].setIcon(crearIcono(ruta, "torre-white.png"));
        botones[7][7].setIcon(crearIcono(ruta, "torre-white.png"));

        // Caballos
        botones[0][1].setIcon(crearIcono(ruta, "caballo-black.png"));
        botones[0][6].setIcon(crearIcono(ruta, "caballo-black.png"));
        botones[7][1].setIcon(crearIcono(ruta, "caballo-white.png"));
        botones[7][6].setIcon(crearIcono(ruta, "caballo-white.png"));

        // Alfiles
        botones[0][2].setIcon(crearIcono(ruta, "alfil-black.png"));
        botones[0][5].setIcon(crearIcono(ruta, "alfil-black.png"));
        botones[7][2].setIcon(crearIcono(ruta, "alfil-white.png"));
        botones[7][5].setIcon(crearIcono(ruta, "alfil-white.png"));

        // Reinas
        botones[0][3].setIcon(crearIcono(ruta, "reina-black.png"));
        botones[7][3].setIcon(crearIcono(ruta, "reina-white.png"));

        // Reyes
        botones[0][4].setIcon(crearIcono(ruta, "rey-black.png"));
        botones[7][4].setIcon(crearIcono(ruta, "rey-white.png"));
    }

    private ImageIcon crearIcono(String ruta, String nombre) {

        ImageIcon icono = new ImageIcon(ruta + nombre);
        icono.setDescription(ruta + nombre);

        return icono;
    }

    private int getFila(JButton boton) {
        String[] partes = boton.getName().split(",");
        return Integer.parseInt(partes[0].trim());
    }

    private int getColumna(JButton boton) {
        String[] partes = boton.getName().split(",");
        return Integer.parseInt(partes[1].trim());
    }

    public void reiniciarJuego() {
        // Limpiar todas las casillas
        for (int fila = 0; fila < TAM; fila++) {
            for (int col = 0; col < TAM; col++) {
                botones[fila][col].setIcon(null);
            }
        }
        // Colocar piezas de nuevo
        colocarPiezasIniciales();

        // Resetear el turno
        esTurnoBlancas = true;

        // Resetear selección
        casillaSeleccionada = null;
        iconoSeleccionado = null;
    }


}
