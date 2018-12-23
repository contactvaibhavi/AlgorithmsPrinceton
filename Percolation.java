import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private int open;
    private final int start;
    private final int end;

    private boolean []blocked;
    private boolean []full;

    private final WeightedQuickUnionUF uf;

    public Percolation(int n)        // create n-by-n grid, with all sites blocked
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException();
        }

        blocked = new boolean[n*n + 2];
        full = new boolean[n*n +2];

        open = 0;
        this.n = n;

        start = 0;
        end = n*n+1;

        uf = new WeightedQuickUnionUF(n*n+2);

        blocked[start] = false;
        blocked[end] = false;

        for (int i = 1; i <= n*n; i++)
            blocked[i] = true;
    }

    private boolean isValid(int row, int col)
    {
        return (row == 0 && col == 1) || (row == n+1 && col == 1) || (row >= 1 && row <= n && col >= 1 && col <= n);
    }

    private int getIndex(int row, int col)
    {
        if (!isValid(row, col))
        {
            throw new IndexOutOfBoundsException();
        }

        if (row == 0)
            return 0;

        if (row == n+1)
            return n*n+1;

        return (row - 1) * n + col;
    }

    private void connect(int row, int col, int i)
    {
        if (isValid(row, col) && isOpen(row, col))
            uf.union(i, getIndex(row, col));
    }

    private void receive(int row, int i)
    {
        if (row == 1)
        {
            uf.union(start, i);
            full[i] = true;
        }
    }

    private void send(int row, int i)
    {
        if (row == n)
        {
            uf.union(i, end);
            full[end] = full[i];
        }
    }

    public void open(int row, int col)    // open site (row, col) if it is not open already
    {
        int i = getIndex(row, col);

        if (!isOpen(row, col))
        {
            blocked[i] = false;
            open++;
        }

        connect(row-1, col, i);
        connect(row+1, col, i);
        connect(row, col-1, i);
        connect(row, col+1, i);

        receive(row, i);
        send(row, i);
    }

    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        return !blocked[getIndex(row, col)];
    }

    public boolean isFull(int row, int col)  // is site (row, col) full?
    {
        int i = getIndex(row, col);

        full[i] = full[i] || uf.connected(0, i);

        return full[i];
    }

    public int numberOfOpenSites()       // number of open sites
    {
        return open;
    }

    public boolean percolates()              // does the system percolate?
    {
        return isFull(n+1, 1);
    }

    // public static void main(String[] args)
    // {
    //     try
    //     {
    //         File file  = new File("input50.txt");
    //         Scanner scanner = new Scanner(file);
    //
    //         int n = Integer.parseInt(scanner.nextLine());
    //
    //         System.out.println(n);
    //         Percolation p = new Percolation(n);
    //
    //         while (scanner.hasNextLine())
    //         {
    //             String []arg = new String[];
    //             arg = scanner.nextLine().split(" ");
    //
    //             System.out.println(arg);
    //
    //             int row = Integer.parseInt(arg[0]);
    //             int col = Integer.parseInt(arg[1]);
    //
    //             p.open(row, col);
    //         }
    //
    //
    //        System.out.println((p.isOpen(1, 1)));
    //        System.out.println((p.isFull(n+1, 1)));
    //        System.out.println((p.percolates()));
    //     }
    //     catch (java.io.IOException e)
    //     {
    //         e.printStackTrace();
    //     }
    // }
}
