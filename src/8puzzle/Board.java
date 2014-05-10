public class Board {
    
    private final int[][] blocks;
    private final int dimension;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = blocks;
        this.dimension = blocks.length;
    }
                                           
    // board dimension N
    public int dimension() {
        return blocks.length;
    }
    
    // number of blocks out of place
    public int hamming() {
        int blocks_out_of_place = 0;
        int expectation = 1;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (blocks[row][col] != expectation) {
                    blocks_out_of_place++;
                }
                expectation++;
            }
        }
        // subtract 1 for empty block
        return blocks_out_of_place - 1;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        // loop through each block
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int block_value = blocks[row][col];
                if (block_value == 0) {
                    continue;
                }
                int goal_row = (block_value - 1) / dimension;
                int goal_col = (block_value - 1) % dimension;
                int row_distance = Math.abs(goal_row - row);
                int col_distance = Math.abs(goal_col - col);
                sum = sum + row_distance + col_distance;
            }
        }
        return sum;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        int expected_block_value = 1;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (blocks[row][col] != expected_block_value
                   && (row != dimension - 1 || col != dimension - 1)) {
                    return false;
                }
                expected_block_value++;
            }
        }
        return true;
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[][] copy = copySquareArray(blocks);
        int value;
        int next_value;
        outerloop:
        for (int row = 0; row < dimension; row++) {
            // don't iterate the last column
            for (int col = 0; col < dimension - 1; col++) {
                value = copy[row][col];
                next_value = copy[row][col+1];
                // swap if both are not zero
                if (value != 0 && next_value != 0) {
                    copy[row][col] = next_value;
                    copy[row][col+1] = value;
                    break outerloop;
                }
            }
        }
        Board twin = new Board(copy);        
        return twin;
    }
    
    // copy a square array
    private int[][] copySquareArray(int[][] original) {
        int len = original.length;
        int[][] copy = new int[len][len];
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++)
                copy[row][col] = original[row][col];
        }
        return copy;
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass());
        Board that = (Board) y;
        if (this.blocks.length != that.blocks.length) return false;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (this.blocks[row][col] != that.blocks[row][col]) {
                    return false;
                }   
            }
        }
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        // find zero
        int row = 0;
        int col = 0;
        outerloop:
        for (row = 0; row < dimension; row++) {
            for (col = 0; col < dimension; col++) {
                if (blocks[row][col] == 0) {
                    break outerloop;
                }
            }
        }
        if (row > 0) {
            // swap 0 with top tile
            Board neighbor = swap(row, col, row - 1, col);
            q.enqueue(neighbor);
        }
        if (col > 0) {
            // swap 0 with left tile
            Board neighbor = swap(row, col, row, col - 1);
            q.enqueue(neighbor);            
        }
        if (row < dimension - 1) {
            // swap 0 with bottom tile
            Board neighbor = swap(row, col, row + 1, col);
            q.enqueue(neighbor);            
        }
        if (col < dimension - 1) {
            // swap 0 with right tile
            Board neighbor = swap(row, col, row, col + 1);
            q.enqueue(neighbor);            
        }
        return q;
    }
    
    private Board swap(int old_zero_row, int old_zero_col, int new_zero_row, int new_zero_col) {
        int[][] copy = copySquareArray(blocks);
        copy[old_zero_row][old_zero_col] = blocks[new_zero_row][new_zero_col];
        copy[new_zero_row][new_zero_col] = 0;
        Board neighbor = new Board(copy);
        return neighbor;
    }
    
    // string representation of the board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}