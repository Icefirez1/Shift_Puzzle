import java.util.ArrayList;
import javafx.scene.layout.GridPane;

public class Board extends GridPane
{
    // Keep track of all tiles
    private ArrayList<Tile> tiles; 

    // Keep track of the empty tile
    private Tile emptyTile;

    /**
     * Create an empty Board with no Tiles
     */
    public Board()
    {
        super();
        this.tiles = new ArrayList<>(); 

        // TODO: should a -1/null/empty Tile be added automatically?
        // Manually add null/-1/empty tile?
        /* tiles.add(new Tile(-1));
        for(int i = 1; i < 16; i++)
        {
           tiles.add(new Tile(i));
        } */
    }

    /**
     * Create a Board with all the Tiles from Tiles
     * @param tiles a list of Tiles to have in the Board at the start
     */
    public Board(ArrayList<Tile> tiles) {
        super();
        this.tiles = tiles;
    }

    /**
     * Add the Tile obj to this Board
     * @param obj the Tile to be added to the Board
     */
    public void add(Tile obj)
    {
        // Add to the Board ArrayList
        // TODO: take obj's position into account
        // Ex. Tile at (2,3) should be at index 3*4+2==14
        tiles.add(obj);

        // Add to the GridPane
        add(obj, obj.getX(), obj.getY());
    }

    // TODO: have swap visually swap Tiles as well by changing GridPane
    // See GridPane's setRowIndex and setColumnIndex
    // TODO: have swap actually alter the ArrayList internally
    private void swap(int j, int k)
    {
        Tile a = tiles.get(j);
        Tile b = tiles.get(k);

        // Swap the Tiles in the ArrayList
        tiles.set(j, b);
        tiles.set(k, a);
        
        // Visually swap the Tiles
        int ax = getRowIndex(a);
        int ay = getColumnIndex(a);
        int bx = getRowIndex(b);
        int by = getColumnIndex(b);

        setRowIndex(a, bx);
        setColumnIndex(a, by);
        setRowIndex(b, ax);
        setColumnIndex(b, ay);

        // Swap the Tiles' internal position
        int tx = a.getX();
        int ty = a.getY();
        a.setPosition(b.getX(), b.getY());
        b.setPosition(tx, ty);
    }

    public void swapTile(Tile swap_obj)
    {
        /*I think we should call this method everytime the button gets clicked*/
        int tilePos = tiles.indexOf(swap_obj);
        int emptyPos = tiles.indexOf(emptyTile);
        
        // Check if tilePos is visually above or below emptyPos
        if (tilePos + 4 == emptyPos || tilePos -4 == emptyPos)
        {
            swap(tilePos, emptyPos);
        }
        // Check if tilePos is visually to the left of emptyPos
        // Special case for edges
        if (tilePos + 1 == emptyPos && tilePos % 4 != 3)
        {
            swap(tilePos, emptyPos);
        }

        // Check if tilePos is visually to the right of emptyPos
        // Special case for edges
        if (tilePos - 1 == emptyPos && tilePos % 4 != 0)
        {
            swap(tilePos, emptyPos);
        }

        // If Tile is to the left of empty
        /* if(tilePos + 1 == emptyPos)
        {
            System.out.println(1);
            swap(tilePos, emptyPos);
        }
        // If the Tile is to the right of empty
        else if(tilePos - 1 == emptyPos)
        {
            System.out.println(2);
            swap(tilePos, emptyPos);
        }
        // If the Tile is above empty
        else if(tilePos + 4 == emptyPos)
        {
            System.out.println(3);
            swap(tilePos, emptyPos);
        }
        // If the Tile is below empty
        else if(tilePos - 4 == emptyPos)
        {
            System.out.println(4);
            swap(tilePos, emptyPos);
        } */
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

    /**
     * Set the empty Tile for this Board
     * @param t the Tile to set as the empty Tile
     */
    public void setEmptyTile(Tile t) {
        this.emptyTile = t;
    }
    
    public static void main(String[] args)
    {
        Board test = new Board(); 
        System.out.println(test);
    }
}
