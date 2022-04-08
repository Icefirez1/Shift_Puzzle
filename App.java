import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class App extends Application
{
    private Board board;
    private BorderPane bp;
    private Text t; 

    @Override
    public void init()
    {

    }
    @Override
    public void start(Stage primary)
    {
        primary.setTitle("Shift Game");
        // Make the center border pane
        this.bp = generateBorderPane();

        Scene s = new Scene(bp);
        // Load stylesheet from external file
        // https://stackoverflow.com/a/22755350
        s.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

        // Add listeners for keyboard controls
        s.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case W:
                case UP:
                    //board.swapTileRelativeToEmpty(0, -1);
                    board.swapTileRelativeToEmpty(0, 1);
                    break;
                case S:
                case DOWN:
                    //board.swapTileRelativeToEmpty(0, 1);
                    board.swapTileRelativeToEmpty(0, -1);
                    break;
                case A:
                case LEFT:
                    //board.swapTileRelativeToEmpty(-1, 0);
                    board.swapTileRelativeToEmpty(1, 0);
                    break;
                case D:
                case RIGHT:
                    //board.swapTileRelativeToEmpty(1, 0);
                    board.swapTileRelativeToEmpty(-1, 0);
                    break;
                // Default helps suppress ~90 warnings for some reason
                default:
                    break;
            }
        });
        
        
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
        // Make the BorderPane
        BorderPane bp = new BorderPane();

        // Make bp have the correct id for styling
        bp.setId("main-pane");

        // Make the board and add it to the BorderPane
        this.board = generateBoard();
        bp.setCenter(this.board);

        // Make a reset button
        BorderPane buttonPane = new BorderPane();
        Button resetButton = new Button("Reset");
        resetButton.setOnAction( e -> 
        {
            //reset it or whatever
            this.board = generateBoard();
            bp.setCenter(this.board);
        });

        
        buttonPane.setBottom(resetButton);
        buttonPane.setTop(t);
        bp.setRight(buttonPane);

        Button solveButton = new Button("Solve");
        buttonPane.setCenter(solveButton);
        solveButton.setOnAction(e -> {
            Solver.solve(board);
        });

        return bp;
    }

    // Generate a Board that is guaranteed to be solvable
    private Board generateBoard()
    {
        // Make an empty Tile ahead of time
        Tile empty = new Tile(-1);
        
        // Fill in an ArrayList with all the tiles
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                // Make sure that this isn't the corner tile
                if (j != 3 || i != 3) {
                    tiles.add(new Tile(i*4+j+1));
                }
            }
        }

        // Apply a Permutation
        Permutation p = Permutation.randomPermutation(15, 100);
        p.applyToList(tiles);

        // Add the empty Tile
        tiles.add(empty);

        // Add the Tiles to a board
        Board b = new Board(tiles);

        // Make the Tiles know their Board
        for (Tile t : tiles)
        {
            t.setBoard(b);
        }

        // Set the empty Tile and make it invisible
        empty.setVisible(false);
        b.setEmptyTile(empty);

        // Make the Board have the right id for styling
        b.setId("title-grid");

        return b;
    }
}
