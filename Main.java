
import java.util.*;

public class Main {
    static int[][] gameState = new int[3][3];
    static int algoFlag = 0;

    private static boolean validateGraph() {
        int[] check = new int[9];
        int valid = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameState[i][j] >= 0 && gameState[i][j] <= 8) {
                    check[gameState[i][j]] = 1;
                } else {
                    return false;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            valid += check[i];
        }
        if (valid == 9) {
            return true;
        }
        return false;
    }

    private static void getInitState() {
        Scanner sc = new Scanner(System.in);
        boolean graphGood = false;
        System.out.println("Enter your puzzle, use a zero to represent the blank");
        // first line
        System.out.println("Enter the first row, use space or tabs between numbers: ");
        gameState[0][0] = sc.nextInt();
        gameState[0][1] = sc.nextInt();
        gameState[0][2] = sc.nextInt();
        // second line
        System.out.println("Enter the second row, use space or tabs between numbers: ");
        gameState[1][0] = sc.nextInt();
        gameState[1][1] = sc.nextInt();
        gameState[1][2] = sc.nextInt();
        // third line
        System.out.println("Enter the third row, use space or tabs between numbers: ");
        gameState[2][0] = sc.nextInt();
        gameState[2][1] = sc.nextInt();
        gameState[2][2] = sc.nextInt();
        // Validate input:
        System.out.println("This is the graph you inputted: ");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(gameState[i][j] + " ");
            }
            System.out.println();
        }
        graphGood = validateGraph();
        if (graphGood == false) {
            System.out.println(
                    "This is an invalid graph. All values must be between 0-9. 0 represents the blank square.");
            // reset graph
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    gameState[i][j] = 0;
                }
            }
            getInitState();
        }
    }

    private static void intro() {
        boolean decidePuzzle = false;
        int algoChoice = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println(
                "Welcome to [sbala025] 8 puzzle solver. Type 1 to use a default puzzle, or 2 to enter your own puzzle.");
        while (decidePuzzle == false) {
            int puzzleType = sc.nextInt();
            if (puzzleType == 1) {
                gameState[0][0] = 1;
                gameState[0][1] = 2;
                gameState[0][2] = 3;
                gameState[1][0] = 4;
                gameState[1][1] = 8;
                gameState[1][2] = 0;
                gameState[2][0] = 7;
                gameState[2][1] = 6;
                gameState[2][2] = 5;
                decidePuzzle = true;
            } else if (puzzleType == 2) {
                decidePuzzle = true;
                getInitState();
            } else {
                System.out.println("Please enter either 1 for a default puzzle or 2 to enter your own puzzle.");
            }
        }
        while (algoChoice == 0) {
            System.out.println(
                    "Enter your choice of algorithm (Either 1, 2, or 3) \n 1 - Uniform Cost Search \n 2 - A* with the Misplaced Tile Heuristic \n 3 - A* with the Euclidean Distance Heuristic");
            algoChoice = sc.nextInt();
            if (algoChoice < 1 || algoChoice > 3) {
                algoChoice = 0;
            } else {
                algoFlag = algoChoice;
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        // Introduction of game
        intro();

        // Output graph and algorithm choice then solve
        System.out.println("This is the graph: ");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(gameState[i][j] + " ");
            }
            System.out.println();
        }
        System.out.print("The algorithm you selected: ");
        if (algoFlag == 1) {
            System.out.println("Uniform Cost Search");
            UniformSearch obj = new UniformSearch(gameState);
            obj.solveUniformSearch(gameState);
        } else if (algoFlag == 2) {
            System.out.println("A* with the Misplaced Tile heuristic");
            MisplacedTile.solveMisplacedTile(gameState);
        } else {
            System.out.println("A* with the Misplaced Tile Distance Heuristic");
            EuclideanDistance.solveEuclideanDistance(gameState);
        }

        System.out.println("GOAL!!!\n \n");

    }
}