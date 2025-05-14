import java.util.*;

public class TSP {

    static int calculateCost(int[][] graph, int[] path) {
        int cost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            cost += graph[path[i]][path[i + 1]];
        }
        cost += graph[path[path.length - 1]][path[0]];
        return cost;
    }

    static void permute(int[] arr, int l, int r, int[][] graph, int[] bestPath) {
        if (l == r) {
            int cost = calculateCost(graph, arr);
            if (cost < bestPath[0]) {
                bestPath[0] = cost;
                System.arraycopy(arr, 0, bestPath, 1, arr.length);
            }
        } else {
            for (int i = l; i <= r; i++) {
                swap(arr, i, l);
                permute(arr, l + 1, r, graph, bestPath);
                swap(arr, i, l);
            }
        }
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int n = 4;

        int[][] graph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        int[] bestPath = new int[n + 1];
        bestPath[0] = Integer.MAX_VALUE;

        int[] cities = new int[n];
        for (int i = 0; i < n; i++) {
            cities[i] = i;
        }

        permute(cities, 0, n - 1, graph, bestPath);

        System.out.println("Best Path: ");
        for (int i = 0; i < bestPath.length - 1; i++) {
            System.out.print(bestPath[i + 1] + " -> ");
        }
        System.out.println(bestPath[1] + " (Back to start)");

        System.out.println("Minimum Cost: " + bestPath[0]);
    }
}
