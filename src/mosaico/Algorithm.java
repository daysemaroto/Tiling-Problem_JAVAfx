package mosaico;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

//import static org.junit.Assert.*;
/**
 * Implementation of an algorithm to fill a grid of 2^n size that has one
 * missing cell with L-shaped tiles.
 *
 * The algorithms is a classic divide-and-conquer where we divide the rectangle
 * into four squares and adding the L-shaped tile in the middle.
 *
 * To make things somewhat interesting this algorithm is __NOT__ implemented in
 * the obvious, recursive way.
 *
 * @author yaron
 *
 */
public class Algorithm {

    public static int global = 0;

    // El tamano de la matrix es 2^n
    int n;

    // La posicion x de la celda pintada o perdida
    int posicionX;

    // La posicion y de la celda pintada o perdida
    int posicionY;

    // El valor de la celda del azulejo pintado
    private static final int PAINTED_TILE = 999;

    private BorderPane panelPrincipal = new BorderPane();

    private Label lblTitulo = new Label("Mosaico Recursivo");

    private Button btnFormarGrid = new Button("Formar");
    private Button btnEmpezarJuego = new Button("Empezar");
    private Button btnLimpiar = new Button("Limpiar");
    private Label lblCantidadN = new Label("Tamano Matriz 2^k. Ingrese el valor de K: ");
    private TextField txtCantidadN = new TextField();
    private Label lblPosicionX = new Label("Posicion x del azulejo: ");
    private TextField txtPosicionX = new TextField();
    private Label lblPosicionY = new Label("Posicion Y del azulejo: ");
    private TextField txtPosicionY = new TextField();
    //private VBox datos1 = new VBox();
    private HBox datos1 = new HBox(20);

    private GridPane grid = new GridPane();
    private StackPane cuadroPosicionPerdida = new StackPane();

    BooleanProperty[][] switches;
    private List<StackPane> listaCeldas = new ArrayList<>();

    /**
     * Initialized a new matrix with one colored cell
     *
     * @param n The size of the matrix is 2^n
     * @param posicionX The x coordinates of the colored cell
     * @param posicionY The y coordinates of the colored cell
     */
    public Algorithm() {
        estiloControles();
        organizarControles();
        manejarEventos();
    }

    public void estiloControles() {
        lblTitulo.setFont(new Font("Arial", 28));
        lblTitulo.setStyle("-fx-font-weight: bold");

        lblCantidadN.setFont(new Font("Arial", 16));
        lblCantidadN.setStyle("-fx-font-weight: bold");

        lblPosicionX.setFont(new Font("Arial", 16));
        lblPosicionX.setStyle("-fx-font-weight: bold");

        lblPosicionY.setFont(new Font("Arial", 16));
        lblPosicionY.setStyle("-fx-font-weight: bold");

        datos1.setPadding(new Insets(20, 0, 0, 0));

    }

    public void organizarControles() {

        datos1.getChildren().addAll(lblCantidadN, txtCantidadN, lblPosicionX, txtPosicionX, lblPosicionY, txtPosicionY, btnFormarGrid, btnEmpezarJuego, btnLimpiar);
        panelPrincipal.setBottom(datos1);
        panelPrincipal.setTop(lblTitulo);
    }

    public void manejarEventos() {
        btnFormarGrid.setOnAction(e -> formarAction());
        btnEmpezarJuego.setOnAction(e -> iniciarJuego());
        btnLimpiar.setOnAction(e -> limpiarTodo());
    }

    public void limpiarTodo() {
        txtCantidadN.clear();
        txtPosicionX.clear();
        txtPosicionY.clear();
        grid.getChildren().remove(cuadroPosicionPerdida);
        grid.getChildren().removeAll(listaCeldas);

        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }

    public void limpiarGrilla() {
        grid.getChildren().remove(cuadroPosicionPerdida);
        grid.getChildren().removeAll(listaCeldas);

        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
    }

