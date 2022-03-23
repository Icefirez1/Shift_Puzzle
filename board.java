import java.util.ArrayList;

public class Board 
{
    private ArrayList<Tile> tiles; 
    public Board()
    {
        this.tiles = new ArrayList<>(); 

    }
    public void add(Tile obj)
    {
        tiles.add(obj);
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
        Tile obj = new Tile(4);
        test.add(obj);
        System.out.println(test);
    }
}
