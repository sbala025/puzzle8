import java.util.*;

public class MisplacedTile implements Comparable<MisplacedTile> {
    private int[][] board;
    private int blankRow;
    private int blankColumn;
    private int cost;
    private MisplacedTile parent;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static final int[][] GOAL_STATE = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };

    private static final int[][][] MOVES = {
            { { 1, 0 }, { 0, 1 } }, // Down and right
            { { 1, 0 }, { 0, -1 } }, // Down and left
            { { -1, 0 }, { 0, -1 } }, // Up and left
            { { -1, 0 }, { 0, 1 } } // Up and right
    };

    public MisplacedTile(int[][] initial) {
        board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = initial[i][j];
                if (board[i][j] == 0) {
                    blankRow = i;
                    blankColumn = j;
                }
            }
        }
        cost = calculateMisplacedTileCost();
    }

    private int calculateMisplacedTileCost() {
        int cost = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != GOAL_STATE[i][j] && board[i][j] != 0) {
                    cost++;
                }
            }
        }
        return cost;
    }

    public boolean isGoal() {
        return cost == 0;
    }

    public List<MisplacedTile> getNextStates() {
        List<MisplacedTile> nextStates = new ArrayList<>();

        // Loop through all possible moves
        for (int[][] move : MOVES) {
            // Calculate the new row and column of the blank space
            int nRow = blankRow + move[0][0];
            int nColumn = blankColumn + move[0][1];

            // Check if the new row and column are within the bounds of the board
            if (nRow >= 0 && nRow < 3 && nColumn >= 0 && nColumn < 3) {
                // Create a new state by swapping the blank space with the adjacent tile
                int[][] newState = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newState[i][j] = board[i][j];
                    }
                }
                newState[blankRow][blankColumn] = newState[nRow][nColumn];
                newState[nRow][nColumn] = 0;

                // Add the new state to the list of next states
                MisplacedTile nextState = new MisplacedTile(newState);
                nextStates.add(nextState);
            }

            // Repeat the same process for the second possible move
            nRow = blankRow + move[1][0];
            nColumn = blankColumn + move[1][1];
            if (nRow >= 0 && nRow < 3 && nColumn >= 0 && nColumn < 3) {
                int[][] newState = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        newState[i][j] = board[i][j];
                    }
                }
                newState[blankRow][blankColumn] = newState[nRow][nColumn];
                newState[nRow][nColumn] = 0;
                MisplacedTile nextState = new MisplacedTile(newState);
                nextStates.add(nextState);
            }
        }

        return nextStates;
    }

    public static void solveMisplacedTile(int[][] start) {
        // Create the initial state
        MisplacedTile initialState = new MisplacedTile(start);

        // Create the priority queue for A* search
        PriorityQueue<MisplacedTile> queue = new PriorityQueue<>(Comparator.comparingInt(state -> state.cost));
        queue.add(initialState);

        // Create a set to keep track of visited states
        Set<MisplacedTile> visited = new HashSet<>();
        visited.add(initialState);

        // Initialize the steps counter, expanded, maxqueue, and depth
        int expandedNodes = 0;
        int maxQueueSize = 1;
        int depth = 0;
        int steps = 0;

        // Perform A* search
        while (!queue.isEmpty()) {
            // Get the state with the lowest cost
            MisplacedTile currentState = queue.poll();
            expandedNodes++;
            // Check if the current state is the goal state
            if (currentState.isGoal()) {
                // Print the steps to solve the puzzle
                while (currentState != null) {
                    System.out.println(currentState.toString());
                    currentState = currentState.parent;
                    steps++;
                }
                System.out.println("Solution found in " + (steps - 1) + " steps.");

                // Check if the puzzle is solvable or not
                // if (steps % 2 == 0) {
                    System.out.println("Total expanded nodes: " + expandedNodes);
                    System.out.println("Max queue size: " + maxQueueSize);
                    System.out.println("Depth of goal node: " + depth);
                // } else {
                //     System.out.println("Total expanded nodes: " + expandedNodes);
                //     System.out.println("Max queue size: " + maxQueueSize);
                //     System.out.println("No solution found.");
                // }

                return;
            }

            // Get the next states and add them to the queue if they haven't been visited
            // yet
            for (MisplacedTile nextState : currentState.getNextStates()) {
                if (!visited.contains(nextState)) {
                    nextState.parent = currentState;
                    queue.add(nextState);
                    visited.add(nextState);
                }

                // Update the max queue size if necessary
                int queueSize = queue.size();
                if (queueSize > maxQueueSize) {
                    maxQueueSize = queueSize;
                }

                // Update the depth of the goal node
                int nextDepth = nextState.getDepth();
                if (nextDepth > depth) {
                    depth = nextDepth;
                }
            }
        }

        // If the queue is empty and we haven't found the goal state, the puzzle is not
        // solvable
        System.out.println("Total expanded nodes: " + expandedNodes);
        System.out.println("Max queue size: " + maxQueueSize);
        System.out.println("The puzzle is not solvable.");
    }

    private int getDepth() {
        int depth = 0;
        MisplacedTile current = this;
        while (current.parent != null) {
            depth++;
            current = current.parent;
        }
        return depth;
    }

    @Override
    public int compareTo(MisplacedTile o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
}
