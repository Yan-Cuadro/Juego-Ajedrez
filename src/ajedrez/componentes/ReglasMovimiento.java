package ajedrez.componentes;

import javax.swing.*;

public class ReglasMovimiento {

    public boolean esMismoColor(Icon icono1, Icon icono2) {

        if (icono2 == null) return false;

        if (!(icono1 instanceof ImageIcon) || !(icono2 instanceof ImageIcon)) return false;

        String ruta1 = ((ImageIcon) icono1).getDescription();
        String ruta2 = ((ImageIcon) icono2).getDescription();

        // Comprobamos si ambos terminan en "-white.png" o "-black.png"
        boolean esBlanco1 = ruta1 != null && ruta1.contains("-white.png");
        boolean esBlanco2 = ruta2 != null && ruta2.contains("-white.png");

        boolean esNegro1 = ruta1 != null && ruta1.contains("-black.png");
        boolean esNegro2 = ruta2 != null && ruta2.contains("-black.png");

        return (esBlanco1 && esBlanco2) || (esNegro1 && esNegro2);
    }

    public boolean esMovimientoValidoPeonBlanco(int filaOrigen, int colOrigen, int filaDestino, int colDestino, JButton[][] botones) {

        // Movimiento hacia adelante una casilla
        if (colOrigen == colDestino && filaDestino == filaOrigen - 1 && botones[filaDestino][colDestino].getIcon() == null) {
            return true;
        }

        // Movimiento hacia delante dos casillas desde la posición inicial
        if (colOrigen == colDestino && filaOrigen == 6 && filaDestino == 4 &&
                botones[5][colDestino].getIcon() == null && botones[4][colDestino].getIcon() == null) {
            return true;
        }

        // captura en diagonal
        if ((colDestino == colOrigen + 1 || colDestino == colOrigen - 1) &&
                filaDestino == filaOrigen - 1 && botones[filaDestino][colDestino].getIcon() != null &&
                !obtenerColorPieza(botones[filaDestino][colDestino].getIcon()).equals("white")) {
            return true;
        }

        return false;
    }

    public boolean esMovimientoValidoPeonNegro(int filaOrigen, int colOrigen, int filaDestino, int colDestino, JButton[][] botones) {
        // Movimiento hacia adelante una casilla
        if (colOrigen == colDestino && filaDestino == filaOrigen + 1 && botones[filaDestino][colDestino].getIcon() == null) {
            return true;
        }

        // Movimiento hacia adelante dos casillas desde la posición inicial
        if (colOrigen == colDestino && filaOrigen == 1 && filaDestino == 3 &&
                botones[2][colDestino].getIcon() == null && botones[3][colDestino].getIcon() == null) {
            return true;
        }

        // Captura en diagonal
        if ((colDestino == colOrigen + 1 || colDestino == colOrigen - 1) &&
                filaDestino == filaOrigen + 1 &&
                botones[filaDestino][colDestino].getIcon() != null &&
                !obtenerColorPieza(botones[filaDestino][colDestino].getIcon()).equals("black")) {
            return true;
        }

        return false;
    }

    public boolean esMovimientoValidoTorre(int filaOrigen, int colOrigen, int filaDestino, int colDestino, JButton[][] botones) {

        // Si se mueve en la misma fila
        if (filaOrigen == filaDestino) {
            int paso = (colDestino > colOrigen) ? 1 : -1;
            for (int c = colOrigen + paso; c != colDestino; c += paso) {
                if (botones[filaOrigen][c].getIcon() != null) return false;
            }
            return true;
        }

        // Si se mueve en la misma columna
        if (colOrigen == colDestino) {
            int paso = (filaDestino > filaOrigen) ? 1 : -1;
            for (int f = filaOrigen + paso; f != filaDestino; f += paso) {
                if (botones[f][colOrigen].getIcon() != null) return false;
            }
            return true;
        }

        return false;
    }

    public boolean esMovimientoValidoAlfil(int filaOrigen, int colOrigen, int filaDestino, int colDestino, JButton[][] botones) {

        if (Math.abs(filaOrigen - filaDestino) != Math.abs(colOrigen - colDestino)) {
            return false;
        }

        return !hayPiezasEnDiagonal(botones, filaOrigen, colOrigen, filaDestino, colDestino);
    }

