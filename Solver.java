import java.util.ArrayList;
import javafx.scene.layout.GridPane;

public class Solver {
    private Board board;
    private Tile empty;

    // The max value that has been moved into place
    private int maxSortedValue;

    public Solver(Board board) {
        this.board = board;
        this.empty = board.getEmptyTile();
        this.maxSortedValue = 0;
    }

    // Rotate a 2x2 area where (sx, sy) is the upper left corner of the square
    // Rotates the 2x2 area clockwise until the Tile originally at
    // (startx, starty) is at (stopx, stopy)
    // startx, starty, stopx, and stopy are all relative coordinates to the upper left corner
    // so they will all be 0 or 1
    // Exampe:   s     i     j
    // rotate2x2(0, 0, 0, 1, 1, 1)
    // 1 2    1        1    3 1    3 1    3        3    2 3    2 3    2  
    // 3   -> 3 2 -> 3 2 ->   2 -> 2   -> 2 1 -> 2 1 ->   1 -> 1   -> 1 3
    private boolean rotate2x2(int sx, int sy, int startx, int starty, int stopx, int stopy) {
        // Validate the square's x and y
        // If invalid, return false
        // Because this is the upper left corner, anything beyond 2 is invalid
        // because there needs to be room for the other 3 squares
        if (sx > 2 || sy > 2 || sx < 0 || sy < 0) {
            return false;
        }

        // Get the 4 tiles in a clockwise order
        // a b 
        // c d becomes {a, b, d, c}
        Tile[] square = {
            board.getTile(sx  , sy  ), // Upper left
            board.getTile(sx+1, sy  ), // Upper right
            board.getTile(sx+1, sy+1), // Lower right
            board.getTile(sx  , sy+1)  // Lower left
        };
        
        // Find the empty Tile
        int emptyIndex = -1;
        for (int k = 0; k < square.length; k++) {
            if (empty.equals(square[k])) {
                emptyIndex = k;
                break;
            }
        }
        
        // If the empty Tile wasn't found, return a failure
        if (emptyIndex == -1) {
            return false;
        }

        // Validate startx, starty, stopx, and stopy and convert them to indices
        // Because the indices are clockwise, the mapping is:
        // (0, 0) -> 0, (1, 0) -> 1, (0, 1) -> 2, (1, 1) -> 3
        // If invalid, return false
        // The switch expression basically converts the startx
        // and starty into a binary number for easier mapping
        int start = switch (startx * 2 + starty) {
            case 0 -> 0;
            case 1 -> 3;
            case 2 -> 1;
            case 3 -> 2;
            default -> -1;
        };
        int stop = switch (stopx * 2 + stopy) {
            case 0 -> 0;
            case 1 -> 3;
            case 2 -> 1;
            case 3 -> 2;
            default -> -1;
        };
        if (start == -1 || stop == -1) {
            return false;
        }

        // Get the Tile we care about and the Tile we want start to end up at
        Tile startTile = square[start];
        Tile stopTile = square[stop];

        // Get the position of each of the Tiles
        int startXPos = GridPane.getColumnIndex(startTile);
        int startYPos = GridPane.getRowIndex(startTile);
        int stopXPos = GridPane.getColumnIndex(stopTile);
        int stopYPos = GridPane.getRowIndex(stopTile);

        // Swap Tiles clockwise until the start Tile
        // is at the old position of the stop Tile
        int i = mod(emptyIndex - 1, square.length);
        while (startXPos != stopXPos || startYPos != stopYPos) {
            // If the current Tile isn't empty, swap it with the empty Tile
            if (i != emptyIndex) {
                board.swapTile(square[i]);
            }

            // Decrement the index
            i = mod(i-1, square.length);

            // Update the position of the Tile we care about
            startXPos = GridPane.getColumnIndex(startTile);
            startYPos = GridPane.getRowIndex(startTile);
        }

        return true;
    }

    // Does the exact some thing as rotate2x2(int, int, int, int, int, int)
    @SuppressWarnings("unused")
    private boolean rotate2x2(Point s, Point start, Point end) {
        return rotate2x2(s.x(), s.y(), start.x(), start.y(), end.x(), end.y());
    }

