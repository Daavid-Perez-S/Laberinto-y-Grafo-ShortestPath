/*
 *  Creado por: David Pérez Sánchez y Fer  :v
 *  Matrícula: 163202
 *  Materia: Estructura de Datos Avanzada
 *  Universidad Politécnica de Chiapas.
 *  Fecha de Creación: 08/04/2017
 */
package Grafico;

import Algoritmos.Node;
import Otros.Archivo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author David Pérez S.
 */
public class FXMLaberintoController implements Initializable {

      @FXML private Canvas canvasGrafico;
      @FXML private Label estadoLaberinto;
      @FXML private Button botonGenerarNuevoLaberinto;
      @FXML private Button botonVerMultimedia;
      private Integer[][] matrizLaberinto;
      private Integer[] previo;
      private List<List<Node>> listaAdyacencia;
      private String caminoCortoLaberinto;
      private String caminoCortoGrafo;
      private int tamañoMatrizInterna;
      private Boolean laberintoConSalida= false;
      
      public void setConfiguracion() throws FileNotFoundException{
            Archivo<Integer[][]> archivo1 = new Archivo<>("Matriz.bin");
            Archivo<Integer[]> archivo2 = new Archivo<>("ShortestPath.bin");
            Archivo<List<List<Node>>> archivo3 = new Archivo<>("ListaAdyacencia.bin");
            
            this.matrizLaberinto=  archivo1.deserializar();
            this.previo= archivo2.deserializar();
            this.listaAdyacencia= archivo3.deserializar();
            this.caminoCortoLaberinto= ",";
            this.caminoCortoGrafo= ",";
            this.tamañoMatrizInterna= matrizLaberinto.length - 2;
            // Generación del camino corto en la matriz INTERNA para uso del GRAFO
            this.laberintoConSalida= hasShortestPath(tamañoMatrizInterna * tamañoMatrizInterna);
            
            // Llenado de matriz auxiliar con las posiciones de los vértices en la matriz interna
            int[][] matrizAux = new int[matrizLaberinto.length][matrizLaberinto[0].length];
            int contador= 1, x = 0;
            String[] verticesAux = caminoCortoGrafo.split(",");
            ArrayList<Integer> listaOrdenadita= new ArrayList<>();
            for(int i=1; i < verticesAux.length; i++)
                listaOrdenadita.add(Integer.parseInt(verticesAux[i]));
            Collections.sort(listaOrdenadita);
            for(int f = 1; f < matrizAux.length - 1; f++) {
                  for(int c = 1; c < matrizAux[0].length - 1 && x < listaOrdenadita.size(); c++, contador++) {
                              if (contador == listaOrdenadita.get(x)) {
                                    matrizAux[f][c] = 1;
                                    x++;
                              }
                  }
            }
            // Generación del camino corto en la matriz EXTERNA para uso del LABERINTO
            contador= 1;
            for (int[] matrizAux1 : matrizAux) {
                  for (int c = 0; c < matrizAux[0].length; c++, contador++) {
                        if (matrizAux1[c] == 1) {
                              caminoCortoLaberinto= caminoCortoLaberinto + String.valueOf(contador) + ",";
                        }
                  }
            }
            System.out.println("\n\n Para llegar a la salida tiene que ir por los vértices del GRAFO: " + caminoCortoGrafo);
            System.out.println(" Para llegar a la salida tiene que ir por los vértices del LABERINTO: " + caminoCortoLaberinto);
            System.out.println("\n\n ------------------------------------------------------------------------------------------------------\n\n\n");
      }
      
      // Comprobación del camino mas corto desde el vértice inicial y final (Inicio - Salida)
      private Boolean hasShortestPath(int destino) {
            if (previo[destino] != -1)    // Si aún poseo un vértice previo
                  hasShortestPath(previo[destino]);       // Recursivamente sigo explorando
            caminoCortoGrafo= caminoCortoGrafo + String.valueOf(destino) + ",";         // Copio los valores a la cadena
            return caminoCortoGrafo.contains(",1,");
      }
      
