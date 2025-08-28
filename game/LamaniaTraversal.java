package game;
import java.util.*;

/*
 Lamania - Text Dungeon Game
 Traversal prototype (single file) - Map fixed to exact layout provided

 Notes:
 - Map rows are parsed from whitespace-separated tokens so multi-char tokens like "|-" are preserved.
 - Player '^' token is detected and removed from the map, setting the player's start position.
 - symbolDescription() uses classic switch-case returns (Java 11 compatible).
*/

public class LamaniaTraversal {
    public static void main(String[] args) {
        new Game().run();
    }
}

/* -------------------------
   Game loop & command UI
   ------------------------- */
class Game {
    private final Scanner in = new Scanner(System.in);
    private Level level;
    private Player player;
    private boolean showLegend = false;

    Game() {
        this.level = Level.sampleLevel1(); // load the prototype level 1 (exact layout)
        this.player = new Player("Nameless Merc", level.getPlayerStartX(), level.getPlayerStartY(), Direction.NORTH);
    }

    public void run() {
        System.out.println("Welcome to Lamania — Text Dungeon (Traversal Prototype)");
        System.out.println("Story: You are a nameless merc seeking fortune in the rumored Demon's Treasury...\n");

        boolean running = true;
        while (running) {
            System.out.println("\nWhat you will do?");
            System.out.println("1. check surrounding");
            System.out.println("2. check map");
            System.out.println("3. move");
            System.out.println("4. interact / open (context-sensitive)");
            System.out.println("0. quit");
            System.out.print("> ");
            String sel = in.nextLine().trim();
            switch (sel) {
                case "1" -> checkSurrounding();
                case "2" -> showMapMenu();
                case "3" -> moveFlow();
                case "4" -> interactFlow();
                case "0" -> { running = false; System.out.println("Bye."); }
                default -> System.out.println("Unknown option. Choose 0-4.");
            }
        }
    }

    private void checkSurrounding() {
        System.out.println();
        String[] results = level.describeAround(player);
        System.out.println(results[0]);
        System.out.println(results[1]);
        System.out.println(results[2]);
        System.out.println(results[3]);

        String overlapMsg = level.overlapMessageAt(player.x, player.y);
        if (overlapMsg != null) System.out.println(overlapMsg);
    }

    private void showMapMenu() {
        boolean showing = true;
        while (showing) {
            level.printMapWithPlayer(player);
            System.out.println("\nMap options: 1) Close map  2) Toggle legend (" + (showLegend ? "on" : "off") + ")");
            System.out.print("> ");
            String s = in.nextLine().trim();
            if (s.equals("1")) showing = false;
            else if (s.equals("2")) { showLegend = !showLegend; if (showLegend) level.printLegend(); }
            else System.out.println("Unknown option.");
        }
    }

    private void moveFlow() {
        System.out.println("\nWhich direction where you head?");
        System.out.println("1. North");
        System.out.println("2. East");
        System.out.println("3. South");
        System.out.println("4. West");
        System.out.println("5. Do nothing");
        System.out.print("> ");
        String dirSel = in.nextLine().trim();
        Direction dir = switch (dirSel) {
            case "1" -> Direction.NORTH;
            case "2" -> Direction.EAST;
            case "3" -> Direction.SOUTH;
            case "4" -> Direction.WEST;
            default -> null;
        };
        if (dir == null) {
            System.out.println("Canceled move.");
            return;
        }

        System.out.println("\nHow many step will you take?");
        System.out.println("1. Until hit something");
        System.out.println("2. 1 step");
        System.out.println("3. 2 steps");
        System.out.println("4. 3 steps");
        System.out.println("5. Do nothing");
        System.out.print("> ");
        String stepSel = in.nextLine().trim();

        int steps = switch (stepSel) {
            case "1" -> Integer.MAX_VALUE; // until hit something
            case "2" -> 1;
            case "3" -> 2;
            case "4" -> 3;
            default -> 0;
        };
        if (steps == 0) { System.out.println("No movement."); return; }

        MovementResult res = level.movePlayer(player, dir, steps);
        System.out.println(res.message);
        String overlapMsg = level.overlapMessageAt(player.x, player.y);
        if (overlapMsg != null) System.out.println(overlapMsg);
    }

