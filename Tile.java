import javafx.scene.control.Button;

public class Tile extends Button
{
    // Standard Tile size
    public static final int size = 150;

    // Hold a reference to the parent Board so it can call Board functions
    private Board board;

    private Integer num;
    // TODO: does a Tile need to know its position?
    // If so, is a 2D position or an index more useful?
    // The 2D position could be acquired via getColumnIndex and getRowIndex in Board
    private int x;
    private int y;

    /**
     * Create a Tile with a value of num at (0, 0) relative to a Board
     * @param num the value of the tile
     */
    public Tile(Board board, Integer num)
    {
        this(board, num, 0, 0);
    }

    /**
     * Create a Tile with a value of num at (x, y) relative to a Board
     * @param num the value of the tile
     * @param x   the x position of the tile
     * @param y   the y position of the tile
     */
    public Tile(Board board, Integer num, int x, int y)
    {
        // Handle Button stuff
        super(Integer.toString(num));

        this.board = board;

        // Handle state
        this.num = num;
        this.x = x;
        this.y = y;

        // Handle clicking
        setOnAction(e -> {
            board.swapTile(this);
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
     * Get the x position of this Tile
     * @return the x position of this Tile
     */
    public int getX()
    {
        return x;
    }

    /**
     * Get the y position of this Tile
     * @return the y position of this Tile
     */
    public int getY()
    {
        return y;
    }

    /**
     * Set the x position of this Tile
     * @param x the new x position of this Tile
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * Set the y position of this Tile
     * @param y the new y position of this Tile
     */
    public void setY(int y)
    {
        this.y = y;
    }

    /**
     * Set the new position of this Tile
     * @param x the new x position of this Tile
     * @param y the new y position of this Tile
     */
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}