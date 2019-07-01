package mosaico;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Simple algorithm executer
 *
 * @author yaron
 *
 */
public class Main extends Application{

    public static void main(String[] args) {

        //int matrixSize = 4;

        // Run the algorithm on 8x8 matrix with cell 1,1 colored
        // las posiciones empiezan desde 0
        //Algorithm algorithm = new Algorithm(matrixSize, 3, 2);
        //algorithm.run();
        
        launch(args);
        
    }
    
    
    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException{
        //Scene sc = new Scene(new Algorithm().getPanelPrincipal(),2000,1300);
        Scene sc = new Scene(new Algorithm().getPanelPrincipal());
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Resolver problema Mosaico Perdido Recursivo");
        sc.getStylesheets().add("mosaico/grid-with-borders.css");
        primaryStage.setScene(sc);
        primaryStage.show();
    }
    
}
