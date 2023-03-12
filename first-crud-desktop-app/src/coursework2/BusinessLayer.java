/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author tstuikys
 */
public class BusinessLayer {

    static ArrayList<Student> StudentList = new ArrayList<>();
    static ArrayList<Activity> ActivityList = new ArrayList<>();
    static DataLayer dl = new DataLayer();
    static GUI gui;
    static String user;
    static String een;

    public static void main(String[] args) {
        initialData();

        
        ///If student files are empty please comment dataTransferFromFileToMemory()
        dataTransferFromFileToMemory();
        dl.createFiles();


        writeAllStudentData(dl);

        writeAllStudentActivityData(dl);

        writeAllActivityData(dl);
        goToScreen(0);

    }


    /*
        addStudent method creates a new Student object and adds it to the global
        array. Parameters:
        1. Student id.
        2. Student Name.
        3. Student chosen Activity. (If unassigned should return null).
     */
    public static Student addStudent(String idn, String nameOfStudent, Activity act) {
        Student s = new Student(idn, nameOfStudent, act);

        for (int i = 0; i < StudentList.size(); i++) {
            String checkName = StudentList.get(i).getStudentName();
            String checkId = StudentList.get(i).getStudentId();

            if (checkName.equals(nameOfStudent) && checkId.equals(idn)) {
                StudentList.remove(i);
            }
        }

        StudentList.add(s);

        return s;
    }

    /*
        printStudentInfo takes a Student object and outputs all of the available
        information to the Console.
     */
    public static void printStudentInfo(Student s) {
        System.out.println(s.getStudentId());
        System.out.println(s.getStudentName());
        System.out.println(s.getStudentActivity());
    }

    /*
        Format student data to be used in writeAllStudentData
        Turns all student data into seperate strings and returns the whole string
        to be written into a file.
     */
    public static String formatStudentData(int n) {

        String id = StudentList.get(n).getStudentId();
        String name = StudentList.get(n).getStudentName();
        String studentActivity = StudentList.get(n).getStudentActivity();

        String data = "id: " + id + " Name: " + name + ", Activity: " + studentActivity;

        return data;
    }

    //Write All Students to students.txt file
    public static void writeAllStudentData(DataLayer d) {

        for (int i = 0; i < StudentList.size(); i++) {
            d.oStudents.println(formatStudentData(i));
        }
        System.out.println("All Student data has been written.");
        d.oStudents.close();
    }

    /*
        createActivity adds a new activity to the ActivityList. The default number
        of students upon creation is 0. Due to 0 students signing up on an activity
        during creation.
     */
    public static Activity createActivity(String aName, String loc, String p, String tutor) {
        Activity act = new Activity(aName, loc, p, tutor);

        for (int i = 0; i < ActivityList.size(); i++) {
            String tChecker = ActivityList.get(i).getTutorName();
            if (tChecker.equals(tutor)) {
                ActivityList.remove(i);
            }

        }
        ActivityList.add(act);

        return act;
    }

    /*
        printsActivityInfo accepts activity as a parameter and prints out all of
        its information.
     */
    public static void printActivityInfo(Activity a) {
        System.out.println(a.getActivityName());
        System.out.println(a.getActivityLocation());
        System.out.println(a.getActivityPlan());
        System.out.println(a.getTutorName());
    }

    // Prints all activities in the ActivityList.
    public static void showAllActivities() {
        int numberOfActivities = ActivityList.size();

        System.out.println("List of all Activities: ");
        for (int i = 0; i < numberOfActivities; i++) {
            System.out.println(ActivityList.get(i).getActivityName());
        }
    }

    /*
        addStudentToActivityList takes a Student s and Activity act.
        The method assigns student to activity and adds him to that activities 
        ArrayList.
        Lastly prints out That the student was added to the activities list.
     */
    public static Activity addStudentToActivityList(Student s, Activity act) {
        s.setStudentActivity(act);
        act.addStudentToList(s);

        System.out.println("Student " + s.getStudentName() + " has been successfully added to the Student list for activity " + act.getActivityName());
        return act;
    }

    //Formats all data to be exported into Activities.txt
    public static String formatActivityData(int n) {
        String activityName = ActivityList.get(n).getActivityName();
        String location = ActivityList.get(n).getActivityLocation();
        String plan = ActivityList.get(n).getActivityPlan();
        String tutor = ActivityList.get(n).getTutorName();

        System.out.println("Plan Here: " + plan);
        String data = "Activity: " + activityName + " Location: " + location + " Plan: " + plan + " Tutor: " + tutor;

        return data;
    }

