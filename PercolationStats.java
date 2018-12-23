
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double []prob;
    private final int nTrials;
    private final int n;
    private final int nSites;
    private final double CONFIDENCE_95;

    public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if (!(n >= 1 && trials >= 1))
        {
            throw new IllegalArgumentException();
        }

        this.n = n;
        nSites = n*n;
        nTrials = trials;

        CONFIDENCE_95 = 1.96;
        prob = new double[nTrials];

        getProb();
    }

    private int getRow(int i)
    {
        return (i / n) + 1;
    }

    private int getCol(int i)
    {
        return (i % n) + 1;
    }

    private void getProb()
    {
        for (int t = 0; t < nTrials; t++)
        {
            Percolation p = new Percolation(n);

            int []permutation = StdRandom.permutation(nSites);

            int i = 0;

            while (!p.percolates())
            {
                int index = permutation[i++];

                p.open(getRow(index), getCol(index));
            }

            prob[t] = p.numberOfOpenSites()*1.0 / nSites;
        }
    }

    public double mean()                          // sample mean of percolation threshold
    {
        return StdStats.mean(prob);
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(prob);
    }

    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return (mean() - CONFIDENCE_95*stddev()/Math.sqrt(nTrials));
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return (mean() + CONFIDENCE_95*stddev()/Math.sqrt(nTrials));
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int testCase = Integer.parseInt(args[1]);
        PercolationStats tester = new PercolationStats(n, testCase);
        System.out.format("mean                    = %f\n", tester.mean());
        System.out.format("stddev                  = %f\n", tester.stddev());
        System.out.format("95%% confidence interval = [%f, %f]\n", tester.confidenceLo(), tester.confidenceHi());
    }
}
