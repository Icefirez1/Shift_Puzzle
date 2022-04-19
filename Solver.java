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

    // Calculate the taxicab distance between 2 points
    private int taxicabDistance(Point a, Point b) {
        return taxicabDistance(a.x(), a.y(), b.x(), b.y());
    }

    // Same thing as taxicabDistance(Point, Point)
    private int taxicabDistance(int ax, int ay, int bx, int by) {
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    // Custom mod function to force mod to be positive
    // https://stackoverflow.com/a/4412200/13996174
    private int mod(int n, int m) {
        return (n % m + m) % m;
    }

    // Move a Tile from the upper left square of the board into position
    // Valid tiles are 1, 2, 3, 5, 6, 7, 9
    // 10 and 11 are covered in a special case
    // Returns true if the Tile is at its final destination
    //   and false otherwise
    private boolean moveUpperLeftTowardsDestination(int num) {
        // Check if num is a valid num
        int validNums[] = {1, 2, 3, 5, 6, 7, 9};
        boolean isValid = false;
        for (int i : validNums) {
            if (num == i) {
                isValid = true;
                break;
            }
        }

        // If isValid is still false, it is invalid
        // Raise an exception
        if (!isValid) {
            throw new IllegalArgumentException("Tile value is out of bounds.");
        }

        // Get the Tile and its current position
        Tile cur = board.getTile(num);
        Point curPos = new Point(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));

        // Find the 2x2 with cur in it
        Point square = find2x2Pos(cur);

        // Get the goal position for the Tile
        Point finalGoal = switch (num) {
            case 1 -> new Point(0, 0);
            case 2 -> new Point(1, 0);
            case 3 -> new Point(2, 0);
            case 5 -> new Point(0, 1);
            case 6 -> new Point(1, 1);
            case 7 -> new Point(2, 1);
            case 9 -> new Point(0, 2);
            default -> new Point(-1, -1);
        };

        // If the Tile is already at the final position,
        //   exit without doing anything
        if (finalGoal.equals(curPos)) {
            return true;
        }

        // Find the best point for the 2x2
        // Start at the upper left corner of the square
        Point bestPoint = square;
        int bestDist = taxicabDistance(bestPoint, finalGoal);

        // Loop over all possible points in the square
        //   and check their distance
        for (int dy = 0; dy <= 1; dy++) {
            for (int dx = 0; dx <= 1; dx++) {
                // Calculate the taxicab distance for the point
                Point p = square.offset(dx, dy);
                int dist = taxicabDistance(p, finalGoal);

                // If it is better, update the best point
                if (dist < bestDist) {
                    bestPoint = p;
                    bestDist = dist;
                }
            }
        }

        // If the Tile is already at the best spot and it is
        //   not at its final spot, move the square right 1
        if (bestPoint.equals(curPos) && bestDist != 0) {
            // Move the square
            square = square.offset(1, 0);

            // Recalculate the bestPoint and bestDist
            bestPoint = square;
            bestDist = taxicabDistance(bestPoint, finalGoal);

            for (int dy = 0; dy <= 1; dy++) {
                for (int dx = 0; dx <= 1; dx++) {
                    // Calculate the taxicab distance for the point
                    Point p = square.offset(dx, dy);
                    int dist = taxicabDistance(p, finalGoal);
    
                    // If it is better, update the best point
                    if (dist < bestDist) {
                        bestPoint = p;
                        bestDist = dist;
                    }
                }
            }
        }

        // Move the empty Tile to the 2x2
        // If the current Tile is in the top left corner
        //   move the empty Tile to the top right corner.
        // Otherwise move it to the top left corner
        if (curPos.equals(square)) {
            moveEmptyToPosition(square.offset(1, 0));
        }
        else {
            moveEmptyToPosition(square);
        }

        // Move 1 to the best square in the 2x2
        rotate2x2(square, curPos.subtract(square), bestPoint.subtract(square));

        // Return if the Tile is finally in the right place
        return bestPoint.equals(finalGoal);
    }

    // Move the 4 or 8 Tile towards its proper position
    // Returns true if the Tile is at its final destination
    //   and false otherwise
    private boolean move48TowardsDestination(int num) {
        // Check if num is a valid num
        int validNums[] = {4, 8};
        boolean isValid = false;
        for (int i : validNums) {
            if (num == i) {
                isValid = true;
                break;
            }
        }

        // If isValid is still false, it is invalid
        // Raise an exception
        if (!isValid) {
            throw new IllegalArgumentException("Tile value must be either 4 or 8.");
        }

        // Get the Tile and its current position
        Tile cur = board.getTile(num);
        Point curPos = new Point(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));

        // Get the final goal position
        Point finalGoal = switch (num) {
            case 4 -> new Point(3, 0);
            case 8 -> new Point(3, 1);
            default -> new Point(-1, -1);
        };

        // If the Tile is already at its final position,
        //   return without doing anything
        if (finalGoal.equals(curPos)) {
            return true;
        }

        // Find the 2x2 with cur in it
        Point square = find2x2Pos(cur);

        // Get the stage 1 goal position of the Tile
        // The state 1 goal is 2 spots below the statge 2/final goal position
        Point stage1Goal = switch (num) {
            case 4 -> new Point(3, 2);
            case 8 -> new Point(3, 3);
            default -> new Point(-1, -1);
        };

        // If the Tile is already at its stage 1 position,
        //   move it into its final position
        if (stage1Goal.equals(curPos)) {
            // Move the empty Tile to the final goal position of the Tile
            moveEmptyToPosition(curPos.offset(0, -2));

            // Calcualte the corner of the 2x2 (and later the 3x2)
            Point corner = curPos.offset(-1, -2);

            // Rotate the 2x2 with the empty Tile
            //   until the Tile to the right of it ((0, 0) locally)
            //   is at the opposite corner ((1, 1) locally)
            rotate2x2(corner, new Point(0, 0), new Point(1, 1));
            
            // Find the final goal position of the n-1 Tile
            Point finalPrevGoal = switch(num) {
                case 4 -> new Point(2, 0);
                case 8 -> new Point(2, 1);
                default -> new Point(-1, -1);
            };

            // Move the 3x2 until the n-1 Tile is in its final position
            rotate3x2(corner, curPos.offset(0, -1).subtract(corner), finalPrevGoal.subtract(corner));

            // Return a guaranteed success
            return true;
        }
        // Otherwise move it towards that position
        else {
            // Find the best point for the 2x2
            // Start at the upper left corner of the square
            Point bestPoint = square;
            int bestDist = taxicabDistance(bestPoint, stage1Goal);

            // Loop over all possible points in the square
            //   and check their distance
            for (int dy = 0; dy <= 1; dy++) {
                for (int dx = 0; dx <= 1; dx++) {
                    // Calculate the taxicab distance for the point
                    Point p = square.offset(dx, dy);
                    int dist = taxicabDistance(p, stage1Goal);

                    // If it is better, update the best point
                    if (dist < bestDist) {
                        bestPoint = p;
                        bestDist = dist;
                    }
                }
            }

            // If the Tile is already at the best spot and it is
            //   not at its final spot, move the square right 1
            if (bestPoint.equals(curPos) && bestDist != 0) {
                // Move the square
                square = square.offset(1, 0);

                // Recalculate the bestPoint and bestDist
                bestPoint = square;
                bestDist = taxicabDistance(bestPoint, stage1Goal);

                for (int dy = 0; dy <= 1; dy++) {
                    for (int dx = 0; dx <= 1; dx++) {
                        // Calculate the taxicab distance for the point
                        Point p = square.offset(dx, dy);
                        int dist = taxicabDistance(p, stage1Goal);
        
                        // If it is better, update the best point
                        if (dist < bestDist) {
                            bestPoint = p;
                            bestDist = dist;
                        }
                    }
                }
            }

            // Move the empty Tile to the 2x2
            // If the current Tile is in the top left corner
            //   move the empty Tile to the top right corner.
            // Otherwise move it to the top left corner
            if (curPos.equals(square)) {
                moveEmptyToPosition(square.offset(1, 0));
            }
            else {
                moveEmptyToPosition(square);
            }

            // Move 1 to the best square in the 2x2
            rotate2x2(square, curPos.subtract(square), bestPoint.subtract(square));

            return bestPoint.equals(finalGoal);
        }
    }

    // Move the 10 Tile towards its proper position
    // Returns true if the Tile is at its final position
    //   and false otherwise
    private boolean move10TowardsDestination() {
        // Get the Tile and its current position
        Tile cur = board.getTile(10);
        Point curPos = new Point(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));

        // Find the 2x2 with cur in it
        Point square = find2x2Pos(cur);

        // Get the goal position for the Tile
        Point finalGoal = new Point(1, 2);

        // If the Tile is already at the final position,
        //   exit without doing anything
        if (finalGoal.equals(curPos)) {
            return true;
        }

        // If 10 is in a non-trapped spot move normally
        if (!(curPos.equals(0, 3) || curPos.equals(1, 3))) {
            // Find the best point for the 2x2
            // Start at the upper left corner of the square
            Point bestPoint = square;
            int bestDist = taxicabDistance(bestPoint, finalGoal);

            // Loop over all possible points in the square
            //   and check their distance
            for (int dy = 0; dy <= 1; dy++) {
                for (int dx = 0; dx <= 1; dx++) {
                    // Calculate the taxicab distance for the point
                    Point p = square.offset(dx, dy);
                    int dist = taxicabDistance(p, finalGoal);

                    // If it is better, update the best point
                    if (dist < bestDist) {
                        bestPoint = p;
                        bestDist = dist;
                    }
                }
            }

            // If the Tile is already at the best spot and it is
            //   not at its final spot, move the square right 1
            if (bestPoint.equals(curPos) && bestDist != 0) {
                // Move the square
                square = square.offset(1, 0);

                // Recalculate the bestPoint and bestDist
                bestPoint = square;
                bestDist = taxicabDistance(bestPoint, finalGoal);

                for (int dy = 0; dy <= 1; dy++) {
                    for (int dx = 0; dx <= 1; dx++) {
                        // Calculate the taxicab distance for the point
                        Point p = square.offset(dx, dy);
                        int dist = taxicabDistance(p, finalGoal);
        
                        // If it is better, update the best point
                        if (dist < bestDist) {
                            bestPoint = p;
                            bestDist = dist;
                        }
                    }
                }
            }

            // Move the empty Tile to the 2x2
            // If the current Tile is in the top left corner
            //   move the empty Tile to the top right corner.
            // Otherwise move it to the top left corner
            if (curPos.equals(square)) {
                moveEmptyToPosition(square.offset(1, 0));
            }
            else {
                moveEmptyToPosition(square);
            }

            // Move 1 to the best square in the 2x2
            rotate2x2(square, curPos.subtract(square), bestPoint.subtract(square));

            return bestPoint.equals(finalGoal);
        }
        // If no path exists, do a special thing
        else {
            // Rotate the 2x3 with 9 until 9 is at the bottom center of the 2x3
            // If this branch is reached it is guaranteed that the empty Tile will
            // be in the 2x3
            rotate2x3(0, 2, 0, 0, 1, 1);

            // Move the empty Tile to just above the 9 Tile
            // This is really janky, but the maxSortedValue is
            //   temporarily decremented to allow the empty Tile
            //   to move freely
            maxSortedValue--;
            moveEmptyToPosition(1, 2);
            maxSortedValue++;

            // Spin 9 into place
            rotate2x2(0, 2, 1, 1, 0, 0);

            // Move the empty Tile to the 2x2 with 10
            moveEmptyToPosition(1, 3);

            // Spin 10 into place
            rotate2x2(1, 2, GridPane.getColumnIndex(cur)-1, GridPane.getRowIndex(cur)-2, 0, 0);
        }

        return false;
    }

    // Move the 11 Tile towards its proper position
    private boolean move11TowardsDestination() {
        // Get the Tile and its current position
        Tile cur = board.getTile(11);
        Point curPos = new Point(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));

        // Find the 2x2 with cur in it
        Point square = find2x2Pos(cur);

        // Get the goal position for the Tile
        Point finalGoal = new Point(2, 2);

        // If the Tile is already at the final position,
        //   exit without doing anything
        if (finalGoal.equals(curPos)) {
            return true;
        }

        // Check if 11 can be solved with a 2x3
        // emptyPos handles a rare edge case where the emtpy Tile is directly
        //   above the 11 Tile
        // The edge case technically isn't required to be accounted for, but
        //   I wanted to account for it
        Point emptyPos = new Point(GridPane.getColumnIndex(empty), GridPane.getRowIndex(empty));
        if (curPos.equals(2, 3) && !emptyPos.equals(2, 2)) {
            // Move the empty Tile into the 2x3 with the 11
            //   if necessary
            moveEmptyToPosition(1, 3);

            // Rotate the 2x3 with 10 until 10 is at the bottom center of the 2x3
            // If this branch is reached it is guaranteed that the empty Tile will
            //   be in the 2x3
            rotate2x3(1, 2, 0, 0, 1, 1);

            // Move the empty Tile to just above the 9 Tile
            // This is really janky, but the maxSortedValue is
            //   temporarily decremented to allow the empty Tile
            //   to move freely
            maxSortedValue--;
            moveEmptyToPosition(2, 2);
            maxSortedValue++;

            // Spin 10 into place
            rotate2x2(1, 2, 1, 1, 0, 0);

            // Move the empty Tile to the 2x2 with 11
            moveEmptyToPosition(2, 3);

            // Spin the 11 into place
            //rotate2x2(2, 2, GridPane.getColumnIndex(cur)-2, GridPane.getRowIndex(cur)-2, 0, 0);

            return finalGoal.equals(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));
        }
        // Check if 11 can be solved by a 2x4
        else if (curPos.equals(0, 3) || curPos.equals(1, 3)) {
            // Rotate the bottom 2x4 until 9 and 10 are
            //   in a different 2x2 from 11
            rotate2x4(0, 2, 0, 0, 2, 0);

            // Rotate the 2x2 with 11 until it is no longer
            //   lined up with 9 and 10
            rotate2x2(0, 2, 0, 0, 1, 1);

            // Rotate the bottom 2x4 until 9 is back in its place
            rotate2x4(0, 2, 2, 0, 0, 0);

            // This will never fully solve the puzzle
            return false;
        }
        // Otherwise solve it normally
        else {
            // Find the best point for the 2x2
            // Start at the upper left corner of the square
            Point bestPoint = square;
            int bestDist = taxicabDistance(bestPoint, finalGoal);

            // Loop over all possible points in the square
            //   and check their distance
            for (int dy = 0; dy <= 1; dy++) {
                for (int dx = 0; dx <= 1; dx++) {
                    // Calculate the taxicab distance for the point
                    Point p = square.offset(dx, dy);
                    int dist = taxicabDistance(p, finalGoal);

                    // If it is better, update the best point
                    if (dist < bestDist) {
                        bestPoint = p;
                        bestDist = dist;
                    }
                }
            }

            // If the Tile is already at the best spot and it is
            //   not at its final spot, move the square right 1
            if (bestPoint.equals(curPos) && bestDist != 0) {
                // Move the square
                square = square.offset(1, 0);

                // Recalculate the bestPoint and bestDist
                bestPoint = square;
                bestDist = taxicabDistance(bestPoint, finalGoal);

                for (int dy = 0; dy <= 1; dy++) {
                    for (int dx = 0; dx <= 1; dx++) {
                        // Calculate the taxicab distance for the point
                        Point p = square.offset(dx, dy);
                        int dist = taxicabDistance(p, finalGoal);
        
                        // If it is better, update the best point
                        if (dist < bestDist) {
                            bestPoint = p;
                            bestDist = dist;
                        }
                    }
                }
            }

            // Move the empty Tile to the 2x2
            // If the current Tile is in the top left corner
            //   move the empty Tile to the top right corner.
            // Otherwise move it to the top left corner
            if (curPos.equals(square)) {
                moveEmptyToPosition(square.offset(1, 0));
            }
            else {
                moveEmptyToPosition(square);
            }

            // Move 1 to the best square in the 2x2
            rotate2x2(square, curPos.subtract(square), bestPoint.subtract(square));

            return bestPoint.equals(finalGoal);
        }
    }

    // Move the 12 Tile towards its proper position
    private boolean move12TowardsDestination() {
        // Get the Tile and its current position
        Tile cur = board.getTile(12);
        Point curPos = new Point(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));

        // Get the goal position for the Tile
        Point finalGoal = new Point(3, 2);

        // If the Tile is already at the final position,
        //   exit without doing anything
        if (finalGoal.equals(curPos)) {
            return true;
        }

        // If 12 is directly below 9, spin stuff so that
        //   it isn't anymore
        if (curPos.equals(0, 3)) {
            // Spin the bottom 2x4 until 9,10,11 are not in the
            //   same 2x2 as 12
            rotate2x4(0, 2, 0, 0, 2, 0);

            // Rotate the 2x2 with 12 so that it is not next to
            //   9 in the cycle
            rotate2x2(0, 2, 0, 0, 1, 1);

            // Rotate the 2x4 until 9,10,11 are back in place
            rotate2x4(0, 2, 2, 0, 0, 0);
        }
        // Otherwise just spin 12 into place
        else {
            // Rotate the bottom 2x4 until 9,10,11 are all
            //   in a different 2x2 from 12 and 11 is at the
            //   edge of the 2x2
            rotate2x4(0, 2, 0, 0, 3, 0);

            // Move the empty Tile into the 2x2 with the 12
            moveEmptyToPosition(1, 2);

            // Rotate the 2x2 with 12 until it lines up with 11
            Point tilePos = new Point(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));
            rotate2x2(new Point(0, 2), tilePos.subtract(0, 2), new Point(1, 1));

            // Rotate the 2x4 until 9 is back in its place
            rotate2x4(0, 2, 3, 0, 0, 0);
        }

        return false;
    }

    // Move 13, 14, 15 towards their proper positions
    private boolean moveFinal3TowardsDestination() {
        // Get the 15 Tile
        Tile cur = board.getTile(15);

        // Rotate the bottom half until 9-12 are in
        //   a single 2x2
        rotate2x4(0, 2, 0, 0, 2, 0);

        // Spin the 2x2 with 13-15 until 15 touches
        //   the 12
        Point tilePos = new Point(GridPane.getColumnIndex(cur), GridPane.getRowIndex(cur));
        rotate2x2(new Point(0, 2), tilePos.subtract(0, 2), new Point(1, 1));

        // Rotate the 2x4 until 15 is in its spot
        rotate2x4(0, 2, 1, 1, 2, 1);

        return true;
    }

    public static void solve(Board board) {
        Solver s = new Solver(board);

        // Solve 1-3
        for (int i = 1; i <= 3; i++) {
            while (!s.moveUpperLeftTowardsDestination(i));
            s.maxSortedValue++;
        }

        // Solve 4
        while (!s.move48TowardsDestination(4));
        s.maxSortedValue++;

        // Solve 5-7
        for (int i = 5; i <= 7; i++) {
            while (!s.moveUpperLeftTowardsDestination(i));
            s.maxSortedValue++;
        }

        // Solve 8
        while (!s.move48TowardsDestination(8));
        s.maxSortedValue++;

        // Solve 9
        while (!s.moveUpperLeftTowardsDestination(9));
        s.maxSortedValue++;

        // Solve 10
        while (!s.move10TowardsDestination());
        s.maxSortedValue++;

        // Solve 11
        while (!s.move11TowardsDestination());
        s.maxSortedValue++;
        
        // Solve 12
        while (!s.move12TowardsDestination());
        s.maxSortedValue++;
        
        // Solve 13,14,15
        s.moveFinal3TowardsDestination();

        // Hacky way to get the win screen to show up
        // Just click a button that can't be moved
        board.getTile(1).fire();
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

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    // Subtract one point from another
    public Point subtract(Point p) {
        return new Point(x() - p.x(), y() - p.y());
    }

    public Point subtract(int px, int py) {
        return new Point(x() - px, y() - py);
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