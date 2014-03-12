public class Percolation {

    private final int size;
    private final boolean[] open;

    // 0 and N*N + 1 are for virtual roots
    private final WeightedQuickUnionUF unionFind;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {

        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        size = N;
        open = new boolean[N * N + 2];
        unionFind = new WeightedQuickUnionUF(N * N + 2);

    }

    private void checkBounds(int i, int j) {

        if (i < 1 || i > size) {
            throw new IndexOutOfBoundsException();
        }

        if (j < 1 || j > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int convertLocationToIndex(int i, int j) {
        return (i - 1) * size + j;
    }

    private int[] convertIndexToLocation(int index) {
        int i = (int) ((float) index / (float) size + 0.9999999999);
        int j = -(size * i) + size + index;
        return new int[] { i, j };
    }

    private int[] neighboorsOf(int i, int j) {
        int[] neighboors = new int[] { -1, -1, -1, -1 };

        // upper neighboor
        if (i != 1) {
            neighboors[0] = convertLocationToIndex(i - 1, j);
        }

        // right neighboor
        if (j != size) {
            neighboors[1] = convertLocationToIndex(i, j + 1);
        }

        // bottom neighboor
        if (i != size) {
            neighboors[2] = convertLocationToIndex(i + 1, j);
        }

        // left neighboor
        if (j != 1) {
            neighboors[3] = convertLocationToIndex(i, j - 1);
        }

        return neighboors;
    }

    private void unionWithOpenNeighboors(int i, int j) {
        int[] neighboors = neighboorsOf(i, j);

        for (int neighboor = 0; neighboor < 4; neighboor++) {
            int[] locationOfNeighboor = convertIndexToLocation(neighboors[neighboor]);
            if (neighboors[neighboor] != -1
                    && isOpen(locationOfNeighboor[0], locationOfNeighboor[1])) {
                unionFind.union(convertLocationToIndex(i, j),
                        neighboors[neighboor]);
            }
        }
    }

    @SuppressWarnings("unused")
	private boolean isConnectedToRoot(int i, int j) {
        return unionFind.connected(0, convertLocationToIndex(i, j));
    }

    private void connectToRoot(int i, int j) {
        unionFind.union(0, convertLocationToIndex(i, j));
    }

    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        checkBounds(i, j);
        if (!isOpen(i, j)) {

            // we union the upper
            // virtual root with the first line
            if (i == 1)
                connectToRoot(i, j);

            // we union the bottom
            // virtual root with the last line
            else if (i == size) {
                    unionFind.union(size * size + 1, convertLocationToIndex(i, j));
            }

            open[convertLocationToIndex(i, j)] = true;
            unionWithOpenNeighboors(i, j);
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        checkBounds(i, j);
        return open[convertLocationToIndex(i, j)];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        checkBounds(i, j);
        return unionFind.connected(0, convertLocationToIndex(i, j));
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFind.connected(0, size * size + 1);
    }

}