    private void interactFlow() {
        Cell cur = level.getCell(player.x, player.y);
        if (cur.hasInteractable()) {
            Interactable it = cur.getInteractable();
            System.out.println("You interact with: " + it.displayName());
            it.onInteract(player, in);
            return;
        }

        System.out.println("No direct interactable on this tile. Nearby options:");
        Map<String, Cell> neighbors = level.getNeighborsMap(player);
        boolean printed = false;
        for (Map.Entry<String, Cell> e : neighbors.entrySet()) {
            String pos = e.getKey();
            Cell c = e.getValue();
            if (c == null) continue;
            if (c.symbol.equals("|") || c.symbol.equals("|-")) {
                System.out.println("- There's a door " + pos + ". (open door?)");
                printed = true;
            }
            if (c.hasInteractable()) {
                System.out.println("- There's " + c.getInteractable().displayName() + " " + pos + ".");
                printed = true;
            }
        }
        if (!printed) System.out.println("Nothing to interact with nearby.");

        System.out.println("\nChoose action: 1) Open door  2) Cancel");
        System.out.print("> ");
        String pick = in.nextLine().trim();
        if (pick.equals("1")) {
            boolean opened = level.tryOpenAdjacentDoor(player);
            if (opened) System.out.println("You opened the door.");
            else System.out.println("No door you can open (it might be locked or absent).");
        } else System.out.println("Canceled.");
    }
}

/* -------------------------
   Level & Map representation
   ------------------------- */
class Level {
    private final int cols;
    private final int rows;
    private final String[][] grid;
    private final Cell[][] cellGrid;
    private final int playerStartX, playerStartY;

    private Level(int cols, int rows, String[][] tokens, int playerStartX, int playerStartY) {
        this.cols = cols;
        this.rows = rows;
        this.grid = tokens;
        this.playerStartX = playerStartX;
        this.playerStartY = playerStartY;
        this.cellGrid = new Cell[rows][cols];
        buildCells();
    }

    public static Level sampleLevel1() {
        // exact rows provided by user (columns a..n, rows 1..7)
        String[] rowsData = new String[] {
            "#  #  #  #  #  #  #  d  #  #  #  #  #  #",
            "#  .  .  .  .  c  #  .  |- .  .  |  .  #",
            "#  .  #  #  #  #  #  #  #  #  .  #  .  #",
            "#  .  #  .  .  |  .  .  #  #  .  #  .  #",
            "#  .  #  .  .  #  #  .  .  .  .  #  .  #",
            "#  .  .  .  .  .  #  ^  #  #  #  #  c  #",
            "#  #  #  #  #  #  #  u  #  #  #  #  #  #"
        };

       final int cols = 14;
        final int rows = 7;
        String[][] tokens = new String[rows][cols];

        int foundStartX = 0, foundStartY = 0;
        boolean startFound = false;

        for (int r = 0; r < rows; r++) {
            String line = rowsData[r].trim();
            String[] parts = line.split("\\s+"); // split on any whitespace; preserves "|-"
            // If row is too short, pad with dots; if too long, truncate.
            for (int c = 0; c < cols; c++) {
                String tok;
                if (c < parts.length) tok = parts[c];
                else tok = ".";
                if (tok.equals(".")) tok = " ";
                if (tok.equals("^")) {
                    foundStartX = c;
                    foundStartY = r;
                    startFound = true;
                    tok = " ";
                }
                tokens[r][c] = tok;
            }
        }

        if (!startFound) {
            foundStartX = 8; foundStartY = 5; // fallback
        }

        return new Level(cols, rows, tokens, foundStartX, foundStartY);
    }

