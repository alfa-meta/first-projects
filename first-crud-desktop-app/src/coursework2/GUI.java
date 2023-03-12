/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author bilaal and Tayabah
 */
public class GUI extends JFrame implements ActionListener {

    //Setting up global variables for initial GUI page.
    JPanel panel = new JPanel();
    JFrame frame = new JFrame("Eastend Highschool Activity Manager");
    
    JButton tButton = new JButton("Tutor");
    JButton sButton = new JButton("Students");
    JButton acButton = new JButton("Activity Coordinator");
    
    //Button used to save all data.
    JButton saveButtonStudent = new JButton("Save");
    
    //Buttons used for changing data on the Tutor page
    JButton planButtonTutor = new JButton("Change Plan");
    JButton locationButtonTutor = new JButton("Change Location");
    
    //Button used for changing data on the Activity Coordinator page
    JButton addActivityButton = new JButton("Add Activity");
    JButton removeActivityButton = new JButton("Remove Activity");

    //Setting up variables for login screen.
    JButton slogin = new JButton();
    JButton tlogin = new JButton();
    JButton alogin = new JButton();
    JTextField nameField = new JTextField();
    JTextField numberField = new JTextField();
    JLabel nameLabel = new JLabel();
    JLabel numberLabel = new JLabel();
    JComboBox comboBox = new JComboBox();

    //Setting up variables for Student page.
    private JList<String> list;
    

    //Array for all generated students
    public Student[] genStudents;

    //Receiving data from business layer
    private String[] sList;
    private String[] aList;
    private String[] tList;

    //User attributes
    private String userType;
    private String name;
    private String ehn; //This will be the identification number used for students.
    private int activIndex;
    public int screenNumber;

    //Gui specific variables used to check the state of the gui
    public boolean saved = false;
    public boolean open;

    BusinessLayer bl = new BusinessLayer();

