package src.game;

import src.characters.Player;

import java.util.*;

public class MapGenerator {
    private final Map<String, Tile> tiles = new HashMap<>();
    private final Random rand;
    private int exitsPlaced = 0;
    private static final int MAX_EXITS = 4;

    // Map dimensions
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private final int x0 = -WIDTH / 2;
    private final int y0 = -HEIGHT / 2;
    // Reveal window (columns x rows). Default 20x10.
    private int revealWidth = 20;
    private int revealHeight = 10;

    public void setRevealSize(int width, int height) {
        if (width < 3) width = 3;
        if (height < 3) height = 3;
        this.revealWidth = width;
        this.revealHeight = height;
    }

    // Expose current reveal window size for external tools/menus
    public int getRevealWidth() { return this.revealWidth; }
    public int getRevealHeight() { return this.revealHeight; }

    public enum Mode { BSP, CAVE }

    public MapGenerator() { this(System.currentTimeMillis(), Mode.BSP); }
    public MapGenerator(long seed) { this(seed, Mode.BSP); }
    public MapGenerator(long seed, Mode mode) { this.rand = new Random(seed); if (mode == Mode.CAVE) generateCaves(); else generateBspDungeon(); }

    // Public helper to pick a safe spawn point on the generated floor.
    // Returns world coordinates {x,y} or {0,0} fallback.
    public int[] findSpawn() {
        // Prefer center (world 0,0) spawn when it's a floor
        int centerGx = WIDTH / 2;
        int centerGy = HEIGHT / 2;
        if (getTileAtGrid(centerGx, centerGy) == Tile.FLOOR) return new int[]{0, 0};

        // Otherwise choose the floor tile closest to center that looks like a safe spawn
        List<int[]> floors = new ArrayList<>();
        for (int gy = 0; gy < HEIGHT; gy++) {
            for (int gx = 0; gx < WIDTH; gx++) {
                if (getTileAtGrid(gx, gy) == Tile.FLOOR) floors.add(new int[]{gx, gy});
            }
        }
        // sort by Manhattan distance to center (closest first)
        floors.sort(Comparator.comparingInt(p -> Math.abs(p[0] - centerGx) + Math.abs(p[1] - centerGy)));
        for (int[] p : floors) {
            int gx = p[0], gy = p[1];
            // require at least 2 adjacent floor tiles (not enclosed)
            int adj = 0;
            for (int dy = -1; dy <= 1; dy++) for (int dx = -1; dx <= 1; dx++) if (!(dx==0 && dy==0)) {
                int nx = gx + dx, ny = gy + dy;
                if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT && getTileAtGrid(nx, ny) == Tile.FLOOR) adj++;
            }
            if (adj < 2) continue;
            int size = reachableFloorSize(gx, gy);
            if (size >= 30) {
                return new int[]{gx + x0, gy + y0};
            }
        }
        // fallback to center world coords
        return new int[]{0,0};
    }

    private int reachableFloorSize(int startGx, int startGy) {
        boolean[][] seen = new boolean[WIDTH][HEIGHT];
        Deque<int[]> dq = new ArrayDeque<>();
        dq.add(new int[]{startGx, startGy});
        seen[startGx][startGy] = true;
        int count = 0;
        while (!dq.isEmpty() && count < 10000) {
            int[] p = dq.removeFirst();
            count++;
            int gx = p[0], gy = p[1];
            for (int d = 0; d < 4; d++) {
                int nx = gx + (d==0?1:(d==1?-1:0));
                int ny = gy + (d==2?1:(d==3?-1:0));
                if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) continue;
                if (seen[nx][ny]) continue;
                if (getTileAtGrid(nx, ny) == Tile.FLOOR) {
                    seen[nx][ny] = true; dq.addLast(new int[]{nx, ny});
                }
            }
        }
        return count;
    }

    // ---------- BSP Dungeon Generation ----------
    private void generateBspDungeon() {
        // init walls
        for (int gy = 0; gy < HEIGHT; gy++) for (int gx = 0; gx < WIDTH; gx++) setTileAtGrid(gx, gy, Tile.WALL);

        class Node {
            int x,y,w,h; Node left,right;
            Node(int x,int y,int w,int h){this.x=x;this.y=y;this.w=w;this.h=h;}
        }

        List<Node> leaves = new ArrayList<>();
        Node root = new Node(1,1, WIDTH-2, HEIGHT-2);
        Deque<Node> stack = new ArrayDeque<>(); stack.add(root);
        int minSize = 12;
        while (!stack.isEmpty()) {
            Node n = stack.removeFirst();
            if (n.w > minSize*2 || n.h > minSize*2) {
                boolean splitH = rand.nextBoolean();
                if (n.w > n.h && n.w > minSize*2) splitH = false;
                if (n.h > n.w && n.h > minSize*2) splitH = true;
                if (splitH) {
                    int split = rand.nextInt(n.h - minSize*2) + n.y + minSize;
                    n.left = new Node(n.x, n.y, n.w, split - n.y);
                    n.right = new Node(n.x, split, n.w, n.y + n.h - split);
                } else {
                    int split = rand.nextInt(n.w - minSize*2) + n.x + minSize;
                    n.left = new Node(n.x, n.y, split - n.x, n.h);
                    n.right = new Node(split, n.y, n.x + n.w - split, n.h);
                }
                stack.addFirst(n.left); stack.addFirst(n.right);
            } else {
                leaves.add(n);
            }
        }

        List<int[]> centers = new ArrayList<>();
        for (Node leaf : leaves) {
            // carve a room inside leaf bounds
            int rw = Math.max(3, Math.min(leaf.w - 2, 3 + rand.nextInt(Math.max(1, leaf.w - 4))));
            int rh = Math.max(3, Math.min(leaf.h - 2, 3 + rand.nextInt(Math.max(1, leaf.h - 4))));
            int rx = leaf.x + 1 + rand.nextInt(Math.max(1, leaf.w - rw - 1));
            int ry = leaf.y + 1 + rand.nextInt(Math.max(1, leaf.h - rh - 1));
            for (int yy = ry; yy < ry + rh; yy++) for (int xx = rx; xx < rx + rw; xx++) setTileAtGrid(xx, yy, Tile.FLOOR);
            int cx = rx + rw/2, cy = ry + rh/2; centers.add(new int[]{cx, cy});
        }

        // connect centers with simple MST-like connection
        if (!centers.isEmpty()) {
            boolean[] used = new boolean[centers.size()]; used[0]=true; int usedCount=1;
            while (usedCount < centers.size()) {
                int bestI=-1, bestJ=-1; int bestDist=Integer.MAX_VALUE;
                for (int i=0;i<centers.size();i++) if (used[i]) {
                    for (int j=0;j<centers.size();j++) if (!used[j]) {
                        int dx = centers.get(i)[0]-centers.get(j)[0];
                        int dy = centers.get(i)[1]-centers.get(j)[1];
                        int d = dx*dx+dy*dy;
                        if (d < bestDist) { bestDist=d; bestI=i; bestJ=j; }
                    }
                }
                if (bestI==-1) break;
                int[] a = centers.get(bestI), b = centers.get(bestJ);
                carveCorridor(a[0], a[1], b[0], b[1]);
                used[bestJ]=true; usedCount++;
            }
        }

        // Place exits on some floor tiles
        placeDirectionalExitsAroundCenter();
    }

    private void carveCorridor(int x1, int y1, int x2, int y2) {
        int x = x1, y = y1;
        while (x != x2) { setTileAtGrid(x, y, Tile.FLOOR); x += (x2 > x) ? 1 : -1; }
        while (y != y2) { setTileAtGrid(x, y, Tile.FLOOR); y += (y2 > y) ? 1 : -1; }
    }

    // ---------- Cave generator (cellular automata) ----------
    private void generateCaves() {
        // initialize randomly
        double fillProb = 0.45;
        for (int gy = 0; gy < HEIGHT; gy++) for (int gx = 0; gx < WIDTH; gx++) {
            if (rand.nextDouble() < fillProb) setTileAtGrid(gx, gy, Tile.WALL); else setTileAtGrid(gx, gy, Tile.FLOOR);
        }
        // run cellular automata iterations
        int iterations = 4;
        for (int it = 0; it < iterations; it++) {
            Map<String, Tile> next = new HashMap<>(tiles);
            for (int gy = 1; gy < HEIGHT-1; gy++) for (int gx = 1; gx < WIDTH-1; gx++) {
                int floors = 0;
                for (int yy = -1; yy <= 1; yy++) for (int xx = -1; xx <= 1; xx++) {
                    if (xx==0 && yy==0) continue;
                    if (getTileAtGrid(gx+xx, gy+yy) == Tile.FLOOR) floors++;
                }
                if (floors >= 5) next.put(keyForGrid(gx,gy), Tile.FLOOR); else next.put(keyForGrid(gx,gy), Tile.WALL);
            }
            tiles.clear(); tiles.putAll(next);
        }
        // place exits
        placeDirectionalExitsAroundCenter();
    }

    // Place exactly one exit in each cardinal direction relative to the map center (player spawn).
    // Attempts to pick a random floor tile in the north/south/east/west bands. If a band is empty,
    // it will fall back to selecting a far tile in that general direction.
    private void placeDirectionalExitsAroundCenter() {
        // collect all floor tiles (world coords)
        List<int[]> floors = new ArrayList<>();
        for (var e : tiles.entrySet()) {
            String k = e.getKey();
            Tile v = e.getValue();
            if (v == Tile.FLOOR) {
                String[] parts = k.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                floors.add(new int[]{x, y});
            }
        }
        if (floors.isEmpty()) return;

        int cx = 0, cy = 0; // center world coords
        int thresholdX = Math.max(1, WIDTH / 10);
        int thresholdY = Math.max(1, HEIGHT / 10);

        List<int[]> north = new ArrayList<>();
        List<int[]> south = new ArrayList<>();
        List<int[]> west = new ArrayList<>();
        List<int[]> east = new ArrayList<>();

        for (int[] p : floors) {
            int x = p[0], y = p[1];
            if (x == cx && y == cy) continue; // don't place on player
            if (y <= cy - thresholdY) north.add(p);
            if (y >= cy + thresholdY) south.add(p);
            if (x <= cx - thresholdX) west.add(p);
            if (x >= cx + thresholdX) east.add(p);
        }

        Collections.shuffle(north, rand);
        Collections.shuffle(south, rand);
        Collections.shuffle(west, rand);
        Collections.shuffle(east, rand);

        exitsPlaced = 0;
        // helper to place if available
        if (!north.isEmpty()) { var p = north.get(0); tiles.put(p[0] + "," + p[1], Tile.EXIT); exitsPlaced++; }
        if (!south.isEmpty()) { var p = south.get(0); tiles.put(p[0] + "," + p[1], Tile.EXIT); exitsPlaced++; }
        if (!west.isEmpty())  { var p = west.get(0);  tiles.put(p[0] + "," + p[1], Tile.EXIT); exitsPlaced++; }
        if (!east.isEmpty())  { var p = east.get(0);  tiles.put(p[0] + "," + p[1], Tile.EXIT); exitsPlaced++; }

        // If any direction had no candidate, try to pick a fallback from floors that lie farthest in that direction
        if (exitsPlaced < MAX_EXITS) {
            // iterate directions individually
            if (north.isEmpty()) {
                int bestDist = Integer.MIN_VALUE; int[] best = null;
                for (int[] p : floors) {
                    if (p[1] < cy && !(p[0]==cx && p[1]==cy)) {
                        int dist = cy - p[1]; if (dist > bestDist) { bestDist = dist; best = p; }
                    }
                }
                if (best != null) { tiles.put(best[0] + "," + best[1], Tile.EXIT); exitsPlaced++; }
            }
            if (south.isEmpty()) {
                int bestDist = Integer.MIN_VALUE; int[] best = null;
                for (int[] p : floors) {
                    if (p[1] > cy && !(p[0]==cx && p[1]==cy)) {
                        int dist = p[1] - cy; if (dist > bestDist) { bestDist = dist; best = p; }
                    }
                }
                if (best != null) { tiles.put(best[0] + "," + best[1], Tile.EXIT); exitsPlaced++; }
            }
            if (west.isEmpty()) {
                int bestDist = Integer.MIN_VALUE; int[] best = null;
                for (int[] p : floors) {
                    if (p[0] < cx && !(p[0]==cx && p[1]==cy)) {
                        int dist = cx - p[0]; if (dist > bestDist) { bestDist = dist; best = p; }
                    }
                }
                if (best != null) { tiles.put(best[0] + "," + best[1], Tile.EXIT); exitsPlaced++; }
            }
            if (east.isEmpty()) {
                int bestDist = Integer.MIN_VALUE; int[] best = null;
                for (int[] p : floors) {
                    if (p[0] > cx && !(p[0]==cx && p[1]==cy)) {
                        int dist = p[0] - cx; if (dist > bestDist) { bestDist = dist; best = p; }
                    }
                }
                if (best != null) { tiles.put(best[0] + "," + best[1], Tile.EXIT); exitsPlaced++; }
            }
        }
        // final clamp
        if (exitsPlaced > MAX_EXITS) exitsPlaced = MAX_EXITS;
    }

    // Helpers
    private String keyForGrid(int gx, int gy) { return (gx + x0) + "," + (gy + y0); }
    private void setTileAtGrid(int gx, int gy, Tile t) { tiles.put(keyForGrid(gx, gy), t); }
    private Tile getTileAtGrid(int gx, int gy) { String k = keyForGrid(gx, gy); return tiles.getOrDefault(k, Tile.WALL); }

    public Tile getTile(int x, int y) { String key = x + "," + y; return tiles.getOrDefault(key, Tile.WALL); }

    public void reveal(int px, int py, Player player, boolean noclip) {
        System.out.println("\n--- Map View ---");
        if (noclip) {
            final String MAGENTA = "\u001B[35m";
            final String RESET = "\u001B[0m";
            System.out.println(MAGENTA + "[Noclip ON] Encounters disabled" + RESET);
        }
    int halfWidthLeft = (revealWidth - 1) / 2;
    int halfWidthRight = (revealWidth - 1) - halfWidthLeft;
    int halfHeightUp = (revealHeight - 1) / 2;
    int halfHeightDown = (revealHeight - 1) - halfHeightUp;
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";
        final String ORANGE = "\u001B[38;5;208m";
        final String YELLOW = "\u001B[33m";
        for (int y = py - halfHeightUp; y <= py + halfHeightDown; y++) {
            for (int x = px - halfWidthLeft; x <= px + halfWidthRight; x++) {
                if (x == px && y == py) {
                    boolean hasExcal = player != null && player.hasExcaliburEquipped();
                    if (hasExcal) System.out.print(YELLOW + '@' + RESET); else System.out.print(ORANGE + '@' + RESET);
                } else {
                    Tile t = getTile(x, y);
                    if (t == Tile.EXIT) System.out.print(GREEN + t.getSymbol() + RESET); else System.out.print(t.getSymbol());
                }
            }
            System.out.println();
        }
    }

    /**
     * Ensure exits are placed at least minSteps (Manhattan) from the given world coordinate px,py.
     * If minSteps is larger than the maximum possible distance in the map, it will be
     * capped to the achievable maximum.
     */
    public void relocateExitsAwayFrom(int px, int py, int minSteps) {
        // Collect floor tiles (world coords) and current exits
        List<int[]> floorList = new ArrayList<>();
        List<String> currentExits = new ArrayList<>();
        for (var e : tiles.entrySet()) {
            String k = e.getKey();
            Tile v = e.getValue();
            String[] parts = k.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            if (v == Tile.FLOOR) floorList.add(new int[]{x, y});
            else if (v == Tile.EXIT) currentExits.add(k);
        }

        if (floorList.isEmpty()) return;

        // compute maximum achievable Manhattan distance among floor tiles
        int maxDist = 0;
        for (int[] p : floorList) maxDist = Math.max(maxDist, Math.abs(p[0] - px) + Math.abs(p[1] - py));
        if (minSteps > maxDist) minSteps = maxDist;

        // Remove current exits (turn to floor) so we can re-place
        for (String k : currentExits) tiles.put(k, Tile.FLOOR);

        // collect candidates at or beyond minSteps
        List<int[]> candidates = new ArrayList<>();
        for (int[] p : floorList) {
            if (p[0] == px && p[1] == py) continue; // don't place on player
            int dist = Math.abs(p[0] - px) + Math.abs(p[1] - py);
            if (dist >= minSteps) candidates.add(p);
        }

        // if no candidates, relax minSteps down to nearest possible (choose farthest available)
        if (candidates.isEmpty()) {
            // find farthest floor tiles
            int best = -1;
            for (int[] p : floorList) {
                if (p[0] == px && p[1] == py) continue;
                best = Math.max(best, Math.abs(p[0] - px) + Math.abs(p[1] - py));
            }
            if (best <= 0) return; // nothing to place
            for (int[] p : floorList) {
                if (p[0] == px && p[1] == py) continue;
                int dist = Math.abs(p[0] - px) + Math.abs(p[1] - py);
                if (dist == best) candidates.add(p);
            }
        }

        Collections.shuffle(candidates, rand);
        int placed = 0;
        for (int[] p : candidates) {
            if (placed >= MAX_EXITS) break;
            tiles.put(p[0] + "," + p[1], Tile.EXIT);
            placed++;
        }
        exitsPlaced = placed;
    }

    /**
     * Find the nearest EXIT tile to the given world coordinates (px,py).
     * Returns {x,y} or null if no exit exists.
     */
    public int[] findNearestExit(int px, int py) {
        int bestDist = Integer.MAX_VALUE;
        int[] best = null;
        for (var e : tiles.entrySet()) {
            if (e.getValue() == Tile.EXIT) {
                String[] parts = e.getKey().split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int dist = Math.abs(x - px) + Math.abs(y - py);
                if (dist == 0) continue; // skip on-top
                if (dist < bestDist) { bestDist = dist; best = new int[]{x, y}; }
            }
        }
        return best;
    }

    // Place an EXIT tile at the given world coordinate (x,y).
    public void placeExitAt(int x, int y) {
        tiles.put(x + "," + y, Tile.EXIT);
    }
}
