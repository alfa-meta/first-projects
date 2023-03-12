/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.awt.Point;
import java.util.ArrayList;
/**
 *
 * @author tomas
 */
public class testing {

    public static void main(String[] args) {
        int col = 10;
        int row = 10;
        Point[][] matrix = new Point[col][row];

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                matrix[i][j] = new Point(i, j);

            }
        }
        System.out.println(matrix[1][3].x);

    }
}
