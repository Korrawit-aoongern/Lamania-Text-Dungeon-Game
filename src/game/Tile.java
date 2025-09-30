package src.game;

public enum Tile {
    WALL('#'),
    FLOOR('.'),
    PLAYER('@'),
    EXIT('E');

    private final char symbol;
    Tile(char symbol) { this.symbol = symbol; }
    public char getSymbol() { return symbol; }
}
