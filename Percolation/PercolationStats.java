public class PercolationStats {

    private final int size;
    private final int numberOfComputations;

    private final double[] fractions;

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        size = N;
        numberOfComputations = T;
        fractions = new double[numberOfComputations];

        runSimulation();
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    private double[] simulation() {

        Percolation percolation = new Percolation(size);
        double[] results = new double[2];

        do {
            int i = StdRandom.uniform(1, size + 1);
            int j = StdRandom.uniform(1, size + 1);
            percolation.open(i, j);
        } while (!percolation.percolates());

        int countOfOpenSites = 0;
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (percolation.isOpen(i, j)) {
                    countOfOpenSites++;
                }
            }
        }

        // 0 : count of open sites
        // 1 : fraction of open sites
        results[0] = countOfOpenSites;
        results[1] = (double) countOfOpenSites / (double) (size * size);

        return results;
    }

    private void runSimulation() {
        System.out.println("Starting simulation with N = " + size + " and T = "
                + numberOfComputations + ".");

        for (int time = 0; time < numberOfComputations; time++) {
            double[] results = simulation();
            fractions[time] = results[1];
        }

        this.mean = StdStats.mean(fractions);
        this.stddev = StdStats.stddev(fractions);
        this.confidenceLo = this.mean
                - (1.96 * (this.stddev / Math.sqrt(numberOfComputations)));
        this.confidenceHi = this.mean
                + (1.96 * (this.stddev / Math.sqrt(numberOfComputations)));

        System.out.println("End of simulation.");
    }

    // test client, described below
    public static void main(String[] args) {

        PercolationStats stats = new PercolationStats(Integer.parseInt("200"),
                Integer.parseInt("100"));

        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println("95% confidence interval = " + stats.confidenceLo()
                + ", " + stats.confidenceHi());
    }

}