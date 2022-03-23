import java.util.ArrayList;
import javafx.scene.layout.GridPane;

public class Board //extends GridPane
{
    private ArrayList<Tile> tiles; 

    /**
     * Create an empty Board with no Tiles
     */
    public Board()
    {
        this.tiles = new ArrayList<>(); 

        tiles.add(new Tile(-1));
        for(int i = 1; i < 16; i++)
        {
           tiles.add(new Tile(i));
        }
    }

    /**
     * Add the Tile obj to this Board
     * @param obj the Tile to be added to the Board
     */
    public void add(Tile obj)
    {
        tiles.add(obj);
    }

    public void swap(Tile swap_obj)
    {
        /*I think we should call this method everytime the button gets clicked*/
        

        
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
            sb.append(String.format("%s, ", tiles.get(k).value()));  

        }
        sb.append(String.format("%s]",tiles.get(tiles.size() - 1).value()));
        return sb.toString();
    }
    
    public static void main(String[] args)
    {
        Board test = new Board(); 
        System.out.println(test);
    }
}
