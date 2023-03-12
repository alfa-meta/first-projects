/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework2;

/**
 *
 * @author tstuikys
 */

/*
    This class is able to:
        View all activities
        Choose one activity
        Have a name
        Have an ID number
        All data is kept in students.txt
*/
public class Student extends User {
    private Activity activity;
    
    // Constructor for Student object
    public Student(String idn, String nameOfStudent, Activity act){
        this.userType = "Student";
        this.id = idn;
        this.name = nameOfStudent;
        this.activity = act;
    }
    
    public String getStudentName(){
        return name;
    }
    
    public String getStudentId(){
        return id;
    }
    
    public String getStudentActivity(){
        String actName = activity.getActivityName();
        return actName;
    }
    
    public void setStudentActivity(Activity act){
        activity = act;
        System.out.println(activity.getActivityName() + " Activity has been added to " + getStudentName());
    }
}