    private void buildCells() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                String t = grid[r][c];
                cellGrid[r][c] = createCellFromToken(t, c, r);
            }
        }
    }

    private Cell createCellFromToken(String token, int x, int y) {
        if (token.equals("#")) return new Cell("#", x, y, false, null);
        if (token.equals("|")) return new Cell("|", x, y, false, new Door(false));
        if (token.equals("|-")) return new Cell("|-", x, y, false, new Door(true));
        if (token.equals("c")) return new Cell("c", x, y, true, new Chest());
        if (token.equals("u")) return new Cell("u", x, y, true, new Stair(true));
        if (token.equals("d")) return new Cell("d", x, y, true, new Stair(false));
        if (token.equals("-")) return new Cell("-", x, y, true, new Trap());
        if (token.equals("t")) return new Cell("t", x, y, true, new Trader());
        if (token.equals("!")) return new Cell("!", x, y, true, new MonsterStub());
        if (token.equals("e")) return new Cell("e", x, y, true, new EventStub());
        return new Cell(" ", x, y, true, null);
    }

    public int getPlayerStartX() { return playerStartX; }
    public int getPlayerStartY() { return playerStartY; }

    public Cell getCell(int x, int y) {
        if (!inBounds(x, y)) return null;
        return cellGrid[y][x];
    }

    public MovementResult movePlayer(Player p, Direction dir, int steps) {
        p.setFacing(dir);
        int moved = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps; i++) {
            int nx = p.x + dir.dx;
            int ny = p.y + dir.dy;
            if (!inBounds(nx, ny)) { sb.append("There's wall in front of you."); break; }
            Cell next = getCell(nx, ny);
            if (!next.walkable) { sb.append("You are blocked by ").append(next.symbolDescription()).append("."); break; }
            p.x = nx; p.y = ny; moved++;
            if (next.hasInteractable()) { sb.append("You moved and stopped on ").append(next.symbolDescription()).append("."); break; }
        }
        if (moved == 0 && sb.length()==0) sb.append("You didn't move.");
        else if (sb.length()==0) sb.append("You moved " + moved + " step(s).");
        return new MovementResult(moved, sb.toString());
    }

    public void printMapWithPlayer(Player p) {
        // print header letters a..n
        System.out.print("   ");
        for (int c = 0; c < cols; c++) {
            System.out.print(" " + (char)('a' + c) + " ");
        }
        System.out.println();
        for (int r = 0; r < rows; r++) {
            System.out.printf("%2d ", r + 1);
            for (int c = 0; c < cols; c++) {
                if (c == p.x && r == p.y) {
                    System.out.print(" " + p.getFacingSymbol() + " ");
                } else {
                    String sym = cellGrid[r][c].symbol;
                    if (sym.equals(" ")) System.out.print(" . ");
                    else System.out.print(" " + sym + " ");
                }
            }
            System.out.println();
        }
    }


    public void printLegend() {
        System.out.println("\nLegend:");
        System.out.println("#  - wall (unwalkable)");
        System.out.println("|  - door (closed, unwalkable)");
        System.out.println("|- - locked door (unwalkable)");
        System.out.println("c  - chest (walkable, overlap, stops on enter)");
        System.out.println("!  - inevitable combat (walkable, overlap)");
        System.out.println("e  - event (walkable, overlap)");
        System.out.println("u  - travel to upper level (walkable, overlap)");
        System.out.println("d  - travel to lower level (walkable, overlap)");
        System.out.println("t  - trader (walkable, overlap)");
        System.out.println("-  - trap (walkable, overlap)");
        System.out.println(".  - empty walkable tile (dot shows on map)");
        System.out.println("^ > v <  - player facing symbol\n");
    }

    public boolean inBounds(int x, int y) { return x >= 0 && x < cols && y >= 0 && y < rows; }

    public String[] describeAround(Player p) {
        Direction f = p.getFacing();
        Direction front = f;
        Direction left = f.left();
        Direction right = f.right();
        Direction back = f.opposite();

        String frontMsg = describeSideMessageFor(p, front, "front");
        String leftMsg = describeSideMessageFor(p, left, "left side");
        String rightMsg = describeSideMessageFor(p, right, "right side");
        String backMsg = describeSideMessageFor(p, back, "behind");

        return new String[] {frontMsg, leftMsg, rightMsg, backMsg};
    }

    private String describeSideMessageFor(Player p, Direction dir, String humanName) {
        int nx = p.x + dir.dx;
        int ny = p.y + dir.dy;
        if (!inBounds(nx, ny)) return "There's wall " + humanName;
        Cell c = getCell(nx, ny);
        if (!c.walkable) {
            if (c.symbol.equals("#")) return "There's wall " + humanName;
            if (c.symbol.equals("|")) return "There's door in " + humanName;
            if (c.symbol.equals("|-")) return "There's locked door " + humanName + ", I must find key";
            return "There's something blocking " + humanName;
        } else {
            switch (c.symbol) {
                case " ":
                    if (humanName.equals("front")) return "I can walk forward";
                    if (humanName.equals("left side")) return "I can step to left side";
                    if (humanName.equals("right side")) return "I can step to right side";
                    return "I can step back";
                case "c": return "I see chest " + humanName;
                case "u": return "There's stair leads into upper level " + humanName;
                case "d": return "There's stair leads into lower level " + humanName;
                case "e": return "There's event " + humanName;
                case "!": return "I see some enemy " + humanName;
                case "t": return "I see trader " + humanName;
                case "-": return "There's trap " + humanName;
                default: return "There's something " + humanName;
            }
        }
    }

    public String overlapMessageAt(int x, int y) {
        Cell c = getCell(x, y);
        if (c == null) return null;
        switch (c.symbol) {
            case "c": return "there's chest";
            case "t": return "I see trader";
            case "-": return "I step into trap, ouch";
            case "u": return "There's stair leads into upper level here";
            case "d": return "There's stair leads into lower level here";
            case "!": return "There's an enemy here!";
            case "e": return "There's an event here";
            default: return null;
        }
    }

    public Map<String, Cell> getNeighborsMap(Player p) {
        Map<String, Cell> out = new LinkedHashMap<>();
        Direction f = p.getFacing();
        out.put("front", getCell(p.x + f.dx, p.y + f.dy));
        out.put("left", getCell(p.x + f.left().dx, p.y + f.left().dy));
        out.put("right", getCell(p.x + f.right().dx, p.y + f.right().dy));
        out.put("behind", getCell(p.x + f.opposite().dx, p.y + f.opposite().dy));
        return out;
    }

    public boolean tryOpenAdjacentDoor(Player p) {
        for (Cell adj : getNeighborsMap(p).values()) {
            if (adj == null) continue;
            if (adj.symbol.equals("|")) {
                adj.symbol = " ";
                adj.interactable = null;
                return true;
            } else if (adj.symbol.equals("|-")) {
                return false;
            }
        }
        return false;
    }
}

