/*
 *  Creado por: David Pérez Sánchez y Fer  :v
 *  Matrícula: 163202
 *  Materia: Estructura de Datos Avanzada
 *  Universidad Politécnica de Chiapas.
 *  Fecha de Creación: 07/04/2018
 */
package Algoritmos;

import Otros.Archivo;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author David Pérez S.
 */
public class AlgoritmoLaberinto {

    private final int tamañoMatriz;
    private int porcentajeParedes;
    private final List<List<Node>> listaAdyacencia;
    private Integer[][] matrizLaberinto;

    public AlgoritmoLaberinto(int tamañoMatriz, int porcentajeParedes) {
        this.listaAdyacencia = new ArrayList<>();
        this.tamañoMatriz = tamañoMatriz;
        this.porcentajeParedes = porcentajeParedes;
    }

    public void inicarAlgoritmo() throws FileNotFoundException {
        generarMatrizInicial();       // Incluye generar la lista de adyacencia
        ejecutarAlgoritmoDijkstra();
        serializarArchivosDeImportancia();
        imprimirListaAdyacencia();
    }

    private void generarMatrizInicial() {

        Integer[][] matrizInicial = new Integer[tamañoMatriz + 2][tamañoMatriz + 2];
        boolean banderaInicioOFinal;
        porcentajeParedes = comprobarRangosProbabilidad(porcentajeParedes);
//             System.out.println("\n Matriz:\n");
        for (int f = 0; f < matrizInicial.length; f++) {
            for (int c = 0; c < matrizInicial.length; c++) {
                if (f == 0 || c == 0 || f == matrizInicial.length - 1 || c == matrizInicial.length - 1) {
                    matrizInicial[f][c] = 0;
                } else {
                    banderaInicioOFinal = (f == 1 && c == 1) || (f == tamañoMatriz && c == tamañoMatriz);
                    Integer numeroAleatorio = generarNumerosAleatorios(1, 100, porcentajeParedes, banderaInicioOFinal);
                    while (((f == 1 && c == 1) || (f == tamañoMatriz && c == tamañoMatriz)) && numeroAleatorio == 0) {
                        numeroAleatorio = generarNumerosAleatorios(1, 100, porcentajeParedes, banderaInicioOFinal);
                    }
                    matrizInicial[f][c] = numeroAleatorio;
                }
//                         System.out.print(matrizInicial[f][c]);
            }
//                   System.out.println();
        }
//             System.out.println(" --------------------\n");
        generarListaAdyacencia(matrizInicial);
        this.matrizLaberinto = matrizInicial;
    }

    private void generarListaAdyacencia(Integer[][] matrizInicial) {

        for (int k = 0; k <= (tamañoMatriz * tamañoMatriz); k++) {
            listaAdyacencia.add(new ArrayList<>());         // Inicializamos la lista de adyacencia
        }
        int contadorPosActual = 1;
        int verticeDestino;
        boolean aristaDirigida = true;
        for (int f = 1; f < matrizInicial.length - 1; f++) {
            for (int c = 1; c < matrizInicial.length - 1; c++) {
                if (matrizInicial[f][c] != 0) {
                    if (matrizInicial[f - 1][c - 1] != 0) {                                        // Diagonal izquierda arriba
                        verticeDestino = contadorPosActual - (tamañoMatriz + 1);
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f - 1][c - 1], aristaDirigida);
                    }
                    if (matrizInicial[f - 1][c] != 0) {                                           // Arriba
                        verticeDestino = contadorPosActual - tamañoMatriz;
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f - 1][c], aristaDirigida);
                    }
                    if (matrizInicial[f - 1][c + 1] != 0) {                                      // Diagonal arriba derecha
                        verticeDestino = contadorPosActual - (tamañoMatriz - 1);
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f - 1][c + 1], aristaDirigida);
                    }
                    if (matrizInicial[f][c + 1] != 0) {                                        // Derecha
                        verticeDestino = contadorPosActual + 1;
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f][c + 1], aristaDirigida);
                    }
                    if (matrizInicial[f + 1][c + 1] != 0) {                                   // Diagonal derecha abajo
                        verticeDestino = contadorPosActual + (tamañoMatriz + 1);
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f + 1][c + 1], aristaDirigida);
                    }
                    if (matrizInicial[f + 1][c] != 0) {                                      // Abajo
                        verticeDestino = contadorPosActual + tamañoMatriz;
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f + 1][c], aristaDirigida);
                    }
                    if (matrizInicial[f + 1][c - 1] != 0) {                                  // Diagonal izquierda abajo
                        verticeDestino = contadorPosActual + (tamañoMatriz - 1);
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f + 1][c - 1], aristaDirigida);
                    }
                    if (matrizInicial[f][c - 1] != 0) {                                      // Izquierda
                        verticeDestino = contadorPosActual - 1;
                        addEdge(contadorPosActual, verticeDestino, matrizInicial[f][c - 1], aristaDirigida);
                    }
                }
                contadorPosActual++;
            }
        }
    }

    private void ejecutarAlgoritmoDijkstra() throws FileNotFoundException {
        AlgoritmoDijkstra algoritmoDijkstra = new AlgoritmoDijkstra((tamañoMatriz * tamañoMatriz), listaAdyacencia);      // Indicamos el número de vértices y la lista de adyacencia
        algoritmoDijkstra.ejecutarAlgoritmo((tamañoMatriz * tamañoMatriz));           // Indicamos donde esta la salida del laberinto
    }

    private void serializarArchivosDeImportancia() throws FileNotFoundException {
        Archivo<Integer[][]> archivo1 = new Archivo<>("Matriz.bin");
        archivo1.crearArchivoVacio(true);
        archivo1.serializar(this.matrizLaberinto);

        Archivo<List<List<Node>>> archivo2 = new Archivo<>("ListaAdyacencia.bin");
        archivo2.crearArchivoVacio(true);
        archivo2.serializar(this.listaAdyacencia);
    }

    private void addEdge(int origen, int destino, int peso, boolean dirigido) {
        listaAdyacencia.get(origen).add(new Node(destino, peso));    //grafo diridigo
        if (!dirigido) {
            listaAdyacencia.get(destino).add(new Node(origen, peso)); //no dirigido
        }
    }

    private int generarNumerosAleatorios(int min, int max, int probabilidad, boolean bandera) {
        if (bandera && probabilidad >= 100) {
            return 1;
        } else {
            int rango = Math.abs(max - min) + 1;
            double random = Math.random() * rango;
            int r = (int) random;
            while (r == 0 && probabilidad == 0) {
                random = Math.random() * rango;
                r = (int) random;
            }
            if (random < probabilidad) {
                return 0;
            } else if (random >= 10) {
                return (int) random / 10;
            } else {
                return (int) random;
            }
        }
    }

    private int comprobarRangosProbabilidad(int porcentajeParedes) {
        if (porcentajeParedes > 100) {
            porcentajeParedes = 100;
        }
        if (porcentajeParedes < 0) {
            porcentajeParedes = 0;
        }
        return porcentajeParedes;
    }
    
    private void imprimirListaAdyacencia(){
          System.out.println("\n");
          for (int i = 1; i < listaAdyacencia.size(); i++) {
                System.out.print(" El vertice " + i + " esta conectado con: ");
                System.out.print(listaAdyacencia.get(i));
                System.out.println("");
          }
    }
}