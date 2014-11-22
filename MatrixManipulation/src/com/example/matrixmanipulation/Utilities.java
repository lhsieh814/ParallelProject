package com.example.matrixmanipulation;

import java.util.Arrays;
import java.util.Random;

public class Utilities {
    
    public static int[][] generateMatrix(int size){
    	int[][] matrix = new int[size][size];
    	
    	for(int i =0;i<size; i++){
    		for(int j = 0 ; j<size; j++){
    			Random r = new Random();
    			
    			matrix[i][j] = r.nextInt()%10;
    		}
    	}
    	
    	return matrix;
    }
    
    public static int[][] flipHorizontally(int[][] matrix){
    	int size = matrix.length;
    	
    	int[][] result = new int[size][size];
    	
    	for(int i =0 ;i<size; i++){
    		for(int j =0; j<size; j++){
    			result[i][j] = matrix[i][size-1-j];
    		}
    	}
    	
    	return result;
    }
    
    
    public static int[][] flipVertically(int[][] matrix){
    	int size = matrix.length;
    	
    	int[][] result = new int[size][size];
    	
    	for(int i =0 ;i<size; i++){
    		for(int j =0; j<size; j++){
    			result[i][j] = matrix[size-1-i][j];
    		}
    	}
    	return result;
    }
    
    public static int[][] rotateClockWise(int[][] matrix){
    	int size = matrix.length;
    	
    	int[][] result = new int[size][size];
    	
    	for(int i =0; i<size; i++){
    		for(int j=0; j<size; j++){
    			result[size-1-j][size-1-i] = matrix[i][j];
    		}
    	}
    	
    	return result;
    }
    
    
    public static int[][] rotateCounterClockWise(int[][] matrix){
    	int size = matrix.length;
    	
    	int[][] result = new int[size][size];
    	
    	for(int i=0; i<size; i++){
    		for(int j=0; j<size; j++){
    			result[j][i] = matrix[i][j];
    		}
    	}
    	
    	
    	return result;
    }
    
    public static int[][] sort(int[][] matrix){
    	int size =matrix.length;
    	
    	int[][] result = new int[size][size];
    	
    	for(int i=0; i<size; i++){
    		for(int j=0; j<size; j++){
    			result[i][j] = matrix[i][j];
    		}
    	}
    	
    	//sort
    	for(int i=0; i<size; i++){
    		Arrays.sort(matrix[i]);
    	}
    	
    	return result;
    }
    
    public static int[][] removeColor(int[][] matrix, int color){
    	int size =matrix.length;
    	
    	int[][] result = new int[size][size];
    	
    	for(int i=0; i<size; i++){
    		for(int j=0; j<size; j++){
    			int cur = matrix[i][j];
    			if(cur==color){
    				result[i][j] = 0;
    			}
    			else{
    				result[i][j] = cur;
    			}
    		}
    	}
    	return result;
    }
}