/* -------------------------
   Cell and Interactable
   ------------------------- */
class Cell {
    String symbol; // token
    final int x, y;
    boolean walkable;
    Interactable interactable;

    Cell(String symbol, int x, int y, boolean walkable, Interactable interactable) {
        this.symbol = symbol;
        this.x = x;
        this.y = y;
        this.walkable = walkable;
        this.interactable = interactable;
    }

    boolean hasInteractable() { return interactable != null; }
    Interactable getInteractable() { return interactable; }

    String symbolDescription() {
        switch (symbol) {
            case "#" : return "a wall";
            case "|" : return "a door";
            case "|-" : return "a locked door";
            case "c" : return "a chest";
            case "u" : return "a stair to upper level";
            case "d" : return "a stair to lower level";
            case "t" : return "a trader";
            case "-" : return "a trap";
            case "!" : return "an enemy";
            case "e" : return "an event";
            default  : return "empty space";
        }
    }
}

/* -------------------------
   Interactable interface & implementations
   ------------------------- */
interface Interactable {
    void onInteract(Player player, Scanner in);
    String displayName();
}

class Chest implements Interactable {
    private boolean opened = false;
    private final String content;

    Chest() { this.content = "a few copper coins"; }

    @Override
    public void onInteract(Player player, Scanner in) {
        if (opened) { System.out.println("The chest is empty."); return; }
        System.out.println("You found a chest. Do you want to open it? 1) Accept  2) Exit");
        System.out.print("> ");
        String s = in.nextLine().trim();
        if (s.equals("1")) {
            opened = true;
            System.out.println("You open the chest... Inside you find " + content + "!");
        } else {
            System.out.println("You step away from the chest.");
        }
    }
    @Override public String displayName() { return "Chest"; }
}