    // Rotate a 3x2 area where (sx, sy) is the upper left corner of the square
    // Rotates the 3x2 area clockwise until the Tile originally at
    // (startx, starty) is at (stopx, stopy)
    // startx, starty, stopx, and stopy are all relative coordinates to the upper left corner
    // startx, stopx can be 0 or 1
    // starty, stopy can be 0, 1, or 2
    // Exampe:   s     i     j
    // rotate3x2(0, 0, 0, 0, 1, 2)
    // 1 2    1 2    1        1    3 1    3 1    3 1    3 1    3        3    5 3    5 3    5 3    5 3
    // 3 4 -> 3   -> 3 2 -> 3 2 ->   2 -> 5 2 -> 5 2 -> 5   -> 5 1 -> 5 1 ->   1 -> 4 1 -> 4 1 -> 4  
    // 5      5 4    5 4    5 4    5 4      4    4      4 2    4 2    4 2    4 2      2    2      2 1
    private boolean rotate3x2(int sx, int sy, int startx, int starty, int stopx, int stopy) {
        // Validate the rectangle's x and y
        // If invalid, return false
        // Because this is the upper left corner, anything beyond (2, 1) is invalid
        // because there needs to be room for the other 3 squares
        if (sx > 2 || sy > 1 || sx < 0 || sy < 0) {
            return false;
        }

        // Get the 4 tiles in a clockwise order
        // a b 
        // c d becomes {a, b, d, f, e, c}
        // e f 
        Tile[] rect = {
            board.getTile(sx  , sy  ), // Upper left
            board.getTile(sx+1, sy  ), // Upper right
            board.getTile(sx+1, sy+1), // Middle right
            board.getTile(sx+1, sy+2), // Lower right
            board.getTile(sx  , sy+2), // Lower left
            board.getTile(sx  , sy+1), // Middle left
        };
        
        // Find the empty Tile
        int emptyIndex = -1;
        for (int k = 0; k < rect.length; k++) {
            if (empty.equals(rect[k])) {
                emptyIndex = k;
                break;
            }
        }
        
        // If the empty Tile wasn't found, return a failure
        if (emptyIndex == -1) {
            return false;
        }

        // Validate startx, starty, stopx, and stopy and convert them to indices
        // Because rect is a cycle, the mappings are weird
        // (0, 0) -> 0, (0, 1) -> 5, (0, 2) -> 4, (1, 0) -> 1, (1, 1) -> 2, (1, 2) -> 3
        // If invalid, return false
        // The switch expression basically converts the startx
        // and starty into a ternary number for easier mapping
        int start = switch (startx * 3 + starty) {
            case 0 -> 0;
            case 1 -> 5;
            case 2 -> 4;
            case 3 -> 1;
            case 4 -> 2;
            case 5 -> 3;
            default -> -1;
        };
        int stop = switch (stopx * 3 + stopy) {
            case 0 -> 0;
            case 1 -> 5;
            case 2 -> 4;
            case 3 -> 1;
            case 4 -> 2;
            case 5 -> 3;
            default -> -1;
        };
        if (start == -1 || stop == -1) {
            return false;
        }

        // Get the Tile we care about and the Tile we want start to end up at
        Tile startTile = rect[start];
        Tile stopTile = rect[stop];

        // Get the position of each of the Tiles
        int startXPos = GridPane.getColumnIndex(startTile);
        int startYPos = GridPane.getRowIndex(startTile);
        int stopXPos = GridPane.getColumnIndex(stopTile);
        int stopYPos = GridPane.getRowIndex(stopTile);

        // Swap Tiles clockwise until the start Tile
        // is at the old position of the stop Tile
        int i = mod(emptyIndex - 1, rect.length);
        while (startXPos != stopXPos || startYPos != stopYPos) {
            // If the current Tile isn't empty, swap it with the empty Tile
            if (i != emptyIndex) {
                board.swapTile(rect[i]);
            }

            // Decrement the index
            i = mod(i-1, rect.length);

            // Update the position of the Tile we care about
            startXPos = GridPane.getColumnIndex(startTile);
            startYPos = GridPane.getRowIndex(startTile);
        }

        return true;
    }

    // Does the exact some thing as rotate3x2(int, int, int, int, int, int)
    @SuppressWarnings("unused")
    private boolean rotate3x2(Point s, Point start, Point end) {
        return rotate3x2(s.x(), s.y(), start.x(), start.y(), end.x(), end.y());
    }