    //Write All Activities to Activities.txt file
    public static void writeAllActivityData(DataLayer d) {

        for (int i = 0; i < ActivityList.size(); i++) {
            d.oActivities.println(formatActivityData(i));
        }
        System.out.println("All Activity data has been written.");
        d.oActivities.close();
    }

    //Writes all Activity and Student data to Student-Activities.txt
    //NOT COMPLETE
    public static void writeAllStudentActivityData(DataLayer d) {

        for (int i = 0; i < ActivityList.size(); i++) {
            for (int j = 0; j < StudentList.size(); j++) {
                Activity a = ActivityList.get(i);
                Student s = StudentList.get(j);
                boolean stuAct = studentUnderTutor(s, a);

                if (stuAct == true) {
                    System.out.println("stuAct == true");
                    d.oStudentActivities.println(formatStudentToTutor(s, a));
                }
            }
        }
        System.out.println("All Student-Activity data has been written.");
        d.oStudentActivities.close();

    }

    //This method checks whether student has a specific tutor.
    public static boolean studentUnderTutor(Student sr, Activity act) {
        boolean result = false;

        //TEST
        String studentName = sr.getStudentName();
        
        String tutorName = act.getTutorName();
        
        
        String studentActivity = sr.getStudentActivity();
        String currentActivity = act.getActivityName();

        if (studentActivity.equals(currentActivity)) {
            result = true;
        }

        System.out.println("Tutor == " + tutorName + " " + studentName + " "  + result);

        return result;
    }

    public static String formatStudentToTutor(Student st, Activity a) {
        String result = "";
        String one = "Tutor: " + a.getTutorName();
        String two = " Student Name: " + st.getStudentName();
        String three = " Activity: " + a.getActivityName();

        result = one + two + three;

        return result;
    }

    /*
        Checks for user login to allow access to the application.
        It only accepts Student, Tutor and Activity Coordinator strings.
     */
    public static void userType(String user) {
        boolean userFound = false;

        switch (user) {
            case "Student":
                userFound = true;
                break;
            case "Tutor":
                userFound = true;
                break;
            case "Activity Coordinator":
                userFound = true;
                break;
        }

        if (userFound == false) {
            System.out.println("Wrong user.");
        }

    }

    /*
        This method is used to swap between screens when data needs to be updated in the GUI
        Everytime this method is called all list data is updated to the GUI.
     */
    public static void goToScreen(int screenNumber) {
        /*
        0 = GUI start page, 1 = StudentLogin, 2 = Tutor Login, 3 = Activity Coordinator login,
        4 = Student Page, 5 = Tutor Page, 6 = Activity Coordinator Page.
         */

        String[] studentArray = studentListToArray(StudentList);
        String[] activityArray = activityListToArray(ActivityList);

        gui = new GUI(studentArray, activityArray, screenNumber);

    }

    // This method just converts an ArrayList to its corresponding Array
    public static String[] studentListToArray(ArrayList al) {
        int length = al.size();

        String[] array = new String[length];

        for (int i = 0; i < length; i++) {
            array[i] = formatStudentData(i);
        }

        return array;
    }

    // This method convers ArrayList to a String Array and returns it.
    public static String[] activityListToArray(ArrayList al) {
        int length = al.size();

        String[] array = new String[length];

        for (int i = 0; i < length; i++) {
            array[i] = formatActivityData(i);
        }

        return array;
    }

    //Adds student from the GUI
    public void addStudentFromGUI() {
        dl.openStudents();
        dl.openActivities();

        String studentIDNumber = gui.getEhn();

        String studentName = gui.getName();

        Activity currentActivity = ActivityList.get(gui.getActivIndex());

        addStudent(studentIDNumber, studentName, currentActivity);
        writeAllStudentData(dl);
        writeAllActivityData(dl);
        writeAllStudentActivityData(dl);
    }

    ////NOT FINISHED
    public String[] returnTutorList(String t) {
        //Originally ActivityList was used for size.
        String[] tutorArray = new String[StudentList.size()];

        int tIndex = activityIndexFromTutorName(t);
        int counter = 0;

        for (int j = 0; j < StudentList.size(); j++) {
            String actName = ActivityList.get(tIndex).getActivityName();
            String stAct = StudentList.get(j).getStudentActivity();
            if (actName.equals(stAct)) {
                tutorArray[counter] = formatStudentData(j);
                counter++;
            }

        }
        return tutorArray;
    }

