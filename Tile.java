public class Tile
{
    private Integer num;
    private int x;
    private int y;

    /**
     * Create a Tile with a value of num at (0, 0) relative to a Board
     * @param num the value of the tile
     */
    public Tile(Integer num)
    {
        this(num, 0, 0);
    }

    /**
     * Create a Tile with a value of num at (x, y) relative to a Board
     * @param num the value of the tile
     * @param x   the x position of the tile
     * @param y   the y position of the tile
     */
    public Tile(Integer num, int x, int y)
    {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    /**
     * Get the value of the Tile
     * @return the value of this Tile
     */
    public int value()
    {
        return num; 
    }
}