    // Rotate a 2x4 area where (sx, sy) is the upper left corner of the square
    // Rotates the 2x4 area clockwise until the Tile originally at
    // (startx, starty) is at (stopx, stopy)
    // startx, starty, stopx, and stopy are all relative coordinates to the upper left corner
    // startx, stopx can be 0, 1, 2 or 3
    // starty, stopy can be 0 or 1
    // Exampe:   s     i     j
    // rotate2x4(0, 0, 0, 0, 3, 1)
    // 1 2 3 4    1 2 3      1 2   3    1   2 3      1 2 3    5 1 2 3    5 1 2 3    5 1 2 3    5 1 2 3    5 1 2 3    5 1 2   
    // 5 6 7   -> 5 6 7 4 -> 5 6 7 4 -> 5 6 7 4 -> 5 6 7 4 ->   6 7 4 -> 6   7 4 -> 6   7 4 -> 6 7   4 -> 6 7 4   -> 6 7 4 3 -> next line
    // 5 1   2    5   1 2      5 1 2    6 5 1 2    6 5 1 2    6 5 1 2    6 5 1 2    6 5 1      6 5   1    6   5 1      6 5 1
    // 6 7 4 3 -> 6 7 4 3 -> 6 7 4 3 ->   7 4 3 -> 7   4 3 -> 7 4   3 -> 7 4 3   -> 7 4 3 2 -> 7 4 3 2 -> 7 4 3 2 -> 7 4 3 2 -> next line
    // 7 6 5 1    7 6 5 1    7 6 5 1    7 6 5 1    7 6 5  
    //   4 3 2 -> 4   3 2 -> 4 3   2 -> 4 3 2   -> 4 3 2 1 
    private boolean rotate2x4(int sx, int sy, int startx, int starty, int stopx, int stopy) {
        // Validate the rectangle's x and y
        // If invalid, return false
        // Because this is the upper left corner, anything beyond (2, 1) is invalid
        // because there needs to be room for the other 3 squares
        if (sx != 0 || sy > 2 || sy < 0) {
            return false;
        }

        // Get the 4 tiles in a clockwise order
        // a b c d
        // e f g h becomes {a, b, c, d, h, g, f, e}
        Tile[] rect = {
            board.getTile(sx  , sy  ), // Upper far left
            board.getTile(sx+1, sy  ), // Upper left
            board.getTile(sx+2, sy  ), // Upper right
            board.getTile(sx+3, sy  ), // Upper far right
            board.getTile(sx+3, sy+1), // Lower far right
            board.getTile(sx+2, sy+1), // Lower right
            board.getTile(sx+1, sy+1), // Lower left
            board.getTile(sx  , sy+1), // Lower far left
        };
        
        // Find the empty Tile
        int emptyIndex = -1;
        for (int k = 0; k < rect.length; k++) {
            if (empty.equals(rect[k])) {
                emptyIndex = k;
                break;
            }
        }
        
        // If the empty Tile wasn't found, return a failure
        if (emptyIndex == -1) {
            return false;
        }

        // Validate startx, starty, stopx, and stopy and convert them to indices
        // Because rect is a cycle, the mappings are weird
        // (0, 0) -> 0, (0, 1) -> 7, (1, 0) -> 1, (1, 1) -> 6, (2, 0) -> 2, (2, 1) -> 5,
        // (3, 0) -> 3, (3, 1) -> 4
        // If invalid, return false
        // The switch expression basically converts the startx
        // and starty into a base 4 number for easier mapping
        int start = switch (startx * 4 + starty) {
            case 0  -> 0;
            case 1  -> 7;
            case 4  -> 1;
            case 5  -> 6;
            case 8  -> 2;
            case 9  -> 5;
            case 12 -> 3;
            case 13 -> 4;
            default -> -1;
        };
        int stop = switch (stopx * 4 + stopy) {
            case 0  -> 0;
            case 1  -> 7;
            case 4  -> 1;
            case 5  -> 6;
            case 8  -> 2;
            case 9  -> 5;
            case 12 -> 3;
            case 13 -> 4;
            default -> -1;
        };
        if (start == -1 || stop == -1) {
            return false;
        }

        // Get the Tile we care about and the Tile we want start to end up at
        Tile startTile = rect[start];
        Tile stopTile = rect[stop];

        // Get the position of each of the Tiles
        int startXPos = GridPane.getColumnIndex(startTile);
        int startYPos = GridPane.getRowIndex(startTile);
        int stopXPos = GridPane.getColumnIndex(stopTile);
        int stopYPos = GridPane.getRowIndex(stopTile);

        // Swap Tiles clockwise until the start Tile
        // is at the old position of the stop Tile
        int i = mod(emptyIndex - 1, rect.length);
        while (startXPos != stopXPos || startYPos != stopYPos) {
            // If the current Tile isn't empty, swap it with the empty Tile
            if (i != emptyIndex) {
                board.swapTile(rect[i]);
            }

            // Decrement the index
            i = mod(i-1, rect.length);

            // Update the position of the Tile we care about
            startXPos = GridPane.getColumnIndex(startTile);
            startYPos = GridPane.getRowIndex(startTile);
        }

        return true;
    }

