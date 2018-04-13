/*
 *  Creado por: David Pérez Sánchez
 *  Matrícula: 163202
 *  Materia: 
 *  Universidad Politécnica de Chiapas.
 *  Fecha de Creación: /10/2017
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author David Pérez S.
 */
public class Main extends Application {
      
      @Override
      public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocumentInicio.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Laberinto");
            stage.setResizable(false);
            stage.show();
      }

      /**
       * @param args the command line arguments
       */
      public static void main(String[] args) {
            launch(args);
      }
}