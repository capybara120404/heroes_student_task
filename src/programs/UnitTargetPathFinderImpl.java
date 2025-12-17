package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;
import java.util.stream.Collectors;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    private static final int[] DX = {-1, 0, 1, 0};
    private static final int[] DY = {0, -1, 0, 1};

    private static class Node {
        final int x;
        final int y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node node)) {
                return false;
            }

            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Node start = new Node(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Node end = new Node(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        Set<Node> obstacles = existingUnitList.stream()
                .filter(u -> u.isAlive() && u != attackUnit)
                .map(u -> new Node(u.getxCoordinate(), u.getyCoordinate()))
                .collect(Collectors.toSet());

        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] prev = new Edge[WIDTH][HEIGHT];

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.equals(end)) {
                break;
            }

            for (int i = 0; i < DX.length; i++) {
                int nx = current.x + DX[i];
                int ny = current.y + DY[i];

                if (isOutOfBounds(nx, ny)) {
                    continue;
                }

                Node next = new Node(nx, ny);
                if (visited[nx][ny] || obstacles.contains(next)) {
                    continue;
                }

                visited[nx][ny] = true;
                prev[nx][ny] = new Edge(current.x, current.y);
                queue.add(next);
            }
        }

        if (!visited[end.x][end.y]) {
            return Collections.emptyList();
        }

        return reconstructPath(end, prev);
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT;
    }

    private List<Edge> reconstructPath(Node end, Edge[][] prev) {
        List<Edge> path = new ArrayList<>();
        Edge current = new Edge(end.x, end.y);

        while (prev[current.getX()][current.getY()] != null) {
            path.add(current);
            current = prev[current.getX()][current.getY()];
        }

        Collections.reverse(path);

        return path;
    }
}