    // Does the exact some thing as rotate2x4(int, int, int, int, int, int)
    @SuppressWarnings("unused")
    private boolean rotate2x4(Point s, Point start, Point end) {
        return rotate2x4(s.x(), s.y(), start.x(), start.y(), end.x(), end.y());
    }

    // Rotate a 2x3 area where (sx, sy) is the upper left corner of the square
    // Rotates the 2x3 area clockwise until the Tile originally at
    // (startx, starty) is at (stopx, stopy)
    // startx, starty, stopx, and stopy are all relative coordinates to the upper left corner
    // startx, stopx can be 0, 1, or 2
    // starty, stopy can be 0 or 1
    // Exampe:   s     i     j
    // rotate2x3(0, 0, 0, 0, 2, 1)
    // 1 2 3    1 2      1   2      1 2    4 1 2    4 1 2    4 1 2    4 1      4   1      4 1 
    // 4 5   -> 4 5 3 -> 4 5 3 -> 4 5 3 ->   5 3 -> 5   3 -> 5 3   -> 5 3 2 -> 5 3 2 -> 5 3 2 -> next line
    // 5 4 1    5 4 1    5 4 1    5 4  
    //   3 2 -> 3   2 -> 3 2   -> 3 2 1
    private boolean rotate2x3(int sx, int sy, int startx, int starty, int stopx, int stopy) {
        // Validate the rectangle's x and y
        // If invalid, return false
        // Because this is the upper left corner, anything beyond (2, 1) is invalid
        // because there needs to be room for the other 3 squares
        if (sx > 1 || sy > 2 || sx < 0 || sy < 0) {
            return false;
        }

        // Get the 4 tiles in a clockwise order
        // a b c d
        // e f g h becomes {a, b, c, d, h, g, f, e}
        Tile[] rect = {
            board.getTile(sx  , sy  ), // Upper left
            board.getTile(sx+1, sy  ), // Upper middle
            board.getTile(sx+2, sy  ), // Upper right
            board.getTile(sx+2, sy+1), // Lower right
            board.getTile(sx+1, sy+1), // Lower middle
            board.getTile(sx  , sy+1), // Lower left
        };
        
        // Find the empty Tile
        int emptyIndex = -1;
        for (int k = 0; k < rect.length; k++) {
            if (empty.equals(rect[k])) {
                emptyIndex = k;
                break;
            }
        }
        
        // If the empty Tile wasn't found, return a failure
        if (emptyIndex == -1) {
            return false;
        }

        // Validate startx, starty, stopx, and stopy and convert them to indices
        // Because rect is a cycle, the mappings are weird
        // (0, 0) -> 0, (0, 1) -> 5, (1, 0) -> 1, (1, 1) -> 4, (2, 0) -> 2, (2, 1) -> 3
        // If invalid, return false
        // The switch expression basically converts the startx
        // and starty into a ternary number for easier mapping
        int start = switch (startx * 3 + starty) {
            case 0  -> 0;
            case 1  -> 5;
            case 3  -> 1;
            case 4  -> 4;
            case 6  -> 2;
            case 7  -> 3;
            default -> -1;
        };
        int stop = switch (stopx * 3 + stopy) {
            case 0  -> 0;
            case 1  -> 5;
            case 3  -> 1;
            case 4  -> 4;
            case 6  -> 2;
            case 7  -> 3;
            default -> -1;
        };
        if (start == -1 || stop == -1) {
            return false;
        }

        // Get the Tile we care about and the Tile we want start to end up at
        Tile startTile = rect[start];
        Tile stopTile = rect[stop];

        // Get the position of each of the Tiles
        int startXPos = GridPane.getColumnIndex(startTile);
        int startYPos = GridPane.getRowIndex(startTile);
        int stopXPos = GridPane.getColumnIndex(stopTile);
        int stopYPos = GridPane.getRowIndex(stopTile);

        // Swap Tiles clockwise until the start Tile
        // is at the old position of the stop Tile
        int i = mod(emptyIndex - 1, rect.length);
        while (startXPos != stopXPos || startYPos != stopYPos) {
            // If the current Tile isn't empty, swap it with the empty Tile
            if (i != emptyIndex) {
                board.swapTile(rect[i]);
            }

            // Decrement the index
            i = mod(i-1, rect.length);

            // Update the position of the Tile we care about
            startXPos = GridPane.getColumnIndex(startTile);
            startYPos = GridPane.getRowIndex(startTile);
        }

        return true;
    }

