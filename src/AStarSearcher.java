import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

    /**
     * Calls the parent class constructor.
     * 
     * @see Searcher
     * @param maze initial maze.
     */
    public AStarSearcher(Maze maze) {
        super(maze);
    }

    /**
     * Main a-star search algorithm.
     * 
     * @return true if the search finds a solution, false otherwise.
     */
    @Override
    public boolean search() {
        // explored list is a Boolean array that indicates if a state associated with a
        // given position in the maze has already been explored.
        boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

        State start = new State(maze.getPlayerSquare(), null, 0, 0);

        // find f(n) = g(n) + h(n) for start node
        double hValue = getHValue(start);
        double fValue = start.getGValue() + hValue;

        // add start to frontier
        PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();
        frontier.add(new StateFValuePair(start, fValue));

        while (!frontier.isEmpty()) {
            StateFValuePair currentStateValPair = frontier.poll();
            explored[currentStateValPair.getState().getX()][currentStateValPair.getState().getY()] = true;

            // update Searcher information
            this.noOfNodesExpanded++;
            this.cost = currentStateValPair.getState().getGValue();
            if (this.maxDepthSearched < currentStateValPair.getState().getDepth()) {
                this.maxDepthSearched = currentStateValPair.getState().getDepth();
            }

            // check if goal state -- update maze if goal
            if (currentStateValPair.getState().isGoal(this.maze)) {
                State parent = currentStateValPair.getState().getParent();
                // create path from start to goal
                while (parent.getGValue() != 0) {
                    this.maze.setOneSquare(parent.getSquare(), '.');
                    parent = parent.getParent();
                }
                return true;
            }

            // iterate through successors
            ArrayList<State> successors = currentStateValPair.getState().getSuccessors(explored, maze);
            for (State successor : successors) {
                // duplicate and new g value is better -- add to frontier
                if (noDuplicates(successor, frontier) == 0) {
                    double fVal = successor.getGValue() + getHValue(successor);
                    frontier.add(new StateFValuePair(successor, fVal));
                }
                // no duplicate -- add to frontier
                else if (noDuplicates(successor, frontier) == -1) {
                    double fVal = successor.getGValue() + getHValue(successor);
                    frontier.add(new StateFValuePair(successor, fVal));
                }
                // duplicate and new value is the same or worse -- throw away
                else {
                    continue;
                }
                if (this.maxSizeOfFrontier < frontier.size()) {
                    this.maxSizeOfFrontier = frontier.size();
                }

            }
        }
        return false;
    }

    /**
     * Private method to find the H value
     * 
     * @param State
     * 
     * @return double -- h value
     */
    private double getHValue(State currentState) {
        // coordinates of goal state
        int P = this.maze.getGoalSquare().X;
        int Q = this.maze.getGoalSquare().Y;

        int U = currentState.getX();
        int V = currentState.getY();

        double UP = U - P;
        double UP2 = UP * UP;

        double VQ = V - Q;
        double VQ2 = VQ * VQ;

        double add = UP2 + VQ2;

        double finalH = Math.sqrt(add);

        return finalH;
    }

    /**
     * Private method to check if a state already exists in the frontier
     * 
     * @param State
     * @param Queue -- frontier
     * 
     * @return true if there are no duplicates, false if otherwise.
     */
    private int noDuplicates(State successor, PriorityQueue<StateFValuePair> frontier) {
        for (StateFValuePair stateValuePair : frontier) {

            // DUPLICATE EXISTS AND G VALUE OF NEW NODE IS GREATER THAN OR EQUAL TO EXISTING
            if (successor.getGValue() >= stateValuePair.getState().getGValue()
                    && successor.getX() == stateValuePair.getState().getX()
                    && successor.getY() == stateValuePair.getState().getY()) {
                return 1;
            }
            // DUPLICATE EXISTS AND G VALUE IS LESS THAN EXISITING NODE
            else if (successor.getGValue() < stateValuePair.getState().getGValue()
                    && successor.getX() == stateValuePair.getState().getX()
                    && successor.getY() == stateValuePair.getState().getY()) {

                // remove existing node from the frontier
                frontier.remove(stateValuePair);
                return 0;
            }
        }
        // DUPLICATE DOES NOT EXIST
        return -1;
    }

}