class Trader implements Interactable {
    @Override
    public void onInteract(Player player, Scanner in) {
        boolean stay = true;
        while (stay) {
            System.out.println("Trader: What do you want? 1) Buy  2) Sell  3) Exit");
            System.out.print("> ");
            String s = in.nextLine().trim();
            switch (s) {
                case "1" -> System.out.println("Trader: I have nothing in this prototype yet.");
                case "2" -> System.out.println("Trader: I buy nothing yet.");
                case "3" -> { stay = false; System.out.println("You leave the trader."); }
                default -> System.out.println("Choose 1-3.");
            }
        }
    }
    @Override public String displayName() { return "Trader"; }
}

class Door implements Interactable {
    private final boolean locked;
    Door(boolean locked) { this.locked = locked; }
    @Override public void onInteract(Player player, Scanner in) {
        if (locked) System.out.println("This door is locked. I must find a key.");
        else System.out.println("It's a closed door. You can open it from nearby (use Open door option).");
    }
    @Override public String displayName() { return locked ? "Locked Door" : "Door"; }
}

class Trap implements Interactable {
    private boolean sprung = false;
    @Override public void onInteract(Player player, Scanner in) {
        if (!sprung) {
            sprung = true;
            System.out.println("You triggered a trap! Ouch. (Prototype: no HP implemented.)");
        } else System.out.println("It's an already sprung trap.");
    }
    @Override public String displayName() { return "Trap"; }
}

class Stair implements Interactable {
    boolean toUpper;
    Stair(boolean toUpper) { this.toUpper = toUpper; }
    @Override public void onInteract(Player player, Scanner in) {
        if (toUpper) System.out.println("This stair leads to upper level. (Prototype: no other levels implemented.)");
        else System.out.println("This stair leads to lower level. (Prototype: no other levels implemented.)");
    }
    @Override public String displayName() { return toUpper ? "Stair up" : "Stair down"; }
}

class MonsterStub implements Interactable {
    @Override public void onInteract(Player player, Scanner in) {
        System.out.println("A monster appears! (Combat not yet implemented in this prototype.)");
    }
    @Override public String displayName() { return "Monster"; }
}

class EventStub implements Interactable {
    @Override public void onInteract(Player player, Scanner in) {
        System.out.println("An event occurs. Do you accept? 1) Accept  2) Exit");
        System.out.print("> ");
        String s = in.nextLine().trim();
        if (s.equals("1")) System.out.println("You accept the event. (Prototype: nothing happens.)");
        else System.out.println("You decline the event.");
    }
    @Override public String displayName() { return "Event"; }
}

/* -------------------------
   Entity and Player (inheritance & encapsulation)
   ------------------------- */
abstract class Entity {
    protected String name;
    protected int x, y;

    protected Entity(String name, int x, int y) {
        this.name = name; this.x = x; this.y = y;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
}

class Player extends Entity {
    private Direction facing;

    public Player(String name, int startX, int startY, Direction facing) {
        super(name, startX, startY);
        this.facing = facing;
    }

    public Direction getFacing() { return facing; }
    public void setFacing(Direction f) { this.facing = f; }

    public char getFacingSymbol() {
        return switch (facing) {
            case NORTH -> '^';
            case EAST -> '>';
            case SOUTH -> 'v';
            case WEST -> '<';
        };
    }
}

/* -------------------------
   Direction utility
   ------------------------- */
enum Direction {
    NORTH(0,-1), EAST(1,0), SOUTH(0,1), WEST(-1,0);

    final int dx, dy;
    Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }

    Direction left() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }
    Direction right() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }
    Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}

/* -------------------------
   Simple helper classes
   ------------------------- */
class MovementResult {
    int stepsMoved;
    String message;
    MovementResult(int stepsMoved, String message) { this.stepsMoved = stepsMoved; this.message = message; }
}