    public boolean hayPiezasEnDiagonal(JButton[][] tablero, int filaOrigen, int colOrigen, int filaDestino, int colDestino) {

        int pasoFila = (filaDestino > filaOrigen) ? 1 : -1;
        int pasoCol = (colDestino > colOrigen) ? 1 : -1;

        int fila = filaOrigen + pasoFila;
        int col = colOrigen + pasoCol;

        while (fila != filaDestino && col != colDestino) {
            if (tablero[fila][col].getIcon() != null) {
                return true;
            }
            fila += pasoFila;
            col += pasoCol;
        }

        return false;
    }

    public boolean hayPiezasMismaFila(int filaOrigen, int colOrigen, int colDestino, JButton[][] botones) {

        int paso = (colDestino > colOrigen) ? 1 : -1;
        for (int c = colOrigen + paso; c != colDestino; c += paso) {
            if (botones[filaOrigen][c].getIcon() != null) return true;
        }
        return false;
    }

    public boolean hayPiezasMismaColumna(int filaOrigen, int colOrigen, int filaDestino, JButton[][] botones) {

        int paso = (filaDestino > filaOrigen) ? 1 : -1;
        for (int f = filaOrigen + paso; f != filaDestino; f += paso) {
            if (botones[f][colOrigen].getIcon() != null) return true;
        }
        return false;
    }

    public boolean esMovimientoValidoCaballo(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        int diffFila = Math.abs(filaOrigen - filaDestino);
        int diffCol = Math.abs(colOrigen - colDestino);

        return (diffFila == 2 && diffCol == 1) || (diffFila == 1 && diffCol == 2);
    }

    public boolean esMovimientoValidoReina(int filaOrigen, int colOrigen, int filaDestino, int colDestino, JButton[][] botones) {
        // Movimiento como torre
        boolean movimientoTorre = (((filaOrigen == filaDestino) && !hayPiezasMismaFila(filaOrigen, colOrigen, colDestino, botones)) || ((colOrigen == colDestino) && !hayPiezasMismaColumna(filaOrigen, colOrigen, filaDestino, botones)));

        // Movimiento como alfil
        boolean movimientoAlfil = (Math.abs(filaOrigen - filaDestino) == Math.abs(colOrigen - colDestino)) && !hayPiezasEnDiagonal(botones, filaOrigen, colOrigen, filaDestino, colDestino);

        return movimientoTorre || movimientoAlfil;
    }

