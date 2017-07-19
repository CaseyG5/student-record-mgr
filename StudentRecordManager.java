package studentrecordmanager;

import java.util.ArrayList;

interface Iterator<E> {
    boolean hasNext();
    E getNext();
}

class Student {
    final String FNAME;
    final String LNAME;
    final int ID;
    String email;
    ArrayList<Course> coursesTaken;

    Student(String fn, String ln, String ea) {
        FNAME = fn;  LNAME = ln;
        ID = 1001 + StudentRecordManager.allStudents++;
        email = ea;
        coursesTaken = new ArrayList<>();
    }

    Student(Student s) {
        FNAME = s.FNAME;  
        LNAME = s.LNAME;
        email = s.email;
        ID = s.ID;
        coursesTaken = s.coursesTaken;
    }

    public Iterator<Course> getCourseIterator() 
    {  return new CourseIterator();  }

    private class CourseIterator implements Iterator<Course> {
        int i=0;

        public boolean hasNext() 
        {  return i < coursesTaken.size(); }

        public Course getNext()
        {  return coursesTaken.get(i++);  }
    }
}
    
class Course {
    String title;
    char gradeRcvd;

    Course(String t, char g) {
        title = t;
        gradeRcvd = g;
    }

    Course(Course c) {
        title = c.title;
        gradeRcvd = c.gradeRcvd;
    }
}

class TranscriptPrinter {

    public void PrintTranscript(Iterator<Student> si, int id) {
        Student s;
        System.out.println(" /// Begin transcript");
        while(si.hasNext()) {
            s = si.getNext();
            if(s.ID == id) {
                System.out.println("" + s.LNAME + ", " 
                        + s.FNAME + " - " + s.ID);
                for( Course c : s.coursesTaken ) 
                    System.out.printf("%c - %s\n", c.gradeRcvd, c.title );
                System.out.println(" /// End transcript\n");
            }
        }
    }

    public void PrintTranscript(Iterator<Student> si, int ... id) {
        Student s;
        System.out.println(" /// Begin transcripts");
        while(si.hasNext()) {
            s = si.getNext();
            for(int i : id)
                if(s.ID == i) {
                    System.out.println("" + s.LNAME + ", " 
                            + s.FNAME + " - " + s.ID);
                    for( Course c : s.coursesTaken ) 
                        System.out.printf("%c - %s\n", c.gradeRcvd, c.title );
                    System.out.println();
                }
        }
        System.out.println(" /// End transcripts\n");
    }
}

public class StudentRecordManager {

    private ArrayList<Student> students;
    static int allStudents;
    
    StudentRecordManager() {
        students = new ArrayList<>();
        allStudents = 0;
    }
    
    public void addStudent(String fn, String ln, String ea) {
        students.add(new Student(fn, ln, ea));
        System.out.println("Student " + ln + ", " + fn + " added.");
    }
    
    public Student removeStudent(int id) {
        int index = -1;
        
        for(int i=0; i<students.size(); i++)
            if(students.get(i).ID == id) {
                index = i; break;
            }
        if(index == -1) {
            System.out.println("Student not found.");
            return null;
        }
        System.out.println("Student " + id + " removed.");
        return students.remove(index);
    }
    
    public Student searchStudents(int id) {
        for(Student x : students)
            if(x.ID == id)
                return new Student(x);
        System.out.println("Student not found.");
        return null;
    }
    
    public ArrayList<Student> searchStudents(String name) {
        ArrayList<Student> matches = new ArrayList<>();
        
        for(Student x : students)
            if(x.LNAME.contains(name) || x.FNAME.contains(name))
                matches.add(new Student(x));
        return matches;
    }
    
    public void addCourse(String t, char g, int id) {
        for( Student x : students )
            if(x.ID == id) {
                x.coursesTaken.add(new Course(t, g));
                System.out.println("Course " + t + " added for "
                        + "student " + x.LNAME + ", " + x.FNAME);
                break;
            }     
    }
    
