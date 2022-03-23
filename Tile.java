public class Tile
{
    private Integer num;
    private int x;
    private int y;

    public Tile(Integer num)
    {
        this(num, 0, 0);
    }

    public Tile(Integer num, int x, int y)
    {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public int value()
    {
        return num; 
    }
}