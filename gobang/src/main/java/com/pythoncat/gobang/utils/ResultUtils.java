package com.pythoncat.gobang.utils;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * @author Administrator
 *         2017/2/2
 *         com.pythoncat.gobang.utils
 */

public class ResultUtils {

    private static boolean checkHor(@NonNull List<Point> points, int num) {
        Log.e("tag", "num = " + num);
        // check white
        for (Point p : points) {
            int count = 1;

            // up
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x - i, p.y))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }
            // down
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x + i, p.y))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }

        }
        return false;
    }

    private static boolean checkVer(@NonNull List<Point> points, int num) {
        Log.e("tag", "num = " + num);
        // check white
        for (Point p : points) {
            int count = 1;

            // up
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x, p.y - i))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }
            // down
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x, p.y + i))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }

        }
        return false;
    }

    private static boolean checkLeftDiagonal(@NonNull List<Point> points, int num) {
        Log.e("tag", "num = " + num);
        // check white
        for (Point p : points) {
            int count = 1;

            // up
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x + i, p.y - i))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }
            // down
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x - i, p.y + i))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }

        }
        return false;
    }

    private static boolean checkRightDiagonal(@NonNull List<Point> points, int num) {
        Log.e("tag", "num = " + num);
        // check white
        for (Point p : points) {
            int count = 1;

            // up
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x + i, p.y + i))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }
            // down
            for (int i = 1; i < num; i++) {
                if (points.contains(new Point(p.x - i, p.y - i))) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == num) {
                return true;
            }

        }
        return false;
    }


    public static boolean isCurrentWin(@NonNull List<Point> currentPointSet, int num) {
        return checkHor(currentPointSet, num)
                || checkVer(currentPointSet, num)
                || checkLeftDiagonal(currentPointSet, num)
                || checkRightDiagonal(currentPointSet, num);
    }

}
