package src.game;

public enum Tile {
    WALL('#'),
    FLOOR('.'),
    PLAYER('@');

    private final char symbol;
    Tile(char symbol) { this.symbol = symbol; }
    public char getSymbol() { return symbol; }
}
