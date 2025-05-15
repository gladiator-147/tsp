import java.util.*;

public class TSP {

    static final int INF = Integer.MAX_VALUE;

    // Function to reduce the matrix and return the reduction cost
    static int reduceMatrix(int[][] matrix, int n, boolean rowReduced, boolean colReduced) {
        int cost = 0;

        // Row reduction
        if (!rowReduced) {
            for (int i = 0; i < n; i++) {
                int min = INF;
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j] < min) min = matrix[i][j];
                }
                if (min != INF && min != 0) {
                    for (int j = 0; j < n; j++) {
                        if (matrix[i][j] != INF) matrix[i][j] -= min;
                    }
                    cost += min;
                }
            }
        }

        // Column reduction
        if (!colReduced) {
            for (int j = 0; j < n; j++) {
                int min = INF;
                for (int i = 0; i < n; i++) {
                    if (matrix[i][j] < min) min = matrix[i][j];
                }
                if (min != INF && min != 0) {
                    for (int i = 0; i < n; i++) {
                        if (matrix[i][j] != INF) matrix[i][j] -= min;
                    }
                    cost += min;
                }
            }
        }

        return cost;
    }

    static class Node {
        int[][] reducedMatrix;
        List<Integer> path;
        int cost;
        int vertex;
        int level;

        Node(int[][] matrix, List<Integer> path, int cost, int vertex, int level) {
            this.reducedMatrix = matrix;
            this.path = path;
            this.cost = cost;
            this.vertex = vertex;
            this.level = level;
        }
    }

    static Node createNode(int[][] parentMatrix, List<Integer> path, int level, int i, int j, int n) {
        int[][] matrix = new int[n][n];
        for (int x = 0; x < n; x++)
            matrix[x] = parentMatrix[x].clone();

        for (int k = 0; k < n; k++) {
            matrix[i][k] = INF;
            matrix[k][j] = INF;
        }
        matrix[j][0] = INF;

        int cost = reduceMatrix(matrix, n, false, false);

        List<Integer> newPath = new ArrayList<>(path);
        newPath.add(j);

        return new Node(matrix, newPath, cost, j, level + 1);
    }

    static void branchAndBound(int[][] costMatrix, int n) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));

        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(0);

        int[][] initialMatrix = new int[n][n];
        for (int i = 0; i < n; i++)
            initialMatrix[i] = costMatrix[i].clone();

        int initialCost = reduceMatrix(initialMatrix, n, false, false);

        pq.add(new Node(initialMatrix, initialPath, initialCost, 0, 0));

        int finalCost = INF;
        List<Integer> bestPath = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.level == n - 1) {
                current.path.add(0); // back to start
                int totalCost = current.cost + costMatrix[current.vertex][0];
                if (totalCost < finalCost) {
                    finalCost = totalCost;
                    bestPath = current.path;
                }
                continue;
            }

            for (int j = 0; j < n; j++) {
                if (!current.path.contains(j)) {
                    Node child = createNode(current.reducedMatrix, current.path, current.level, current.vertex, j, n);
                    child.cost += current.cost + costMatrix[current.vertex][j];
                    pq.add(child);
                }
            }
        }

        System.out.println("Minimum Cost: " + finalCost);
        System.out.println("Best Path: " + bestPath);
    }

    public static void main(String[] args) {
        int[][] graph = {
            {INF, 10, 15, 20},
            {10, INF, 35, 25},
            {15, 35, INF, 30},
            {20, 25, 30, INF}
        };

        int n = 4;
        branchAndBound(graph, n);
    }
}
