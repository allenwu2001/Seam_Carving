/* *****************************************************************************
 *  Name:    Katie Kolodner
 *  NetID:   katielk
 *  Precept: P04
 *
 *  Partner Name:    Allen Wu
 *  Partner NetID:   allenwu
 *  Partner Precept: P03
 *
 *  Description:  This algorithm uses seam-carving, a content-aware image
 *  resizing technique where the image is reduced in size by one pixel of
 *  height (or width) at a time. A vertical seam in an image is a path of
 *  pixels connected from the top to the bottom with one pixel in each row;
 *  a horizontal seam is a path of pixels connected from the left to the right
 *  with one pixel in each column. This program resizes a W-by-H image with
 *  the seam-carving technique, where the seam removed is the path with the
 *  minimum total energy.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class SeamCarver {
    private Picture picture;                    // picture
    private int width;                          // width
    private int height;                         // height

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Null argument");
        }
        width = picture.width();                // initialize width
        height = picture.height();              // initialize height
        this.picture = new Picture(picture);    // deep-copy picture
    }

    // current picture
    public Picture picture() {
        // deep-copy and return picture
        Picture picNew = new Picture(picture);
        return picNew;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // helper method to calculate energy of given pixel
    private double calculateEnergy(int prevX, int prevY, int nextX, int nextY) {
        // calculate first RGB
        int rgb0 = picture.getRGB(prevX, prevY);
        int r0 = (rgb0 >> 16) & 0xFF;
        int g0 = (rgb0 >> 8) & 0xFF;
        int b0 = (rgb0) & 0xFF;

        // calculate second RGB
        int rgb1 = picture.getRGB(nextX, nextY);
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1) & 0xFF;

        // return differences in RGB squared
        return (r1 - r0) * (r1 - r0) + (g1 - g0) * (g1 - g0) + (b1 - b0)
                * (b1 - b0);
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Out of range");
        }

        // previous and next rows for energy calculations
        int prevRow = (y + height - 1) % height;
        int nextRow = (y + height + 1) % height;

        // previous and next columns for energy calculations
        int prevCol = (x + width - 1) % width;
        int nextCol = (x + width + 1) % width;

        // calculate deltaX^2 and deltaY^2
        double deltaX = calculateEnergy(prevCol, y, nextCol, y);
        double deltaY = calculateEnergy(x, prevRow, x, nextRow);

        return Math.sqrt(deltaX + deltaY);
    }

    // transpose picture helper method
    private void transpose(Picture transposed) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                transposed.setRGB(j, i, picture.getRGB(i, j));
            }
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // redetermine width and height
        width = picture.width();
        height = picture.height();

        Picture transposed = new Picture(height, width);     // transposed
        Picture original = picture;                          // copy of original

        // transpose picture and find vertical seam
        transpose(transposed);
        picture = transposed;
        int[] seam = findVerticalSeam();

        // change picture back to original picture
        picture = original;
        width = picture.width();
        height = picture.height();
        return seam;
    }

    // helper method to calculate 1D equivalent of 2D row-col coordinates
    private int convert(int row, int col) {
        return (row * width) + col;
    }

    // helper method to relax edges
    private void relax(int index, int next, double[] distTo, int[] edgeTo,
                       double[][] energies) {
        if (next >= width * height) return;         // out of bounds
        // relax edges by updating distTo and edgeTo
        if (distTo[next] > distTo[index] + energies[next % width]
                [next / width]) {
            distTo[next] = distTo[index] + energies[next % width]
                    [next / width];
            edgeTo[next] = index;
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // redetermine width and height
        width = picture.width();
        height = picture.height();

        // energies, distTo, and edgeTo arrays for topological sort
        double[][] energies = new double[width][height];
        double[] distTo = new double[width * height];
        int[] edgeTo = new int[width * height];

        // calculate energies and initialize distTo to infinity
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                distTo[j * width + i] = Double.POSITIVE_INFINITY;
                energies[i][j] = energy(i, j);
            }
        }
        // initialize distTo for first row
        for (int i = 0; i < width; i++) {
            distTo[i] = energies[i][0];
        }
        // relax edges appropriate edges according to position using
        // inherent topological order
        for (int index = 0; index < width * height; index++) {
            if (index % width == 0) {
                relax(index, index + width, distTo, edgeTo, energies);
                relax(index, index + width + 1, distTo, edgeTo, energies);
            }
            else if (index % width == width - 1) {
                relax(index, index + width, distTo, edgeTo, energies);
                relax(index, index + width - 1, distTo, edgeTo, energies);
            }
            else {
                relax(index, index + width - 1, distTo, edgeTo, energies);
                relax(index, index + width, distTo, edgeTo, energies);
                relax(index, index + width + 1, distTo, edgeTo, energies);
            }
        }

        int[] seam = new int[height];                 // seam
        int index = convert(height - 1, 0);           // first index of last row
        double min = distTo[index];                   // distTo of index

        // determine shortest path index based on last row
        for (int i = 0; i < width; i++) {
            if (distTo[convert(height - 1, i)] < min) {
                min = distTo[convert(height - 1, i)];
                index = convert(height - 1, i);
            }
        }
        // calculate shortest path from last to top row
        for (int i = height - 1; i >= 0; i--) {
            seam[i] = index % width;
            index = edgeTo[index];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // redetermine width and height
        width = picture.width();
        height = picture.height();

        if (seam == null || height == 1 || seam.length != width) {
            throw new IllegalArgumentException("Arguments are invalid");
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("Invalid seam");
            }
        }

        Picture transposed = new Picture(height, width);      // transposed
        // transpose picture and find vertical seam
        transpose(transposed);
        picture = transposed;
        removeVerticalSeam(seam);

        Picture newTranspose = new Picture(height, width);   // re-transposed
        // re-transpose picture to new normal picture
        transpose(newTranspose);
        picture = newTranspose;
        width = picture.width();
        height = picture.height();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // redetermine width and height
        width = picture.width();
        height = picture.height();

        if (seam == null || width == 1 || seam.length != height) {
            throw new IllegalArgumentException("Arguments are invalid");
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("Invalid seam");
            }
        }

        Picture picNew = new Picture(width - 1, height);     // modified picture

        // shift pixels over by 1 depending on placement of seam in row
        for (int j = 0; j < height; j++) {
            for (int k = 0; k < seam[j]; k++) {
                picNew.setRGB(k, j, picture.getRGB(k, j));
            }
            for (int i = seam[j]; i < width - 1; i++) {
                picNew.setRGB(i, j, picture.getRGB(i + 1, j));
            }
        }
        // reassign picture to new picture
        picture = picNew;
        width = picNew.width();
        height = picNew.height();
    }

    //  unit testing (required)
    public static void main(String[] args) {
        if (args.length != 3) {
            StdOut.println(
                    "Usage:\njava ResizeDemo [image filename]"
                            + "[num columns to remove] [num rows to remove]");
            return;
        }

        // command-line arguments: picture, # columns removed, # rows removed
        Picture picture = new Picture(args[0]);
        int removeColumns = Integer.parseInt(args[1]);
        int removeRows = Integer.parseInt(args[2]);

        // print image dimensions and create picture
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        SeamCarver sc = new SeamCarver(picture);

        // print energies
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%7.2f ", sc.energy(col, row));
            StdOut.println();
        }

        Stopwatch sw = new Stopwatch();                 // stopwatch

        // remove horizontal seams
        for (int i = 0; i < removeRows; i++) {
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
        }

        // remove vertical seams
        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }

        // print new image size and time elapsed
        StdOut.printf("new image size is %d columns by %d rows\n",
                      sc.width(), sc.height());
        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");

        // show images
        picture.show();
        sc.picture().show();
    }
}
