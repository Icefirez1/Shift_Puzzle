import javax.swing.plaf.synth.SynthListUI;

import javafx.application.Platform;
import javafx.scene.control.Button;

public class Tile extends Button
{
    // Standard Tile size
    public static final int size = 150;

    // Hold a reference to the parent Board so it can call Board functions
    private Board board;

    private Integer num;

    /**
     * Create a Tile with a value of num and no parent Board
     * @param num the value of the tile
     */
    public Tile(Integer num)
    {
        this(null, num);
    }

    /**
     * Create a Tile with a value of num and a parent Board board
     * @param board the parent board
     * @param num   the value of the tile
     */
    public Tile(Board board, Integer num)
    {
        // Handle Button stuff
        super(Integer.toString(num));

        this.board = board;

        // Handle state
        this.num = num;

        // Handle clicking
        setOnAction(e -> {
            this.board.swapTile(this);
            this.board.solved = this.board.check();
            System.out.println(this.board.solved); 
            if(this.board.solved)
            {
                System.out.println("yuh");
                //Runtime.exec("");
                // I gotta figure out how to like run the won.java popup whenever this.board is solved
            }
        
        });
    }

    /**
     * Get the value of the Tile
     * @return the value of this Tile
     */
    public int value()
    {
        return num; 
    }
    public void setValue(Integer new_num)
    {
        num = new_num; 
    }

    /**
     * Set the Board that this Tile knows
     * @param b the Board to set in the Tile
     */
    public void setBoard(Board b)
    {
        this.board = b;
    }
}