    // Does the exact some thing as rotate2x3(int, int, int, int, int, int)
    @SuppressWarnings("unused")
    private boolean rotate2x3(Point s, Point start, Point end) {
        return rotate2x3(s.x(), s.y(), start.x(), start.y(), end.x(), end.y());
    }

    // Find the coordinates of the upper left corner of the
    // 2x2 square with the given Tile and no solved tiles
    // Returns null if no such square exists
    @SuppressWarnings("unused")
    private Point find2x2Pos(Tile t) {
        // Get the absolute x and y position of the Tile
        int absx = GridPane.getColumnIndex(t);
        int absy = GridPane.getRowIndex(t);

        // Find the upper left corner of the 2x2 with the Tile
        // that is also closest to (0, 0) as possible
        //int sqrx = Math.max(absx - 1, 0);
        //int sqry = Math.max(absy - 1, 0);

        // Search all 4 surrounding 2x2s to check if one is valid
        // The order to check them in is:
        // (x-1, y-1)
        // (x  , y-1)
        // (x-1, y  )
        // (x  , y  )
        for (int dy = 1; dy >= 0; dy--) {
            for (int dx = 1; dx >= 0; dx--) {
                // Get the current square
                Tile[] square = {
                    board.getTile(absx - dx    , absy - dy    ), // Upper left
                    board.getTile(absx - dx + 1, absy - dy    ), // Upper right
                    board.getTile(absx - dx + 1, absy - dy + 1), // Lower right
                    board.getTile(absx - dx    , absy - dy + 1)  // Lower left
                };

                // Make sure no solved Tiles are in the square
                boolean isValid = true;
                for (Tile s : square) {
                    // Make sure the Tile isn't null
                    if (s == null) {
                        isValid = false;
                        break;
                    }

                    // A Tile can't be in a 2x2 if it is already sorted
                    // An exception needs to be made for the empty Tile
                    // because it has a value of -1
                    if (s.value() <= maxSortedValue && s.value() != -1) {
                        isValid = false;
                        break;
                    }
                }

                // If the square is valid, return it
                if (isValid) {
                    return new Point(absx - dx, absy - dy);
                }
            }
        }

        return null;
    }

    // Move the empty Tile to the given position if possible
    // A position is impossible to reach if it will move already
    // sorted Tiles or the Tile currently being sorted,
    // which is 1 more than the current maxSortedValue
    // If a move occurs, the function will return true
    // If no moves occur, the function will return false
    @SuppressWarnings("unused")
    private boolean moveEmptyToPosition(int x, int y) {
        return moveEmptyToPosition(new Point(x, y));
    }

    // Does the exact some thing as moveEmptyToPosition(int, int)
    private boolean moveEmptyToPosition(Point pos) {
        // Validate pos
        if (pos.x() < 0 || pos.x() > 3 || pos.y() < 0 || pos.y() > 3) {
            return false;
        }

        Tile target = board.getTile(pos.x(), pos.y());
        if (target.value() <= maxSortedValue + 1 && target.value() != -1) {
            return false;
        }

        // Get the empty Tile position
        Point emptyPos = new Point(GridPane.getColumnIndex(empty), GridPane.getRowIndex(empty));

        // Do some pathfinding
        // This code is absolutely terrible
        // Looking at it for more than a few minutes at a time can result in permanent blindness
        ArrayList<Direction> directions = new ArrayList<>();
        try {
            tryAllDirections(emptyPos, pos, directions, new ArrayList<Point>());
        }
        catch (PathNotFoundExcpetion ex) {
            System.out.println("Something is wrong.");
        }
        
        // Convert the directions into Tiles to board.swapRelativeToEmpty calls
        // Make sure to iterate over directions backwards
        for (int i = directions.size() - 1; i >= 0; i--) {
            // Get the direction
            Direction d = directions.get(i);

            // Swap the Tile in that direction
            board.swapTileRelativeToEmpty(d.getX(), d.getY());
        }

        return true;
    }

