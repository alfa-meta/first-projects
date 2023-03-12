/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework2;
import java.util.ArrayList;
/**
 *
 * @author tstuikys
 */

/*
    Should be accessed by all users
    With varying degrees of interactivity
*/

/// List of Default Tutors:
///     Dan, Cameron, August, Alessa.
public class Activity {
    
    private String activityName;
    private String location;
    private String plan;
    private String tutor;
    private ArrayList<Student> studentsInActivity = new ArrayList<>(); 
    
    /*
        Activity Constructor
        nOStudents = numOfStudents;
        aName = activityName;
        loc = location
        p = plan
    */
    public Activity(String aName, String loc, String p, String t){
        this.activityName = aName;
        this.location = loc;
        this.plan = p;
        this.tutor = t;
    }
    
    
    public String getActivityName(){
        return activityName;
    }
    
    public String getActivityLocation(){
        return location;
    }
    
    public void setActivityLocation(String s){
        location = s;
    }
    
    public void setActivityPlan(String s){
        plan = s;
    }
    
    public String getActivityPlan(){
        return plan;
    }
    
    public void addStudentToList(Student s){
        studentsInActivity.add(s);
    }
    
    public String getTutorName(){
        return tutor;
    }
    
}
