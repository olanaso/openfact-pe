package org.openfact.pe.representations.idm;

/**
 * Created by admin on 6/6/17.
 */
public class Test {

    public static void main(String args[]) {
        int[][] arr = {
                {0, -4, -6, 0, -7, -6},
                {-1, -2, -6, -8, -3, -1},
                {-8, -4, -2, -8, -8, -6},
                {-3, -1, -2, -5, -7, -4},
                {-3, -5, -3, -6, -6, -6},
                {-3, -6, 0, -8, -6, -7}
        };

        int greater = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int sum = arr[i][j] + arr[i][j + 1] + arr[i][j + 2];
                sum = sum + arr[i + 1][j + 1];
                sum = sum + arr[i + 2][j] + arr[i + 2][j + 1] + arr[i + 2][j + 2];
                if (sum > greater) {
                    greater = sum;
                }
                if (i == 0 && j == 0) {
                    greater = sum;
                }
            }
        }
        System.out.println(greater);
    }
}
