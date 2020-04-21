import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

    /**
     * Calls the parent class constructor.
     * 
     * @see Searcher
     * @param maze initial maze.
     */
    public BreadthFirstSearcher(Maze maze) {
        super(maze);
    }

    /**
     * Main breadth first search algorithm.
     * 
     * @return true if the search finds a solution, false otherwise.
     */
    @Override
    public boolean search() {
        // explored list is a 2D Boolean array that indicates if a state associated with
        // a given position in the maze has already been explored.
        boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

        State start = new State(maze.getPlayerSquare(), null, 0, 0);

        // Queue implementing the Frontier list
        LinkedList<State> queue = new LinkedList<State>();
        queue.add(start);

        while (!queue.isEmpty()) {
            State currentState = queue.pop();
            explored[currentState.getX()][currentState.getY()] = true;

            // update Searcher information
            this.noOfNodesExpanded++;
            if (this.maxDepthSearched < currentState.getDepth()) {
                this.maxDepthSearched = currentState.getDepth();
            }
            this.cost = currentState.getGValue();

            // check if goal state -- update maze if goal
            if (currentState.isGoal(this.maze)) {
                State parent = currentState.getParent();
                // create path from start to goal
                while (parent.getGValue() != 0) {
                    this.maze.setOneSquare(parent.getSquare(), '.');
                    parent = parent.getParent();
                }
                return true;
            }

            // iterate through successors
            ArrayList<State> successors = currentState.getSuccessors(explored, maze);
            for (State successor : successors) {
                // add successor to frontier if it is not already in there
                if (noDuplicates(successor, queue) == false) {
                    queue.add(successor);
                }
            }

            if (this.maxSizeOfFrontier < queue.size()) {
                this.maxSizeOfFrontier = queue.size();
            }
        }

        return false;
    }

    /**
     * Private method to check if a state already exists in the frontier
     * 
     * @param State
     * @param Queue -- frontier
     * 
     * @return true if there are no duplicates, false if otherwise.
     */
    private boolean noDuplicates(State successor, LinkedList<State> queue) {
        for (State state : queue) {
            if (successor.getX() == state.getX() && successor.getY() == state.getY()) {
                return true;
            }
        }
        return false;
    }
}