    // Does the same thing as tryAllDirections with Points
    @SuppressWarnings("unused")
    private void tryAllDirections(int curx, int cury, int goalx, int goaly,
                                  ArrayList<Direction> directions, ArrayList<Point> used) throws PathNotFoundExcpetion {
        tryAllDirections(new Point(curx, cury), new Point(goalx, goaly), directions, used);
    }

    // Helper method for moveEmptyToPosition
    // Recursively find valid moves to the target destination
    // The end list of directions will be in the directions ArrayList
    // The list of directions will be in reverse order
    // Used should be an empty ArrayList on the first call
    // It will contain all check points
    private void tryAllDirections(Point cur, Point goal, 
                                  ArrayList<Direction> directions, ArrayList<Point> used) throws PathNotFoundExcpetion{
        // If the current point is the goal, start exiting recursion
        if (cur.equals(goal)) {
            directions = new ArrayList<>();
            return;
        }

        // If the current point isn't the goal, add it to the used points list
        used.add(cur);

        // Check each point surrounding the current point
        for (Direction dir : Direction.values()) {
            // Calculate the new point to check
            Point newPoint = cur.offset(dir);

            // If it is already used, just skip it
            if (used.contains(newPoint)) {
                continue;
            }

            // Get the new Tile
            // If it is null/out of bounds, just skip it
            Tile t = board.getTile(newPoint.x(), newPoint.y());
            if (t == null) {
                continue;
            }

            // If the point is immovable, skip it
            if (t.value() <= maxSortedValue + 1) {
                continue;
            }

            // Call tryAllDirections again with the new point
            try {
                tryAllDirections(newPoint, goal, directions, used);
            }
            // If an excpetion is thrown, this path is a dead end
            catch (PathNotFoundExcpetion ex) {
                continue;
            }

            // If it isn't, start building the direction list
            directions.add(dir);
            return;
        }

        // If this point is reached, this point is a dead end
        // Throw an error if a dead-end is found
        throw new PathNotFoundExcpetion();
    }

    // Custom mod function to force mod to be positive
    // https://stackoverflow.com/a/4412200/13996174
    private int mod(int n, int m) {
        return (n % m + m) % m;
    }

    public static void solve(Board board) {
        Solver s = new Solver(board);
        //s.rotate2x2(2, 2, 0, 0, 1, 1);
        //s.rotate3x2(2, 1, 0, 0, 1, 2);
        //s.rotate2x4(0, 2, 0, 0, 3, 1);
        //s.rotate2x3(1, 2, 0, 0, 2, 1);
        /* for (Tile t : s.tiles) {
            Point p = s.find2x2Pos(t);
            if (p != null) {
                System.out.printf("%d: (%d, %d)\n", t.value(), p.x(), p.y());
            }
        } */
        //s.moveEmptyToPosition(3, 1);
        /* for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("%d, %d\n", i, j);
                s.moveEmptyToPosition(i, j);
            }
        } */
    }

    public static void main(String[] args) {
        App.main(null);
    }
}

// Makes transferring coordinates easier
record Point(int x, int y) {
    // Get a point with the given offset
    public Point offset(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    public Point offset(Direction d) {
        return new Point(x + d.getX(), y + d.getY());
    }

    public boolean equals(Point p) {
        return p.x() == x && p.y() == y;
    }
}

// Handle directions for the solver
enum Direction {
    // Since the y-axis is flipped, visual up actually has a negative y component
    // This is reflect in the enum values
    UP (0, -1),
    DOWN (0, 1),
    LEFT (-1, 0),
    RIGHT (1, 0);

    private final Point direction;

    private Direction(Point pos) {
        this.direction = pos;
    }

    private Direction(int x, int y) {
        this.direction = new Point(x, y);
    }

    public Point getDirection() {
        return direction;
    }

    public int getX() {
        return direction.x();
    }

    public int getY() {
        return direction.y();
    }
}

// Excpetion for when a dead end is found
class PathNotFoundExcpetion extends Exception {}