      private void dibujarLaberinto() {
            GraphicsContext gc = canvasGrafico.getGraphicsContext2D();
            gc.setStroke(Color.BLACK);                      // Color negro - BORDES
            gc.setLineWidth(1);           // Grosor de las líneas
            double largoCuadro = canvasGrafico.getWidth() / matrizLaberinto.length;
            double anchoCuadro = canvasGrafico.getHeight() / matrizLaberinto[0].length;
            int contadorPosicionVertice= 1, f = 0;
            
            // Dibujamos los cuadritos, sus colores y sus etiquetas respectivamente
            for (double y = 0; y < canvasGrafico.getHeight() && f < matrizLaberinto.length; f++, y += anchoCuadro) {
                  int c = 0;
                  for (double x = 0; x < canvasGrafico.getWidth() && c < matrizLaberinto[0].length; c++, contadorPosicionVertice++, x += largoCuadro) {
                        if (matrizLaberinto[f][c] == 0)
                              gc.setFill(Color.rgb(150, 150, 150));           // Color gris - PARED
                        else if (f == 1 && c == 1)
                              gc.setFill(Color.rgb(30, 150, 255));            // Color celeste - INICIO
                        else if (f == (matrizLaberinto.length - 2) && c == (matrizLaberinto[0].length - 2))
                              gc.setFill(Color.rgb(255, 50, 30));             // Color rojo - SALIDA
                        else if (caminoCortoLaberinto.contains("," + String.valueOf(contadorPosicionVertice) + ","))
                              gc.setFill(Color.rgb(255, 200, 30));            // Color naranja - CAMINO
                        else if (matrizLaberinto[f][c] != 0)
                              gc.setFill(Color.WHITE);                        // Color blanco - ESPACIO
                        gc.fillRect(x, y, largoCuadro, anchoCuadro);
                        gc.strokeRect(x, y, largoCuadro, anchoCuadro);
                        gc.setFill(Color.BLACK);
                        gc.fillText(String.valueOf(matrizLaberinto[f][c]), x + (largoCuadro / 2), y + (anchoCuadro / 2));
                  }
            }
            gc.strokeRect(0, 0, canvasGrafico.getWidth(), canvasGrafico.getHeight());
      }
      
      private void imprimirLaberintoEstado() {
            if (laberintoConSalida) {
                  estadoLaberinto.setTextFill(Color.rgb(30, 150, 255));
                  estadoLaberinto.setText("Salida encontrada");
            } else {
                  estadoLaberinto.setTextFill(Color.rgb(255, 50, 30));
                  estadoLaberinto.setText("Laberinto sin salida");
            }
      }
      
