package com.example.matrixmanipulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.graphics.Bitmap;

public class Utilities {

    private static final int NUM_THREADS = 4;

    public static int[][] generateMatrix(int size) {
        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Random r = new Random();

                matrix[i][j] = r.nextInt() % 10;
            }
        }

        return matrix;
    }

    public static int[][] flipHorizontally(int[][] matrix, int start, int size) {
        int[][] result = new int[size][matrix.length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[start + i][matrix.length - 1 - j];
            }
        }

        return result;
    }

    public static int[][] flipVertically(int[][] matrix, int start, int size) {

        int[][] result = new int[size][matrix.length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[start + size - 1 - i][j];
            }
        }
        return result;
    }

    public static int[][] rotateClockWise(int[][] matrix, int start, int size) {

        int[][] result = new int[size][matrix.length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[matrix.length - 1 - j][start + i];
            }
        }

        return result;
    }

    public static int[][] rotateCounterClockWise(int[][] matrix, int start, int size) {

        int[][] result = new int[size][matrix.length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[j][matrix.length - 1 - start - i];
            }
        }

        return result;
    }

    public static int[][] sort(int[][] matrix, int start, int size) {

        int[][] result = new int[size][matrix.length];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[start + i][j];
            }
        }

        //sort
        for (int i = 0; i < size; i++) {
            Arrays.sort(result[i]);
        }

        return result;
    }

    public static int[][] removeColor(int[][] matrix, int color) {
        int size = matrix.length;

        int[][] result = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int cur = matrix[i][j];
                if (cur == color) {
                    result[i][j] = 0;
                } else {
                    result[i][j] = cur;
                }
            }
        }
        return result;
    }

    public static int[][] convertMapToArray(Bitmap map) {
        int width = map.getWidth();
        int height = map.getHeight();
        int[][] result = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result[i][j] = map.getPixel(j, i);
            }
        }
        return result;

    }

    public static Bitmap convertArrayToMap(int[][] array) {

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(array[0].length, array.length, conf);

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                bmp.setPixel(j, i, array[i][j]);
            }
        }
        return bmp;
    }

    public static int[][] matrixManipulation(int[][] matrix, boolean flipH, boolean flipV, boolean rotateCW, boolean rotateCCW, boolean sort, boolean serial) {
        int num_rows = matrix.length / NUM_THREADS;
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        Callable<int[][]> callable;
        List<Callable<int[][]>> tasks;
        List<Future<int[][]>> list;

        //flip horizontally
        if (flipH) {
            if (serial) {
                matrix = flipHorizontally(matrix, 0, matrix.length);
            } else {
                tasks = new ArrayList<Callable<int[][]>>();

                // distribute rows to each thread
                for (int x = 0; x < NUM_THREADS; x++) {
                    callable = new ParallelCallable(matrix, x * num_rows, num_rows, ManipulationType.FLIP_H);
                    // store the callable objects in tasks to run with executor service all at once
                    tasks.add(callable);
                }

                try {
                    // Run all the tasks in parallel
                    // invokeAll() blocks until all tasks are done
                    list = executorService.invokeAll(tasks);
                    for (int thread_id = 0; thread_id < list.size(); thread_id++) {
                        // copy the rows returned by each thread back to the master matrix
                        System.arraycopy(list.get(thread_id).get(), 0, matrix, thread_id * num_rows, num_rows);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        //flip vertically
        if (flipV) {
            if (serial) {
                matrix = flipVertically(matrix, 0, matrix.length);
            } else {
                tasks = new ArrayList<Callable<int[][]>>();

                for (int x = 0; x < NUM_THREADS; x++) {
                    callable = new ParallelCallable(matrix, x * num_rows, num_rows, ManipulationType.FLIP_V);
                    tasks.add(callable);
                }

                try {
                    list = executorService.invokeAll(tasks);
                    for (int thread_id = 0; thread_id < list.size(); thread_id++) {
                        System.arraycopy(list.get(thread_id).get(), 0, matrix, matrix.length - ((thread_id + 1) * num_rows), num_rows);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }

        //rotate clockwise
        if (rotateCW) {
            if (serial) {
                matrix = rotateClockWise(matrix, 0, matrix.length);
            } else {
                tasks = new ArrayList<Callable<int[][]>>();

                for (int x = 0; x < NUM_THREADS; x++) {
                    callable = new ParallelCallable(matrix, x * num_rows, num_rows, ManipulationType.ROTATE_CW);
                    tasks.add(callable);
                }

                try {
                    list = executorService.invokeAll(tasks);
                    for (int thread_id = 0; thread_id < list.size(); thread_id++) {
                        System.arraycopy(list.get(thread_id).get(), 0, matrix, thread_id * num_rows, num_rows);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        //rotate counter clockwise
        if (rotateCCW) {
            if (serial) {
                matrix = rotateCounterClockWise(matrix, 0, matrix.length);
            } else {
                tasks = new ArrayList<Callable<int[][]>>();

                for (int x = 0; x < NUM_THREADS; x++) {
                    callable = new ParallelCallable(matrix, x * num_rows, num_rows, ManipulationType.ROTATE_CCW);
                    tasks.add(callable);
                }

                try {
                    list = executorService.invokeAll(tasks);
                    for (int thread_id = 0; thread_id < list.size(); thread_id++) {
                        System.arraycopy(list.get(thread_id).get(), 0, matrix, thread_id * num_rows, num_rows);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        //sort
        if (sort) {
            if (serial) {
                matrix = sort(matrix, 0, matrix.length);
            } else {
                tasks = new ArrayList<Callable<int[][]>>();

                for (int x = 0; x < NUM_THREADS; x++) {
                    callable = new ParallelCallable(matrix, x * num_rows, num_rows, ManipulationType.SORT);
                    tasks.add(callable);
                }

                try {
                    list = executorService.invokeAll(tasks);
                    for (int thread_id = 0; thread_id < list.size(); thread_id++) {
                        System.arraycopy(list.get(thread_id).get(), 0, matrix, thread_id * num_rows, num_rows);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        // Close down executor service for multi-threading
        executorService.shutdown();

        return matrix;
    }

}