    public BorderPane getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(BorderPane panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    /**
     * accion del boton Formar Grid
     *
     * @param n1 tamano matriz
     * @param x1 posicion x del azulejo perdido
     * @param y1 posicion y del azulejo perdido
     */
    void formarAction() {

        if ((txtCantidadN.getText().isEmpty()) || (txtPosicionX.getText().isEmpty()) || (txtPosicionY.getText().isEmpty())) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Mensaje");
            alerta.setContentText("Debe llenar todos los campos");
            alerta.show();
        } else {
            limpiarGrilla();
            int n1 = Integer.parseInt(txtCantidadN.getText());
            int x1 = Integer.parseInt(txtPosicionX.getText());
            int y1 = Integer.parseInt(txtPosicionY.getText());

            // this.n = n1;
            this.n = 1 << n1;
            this.posicionX = x1;
            this.posicionY = y1;

            try {
                switches = new BooleanProperty[n][n];

                for (int x = 0; x < n1; x++) {
                    for (int y = 0; y < n1; y++) {
                        switches[x][y] = new SimpleBooleanProperty();
                    }
                }
                createGrid();
            } catch (OutOfMemoryError error) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Mensaje");
                alerta.setContentText("No se puede crear GRID, sobrepasa el limite permitido para su computadora!");
                alerta.show();
            }

        }

    }

    /**
     * Metodo recursivo para llenar las celdas
     */
    void iniciarJuego() {
        global = 0;
        try {
            int[][] array = new int[n][n];
            //System.out.println(n);
            for (int i = 0; i < n; ++i) {

                for (int j = 0; j < n; ++j) {
                    array[i][j] = 0;
                }
            }

            long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
            TInicio = System.currentTimeMillis(); //Tomamos la hora en que inicio el algoritmo y la almacenamos en la variable inicio

            tromino(n, posicionX, posicionY, 0, 0, n, array); // calling the tromino function
            TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
            tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
            System.out.println("Tiempo de ejecución en milisegundos: " + tiempo); //Mostramos en pantalla el tiempo de ejecución en milisegundos

            print(array);
        } catch (OutOfMemoryError error) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Mensaje");
            alerta.setContentText("No se puede realizar el calculo, sobrepasa el limite permitido para su computadora!");
            alerta.show();

        }

        /*
        //probar con mas elementos mayores a 13 en el tamano del 2^k
        int nPrueba=1<<14;
        int arrayPrueba[][]=new int[nPrueba][nPrueba];
        tromino(nPrueba, posicionX, posicionY, 0, 0, n, arrayPrueba); // calling the tromino function
        print(arrayPrueba);
         */
    }

    void tromino(int board_size, int x_missing, int y_missing, int x_board, int y_board, int size, int array[][]) { // tromino implemenation
        if (board_size == 2) {
            global++;
            int i, j;

            for (i = x_board; i < (x_board + board_size); ++i) {
                for (j = y_board; j < (y_board + board_size); ++j) {
                    if (!(i == x_missing && j == y_missing)) {
                        array[i][j] = global;
                    }
                }
            }
            return;

        }
        global++;
        int half_size = board_size / 2, x_center, y_center;
        int x_upper_right = 0, y_upper_right = 0, x_upper_left = 0, y_upper_left = 0, x_lower_right = 0, y_lower_right = 0, x_lower_left = 0, y_lower_left = 0;
        x_center = x_board + half_size;
        y_center = y_board + half_size;

        if (x_missing < x_center && y_missing < y_center) { // checking that hole in the first quad, if yes than put tromino in center opposite quad.
            //printf("First qua\n");
            array[x_center - 1][y_center] = array[x_center][y_center - 1] = array[x_center][y_center] = global;

            x_upper_left = x_missing;
            y_upper_left = y_missing;

            x_upper_right = x_center - 1;
            y_upper_right = y_center;

            x_lower_left = x_center;
            y_lower_left = y_center - 1;

            x_lower_right = x_center;
            y_lower_right = y_center;

        } else if (x_missing < x_center && y_missing >= y_center) { // checking that hole in the second quad, if yes than put tromino in center opposite quad.
            //printf("Second qua\n");
            array[x_center - 1][y_center - 1] = array[x_center][y_center - 1] = array[x_center][y_center] = global;

            x_upper_left = x_center - 1;
            y_upper_left = y_center - 1;

            x_upper_right = x_missing;
            y_upper_right = y_missing;

            x_lower_left = x_center;
            y_lower_left = y_center - 1;

            x_lower_right = x_center;
            y_lower_right = y_center;

        } else if (x_missing >= x_center && y_missing < y_center) { // checking that hole in the third quad, if yes than put tromino in center opposite quad.
            //printf("Third Qua\n");
            array[x_center - 1][y_center - 1] = array[x_center - 1][y_center] = array[x_center][y_center] = global;

            x_upper_left = x_center - 1;
            y_upper_left = y_center - 1;

            x_upper_right = x_center - 1;
            y_upper_right = y_center;

            x_lower_left = x_missing;
            y_lower_left = y_missing;

            x_lower_right = x_center;
            y_lower_right = y_center;

        } else if (x_missing >= x_center && y_missing >= y_center) { // checking that hole in the fourth quad, if yes than put tromino in center opposite quad.
            //printf("Fourth Qua\n");
            array[x_center - 1][y_center - 1] = array[x_center - 1][y_center] = array[x_center][y_center - 1] = global;

            x_upper_left = x_center - 1;
            y_upper_left = y_center - 1;

            x_upper_right = x_center - 1;
            y_upper_right = y_center;

            x_lower_left = x_center;
            y_lower_left = y_center - 1;

            x_lower_right = x_missing;
            y_lower_right = y_missing;

        }

        tromino(half_size, x_upper_left, y_upper_left, x_board, y_board, size, array); // recursive call to the first qua

        tromino(half_size, x_upper_right, y_upper_right, x_board, y_board + half_size, size, array); // recursive call to the second qua

        tromino(half_size, x_lower_left, y_lower_left, x_board + half_size, y_board, size, array); // recursive call to the third qua

        tromino(half_size, x_lower_right, y_lower_right, x_board + half_size, y_board + half_size, size, array); // recursive call to the fourth qua

    }

    /**
     * Crea mi grid
     */
    private void createGrid() {

        int numCols = switches.length;
        int numRows = switches[0].length;

        for (int x = 0; x < numCols; x++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }
        for (int y = 0; y < numRows; y++) {
            RowConstraints rc = new RowConstraints();
            rc.setFillHeight(true);
            rc.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rc);
        }

        cuadroPosicionPerdida.getStyleClass().add("cuadro");
        grid.add(cuadroPosicionPerdida, posicionY, posicionX);
        grid.getStyleClass().add("grid");
        StackPane root = new StackPane(grid);

        panelPrincipal.setCenter(root);

    }

    /**
     * crea mis stackpane que van a ser de color rojo para ir rellenando la grid
     *
     * @return
     */
    private StackPane createCuadro() {
        StackPane cell = new StackPane();
        listaCeldas.add(cell);
        cell.getStyleClass().add("cuadroJuego");
        return cell;
    }

    /**
     *
     * Imprime los datos en la matriz
     *
     * @param iteration
     * @param data
     */
    private void print(int[][] data) {

        // System.out.println("Iterration " + iteration + ':');
        System.out.println("---------------------nueva-----------------------");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                System.out.printf("%5d ", data[i][j]);
                //if (data[i][j] != 0 & data[i][j] != 999) {
                if (data[i][j] != 0) {
                    grid.add(createCuadro(), j, i);
                }
            }
            System.out.println();
        }
    }

}
