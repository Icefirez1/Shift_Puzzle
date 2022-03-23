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
    private void swap(int j, int k)
    {
        Tile tmp = tiles.get(j);
        tiles.set(j, tiles.get(k));
        tiles.set(k, tmp);
    } 
    public void swapTile(Tile swap_obj)
    {
        /*I think we should call this method everytime the button gets clicked*/
        int pos = tiles.indexOf(swap_obj);
        if(pos + 1 < 16 && tiles.get(pos).value() == -1)
        {
            swap(pos, pos + 1);
        }
        else if(pos - 1  > 0 && tiles.get(pos).value() == -1)
        {
            swap(pos, pos + 1);
        }
        else if(pos + 4 < 16 && tiles.get(pos).value() == -1)
        {
            swap(pos, pos + 1);
        }
        else if(pos - 1 < 0 && tiles.get(pos).value() == -1)
        {
            swap(pos, pos + 1);
        }
        // i still havent accounted for like those weird cases but I no longer have any motivation

        
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
