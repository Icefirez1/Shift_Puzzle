import java.util.ArrayList;
import javafx.scene.layout.GridPane;

public class Board //extends GridPane
{
    private ArrayList<ArrayList<Tile>> tiles; 
    public Board()
    {
        this.tiles = new ArrayList<>(); 

        Integer num = 1; 
        for(int i = 0; i < 4; i++)
        {
            ArrayList<Tile> current = new ArrayList<>(); 
            for (int j = 0; j < 4; j++)
            {
                current.add(new Tile(num));
                num ++; 
            }
            tiles.add(current); 
        }
        tiles.get(3).add(new Tile(-1));

    }
    // public void add(Tile obj)
    // {
    //     tiles.add(obj);
    // }

    public void swap(Tile swap_obj)
    {
        /*I think we should call this method everytime the button gets clicked*/

        
    }
    private String AString(ArrayList<Tile> array)
    {
        StringBuffer sb = new StringBuffer();
        if (array.size() == 0)
        {
            return "[]";
        }
        sb.append("[");
        for(int k = 0; k < array.size() - 1; k++)
        {
            sb.append(String.format("%s, ", tiles.get(k)));  
            
        }
        sb.append(String.format("%s]",tiles.get(tiles.size() - 1)));
        return sb.toString();
    }
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (tiles.size() == 0)
        {
            return "[]";
        }
        sb.append("[");
        for(int k = 0; k < tiles.size() - 1; k++)
        {
            sb.append(String.format("%s, ", AString(tiles.get(k))));  
            
        }
        sb.append(String.format("]"));
        return sb.toString();
    }
    
    public static void main(String[] args)
    {
        Board test = new Board(); 
        System.out.println(test);
    }
}