    GUI(String[] studentList, String[] activityList, int screen) {
        sList = studentList;
        aList = activityList;
        //tList = t;
        screenNumber = screen;
        open = true;

        switch (screen) {
            case 0:
                GUIConstructor();
                break;
            case 1:
                setUserType("Student");
                StudentConstructor();
                break;
            case 2:
                setUserType("Tutor");
                TutorConstructor();
                break;
            case 3:
                setUserType("Student");
                studentActivityPage();
                break;
            case 4:
                setUserType("Tutor");
                //tutorActivityPage();
                break;
            case 5:
                setUserType("Activity Coordinator");
                //activityCoordinatorPage();
                break;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Actions performed based on which button is pressed:
        // Changes the current GUI frame to Student
        if (e.getSource() == sButton) {
            destroyCurrentFrame();
            screenNumber = 1;
            StudentConstructor();
        }
        
        if (e.getSource() == tButton) {
            destroyCurrentFrame();
            screenNumber = 2;
            TutorConstructor();
        }
        
        if (e.getSource() == acButton){
            destroyCurrentFrame();
            screenNumber = 5;
            activityCoordinatorPage();
        }

        // Changes the current GUI to the Student Page when the login
        // button is clicked.
        if (e.getSource() == slogin) {
            System.out.println("login was clicked");
            
            ehn = numberField.getText(); 
            boolean cNumber = correctNumber(ehn);

            
            name = nameField.getText();         
            boolean cName = correctName(name);
            
            if (cName == true && cNumber == true){
                frame.dispose();
                frame.getContentPane().removeAll();
                screenNumber = 3;
                studentActivityPage();
            }
        }
        
        if (e.getSource() == tlogin) {
            System.out.println(" Tutor login was clicked");
            name = nameField.getText();    
            System.out.println(name);
            boolean tName = correctTutorName(name);
            
            
            if (tName == true){
                tList = bl.returnTutorList(name);
                destroyCurrentFrame();
                screenNumber = 4;
                tutorActivityPage();
            }
        }
        

        // Saves activity for current student
        if (e.getSource() == saveButtonStudent) {
            ///sets activIndex to the index of the activity selected.
            activIndex = list.getSelectedIndex();
            saved = true;
            System.out.println(activIndex);
            bl.addStudentFromGUI();
            //This should refresh all data.
            destroyCurrentFrame();
            studentActivityPage();
        }
        
        if (e.getSource() == planButtonTutor){
            //Tutor saves changes
            JFrame popUp = new JFrame();
            String plan = JOptionPane.showInputDialog(popUp, "Enter new Plan");
            
            System.out.println(plan);
            bl.changeTutorPlan(plan , name);

        }
        
        if (e.getSource() == locationButtonTutor){
            //Tutor Changes location
            JFrame popUp = new JFrame();
            String plan = JOptionPane.showInputDialog(popUp, "Enter new Location");
            
            System.out.println(plan);
            bl.changeTutorLocation(plan , name);
        }
        
        ///When add activity button is pressed AC should be able to add a new activity.
        if (e.getSource() == addActivityButton){
            JFrame popUp = new JFrame();
            String actName = JOptionPane.showInputDialog(popUp, "Enter new Activity Name");
            
            System.out.println(actName);
            
            String actLoc = JOptionPane.showInputDialog(popUp, "Enter new Location");
            System.out.println(actLoc);
            
            String actPlan = JOptionPane.showInputDialog(popUp, "Enter new Activity Plan");
            System.out.println(actPlan);
            
            String actTutor = JOptionPane.showInputDialog(popUp, "Enter new Activity Tutor");
            System.out.println(actTutor);
            
            bl.coordinatorAddActivity(actName, actLoc, actPlan, actTutor);

        }
        
        if (e.getSource() == removeActivityButton){
            //remove selected activity
            activIndex = list.getSelectedIndex();
            bl.coordinatorRemoveActivity(activIndex);
            destroyCurrentFrame();
            activityCoordinatorPage();
        }
    }

    // This method is used to condense the huge amount of lines in the main GUI 
    // class.
    private void GUIConstructor() {
        // Student Button
        sButton.setBounds(50, 100, 300, 40);
        sButton.setFocusable(false);
        sButton.addActionListener(this);

        // Tutor Button
        tButton.setBounds(50, 150, 300, 40);
        tButton.setFocusable(false);
        tButton.addActionListener(this);
        // Activity Coordinator
        acButton.setBounds(50, 200, 300, 40);
        acButton.setFocusable(false);
        acButton.addActionListener(this);
        // Adds buttons
        frame.add(sButton);
        frame.add(tButton);
        frame.add(acButton);
        // Makes the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void StudentConstructor() {
        panel.setLayout(null);
        frame.add(comboBox);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(null);
        frame.setVisible(true);
        // this is where name is 
        nameLabel.setText("Name");
        nameLabel.setBounds(10, 10, 150, 50);
        ///panel.add(label);
        frame.add(nameLabel);

        nameField.setPreferredSize(new Dimension(300, 40));
        nameField.setBounds(120, 10, 250, 40);

        frame.add(nameField);
        // EHS number 
        numberLabel.setText("EHS Number");
        numberLabel.setBounds(10, 60, 150, 50);
        frame.add(numberLabel);

        numberField.setPreferredSize(new Dimension(300, 40));
        numberField.setBounds(120, 60, 250, 40);
        frame.add(numberField);

        //Login button
        slogin.setBounds(170, 150, 110, 50);
        frame.add(slogin);
        slogin.addActionListener(this);
        slogin.setText("login");

        String name1 = nameField.getText();
        System.out.println(name1);

    }
    
    private void TutorConstructor() {
        panel.setLayout(null);
        frame.add(comboBox);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(null);
        frame.setVisible(true);
        // this is where name is 
        nameLabel.setText("Name");
        nameLabel.setBounds(10, 10, 150, 50);
        ///panel.add(label);
        frame.add(nameLabel);

        nameField.setPreferredSize(new Dimension(300, 40));
        nameField.setBounds(120, 10, 250, 40);

        frame.add(nameField);

        //Login button
        tlogin.setBounds(170, 150, 110, 50);
        frame.add(tlogin);
        tlogin.addActionListener(this);
        tlogin.setText("login");

        String name1 = nameField.getText();
        System.out.println(name1);

    }
    

    private void studentActivityPage() {
        aList = bl.returnAList();
        System.out.println("We are in StudentActivityPage");
        insertRows(aList);
        System.out.println("Rows have been inserted");
    }
    
    private void tutorActivityPage(){
        tList = bl.returnTutorList(name);
        System.out.println("We are in TutorActivityPage");
        insertRowsTutor(tList);
        System.out.println("Rows have been inserted");
    }
    
    private void activityCoordinatorPage(){
        aList = bl.returnAList();
        System.out.println("We are in ActivityCoordinatorPage");
        insertRowsAC(aList);
        System.out.println("Rows have been inserted");
    }

    private void insertRows(String[] act) {
        JLabel greeting = new JLabel("Hello, " + name + ehn);
        greeting.setBounds(10, 420, 150, 50);

        //String subject[] = {"Rugby", "Basketball", "Chess", "Ice Skating"};
        JFrame frame1 = new JFrame("Select an Activity");
        JPanel panel1 = new JPanel();

        //Adds save button
        saveButtonStudent.setBounds(170, 380, 120, 40);
        saveButtonStudent.setFocusable(false);
        saveButtonStudent.addActionListener(this);

        frame1.add(saveButtonStudent);
        frame1.add(greeting);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JList list1 = new JList(act);
        list1.setBounds(1000, 100, 300, 150);
        panel1.add(list1);
        frame1.add(panel1);
        frame1.setSize(700, 500);
        frame1.setVisible(true);

        frame = frame1;
        panel = panel1;
        list = list1;
    }
    
    private void insertRowsTutor(String[] act) {
        JLabel greeting = new JLabel("Hello, " + name);
        greeting.setBounds(10, 420, 150, 50);

        //String subject[] = {"Rugby", "Basketball", "Chess", "Ice Skating"};
        JFrame frame1 = new JFrame("Select a Student");
        JPanel panel1 = new JPanel();

        //Adds plan button
        planButtonTutor.setBounds(170, 380, 120, 40);
        planButtonTutor.setFocusable(false);
        planButtonTutor.addActionListener(this);

        //Adds location button
        locationButtonTutor.setBounds(170, 380, 150, 40);
        locationButtonTutor.setLocation(310, 380);
        locationButtonTutor.setFocusable(false);
        locationButtonTutor.addActionListener(this);
        
        
        
        frame1.add(locationButtonTutor);
        frame1.add(planButtonTutor);
        frame1.add(greeting);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JList list1 = new JList(act);
        list1.setBounds(1000, 100, 300, 150);
        panel1.add(list1);
        frame1.add(panel1);
        frame1.setSize(700, 500);
        frame1.setVisible(true);

        frame = frame1;
        panel = panel1;
        list = list1;
    }
    
    private void insertRowsAC(String[] act) {
        JLabel greeting = new JLabel("Hello, Activity Coordinator");
        greeting.setBounds(10, 420, 150, 50);

        JFrame frame1 = new JFrame("Select an Activity");
        JPanel panel1 = new JPanel();

        //Adds plan button
        addActivityButton.setBounds(170, 380, 120, 40);
        addActivityButton.setFocusable(false);
        addActivityButton.addActionListener(this);

        //Adds location button
        removeActivityButton.setBounds(170, 380, 150, 40);
        removeActivityButton.setLocation(310, 380);
        removeActivityButton.setFocusable(false);
        removeActivityButton.addActionListener(this);
        
        
        
        frame1.add(removeActivityButton);
        frame1.add(addActivityButton);
        frame1.add(greeting);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JList list1 = new JList(act);
        list1.setBounds(1000, 100, 300, 150);
        panel1.add(list1);
        frame1.add(panel1);
        frame1.setSize(700, 500);
        frame1.setVisible(true);

        frame = frame1;
        panel = panel1;
        list = list1;
    }
    
    
    

    public String getName() {
        return name;
    }

    public String getEhn() {
        return ehn;
    }

    public int getScreenNumber() {
        return screenNumber;
    }

    public String userType() {
        return userType;
    }

    private void setUserType(String user) {
        userType = user;
    }

    public int getActivIndex() {
        return activIndex;
    }

    private void generateNewStudent(String num, String sName, Activity act) {
        Student s = new Student(num, sName, act);

        String sAct = s.getStudentActivity();

        String student = num + sName + sAct;
        int n = sList.length;
        sList[n] = student;
    }

    //Checks if the name has correct inputs.
    private boolean correctName(String s) {
        String checker = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        boolean correct = false;
        int lengthOfString = s.length();
        int counter = 0;

        for (int i = 0; i < lengthOfString; i++) {
            for (int j = 0; j < checker.length(); j++) {
                if (s.charAt(i) == checker.charAt(j)) {
                    System.out.println("Counter: " + counter);
                    counter++;
                }

            }

        }

        System.out.println("Coutner is: " + counter);
        System.out.println("Lenght of String: " + lengthOfString);

        if (counter == lengthOfString && counter > 0) {
            correct = true;
        }

        System.out.println("Correct is: " + correct);
        return correct;
    }
    
    private boolean correctTutorName(String s){
        //String[] array = {"Dan", "Cameron", "August", "Alessa"};
        String[] array = bl.returnAllTutorNames();
        ////////////////////////////////// Need to make this dynamic.
        boolean val = false;
       
        
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(s)){
                val = true;
            }
            
        }
        
        return val;
    }

    //Checks if the number is input correctly.
    private boolean correctNumber(String s) {
        String checker = "1234567890";
        boolean correct = false;
        int lengthOfString = s.length();
        int counter = 0;

        for (int i = 0; i < lengthOfString; i++) {
            for (int j = 0; j < checker.length(); j++) {
                if (s.charAt(i) == checker.charAt(j)) {
                    System.out.println("Counter: " + counter);
                    counter++;
                }

            }

        }
        
        System.out.println("Number Coutner is: " + counter);
        System.out.println("Number Lenght of String: " + lengthOfString);
        
        if (counter == lengthOfString && counter > 0) {
            correct = true;
        }
        
        System.out.println("Number Correct is: " + correct);
        return correct;
    }

    private void destroyCurrentFrame(){
        frame.dispose();
        frame.getContentPane().removeAll();
    }
}
