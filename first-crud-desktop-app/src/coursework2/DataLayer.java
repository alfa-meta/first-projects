/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.File;

/**
 *
 * @author tstuikys
 */
public class DataLayer {

    //Global variable to create a directory for all the relevant data.
    static File dir = new File("school data");

    //Global variables for Output Files. 
    static PrintWriter oStudents;
    static PrintWriter oActivities;
    static PrintWriter oStudentActivities;


    /*
        createFiles() method is used to create a directory with relevant text
        files inside. This method opens up an output stream but does not close it.
     */
    public static void createFiles() {
        try {
            createDirectory();

            if (doesFileExist()) {

                oStudents = new PrintWriter(new FileOutputStream("school data/Students.txt"));
                oActivities = new PrintWriter(new FileOutputStream("school data/Activities.txt"));
                oStudentActivities = new PrintWriter(new FileOutputStream("school data/Student-Activities.txt"));
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException was thrown.");
        }
        System.out.println("All files have been successfully created.");

    }

    //Simply makes a directory.
    public static void createDirectory() {
        if (dir.exists() == false) {
            dir.mkdir();
        }
    }

    //Closes all OutputStream files
    public static void closeAllOutFiles() {
        oStudents.close();
        oActivities.close();
        oStudentActivities.close();

        System.out.println("All Output files have been closed.");
    }

    public static boolean doesFileExist() {
        boolean exist = false;
        File file = new File("school data/Students.txt");
        File file1 = new File("school data/Activities.txt");
        File file2 = new File("school data/Student-Activities.txt");

        File[] array = {file, file1, file2};

        int counter = 0;

        for (int i = 0; i < 3; i++) {
            if (array[i].exists() == true) {
                counter++;

            }
        }
        if (counter == 3) {
            exist = true;
        }

        System.out.println("Exists == : " + exist);
        System.out.println("File counter: " + counter);

        return exist;
    }

    public static void openStudents() {
        try {
            oStudents = new PrintWriter(new FileOutputStream("school data/Students.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException was thrown on openStudents().");
        }
    }
    
    public static void openActivities() {
        try {
            oActivities = new PrintWriter(new FileOutputStream("school data/Activities.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException was thrown on openActivities().");
        }
    }
    
    public static void openStudentActivities() {
        try {
            oStudentActivities = new PrintWriter(new FileOutputStream("school data/Student-Activities.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException was thrown on openStudentActivities().");
        }
    }
}
