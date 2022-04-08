import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.scene.layout.GridPane;

public class Board extends GridPane
{
    // Keep track of all tiles
    private ArrayList<Tile> tiles; 

    // Keep track of the empty tile
    private Tile emptyTile;

    //Keep track of the solveable of the board 
    public Boolean solved = false; 
    /**
     * Create an empty Board with no Tiles
     */
    public Board()
    {
        super();
        this.tiles = new ArrayList<>(); 
    }

    /**
     * Create a Board with all the Tiles from Tiles
     * @param tiles a list of Tiles to have in the Board at the start
     */
    public Board(ArrayList<Tile> tileList) {
        super();
        
        /* // Add Tiles to internal ArrayList
        this.tiles = tiles;

        // Add Tiles to GridPane
        // Add as many Tiles as are in tiles, but no more than 16
        int x = 0;
        int y = 0;
        for (int i = 0; i < Math.min(tiles.size(), 16); i++) {
            // Add Tile to GridPane
            add(tiles.get(i), x, y);
            
            // Move to next position, wrapping as necessary
            x++;
            if (x >= 4) {
                x %= 4;
                y++;
            }
        } */
        this.tiles = new ArrayList<>();

        int x = 0;
        int y = 0;
        for (Tile t : tileList) {
            // Add the Tile to the ArrayList
            this.addTile(t, x, y);

            // Move to the next position, wrapping as necessary
            x++;
            if (x >= 4) {
                x %= 4;
                y++;
            }
        }
    }

    /**
     * Add the Tile obj to this Board
     * @param obj the Tile to be added to the Board
     * @param x   the x coordinate to add the Tile at
     * @param y   the y coordinate to add the Tile at
     */
    public void addTile(Tile obj, int x, int y)
    {
        // Add to the Board ArrayList
        tiles.add(obj);

        // Add to the GridPane
        if (obj.value() == -1)
        {
            obj.setText("");
            add(obj, x, y);
        }
        else
        {
            add(obj, x, y);
        }

    }

    private void swap(int j, int k)
    {
        // Get the Tiles
        Tile a = tiles.get(j);
        Tile b = tiles.get(k);

        // Get the position in the GridPane
        int ax = getRowIndex(a);
        int ay = getColumnIndex(a);
        int bx = getRowIndex(b);
        int by = getColumnIndex(b);

        // Swap the Tiles in the ArrayList
        tiles.set(j, b);
        tiles.set(k, a);
        
        // Visually swap the Tiles
        setRowIndex(a, bx);
        setColumnIndex(a, by);
        setRowIndex(b, ax);
        setColumnIndex(b, ay);
    }

    /**
     * Swap swap_obj with the empty Tile if it is next to it
     * @param swap_obj the Tile to try to swap
     * @return true if a swap happened, false otherwise
     */
    public boolean swapTile(Tile swap_obj)
    {
        // Get the index of the current Tile and the empty Tile
        int tilePos = tiles.indexOf(swap_obj);
        int emptyPos = tiles.indexOf(emptyTile);
        
        // Check if tilePos is visually above or below emptyPos
        if (tilePos + 4 == emptyPos || tilePos -4 == emptyPos)
        {
            swap(tilePos, emptyPos);
            return true;


        }
        // Check if tilePos is visually to the left of emptyPos
        // Special case for edges
        if (tilePos + 1 == emptyPos && tilePos % 4 != 3)
        {
            swap(tilePos, emptyPos);
            return true;
        }

        // Check if tilePos is visually to the right of emptyPos
        // Special case for edges
        if (tilePos - 1 == emptyPos && tilePos % 4 != 0)
        {
            swap(tilePos, emptyPos);
            return true;
        }

        // If no if statements were it, return a failure
        return false;
    }

    /**
     * Swap the empty Tile with a Tile at the position (x,y)
     * relative to the empty Tile
     * @param x the relative x coordinate of the Tile to swap
     * @param y the relative y coordinate of the Tile to swap
     */
    public void swapTileRelativeToEmpty(int x, int y)
    {
        // Get the coordinates of the empty Tile
        int ex = getColumnIndex(emptyTile);
        int ey = getRowIndex(emptyTile);

        // Calculate the new offset and get the new Tile
        int tx = ex + x;
        int ty = ey + y;
        Tile t = getTile(tx, ty);

        // If t isn't null, "click" it
        if (t != null)
        {
            t.fire();
        }
    }

    /**
     * Get the Tile at (x, y)
     * @param x the x coordinate of the Tile to look for
     * @param y the y coordinate of the Tile to look for
     * @return the Tile at (x,y) or null if no such Tile was found
     */
    public Tile getTile(int x, int y)
    {
        for (Tile t : tiles)
        {
            int tx = getColumnIndex(t);
            int ty = getRowIndex(t);
            if (tx == x && ty == y)
            {
                return t;
            }
        }
        return null;
    }

    /**
     * Get the Tile with the given value
     * @param val the value of the Tile to look for
     * @return the Tile with the specified value or null if no such Tile was found
     */
    public Tile getTile(int val)
    {
        for (Tile t : tiles)
        {
            if (t.value() == val) {
                return t;
            }
        }
        return null;
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

    /**
     * Get the empty Tile for this Board
     * @return the empty Tile for this board
     */
    public Tile getEmptyTile() {
        return this.emptyTile;
    }

    /**
     * checks if the -1 tile is in top left or top right
     * @return true if tile on top left or bottom right AND List is ordered, false otherwise
     */
    public Boolean check()
    {
        ArrayList<Integer> validNumTop = new ArrayList<>(Arrays.asList(-1,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
        ArrayList<Integer> validNumBottom = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,-1));
        for(int i = 0; i < 16; i++)
        {
            if(tiles.get(i).value() != validNumTop.get(i) && tiles.get(15).value() != -1)
            {
                return false;
            }
        }
        for(int i = 0; i < 16; i++)
        {
            if(tiles.get(i).value() != validNumBottom.get(i) && tiles.get(0).value() != -1)
            {
                return false;
            }
        }
        
        return true; 
    }
    /**
     * display that they won
     */
    public void displayWon()
    {
        if(solved)
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "You Won!");
        }
    }
    
    public static void main(String[] args)
    {
        App.main(null);
        Board yuh = new Board(); 
        yuh.solved = true; 
        yuh.displayWon();

    }

   
}
