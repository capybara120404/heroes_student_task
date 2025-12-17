package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    private static class Node {
        int x, y, d;
        Node(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }
    }

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int sx = attackUnit.getxCoordinate(), sy = attackUnit.getyCoordinate();
        int tx = targetUnit.getxCoordinate(), ty = targetUnit.getyCoordinate();

        boolean[][] occupied = new boolean[WIDTH][HEIGHT];
        for (Unit u : existingUnitList) {
            if (u.isAlive() && u != attackUnit && u != targetUnit) {
                occupied[u.getxCoordinate()][u.getyCoordinate()] = true;
            }
        }

        int[][] dist = new int[WIDTH][HEIGHT];
        for (int[] r : dist) {
            Arrays.fill(r, Integer.MAX_VALUE);
        }
        Edge[][] parent = new Edge[WIDTH][HEIGHT];

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.d));
        dist[sx][sy] = 0;
        pq.offer(new Node(sx, sy, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.d > dist[cur.x][cur.y]) {
                continue;
            }
            if (cur.x == tx && cur.y == ty) {
                break;
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    int nx = cur.x + dx, ny = cur.y + dy;
                    if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT && !occupied[nx][ny]) {
                        int nd = cur.d + 1;
                        if (nd < dist[nx][ny]) {
                            dist[nx][ny] = nd;
                            parent[nx][ny] = new Edge(cur.x, cur.y);
                            pq.offer(new Node(nx, ny, nd));
                        }
                    }
                }
            }
        }

        if (dist[tx][ty] == Integer.MAX_VALUE) {
            return Collections.emptyList();
        }

        List<Edge> path = new ArrayList<>();
        int x = tx, y = ty;
        while (true) {
            path.add(new Edge(x, y));
            if (x == sx && y == sy) {
                break;
            }
            Edge p = parent[x][y];
            x = p.getX(); y = p.getY();
        }
        Collections.reverse(path);

        return path;
    }
}
