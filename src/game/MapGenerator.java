package src.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MapGenerator {
    private final Map<String, Tile> tiles = new HashMap<>();
    private final Random rand = new Random();

    // เก็บตำแหน่งทางออก
    private int exitX = -1;
    private int exitY = -1;

    public Tile getTile(int x, int y) {
        String key = x + "," + y;
        if (!tiles.containsKey(key)) {
            // Randomly generate tile: 75% floor, 25% wall
            Tile t = rand.nextInt(100) < 75 ? Tile.FLOOR : Tile.WALL;
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
        ensureExit(); // ตรวจสอบให้มีประตูทางออก

        System.out.println("\n=== Full Map (20x10) ===");
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 20; x++) {
                if (x == px && y == py) {
                    System.out.print('@'); // ผู้เล่น
                } else {
                    System.out.print(getTile(x, y).getSymbol());
                }
            }
            System.out.println();
        }
    }
}
