import javafx.scene.control.Button;

public class Tile //extends Button
{
    private Integer num;
    public Tile(Integer num)
    {
        this.num = num; 
    }
    public int value()
    {
        return num; 
    }
    public String toString()
    {
        return "" + num; 

    }

    public static void main(String[] args)
        {

    }
}