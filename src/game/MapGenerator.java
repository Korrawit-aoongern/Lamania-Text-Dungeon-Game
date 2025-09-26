package src.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MapGenerator {
    private final Map<String, Tile> tiles = new HashMap<>();
    private final Random rand = new Random();

    public Tile getTile(int x, int y) {
        String key = x + "," + y;
        if (!tiles.containsKey(key)) {
            // Randomly generate tile: 75% floor, 25% wall
            Tile t = rand.nextInt(100) < 75 ? Tile.FLOOR : Tile.WALL;
            tiles.put(key, t);
        }
        return tiles.get(key);
    }

    public void reveal(int px, int py) {
        System.out.println("\n--- Map View ---");
        for (int y = py - 2; y <= py + 2; y++) {
            for (int x = px - 2; x <= px + 2; x++) {
                if (x == px && y == py) System.out.print('@');
                else System.out.print(getTile(x, y).getSymbol());
            }
            System.out.println();
        }
    }
}
