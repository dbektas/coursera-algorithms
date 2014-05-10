public class Solver {
    private SearchNode result;
    
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous_node;
        private final int priority;
        
        private SearchNode(Board b, SearchNode p) {
            board = b;
            previous_node = p;
            if (previous_node == null) {
                moves = 0;
            } else {
                moves = previous_node.moves + 1;
            }
            priority = board.manhattan() + moves;
        }
        
        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twin_pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, null));
        twin_pq.insert(new SearchNode(initial.twin(), null));
        while (true) {
            SearchNode lowest_priority_node = pq.delMin();
            SearchNode twin_lowest_priority_node = twin_pq.delMin();
            if (lowest_priority_node.board.isGoal()) {
                result = lowest_priority_node;
                break;
            }
            if (twin_lowest_priority_node.board.isGoal()) {
                result = null;
                break;
            }
            for (Board neighbor: lowest_priority_node.board.neighbors()) {
                // critical optimization
                if (lowest_priority_node.previous_node == null || !neighbor.equals(lowest_priority_node.previous_node.board)) {
                    pq.insert(new SearchNode(neighbor, lowest_priority_node));
                }
            }
            for (Board neighbor: twin_lowest_priority_node.board.neighbors()) {
                // critical optimization
                if (twin_lowest_priority_node.previous_node == null || !neighbor.equals(twin_lowest_priority_node.previous_node.board)) {
                    twin_pq.insert(new SearchNode(neighbor, twin_lowest_priority_node));
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return result != null;
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        if (result != null) {
            return result.moves;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        if (result == null) {
            return null;
        }
        Stack<Board> s = new Stack<Board>();
        SearchNode n = result;
        while (n != null) {
            s.push(n.board);
            n = n.previous_node;
        }
        return s;
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}