import java.util.*;

public class UniformSearch {
    private int[][] board;

    public int[][] getBoard() {
        return board;
    }

    public UniformSearch(int[][] board) {
        this.board = board;
    }

    // Print the board after each step
    public void printBoard(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Solve by getting the next possible states of the board
    public List<UniformSearch> getNextStates() {
        List<UniformSearch> nextStates = new ArrayList<>();
        int zeroRow = 0, zeroCol = 0;

        // Find the row and column of the zero tile (empty space) in the board
        // Need this to know where it is possible for a number to move
        // Each possiviple movement (to right/left/up/down) will be checked
        // independently
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
        // Depending on where the blank space is we can decide which next movement is
        // possible
        // If there's a possible move to the top, add the new state to the nextStates
        // list
        if (zeroRow > 0) {
            int[][] newState = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newState[i][j] = board[i][j];
                }
            }
            newState[zeroRow][zeroCol] = newState[zeroRow - 1][zeroCol];
            newState[zeroRow - 1][zeroCol] = 0;
            nextStates.add(new UniformSearch(newState));
        }
        // If there's a possible move to the bottom, add the new state to the nextStates
        // list
        if (zeroRow < 2) {
            int[][] newState = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newState[i][j] = board[i][j];
                }
            }
            newState[zeroRow][zeroCol] = newState[zeroRow + 1][zeroCol];
            newState[zeroRow + 1][zeroCol] = 0;
            nextStates.add(new UniformSearch(newState));
        }
        // If there's a possible move to the left, add the new state to the nextStates
        // list
        if (zeroCol > 0) {
            int[][] newState = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newState[i][j] = board[i][j];
                }
            }
            newState[zeroRow][zeroCol] = newState[zeroRow][zeroCol - 1];
            newState[zeroRow][zeroCol - 1] = 0;
            nextStates.add(new UniformSearch(newState));
        }
        // If there's a possible move to the right, add the new state to the nextStates
        // list
        if (zeroCol < 2) {
            int[][] newState = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newState[i][j] = board[i][j];
                }
            }
            newState[zeroRow][zeroCol] = newState[zeroRow][zeroCol + 1];
            newState[zeroRow][zeroCol + 1] = 0;
            nextStates.add(new UniformSearch(newState));
        }
        return nextStates;
    }

    // This compares against what the goal function is
    public boolean isGoal() {
        int[][] goal = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != goal[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void solveUniformSearch(int[][] initial) {
        board = new int[3][3];
        // Copy the initial state of the puzzle to the board variable
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = initial[i][j];
            }
        }

        // Create a priority queue to hold the nodes in the search frontier
        PriorityQueue<NodeUS> frontier = new PriorityQueue<>();
        Set<String> explored = new HashSet<>();
        // Initialize counters
        int nodesExpanded = 0;
        int maxQueueSize = 0;
        // Create the start node with the initial state of the puzzle
        NodeUS start = new NodeUS(this, null, 0, 0, "");
        frontier.offer(start);

        // Loop until the search frontier is empty
        while (!frontier.isEmpty()) {
            // Need the node with the lowest cost from the search frontier and the current
            // node
            NodeUS currentNode = frontier.poll();
            nodesExpanded++;
            UniformSearch currentState = currentNode.getState();

            // If the current state is the goal state, print the solution path and exit
            if (currentState.isGoal()) {
                List<UniformSearch> path = new ArrayList<>();
                NodeUS node = currentNode;
                while (node.getParent() != null) {
                    path.add(0, node.getState());
                    node = node.getParent();
                }
                path.add(0, node.getState());
                System.out.println("Solution found in " + currentNode.getCost() + " steps.");
                for (UniformSearch state : path) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            System.out.print(state.getBoard()[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
                System.out.println("Total nodes expanded: " + nodesExpanded);
                System.out.println("Max queue size: " + maxQueueSize);
                System.out.println("Depth of goal node: " + currentNode.getDepth());
                return;
            }
            // Add the current state to the set of explored states
            explored.add(currentState.toString());
            for (UniformSearch nextState : currentState.getNextStates()) {
                // If the next state has not been explored, add it to the search frontier
                if (!explored.contains(nextState.toString())) {
                    int newCost = currentNode.getCost() + 1;
                    int newDepth = currentNode.getDepth() + 1;
                    NodeUS nextNode = new NodeUS(nextState, currentNode, newCost, newDepth);
                    frontier.offer(nextNode);
                    maxQueueSize = Math.max(maxQueueSize, frontier.size());
                }
            }
        }
        System.out.println("Total nodes expanded: " + nodesExpanded);
        System.out.println("Max queue size: " + maxQueueSize);
        System.out.println("No solution found.");
    }
}