      @FXML
      private void generarNuevoLaberinto(ActionEvent event) throws IOException, URISyntaxException {
            generarNuevoLaberinto();
      }
      private void generarNuevoLaberinto() throws IOException, URISyntaxException {
          
          Stage old = (Stage) estadoLaberinto.getScene().getWindow();
          Stage nuevo = new Stage();
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("/FXMLDocumentInicio.fxml"));
          Parent root = (Parent) loader.load();
          Scene scene = new Scene(root);
          nuevo.setScene(scene);
          nuevo.setResizable(false);
          old.close();
          nuevo.show();
      }
      @FXML
      private void generarMultimedia(ActionEvent event){
            generarMultimedia();
      }
      private void generarMultimedia(){
            GraphicsContext gc = canvasGrafico.getGraphicsContext2D();
            if(botonVerMultimedia.getText().equals("Ver Grafo")) {
                  botonVerMultimedia.setText("Ver Laberinto");
                  gc.clearRect(0, 0, canvasGrafico.getWidth(), canvasGrafico.getHeight());
                  botonVerMultimedia.setLayoutX(530);
                  dibujarGrafo();
            } else {
                  botonVerMultimedia.setText("Ver Grafo");
                  botonVerMultimedia.setLayoutX(540);
                  gc.clearRect(0, 0, canvasGrafico.getWidth(), canvasGrafico.getHeight());
                  dibujarLaberinto();
            }
      }
      private void dibujarGrafo() {
            GraphicsContext gc = canvasGrafico.getGraphicsContext2D();
            double largoCirculo = (canvasGrafico.getWidth() - 4) / (tamañoMatrizInterna + (tamañoMatrizInterna -1 ));
            double anchoCirculo = (canvasGrafico.getHeight() - 4) / (tamañoMatrizInterna + (tamañoMatrizInterna -1 ));
            int contadorPosicionVertice= 1;
            
            // Dibujar las líneas que unirán a cada nodo
            int limA= 1, limB= tamañoMatrizInterna;
            for (double y = 2; y < canvasGrafico.getHeight() - 2; y += (anchoCirculo + anchoCirculo)) {
                  for (double x = 2; x < canvasGrafico.getWidth() - 2; contadorPosicionVertice++, x += (largoCirculo + largoCirculo)) {
                        if(contadorPosicionVertice > limB) {
                              limA += tamañoMatrizInterna;
                              limB += tamañoMatrizInterna;
                        }
                        for (int contadorVerticeAdyacente = 0; contadorVerticeAdyacente < listaAdyacencia.get(contadorPosicionVertice).size(); contadorVerticeAdyacente++) {
                              Node nodo = listaAdyacencia.get(contadorPosicionVertice).get(contadorVerticeAdyacente);
                              if (caminoCortoGrafo.contains("," + String.valueOf(nodo.getFirst()) + ",") && caminoCortoGrafo.contains("," + String.valueOf(contadorPosicionVertice) + ",")) {
                                    gc.setStroke(Color.rgb(255, 200, 30));          // Color naranja - CAMINO LÍNEA
                                    gc.setLineWidth(5);           // Grosor de las líneas
                              } else {
                                    gc.setStroke(Color.BLACK);                      // Color negro - CAMINO NORMAL
                                    gc.setLineWidth(1);           // Grosor de las líneas
                              }
                              if (nodo.getFirst() >= limA && nodo.getFirst() <= limB) {
                                    // Vértice en la misma fila
                                    if (nodo.getFirst() > contadorPosicionVertice)
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x + (largoCirculo + largoCirculo + (largoCirculo / 2)), y + (anchoCirculo / 2));
                                    else
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x - (largoCirculo + (largoCirculo / 2)), y + (anchoCirculo / 2));
                              } else {
                                    // Vértice en alguna otra fila
                                    if(nodo.getFirst() == contadorPosicionVertice - (tamañoMatrizInterna + 1))          // Diagonal izquierda arriba
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x - (largoCirculo + (largoCirculo / 2)), y - anchoCirculo - (anchoCirculo / 2));
                                    else if(nodo.getFirst() == contadorPosicionVertice - tamañoMatrizInterna)           // Arriba
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x + (largoCirculo / 2), y - anchoCirculo - (anchoCirculo / 2));
                                    else if(nodo.getFirst() == contadorPosicionVertice - (tamañoMatrizInterna - 1))     // Diagonal derecha arriba
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x + (largoCirculo + largoCirculo + (largoCirculo / 2)), y - anchoCirculo - (anchoCirculo / 2));
                                    else if(nodo.getFirst() == contadorPosicionVertice + (tamañoMatrizInterna + 1))     // Diagonal derecha abajo
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x + (largoCirculo + largoCirculo + (largoCirculo / 2)), y + anchoCirculo + anchoCirculo + (anchoCirculo / 2));
                                    else if(nodo.getFirst() == contadorPosicionVertice + tamañoMatrizInterna)           // Abajo
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x + (largoCirculo / 2), y + anchoCirculo + anchoCirculo + (anchoCirculo / 2));
                                    else if(nodo.getFirst() == contadorPosicionVertice + (tamañoMatrizInterna - 1))     // Diagonal izquierda abajo
                                          gc.strokeLine(x + (largoCirculo / 2), y + (anchoCirculo / 2), x - (largoCirculo + (largoCirculo / 2)), y + anchoCirculo + anchoCirculo + (anchoCirculo / 2));
                              }
                        }
                  }
            }
            // Dibujamos los vértices (círculos), sus colores y sus etiquetas respectivamente
            contadorPosicionVertice= 1;
            gc.setStroke(Color.rgb(255, 200, 30));                      // Color naranja - BORDES
            gc.setLineWidth(1);           // Grosor de las líneas
            for (double y = 2; y < canvasGrafico.getHeight() - 2; y += (anchoCirculo + anchoCirculo)) {
                  for (double x = 2; x < canvasGrafico.getWidth() - 2; contadorPosicionVertice++, x += (largoCirculo + largoCirculo)) {
                        if (caminoCortoGrafo.contains("," + String.valueOf(contadorPosicionVertice) + ","))
                              // Dibujamos los círculos de color naranja - CAMINO
                              gc.setFill(Color.rgb(255, 200, 30));
                        else
                              // Dibujamos los círculos de color blanco - ESPACIO
                              gc.setFill(Color.WHITE);
                        gc.fillOval(x, y, largoCirculo, anchoCirculo);
                        gc.strokeOval(x, y, largoCirculo, anchoCirculo);
                        gc.setFill(Color.BLACK);
                        if (contadorPosicionVertice >= 10)
                              gc.fillText(String.valueOf(contadorPosicionVertice), x + (largoCirculo / 2) - (largoCirculo / 4), y + (anchoCirculo / 2) + (anchoCirculo / 8));
                        else
                              gc.fillText(String.valueOf(contadorPosicionVertice), x + (largoCirculo / 2) - (largoCirculo / 10), y + (anchoCirculo / 2) + (anchoCirculo / 8));
                  }
            }
      }
      /**
       * Initializes the controller class.
       * @param url
       * @param rb
       */
      @Override
      public void initialize(URL url, ResourceBundle rb) {
            try {
                  setConfiguracion();
            } catch (FileNotFoundException ex) {
                  Logger.getLogger(FXMLaberintoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            dibujarLaberinto();
            imprimirLaberintoEstado();
            botonGenerarNuevoLaberinto.setFocusTraversable(true);
            botonGenerarNuevoLaberinto.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent E) -> {
                  if (E.getCode() == ENTER)
                        try {
                              generarNuevoLaberinto();
                  } catch (IOException | URISyntaxException ex) {
                        Logger.getLogger(FXMLaberintoController.class.getName()).log(Level.SEVERE, null, ex);
                  }
            });
            botonVerMultimedia.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent E) -> {
                  if (E.getCode() == ENTER)
                        generarMultimedia();
            });
      }
}