    //Accepts tutors name and allows 
    public int activityIndexFromTutorName(String t) {
        int index = 0;

        for (int i = 0; i < ActivityList.size(); i++) {
            String tutorName = ActivityList.get(i).getTutorName();
            if (tutorName.equals(t)) {
                index = i;
            }

        }

        return index;
    }

    /*
        The purpose of this method is to open a file scan every line and add each
        member of the file to the Student List. This way when the I/O stream
        is opened all previous entries to the program are saved.
     */
    public static void readStudentInfoFromFile() {
        //Data format. String data = "id: " + id + " Name: " + name + ", Activity: " + studentActivity;
        //Format
        String id = "id: ";
        String name = " Name: ";
        String activity = ", Activity: ";

        try {
            File file = new File("school data/Students.txt");
            Scanner read = new Scanner(file);
            while (read.hasNextLine()) {
                String data = read.nextLine();
                formatStudentDataFromFile(data);
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("Students.txt File was not found.");
        }

        //Open file
        //Read file
        //Take the whole string
    }

    public static void formatStudentDataFromFile(String d) {
        String id = "id: ";
        String name = " Name: ";
        String activity = ", Activity: ";

        /*
            Number range required to format data.
            (lastIndexOf(id) + string.length()) to (firstIndexOf(name) - 1) This should give me the id;
            (lastIndexOf(name) + string.length()) to (firstIndexOf(activity) - 1) This should give me name;
            (lastIndexOf(activity) + string.length()) to (string.length() - 1);
         */
        //sName represents student's full name
        int index1 = d.lastIndexOf(id) + id.length();
        int index2 = d.indexOf(name);

        String sId = returnDataFromStringRanges(d, index1, index2);

        //sId represents students id number;
        index1 = d.lastIndexOf(name) + name.length();
        index2 = d.indexOf(activity);

        String sName = returnDataFromStringRanges(d, index1, index2);

        //sActivity represents students Activity name;
        index1 = d.lastIndexOf(activity) + activity.length();
        index2 = d.length();

        String sActivity = returnDataFromStringRanges(d, index1, index2);

        //Finding activity that the student has;
        Activity currentActivity;
        currentActivity = returnActivityFromString(sActivity);

        addStudent(sId, sName, currentActivity);

    }

    //Returns the string from specified ranges
    public static String returnDataFromStringRanges(String s, int r1, int r2) {
        String data = "";

        for (int i = r1; i < r2; i++) {
            data += s.charAt(i);

        }

        return data;
    }

    //Returns activity based off the name of the activity
    public static Activity returnActivityFromString(String s) {
        //System.out.println(s.length());
        //System.out.println("Rugby".length());
        Activity act = null;
        for (int i = 0; i < ActivityList.size(); i++) {
            String something = ActivityList.get(i).getActivityName();
            if (something.equals(s)) {
                act = ActivityList.get(i);
            }
        }

        return act;

    }

    public static void readActivityInfoFromFile() {
        String activityName = "Activity: ";
        String plan = " Plan: ";
        String location = " Location: ";
        String tutorName = " Tutor: ";

        try {
            File file = new File("school data/Activities.txt");
            Scanner read = new Scanner(file);
            while (read.hasNextLine()) {
                String data = read.nextLine();
                formatActivityDataFromFile(data);
                System.out.println(data);
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("Activity.txt File was not found.");
        }

        //Open file
        //Read file
        //Take the whole string
    }

    public static void formatActivityDataFromFile(String d) {
        String activityName = "Activity: ";
        String location = " Location: ";
        String plan = " Plan: ";
        String tutorName = " Tutor: ";

        /*
            Number range required to format data.
            (lastIndexOf(activityName) + 1)
         */
        //sName represents student's full name
        int index1 = d.lastIndexOf(activityName) + activityName.length();
        int index2 = d.indexOf(location);

        String actName = returnDataFromStringRanges(d, index1, index2);

        //sId represents students id number;
        index1 = d.lastIndexOf(location) + location.length();
        index2 = d.indexOf(plan);

        String loc = returnDataFromStringRanges(d, index1, index2);

        //sActivity represents students Activity name;
        index1 = d.lastIndexOf(plan) + plan.length();
        index2 = d.indexOf(tutorName);

        String p = returnDataFromStringRanges(d, index1, index2);

        //Finding activity that the student has;
        index1 = d.lastIndexOf(tutorName) + tutorName.length();
        index2 = d.length();

        String t = returnDataFromStringRanges(d, index1, index2);

        System.out.println(t);
        createActivity(actName, loc, p, t);

    }

    public void changeTutorPlan(String plan, String name) {
        dl.openActivities();
        int actIndex = activityIndexFromTutorName(name);

        ActivityList.get(actIndex).setActivityPlan(plan);
        writeAllActivityData(dl);
    }

    public void changeTutorLocation(String loc, String name) {
        dl.openActivities();
        int actIndex = activityIndexFromTutorName(name);

        ActivityList.get(actIndex).setActivityLocation(loc);
        writeAllActivityData(dl);
    }

    public void coordinatorAddActivity(String aName, String aLoc, String aPlan, String tName) {
        dl.openActivities();

        createActivity(aName, aLoc, aPlan, tName);
        int lastElement = ActivityList.size() - 1;

        //String data = formatActivityData(lastElement);
        writeAllActivityData(dl);

    }

    public void coordinatorRemoveActivity(int index) {
        dl.openActivities();
        dl.openStudentActivities();
        System.out.println("111111111111111111111");

        Activity act = ActivityList.get(index);

        System.out.println(act.getActivityName());
        removeAllStudentsFromActivity(act);

        System.out.println("22222222222222222");
        ActivityList.remove(index);

        System.out.println("33333333333333333333");
        writeAllActivityData(dl);
        writeAllStudentActivityData(dl);
    }

    /// This method serves the purpose of default data. if the program is working as
    /// intended this method needs to be called only the first time the program has been
    /// executed or if the files get corrupted due to user error.
    public static void initialData() {
        Activity nullAct = createActivity("Empty", "Empty", "Empty", "Activity Coordinator");
        Activity pisoc = createActivity("Pisoc", "Computer Suite", "Automating life", "Cameron");
        Activity rugby = createActivity("Rugby", "Football Field", "Practising set-plays", "Dan");
        Activity basketball = createActivity("Basketball", "Basketball court", "Practising Dribbling", "August");
        Activity iceskating = createActivity("Ice-skating", "Ice Arena", "Stamina training", "Alessa");

        Student Tomas = addStudent("18005479", "Tomas Stuikys", pisoc);
        Student David = addStudent("23000001", "Deividas Baksys", rugby);
        Student Hagrid = addStudent("19000311", "Hagrid", basketball);
        Student Harry = addStudent("9821300", "Harry", pisoc);
        Student Hermione = addStudent("03021001", "Hermione", iceskating);
        Student Random = addStudent("23000000", "Random", rugby);
        Student Jesper = addStudent("18990213", "Jesper", rugby);
    }

    //Reads all data from Activity and Student files
    public static void dataTransferFromFileToMemory() {
        readActivityInfoFromFile();
        readStudentInfoFromFile();
    }

    public static void removeAllStudentsFromActivity(Activity act) {
        String newAct = act.getActivityName();
        Activity empty = ActivityList.get(0);

        for (int i = 0; i < StudentList.size(); i++) {
            String studentActivity = StudentList.get(i).getStudentActivity();
            if (studentActivity.equals(newAct)) {
                StudentList.get(i).setStudentActivity(empty);
            }

        }

        dl.openStudents();
        writeAllStudentData(dl);
    }

    public String[] returnAList() {
        ArrayList aL = new ArrayList<>();

        for (int i = 0; i < ActivityList.size(); i++) {
            String empty = ActivityList.get(i).getActivityName();
            if (empty != "Empty") {
                Activity act = ActivityList.get(i);
                aL.add(act);
            }
        }

        String[] newArray = activityListToArray(aL);

        return newArray;
    }

    public String[] returnAllTutorNames() {
        String[] TutorList = new String[ActivityList.size()];

        /// It starts at one because I dont want the first activity to be used.
        for (int i = 0; i < TutorList.length; i++) {
            String tutorName = ActivityList.get(i).getTutorName();
            if (tutorName != "Activity Coordinator") {
                TutorList[i] = ActivityList.get(i).getTutorName();
            }
        }

        return TutorList;
    }

}
