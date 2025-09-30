package src.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MapGenerator {
    private final Map<String, Tile> tiles = new HashMap<>();
    private final Random rand = new Random();
    private int exitsPlaced = 0;
    private static final int MAX_EXITS = 25;

    // เก็บตำแหน่งทางออก
    private int exitX = -1;
    private int exitY = -1;

    public Tile getTile(int x, int y) {
        String key = x + "," + y;
        if (!tiles.containsKey(key)) {
            // Randomly generate tile: 75% floor, 25% wall
            Tile t = rand.nextInt(100) < 75 ? Tile.FLOOR : Tile.WALL;
            // Attempt to spawn exit with 0.01% chance (1 in 10,000) if we haven't hit the cap
            if (exitsPlaced < MAX_EXITS && rand.nextInt(10000) < 1) {
                t = Tile.EXIT;
                exitsPlaced++;
            }
            tiles.put(key, t);
        }
        return tiles.get(key);
    }

    // วางทางออกให้อยู่ไกลที่สุดและสร้างทางเดินจากจุดเริ่มต้น
    private void ensureExit() {
        if (exitX == -1 && exitY == -1) {
            int maxDist = -1;
            int bestX = 0, bestY = 0;

            // วนหาตำแหน่งไกลที่สุด
            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 20; x++) {
                    if (getTile(x, y) == Tile.FLOOR) {
                        int dist = (x * x) + (y * y);
                        if (dist > maxDist) {
                            maxDist = dist;
                            bestX = x;
                            bestY = y;
                        }
                    }
                }
            }

            exitX = bestX;
            exitY = bestY;

            // สร้างทางเดินตรง (หรือ zig-zag ง่าย ๆ) จาก (0,0) ไป (exitX, exitY)
            int cx = 0, cy = 0;
            while (cx != exitX || cy != exitY) {
                tiles.put(cx + "," + cy, Tile.FLOOR); // บังคับเป็นพื้น
                // เลื่อนแนวนอนก่อน
                if (cx < exitX) cx++;
                else if (cx > exitX) cx--;
                // เลื่อนแนวตั้ง
                if (cy < exitY) cy++;
                else if (cy > exitY) cy--;
            }

            // วาง EXIT
            tiles.put(exitX + "," + exitY, Tile.EXIT);
        }
    }

    public void reveal(int px, int py) {
        System.out.println("\n--- Map View ---");
        // show a view of width 20 and height 10 centered on the player
        int halfWidthLeft = 9;   // px - 9 to px + 10 => 20 columns
        int halfWidthRight = 10;
        int halfHeightUp = 4;    // py - 4 to py + 5 => 10 rows
        int halfHeightDown = 5;
        for (int y = py - halfHeightUp; y <= py + halfHeightDown; y++) {
            for (int x = px - halfWidthLeft; x <= px + halfWidthRight; x++) {
                if (x == px && y == py) System.out.print('@');
                else System.out.print(getTile(x, y).getSymbol());
            }
            System.out.println();
        }
    }
}
