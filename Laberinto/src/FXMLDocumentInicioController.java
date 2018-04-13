/*
 *  Creado por: David Pérez Sánchez y Fer  :v
 *  Matrícula: 163202
 *  Materia: Estructura de Dtaos Avanzadas
 *  Universidad Politécnica de Chiapas.
 *  Fecha de Creación: 21/03/2018
 */
import Algoritmos.AlgoritmoLaberinto;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
/**
 *
 * @author David Pérez S.
 */
public class FXMLDocumentInicioController implements Initializable {
      
      @FXML private Label labelMatriz;
      @FXML private TextField inputTamanio;
      @FXML private TextField inputParedes;
      @FXML private Button botonGenerar;
      
      @FXML
      private void generarMatrizInicial(ActionEvent event) throws IOException {
            generarMatrizInicial();
      }
      private void generarMatrizInicial() throws IOException {
            
            if (isNumeric(inputTamanio.getText()) && isNumeric(inputParedes.getText())) {
                  File file1 = new File("ListaAdyacencia.bin");
                  file1.delete();
                  File file2 = new File("Matriz.bin");
                  file2.delete();
                  File file3 = new File("ShortestPath.bin");
                  file3.delete();
                  if( Integer.parseInt(inputTamanio.getText()) >= 2 ){
                        int tamañoMatriz = Integer.parseInt(inputTamanio.getText());
                        int porcentajeParedes = Integer.parseInt(inputParedes.getText());
                        AlgoritmoLaberinto algoritmo = new AlgoritmoLaberinto(tamañoMatriz, porcentajeParedes);
                        algoritmo.inicarAlgoritmo();
                        abrirVentanaGrafica();
                  } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Laberinto");
                        alert.setContentText("El tamaño del laberinto tiene que ser mayor o igual a 2");
                        alert.showAndWait();
                  }
            } else {
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Laberinto");
                  alert.setContentText("Comprueba que tus datos sean correctos antes de iniciar");
                  alert.showAndWait();
            }
      }
      private void abrirVentanaGrafica() throws IOException{
          Stage old = (Stage) labelMatriz.getScene().getWindow();
          Stage nuevo = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("Grafico/FXMLaberinto.fxml"));
          Parent root = (Parent) loader.load();
          Scene scene = new Scene(root);
          nuevo.setScene(scene);
          nuevo.setResizable(false);
          nuevo.setTitle("Laberinto y Grafo");
          old.close();
          nuevo.show();
      }
      private static boolean isNumeric(String cadena) {
            boolean resultado;
            try {
                  Integer.parseInt(cadena);
                  resultado = true;
            } catch (NumberFormatException excepcion) {resultado = false;}
            return resultado;
      }
      @Override
      public void initialize(URL url, ResourceBundle rb) {
            inputTamanio.textProperty().addListener((observable, oldvalue, newvalue) -> {
                  labelMatriz.setText("Matriz de " + inputTamanio.getText() + " x " + inputTamanio.getText());
            });
            botonGenerar.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent E) -> {
                  if(E.getCode() == ENTER)
                        try {
                              generarMatrizInicial();
                  } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentInicioController.class.getName()).log(Level.SEVERE, null, ex);
                  }
            });
      }           
}