    public boolean esMovimientoValidoRey(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {

        int diferenciaFilas = Math.abs(filaOrigen - filaDestino);
        int diferenciaColumnas = Math.abs(colOrigen - colDestino);

        return diferenciaFilas <= 1 && diferenciaColumnas <= 1;
    }

    public boolean estaEnJaque(boolean reyBlanco, int TAM, JButton[][] botones) {

        // 1) Buscar la posición del rey correspondiente
        int filaRey = -1;
        int colRey = -1;
        for (int f = 0; f < TAM; f++) {
            for (int c = 0; c < TAM; c++) {
                Icon icon = botones[f][c].getIcon();
                if (icon != null) {
                    String nombre = obtenerNombrePieza(icon, "rey");
                    String color = obtenerColorPieza(icon);
                    if (nombre.equals("rey") && ((reyBlanco && color.equals("white")) || (!reyBlanco && color.equals("black")))) {
                        filaRey = f;
                        colRey = c;
                        break;

                    }
                }
            }
            if (filaRey != -1) break;
        }

        if (filaRey == -1) {
            // no encontramos el rey (no debería pasar en juego correcto)
            return false;
        }

        // 2) Revisar todas las piezas enemigas para ver si alguna puede atacar la casilla del rey
        String colorEnemigo = reyBlanco ? "black" : "white";

        for (int f = 0; f < TAM; f++) {
            for (int c = 0; c < TAM; c++) {
                Icon icon = botones[f][c].getIcon();
                if (icon == null) continue;

                String colorP = obtenerColorPieza(icon);
                if (!colorP.equals(colorEnemigo)) continue;

                // Determinar el tipo de pieza y usar la validación correspondiente
                if (obtenerNombrePieza(icon, "peon").equals("peon")) {
                    if (colorP.equals("white")) {
                        if (esMovimientoValidoPeonBlanco(f, c, filaRey, colRey, botones)) return true;
                    } else {
                        if (esMovimientoValidoPeonNegro(f, c, filaRey, colRey, botones)) return true;
                    }

                } else if (obtenerNombrePieza(icon, "torre").equals("torre")) {
                    if (esMovimientoValidoTorre(f, c, filaRey, colRey, botones)) return true;

                } else if (obtenerNombrePieza(icon,"alfil").equals("alfil")) {
                    if (esMovimientoValidoAlfil(f, c, filaRey, colRey, botones)) return true;

                } else if (obtenerNombrePieza(icon, "caballo").equals("caballo")) {
                    if (esMovimientoValidoCaballo(f, c, filaRey, colRey)) return true;

                } else if (obtenerNombrePieza(icon, "reina").equals("reina")) {
                    if (esMovimientoValidoReina(f, c, filaRey, colRey, botones)) return true;

                } else if (obtenerNombrePieza(icon, "rey").equals("rey")) {
                    if (esMovimientoValidoRey(f, c, filaRey, colRey)) return true;

                }

            }
        }

        return false;
    }

    public boolean esJaqueMate(boolean reyBlanco, int TAM, JButton[][] botones) {

        // Si no está en jaque, no puede ser jaque mate
        if (!estaEnJaque(reyBlanco, TAM, botones)) {
            return false;
        }

        // Probar todos los movimientos de todas las piezas del color
        for (int f = 0; f < TAM; f++) {
            for (int c = 0; c < TAM; c++) {
                Icon icon = botones[f][c].getIcon();
                if (icon == null) continue;

                String colorP = obtenerColorPieza(icon);
                if ((reyBlanco && !colorP.equals("white")) || (!reyBlanco && !colorP.equals("black"))) {
                    continue;
                }

                // Probar movimientos a todas las casillas
                for (int f2 = 0; f2 < TAM; f2++) {
                    for (int c2 = 0; c2 < TAM; c2++) {
                        if (f == f2 && c == c2) continue;
                        if (esMismoColor(botones[f][c].getIcon(), botones[f2][c2].getIcon())) continue;

                        if (puedeMoverPieza(f, c, f2, c2, botones)) {
                            // Simular movimiento
                            Icon temp = botones[f2][c2].getIcon();
                            botones[f2][c2].setIcon(botones[f][c].getIcon());
                            botones[f][c].setIcon(null);

                            boolean sigueEnJaque = estaEnJaque(reyBlanco, TAM, botones);

                            // Deshacer movimiento
                            botones[f][c].setIcon(botones[f2][c2].getIcon());
                            botones[f2][c2].setIcon(temp);

                            if (!sigueEnJaque) {
                                return false;  // Hay al menos un movimiento que salva al rey

                            }
                        }
                    }
                }
            }
        }

        return true; // Ningún movimiento salva al rey → jaque mate
    }

    private boolean puedeMoverPieza(int filaOrigen, int colOrigen, int filaDestino, int colDestino, JButton[][] botones) {
        Icon icon = botones[filaOrigen][colOrigen].getIcon();
        if (icon == null) return false;

        if (obtenerNombrePieza(icon, "peon").equals("peon")) {
            if (obtenerColorPieza(icon).equals("white")) {
                return esMovimientoValidoPeonBlanco(filaOrigen, colOrigen, filaDestino, colDestino, botones);
            } else {
                return esMovimientoValidoPeonNegro(filaOrigen, colOrigen, filaDestino, colDestino, botones);
            }
        } else if (obtenerNombrePieza(icon, "torre").equals("torre")) {
            return esMovimientoValidoTorre(filaOrigen, colOrigen, filaDestino, colDestino, botones);
        } else if (obtenerNombrePieza(icon, "alfil").equals("alfil")) {
            return esMovimientoValidoAlfil(filaOrigen, colOrigen, filaDestino, colDestino, botones);
        } else if (obtenerNombrePieza(icon, "caballo").equals("caballo")) {
            return esMovimientoValidoCaballo(filaOrigen, colOrigen, filaDestino, colDestino);
        } else if (obtenerNombrePieza(icon, "reina").equals("reina")) {
            return esMovimientoValidoReina(filaOrigen, colOrigen, filaDestino, colDestino, botones);
        } else if (obtenerNombrePieza(icon, "rey").equals("rey")) {
            return esMovimientoValidoRey(filaOrigen, colOrigen, filaDestino, colDestino);
        }

        return false;
    }

    public boolean hayMovimientoLegales(boolean reyBlanco, int TAM, JButton[][] botones) {
        for (int f = 0; f < TAM; f++) {
            for (int c = 0; c < TAM; c++) {
                Icon icon = botones[f][c].getIcon();
                if (icon == null) continue;

                String colorp = obtenerColorPieza(icon);
                if ((reyBlanco && !colorp.equals("white")) || (!reyBlanco && !colorp.equals("black"))) {
                    continue;
                }

                // intentar mover esa pieza a todas las casillas
                for (int f2 = 0; f2 < TAM; f2++) {
                    for (int c2 = 0; c2 <TAM; c2++) {
                        if (f == f2 && c == c2) continue;
                        // no intentar capturar pieza propia
                        if (esMismoColor(botones[f][c].getIcon(), botones[f2][c2].getIcon())) continue;

                        if (puedeMoverPieza(f, c, f2, c2, botones)) {
                            // simular movimiento
                            Icon origen = botones[f][c].getIcon();
                            Icon destinoPrev = botones[f2][c2].getIcon();

                            botones[f2][c2].setIcon(origen);
                            botones[f][c].setIcon(null);

                            boolean sigueEnJaque = estaEnJaque(reyBlanco, TAM, botones);

                            // deshacer simulación
                            botones[f][c].setIcon(origen);
                            botones[f2][c2].setIcon(destinoPrev);

                            if (!sigueEnJaque) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean esTablasPorAhogado(boolean reyBlanco, int TAM, JButton[][] botones) {
        // Si está en jaque, no es ahogado
        if (estaEnJaque(reyBlanco, TAM, botones)) return false;
        // Si no hay movimientos legales, es ahogado
        return !hayMovimientoLegales(reyBlanco, TAM, botones);
    }

    /*
     Insuficiencia de material (casos simples):
     - Solo reyes (2 piezas) => tablas
     - Solo un alfil o un caballo además de los reyes (es decir: total piezas = 3
       y la pieza extra es "alfil" o "caballo") => tablas
     - Puedes ampliar más casos si quieres (por ejemplo: rey+alfil vs rey+alfil con
       alfiles en el mismo color de casilla), pero esto cubre los casos habituales
    */
    public boolean esMaterialInsuficiente(int TAM, JButton[][] botones) {
        int countKings = 0;
        int countQueens = 0;
        int countRooks = 0;
        int countBishops = 0;
        int countKnights = 0;
        int countPawns = 0;
        int totalPieces = 0;

        for (int f = 0; f < TAM; f++) {
            for (int c = 0; c < TAM; c++) {
                Icon icon = botones[f][c].getIcon();
                if (icon == null) continue;
                totalPieces++;

                if (obtenerNombrePieza(icon, "rey").equals("rey")) countKings++;
                else if (obtenerNombrePieza(icon, "reina").equals("reina")) countQueens++;
                else if (obtenerNombrePieza(icon, "torre").equals("torre")) countRooks++;
                else if (obtenerNombrePieza(icon, "alfil").equals("alfil")) countBishops++;
                else if (obtenerNombrePieza(icon, "caballo").equals("caballo")) countKnights++;
                else if (obtenerNombrePieza(icon, "peon").equals("peon")) countPawns++;
            }
        }

        // Solo reyes
        if (totalPieces == 2 && countKings == 2) return true;

        // Solo un minor piece (alfil o caballo) además de los reyes
        if (totalPieces == 3 && (countBishops + countKnights) == 1 && countQueens == 0 && countRooks == 0 && countPawns == 0) {
            return true;
        }

        return false;
    }

    public boolean esTablas(boolean reyBlanco, int TAM, JButton[][] botones) {
        // 1) Ahogado
        if (esTablasPorAhogado(reyBlanco, TAM, botones)) return true;

        // 2) Insuficiencia de material
        if (esMaterialInsuficiente(TAM, botones)) return true;

        return false;
    }


    public String obtenerColorPieza(Icon icon) {
        if (icon == null) return "";

        String nombre = icon.toString().toLowerCase();

        if (nombre.contains("white")) return "white";
        if (nombre.contains("black")) return "black";

        return "";
    }

    public String obtenerNombrePieza(Icon icon, String pieza) {
        if (icon == null) return "";

        String nombre = icon.toString().toLowerCase();

        if (nombre.contains(pieza.toLowerCase())) return pieza.toLowerCase();

        return "";
    }

}
