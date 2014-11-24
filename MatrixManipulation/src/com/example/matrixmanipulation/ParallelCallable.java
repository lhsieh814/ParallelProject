package com.example.matrixmanipulation;

import java.util.concurrent.Callable;

public class ParallelCallable implements Callable<int[][]> {
    private int[][] matrix;
    private int start;
    private int size;
    private ManipulationType type;

    public ParallelCallable(int[][] matrix, int start, int size, ManipulationType type) {
        this.matrix = matrix;
        this.start = start;
        this.size = size;
        this.type = type;
    }

    @Override
    public int[][] call() throws Exception {
        int[][] result;

        switch(type) {
            case FLIP_H:
                result = Utilities.flipHorizontally(matrix, start, size);
                return result;
            case FLIP_V:
                result = Utilities.flipVertically(matrix, start, size);
                return result;
            case ROTATE_CW:
                result = Utilities.rotateClockWise(matrix, start, size);
                return result;
            case ROTATE_CCW:
                result = Utilities.rotateCounterClockWise(matrix, start, size);
                return result;
            case SORT:
                result = Utilities.sort(matrix, start, size);
                return result;
            default:
                throw new Exception("The operation is not supported");
        }
    }

}
