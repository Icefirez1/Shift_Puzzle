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
    private GridPane grid;

    public App()
    {
        grid = new GridPane(); 
    }
    @Override
    public void init()
    {

    }
    @Override
    public void start(Stage primary)
    {
        primary.setTitle("Shift Game");

        BorderPane bp = new BorderPane(); 

        bp.setCenter(grid);

        Scene s = new Scene(bp, 694.20, 694.20);
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
}