    public Course removeCourse(String t, int id) {
        int cIndex = -1;
        
        for( Student x : students )
            if(x.ID == id) {
                for(int i=0; i< x.coursesTaken.size(); i++) 
                    if(x.coursesTaken.get(i).title.equalsIgnoreCase(t)) {
                        cIndex = i;  break;
                    }
                if( cIndex != -1 ) {
                    System.out.println("Course " 
                            + x.coursesTaken.get(cIndex).title + " removed.");
                    return new Course (x.coursesTaken.remove(cIndex));
                }
            }
        System.out.println("Course not found.");
        return null;
    }
    
    public Iterator<Student> getStudentIterator() {  
        return new Iterator<Student>() {
            int i=0;
        
            public boolean hasNext() 
            {  return i < students.size();  }
        
            public Student getNext()
            {  return students.get(i++);  }
        };
    }
    
    public static void main(String[] args) {
        StudentRecordManager srm = new StudentRecordManager();
        
        srm.addStudent("Alan", "Turing", "aturing@atcs.org");
        srm.addStudent("Raymond", "Smullyan", "ray@smully.com");
        srm.addStudent("Happy", "Go Lucky", "guy@happy.net");
        srm.addStudent("Stevie", "Ray Vaughn", "stevie@bcc.edu");
        System.out.println();
        srm.addCourse("Advanced State Machines", 'A', 1001);
        srm.addCourse("Theories of Computation", 'A', 1001);
        srm.addCourse("Advanced Logic", 'A', 1002);
        srm.addCourse("Organic Chemistry", 'C', 1003);
        srm.addCourse("Urban Planning", 'B', 1003);
        srm.addCourse("Ethical Hacking", 'A', 1003);
        srm.addCourse("Advanced Blues Guitar", 'A', 1004);
        System.out.println();
        
        TranscriptPrinter tp = new TranscriptPrinter();
        tp.PrintTranscript(srm.getStudentIterator(), 1001, 1002, 1003, 1004);
        
        srm.removeCourse("Organic Chemistry", 1003);
        srm.removeCourse("Theories of Computation", 1001);
        srm.removeCourse("Volleyball", 1003);
        
        ArrayList<Student> searchResults = srm.searchStudents("Ray");
        System.out.println("\nFound the following " + searchResults.size() 
                + " students:");
        for(Student s : searchResults)
            System.out.println(s.LNAME + ", " + s.FNAME);
        System.out.println();
        
        srm.removeStudent(1004);
        srm.removeStudent(1001);
        srm.removeStudent(1002);
        srm.removeStudent(1001);
        System.out.println();
        tp.PrintTranscript(srm.getStudentIterator(), 1003);
    }
}
/* run:
Student Turing, Alan added.
Student Smullyan, Raymond added.
Student Go Lucky, Happy added.
Student Ray Vaughn, Stevie added.

Course Advanced State Machines added for student Turing, Alan
Course Theories of Computation added for student Turing, Alan
Course Advanced Logic added for student Smullyan, Raymond
Course Organic Chemistry added for student Go Lucky, Happy
Course Urban Planning added for student Go Lucky, Happy
Course Ethical Hacking added for student Go Lucky, Happy
Course Advanced Blues Guitar added for student Ray Vaughn, Stevie

 /// Begin transcripts
Turing, Alan - 1001
A - Advanced State Machines
A - Theories of Computation

Smullyan, Raymond - 1002
A - Advanced Logic

Go Lucky, Happy - 1003
C - Organic Chemistry
B - Urban Planning
A - Ethical Hacking

Ray Vaughn, Stevie - 1004
A - Advanced Blues Guitar

 /// End transcripts

Course Organic Chemistry removed.
Course Theories of Computation removed.
Course not found.

Found the following 2 students:
Smullyan, Raymond
Ray Vaughn, Stevie

Student 1004 removed.
Student 1001 removed.
Student 1002 removed.
Student not found.

 /// Begin transcript
Go Lucky, Happy - 1003
B - Urban Planning
A - Ethical Hacking
 /// End transcript
*/
