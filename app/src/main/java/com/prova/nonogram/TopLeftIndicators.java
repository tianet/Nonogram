package com.prova.nonogram;

import android.util.Log;

/**
 * Created by Juancki on 19/09/16.
 */

public class TopLeftIndicators {

    private int [][] topInd;
    private boolean [][] topIndCirc;
    private int [][] leftInd;
    private boolean [][] lefIndCirc;


    public int[][] getTopInd() {
        return topInd;
    }

    public boolean[][] getTopIndCirc() {
        return topIndCirc;
    }

    public boolean[][] getLefIndCirc() {
        return lefIndCirc;
    }

    public int[][] getLeftInd() {
        return leftInd;
    }

    public TopLeftIndicators(TopLeftIndicators tli){
        this.leftInd =          new int [tli.getLeftInd().length]       [tli.getLeftInd()[0].length];
        this.lefIndCirc =   new boolean [tli.getLefIndCirc().length]    [tli.getLefIndCirc()[0].length];
        this.topInd =           new int [tli.getTopInd().length]        [tli.getTopInd()[0].length];
        this.topIndCirc =   new boolean [tli.getTopIndCirc().length]    [tli.getTopIndCirc()[0].length];

        for (int i = 0; i < leftInd.length; i++){
            System.arraycopy(tli.getLeftInd()[i],0,leftInd[i],0,leftInd[i].length);
            System.arraycopy(tli.getLefIndCirc()[i],0,lefIndCirc[i],0,lefIndCirc[i].length);
        }
        for (int j = 0; j<topInd.length;j++){
            System.arraycopy(tli.getTopInd()[j],0,topInd[j],0,topInd[j].length);
            System.arraycopy(tli.getTopIndCirc()[j],0,topIndCirc[j],0,topIndCirc[j].length);
        }

    }

    public TopLeftIndicators(Grid grid) {
        int rows = grid.getRows();
        int cols = grid.getCols();
        Col [] colors = grid.getColors();
        int n_cols = colors.length;
        Col [][] array = grid.getArray_cols();
        this.leftInd = new int [rows][n_cols];
        this.lefIndCirc = new boolean[rows][n_cols];
        this.topInd = new int[cols][n_cols];
        this.topIndCirc = new boolean[cols][n_cols];
        for (int aux = 0 ; aux<n_cols;aux++){
            Col color = colors[aux];
            int status  =0;
            for (int row = 0; row <rows; row ++) {
                status = 0;
                for (int col = 0; col < cols; col++) {
                    switch (status) {
                        case 0:
                            if (color == array[col][row]) {
                                leftInd[row][aux]++;
                                status = 1;
                            }
                            break;
                        case 1:
                            if (color == array[col][row]) {
                                leftInd[row][aux]++;
                            } else {
                                status = 2;
                            }
                            break;
                        case 2:
                            if (color == array[col][row]) {
                                leftInd[row][aux]++;
                                status = 3;
                            }
                            break;
                        case 3:
                            if (color == array[col][row]) {
                                leftInd[row][aux]++;
                            }
                            break;
                    }
                }
                lefIndCirc [row][aux] = (status==1 || status==2) && leftInd[row][aux]>1;
            }
        }

        for (int aux = 0 ; aux<n_cols;aux++) {
            Col color = colors[aux];
            int status = 0;
            for (int col = 0; col < cols; col++) {
                status =0 ;
                for (int row = 0; row < rows; row++) {
                    switch (status) {
                        case 0:
                            if (color == array[col][row]) {
                                topInd[col][aux]++;
                                status = 1;
                            }
                            break;
                        case 1:
                            if (color == array[col][row]) {
                                topInd[col][aux]++;
                            } else {
                                status = 2;
                            }
                            break;
                        case 2:
                            if (color == array[col][row]) {
                                topInd[col][aux]++;
                                status = 3;
                            }
                            break;
                        case 3:
                            if (color == array[col][row]) {
                                topInd[col][aux]++;
                            }
                            break;
                    }
                }
                topIndCirc [col][aux] = (status==1 || status==2) && topInd[col][aux]>1;
            }
        }
    }

    /**
     * Returns the difficulty of the grid.
     * @param tli
     * @param grid
     * @return
     */
    public static JocProvaActivity.Difficulty getDifficulty (TopLeftIndicators tli, Grid grid){
       // Log.v("Debug","It arrives here");
        int numberOfCircles=0;
        int totalNumber = grid.getCols()*grid.getN_cols() + grid.getRows()*grid.getN_cols();
        //Circles in left difficulties
        for (int i = 0; i < grid.getRows()-1 ; i++) {
            for (int j = 0; j < grid.getN_cols()-1; j++ ){
                if (tli.getLefIndCirc()[i][j]) {
                    numberOfCircles++;
                }
            }
        }
        for (int i = 0; i < grid.getCols() ; i++) {
            for (int j = 0; j < grid.getN_cols(); j++ ){
                if (tli.getTopIndCirc()[i][j]) {
                    numberOfCircles++;
                }
            }
        }

        float percentage = (float) numberOfCircles/(float) totalNumber;
        System.out.println("Number of circles "+numberOfCircles + "Total Number " + totalNumber+  " Percentatge:"+percentage);
        if (totalNumber < 20) {
            if (percentage > 0.25) {
                return JocProvaActivity.Difficulty.EASY;
            } else if (percentage < 0.25 && percentage > 0.10 ) {
                return JocProvaActivity.Difficulty.MEDIUM;
            } else {
                return JocProvaActivity.Difficulty.DIFFICULT;
            }
        } else if (totalNumber >20 && totalNumber < 30) {
            if (percentage > 0.15) {
                return JocProvaActivity.Difficulty.EASY;
            } else if (percentage < 0.15 && percentage > 0.075 ) {
                return JocProvaActivity.Difficulty.MEDIUM;
            } else {
                return JocProvaActivity.Difficulty.DIFFICULT;
            }
        } else {
            if (percentage > 0.1) {
                return JocProvaActivity.Difficulty.EASY;
            } else if (percentage < 0.1 && percentage > 0.05 ) {
                return JocProvaActivity.Difficulty.MEDIUM;
            } else {
                return JocProvaActivity.Difficulty.DIFFICULT;
            }
        }

    }
}
