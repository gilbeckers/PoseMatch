package com.gil.posematch;

/**
 * Created by Gil on 30/11/2017.
 */

// https://gist.github.com/dweidele/83e4203141da2f2c8733

//import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This computes registration parameters for an optimal affine transformation
 * between 2 matched point sets towards the least squared error.
 * <p>
 * The implementation is based on <br />
 * <i> Chang, Shih-Hsu, et al.
 * "Fast algorithm for point pattern matching: invariant to translations, rotations and scale changes."
 * Pattern recognition 30.2 (1997): 311-320.</i>
 * </p>
 *
 * @author Daniel Weidele (daniel.weidele@uni-konstanz.de)
 * @version 14.11.2014
 */
public class AffineTransformation
{
    /*
    public static void main(String[] args)
    {
        List<Point2D> A = Arrays.asList(new Point2D(1, 1), new Point2D(5, 7));
        List<Point2D> B = Arrays.asList(new Point2D(8, 11), new Point2D(8, 9));

        System.out.println(e(t(A, r(A, B)), B) < 0.000001d ? "SUCCESS" : "FAIL");
    }
    */

    /**
     * Computes the {@link Registration} parameters of an affine transformation
     * that leads to least squared error between A and B. The lists of points
     * must be equal in length. Points are matched according to positions in
     * their respective lists.
     *
     * @param A
     *            list of points to match to B
     * @param B
     *            list of points to matched by A
     * @return {@link Registration} parameters of the transformation that leads
     *         to least squared error between A and B
     */
    public static Registration r(List<Point2D> A, List<Point2D> B)
    {
        int k = A.size();
        double m_xa = 0, m_ya = 0, m_xb = 0, m_yb = 0, lAPlusB = 0, lAMinusB = 0, lA = 0;
        for (int i = 0; i < k; i++)
        {
            m_xa += A.get(i).x;
            m_ya += A.get(i).y;
            m_xb += B.get(i).x;
            m_yb += B.get(i).y;
            lAPlusB += A.get(i).x * B.get(i).x + A.get(i).y * B.get(i).y;
            lAMinusB += A.get(i).x * B.get(i).y - A.get(i).y * B.get(i).x;
            lA += Math.pow(A.get(i).x, 2) + Math.pow(A.get(i).y, 2);
        }
        double det = k * lA - Math.pow(m_xa, 2) - Math.pow(m_ya, 2);
        Registration r = new Registration();
        r.tx = (lA * m_xb - m_xa * lAPlusB + m_ya * lAMinusB) / det;
        r.ty = (lA * m_yb - m_ya * lAPlusB - m_xa * lAMinusB) / det;
        r.sct = (-m_xa * m_xb - m_ya * m_yb + k * lAPlusB) / det;
        r.sst = (m_ya * m_xb - m_xa * m_yb + k * lAMinusB) / det;
        return r;
    }

    /**
     * Applies the affine transformation based on the {@link Registration}
     * parameters.
     *
     * @param A
     *            the list of points to be transformed
     * @param r
     *            the {@link Registration} parameters
     * @return a copy of transformed points A
     */
    public static List<Point2D> t(List<Point2D> A, Registration r)
    {
        List<Point2D> T = new ArrayList<Point2D>();
        for (Point2D a : A)
        {
            Point2D t = new Point2D();
            t.x = r.tx + a.x * r.sct - a.y * r.sst;
            t.y = r.ty + a.y * r.sct + a.x * r.sst;
            T.add(t);
        }
        return T;
    }

    /**
     * Computes the least squared error between point sets A and B. The lists
     * must have equal length.
     *
     * @param A
     *            list of points
     * @param B
     *            list of points
     * @return the least squared error between A and B
     */
    public static double e(List<Point2D> A, List<Point2D> B)
    {
        int k = A.size();
        double e = 0;
        for (int i = 0; i < k; i++)
        {
            e += Math.sqrt(Math.pow(A.get(i).x - B.get(i).x, 2) + Math.pow(A.get(i).y - B.get(i).y, 2));
        }
        return e;
    }

    /**
     * Registration parameters for an affine transformation.
     *
     * @author Daniel Weidele
     * @version 14.11.2014
     */
    public static class Registration
    {
        private double tx;
        private double ty;
        private double sct;
        private double sst;

        /**
         * Getter for x translation
         *
         * @return x translation for the affine transformation
         */
        public double getTx()
        {
            return this.tx;
        }

        /**
         * Getter for y translation
         *
         * @return y translation for the affine transformation
         */
        public double getTy()
        {
            return this.ty;
        }

        /**
         * Getter for scaling
         *
         * @return scaling
         */
        public double getScaling()
        {
            return Math.sqrt(Math.pow(this.sct, 2) + Math.pow(this.sst, 2));
        }

        /**
         * Getter for rotation (in degrees)
         *
         * @return rotation (in degrees)
         */
        public double getRotation()
        {
            return Math.toDegrees(Math.acos(this.sct / getScaling()));
        }

        @Override
        public String toString()
        {
            return this.tx + "," + this.ty + "," + getScaling() + "," + getRotation();
        }
    }
}
