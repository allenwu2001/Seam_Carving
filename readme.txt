/* *****************************************************************************
 *  Name: Katie Kolodner
 *  NetID: katielk
 *  Precept: P04
 *
 *  Partner Name:    Allen Wu
 *  Partner NetID:   allenwu
 *  Partner Precept: P03
 *
 *  Hours to complete assignment (optional):
 *
 **************************************************************************** */

Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */

 To find a vertical seam, we maintained energies in a local double array as
 well as distTo and edgeTo in local single arrays. We then initialized all
 energy and distTo values, specifically assigning the top row values. Then, we
 relaxed the edges using the topological order of the energies to calculate
 the shortest path possible starting from any vertex on the top row, looking
 at pixels that were diagonal from and below the current pixel. Finally, to
 determine the seam, we found the minimum distTo value from the bottom row
 and calculated the shortest path from the bottom to top row.


/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

 The seam-carving technique avoids removing high-energy pixels, so an image with
 a significant amount of non-high-energy among a few select spots of
 high energy would work well. An image that would not work well is an image
 with solely high-energy pixels, as the differences in the image would be very
 noticeable and thus would not preserve the content and structure of the
 original image.


/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
250                0.329          ---           ---
500                0.718          2.18          1.12
1000               1.337          1.86          0.90
2000               2.575          1.93          0.95
4000               5.497          2.13          1.09
8000               12.225         2.22          1.15


(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
250                0.403          ---           ---
500                0.711          1.76          0.82
1000               1.592          2.24          1.16
2000               2.723          1.71          0.77
4000               5.350          1.96          0.97
8000               12.254         2.39          1.26



/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~  2.4*10^-8 * W^1.3 * H^1.2
       _______________________________________

The formula for running time was determined by looking at the log ratios from
the runtime experimental data. In particular, the data where "W" was the
independent variable appeared to have a maximum log ratio value of 1.15 ≈ 1.2.
The data where "H" was the independent variable appeared to have a maximum log
ratio value of 1.26 ≈ 1.3. Using the data point (2000, 8000), the coefficient
was determined by dividing 12.225 seconds, the runtime, by (2000^1.15 *
8000^1.26).


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
 N/A

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */
 N/A

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
 N/A

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
 One partner (Katie) coded energy and findVerticalSeams while the other partner
 was present, working collaboratively. The other partner (Allen) coded
 removeVerticalSeams and the corresponding horizontal methods while the partner
 was present, also working collaboratively. When debugging, both partners
 tried to fix the code to figure out the problem. We thus followed the protocol
 as described on the assignment page.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
 N/A
