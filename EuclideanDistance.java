import java.util.*;

public class EuclideanDistance implements Comparable<EuclideanDistance> {
    private int[][] board;
    private int cost;
    private EuclideanDistance parent;
    private int depth;

    public EuclideanDistance(int[][] board, int depth) {
        this.board = board;
        this.cost = 0;
        this.parent = null;
        this.depth = depth;
    }

    // Implement the compareTo method for the priority queue
    @Override
    public int compareTo(EuclideanDistance o) {
        return Integer.compare(this.cost + heuristic(), o.cost + o.heuristic());
    }

    // Implement the equals method for the set of visited states
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EuclideanDistance)) {
            return false;
        }
        EuclideanDistance state = (EuclideanDistance) o;
        return Arrays.deepEquals(this.board, state.board);
    }

    // Implement the hashCode method for the set of visited states
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Helper method to calculate the Euclidean distance heuristic
    private int heuristic() {
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int value = board[i][j];
                if (value != 0) {
                    int targetX = (value - 1) / board.length;
                    int targetY = (value - 1) % board.length;
                    distance += Math.sqrt(Math.pow(i - targetX, 2) + Math.pow(j - targetY, 2));
                }
            }
        }
        return distance;
    }

    // Method to check if the current state is the goal state
    public boolean isGoal() {
        int count = 1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                // Handles boards of different sizes
                if (board[i][j] != count % (board.length * board[i].length)) {
                    return false;
                }
                count++;
            }
        }
        return true;
    }

    public List<EuclideanDistance> getNextStates() {
        List<EuclideanDistance> nextStates = new ArrayList<>();
        int x = -1;
        int y = -1;

        // Find the blank tile
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        // Generate the next possible states
        if (x > 0) {
            int[][] newState = new int[3][3];
            copyBoard(newState, board);
            swap(newState, x, y, x - 1, y);
            nextStates.add(new EuclideanDistance(newState, depth + 1)); // add the new state to the list
        }
        if (x < board.length - 1) {
            int[][] newState = new int[3][3];
            copyBoard(newState, board);
            swap(newState, x, y, x + 1, y);
            nextStates.add(new EuclideanDistance(newState, depth + 1)); // add the new state to the list
        }
        if (y > 0) {
            int[][] newState = new int[3][3];
            copyBoard(newState, board);
            swap(newState, x, y, x, y - 1);
            nextStates.add(new EuclideanDistance(newState, depth + 1)); // add the new state to the list
        }
        if (y < board.length - 1) {
            int[][] newState = new int[3][3];
            copyBoard(newState, board);
            swap(newState, x, y, x, y + 1);
            nextStates.add(new EuclideanDistance(newState, depth + 1)); // add the new state to the list
        }

        return nextStates;
    }

    private void swap(int[][] board, int x1, int y1, int x2, int y2) {
        int temp = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = temp;
    }

    private void copyBoard(int[][] dest, int[][] src) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, src[i].length);
        }
    }

    public static void printSolution(EuclideanDistance state) {
        if (state == null) {
            return;
        }
        printSolution(state.getParent());
        System.out.println(state.toString());
    }

    private int getDepth() {
        return depth;
    }

    private EuclideanDistance getParent() {
        return parent;
    }

    public static void solveEuclideanDistance(int[][] start) {
        // Create the initial state and add it to the priority queue
        EuclideanDistance initialState = new EuclideanDistance(start, 0);
        PriorityQueue<EuclideanDistance> queue = new PriorityQueue<>();
        queue.add(initialState);

        // Initialize counters
        int nodesExpanded = 0;
        int maxQueueSize = 0;

        // Create a set to keep track of visited states
        Set<EuclideanDistance> visited = new HashSet<>();
        visited.add(initialState);

        while (!queue.isEmpty()) {
            // Get the state with the lowest cost
            EuclideanDistance current = queue.poll();
            nodesExpanded++;

            // Check if the current state is the goal state
            if (current.isGoal()) {
                // Print the solution path
                printSolution(current);
                System.out.println("Total nodes expanded: " + nodesExpanded);
                System.out.println("Max queue size: " + maxQueueSize);
                System.out.println("Depth of goal node: " + current.getDepth());
                return;
            }

            // Generate the next possible states
            List<EuclideanDistance> nextStates = current.getNextStates();
            for (EuclideanDistance state : nextStates) {
                // Check if the state has already been visited
                if (!visited.contains(state)) {
                    // Calculate the cost of the state
                    state.cost = current.cost + 1;
                    state.parent = current;

                    // Add the state to the priority queue and visited set
                    queue.add(state);
                    maxQueueSize = Math.max(maxQueueSize, queue.size());
                    visited.add(state);
                }
            }
        }

        // If the priority queue is empty and a solution has not been found, print an
        // error message
        System.out.println("Total nodes expanded: " + nodesExpanded);
        System.out.println("Max queue size: " + maxQueueSize);
        System.out.println("Error: no solution found");
    }

}
