import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class App extends Application
{
    private Board board;
    private BorderPane bp;

    public App()
    {
        board = new Board();
    }
    @Override
    public void init()
    {

    }
    @Override
    public void start(Stage primary)
    {
        primary.setTitle("Shift Game");

        this.bp = generateBorderPane(); 

        Scene s = new Scene(bp);
        // Load stylesheet from external file
        // https://stackoverflow.com/a/22755350
        s.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        
        primary.setScene(s);
        primary.show();
    }
    @Override
    public void stop()
    {
        
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Create a BorderPane with all necessary UI for the game
    private BorderPane generateBorderPane() {
        // Make the BorderPane with the grid in the center
        BorderPane bp = new BorderPane(board);

        // Make bp have the correct id for styling
        bp.setId("main-pane");

        // Make grid have the right id for styling
        board.setId("title-grid");
        
        // Fill in the grid with all the tiles
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // Make sure that this isn't the corner tile
                // TODO: add some kind of null tile instead of nothing
                if (j != 3 || i != 3) {
                    board.add(new Tile(j*4+i+1, i, j));
                }
            }
        }

        // Make a reset button
        BorderPane buttonPane = new BorderPane();
        Button resetButton = new Button("Reset");
        buttonPane.setBottom(resetButton);
        bp.setRight(buttonPane);

        return bp;
    }
}
