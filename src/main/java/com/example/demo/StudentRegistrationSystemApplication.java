package com.example.demo;


import com.example.demo.services.ScoreGrade_service;
import com.example.demo.services.Classes_service;
import com.example.demo.services.CourseCredit_service;
import com.example.demo.services.Courses_service;

import com.example.demo.services.Student_service;



import com.example.demo.services.GEnrollment_service;

import com.example.demo.services.Logs_service;
import com.example.demo.services.Prerequisite_Service;
import com.example.demo.Databaseconfig.Db_Configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo.controller","com.example.demo.services","com.example.demo.Databaseconfig"})
public class StudentRegistrationSystemApplication implements CommandLineRunner {

    @Autowired
    private Student_service studentService;

    @Autowired
    private Courses_service courseService;

    @Autowired
    private CourseCredit_service courseCreditService;

    @Autowired
    private Classes_service classesService;

    @Autowired
    private ScoreGrade_service scoreGradeService;
    
    @Autowired
    private GEnrollment_service gEnrollmentsService;
    
    @Autowired
    private Prerequisite_Service PrerequisiteService; 
    
    @Autowired
    private Logs_service logService;
    
    @Autowired
    private Db_Configuration dbConfig;
    
    

    public static void main(String[] args) {
    	
        SpringApplication.run(StudentRegistrationSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    	boolean exit = false;
    	while(!exit) {
        	System.out.println();
        	String input;
        	BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        	System.out.println("Select anyone from the following:");
        	System.out.println("1.Display all tuples");
        	System.out.println("2.Add new student");
        	System.out.println("3 check student information enrolled in the class");
        	System.out.println("4.Display preriquisite courses");
        	System.out.println("5.Enroll students into class");
        	System.out.println("6.drop enrolled students from class");
        	System.out.println("7.delete a student from student table");
        	System.out.println("8. add courses");
        	System.out.println("9. delete courses");
        	System.out.println("10. add classes");
        	System.out.println("11. delete classes");
			System.out.println("12.exit");
        	
        	input = userInput.readLine();
        	
        	boolean exit1 = false;
        	if(input.equals("1")) {
        		while(!exit1) {
        			System.out.println();
        			String tableselected;
        			
        			System.out.println("choose a table to display:");
        			System.out.println("1.students");
        			System.out.println("2.courses");
        			System.out.println("3.classes");
        			System.out.println("4.scoregrades");
        			System.out.println("5.Enrollments");
        			System.out.println("6.CourseCredits");
        			System.out.println("7.Prerequisites");
        			
        			System.out.println("8.logs");
        			
					tableselected = userInput.readLine();
					
					switch(tableselected){
                        case "1":
                        	     showStudents();;
                                 exit1=true;
                                 break;
                        case "2":
                        	     showCourses();
                                 exit1=true;
                                 break;
                        case "3":
                        	     showClasses();
                                 exit1=true;
                                 break;
                        case "4":
                        	     showScoreGrades();
                                 exit1 = true;
                                 break;
                        case "5":
                        	     showEnrollments();
                                 exit1 = true;
                                 break; 
                        case "6":
                        	     showCourseCredits();
                                 exit1 = true;
                                 break;
                                 
                        case "7":
                        	    showPrerequisite();
                                exit1 = true;
                                break;   
                                
                        case "8":
                        	    showLogs();
                                exit1 = true;
                                break;           
                                
                        default:
                                System.out.println("Invalid input!Please select correct option");
                                break;

                    }
        		}
        	}
        	//BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
     
        	else if(input.equals("2")) {
                String BNumber;
            String firstname;
            String lastname;
            String status;
            Double gpa;
            String email;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            System.out.println("Enter Bnumber :");
            BNumber = userInput.readLine();
            System.out.println("Enter student firstname :");
            firstname = userInput.readLine();
            System.out.println("Enter student lastname :");
            lastname = userInput.readLine();
            System.out.println("Enter student status :");
            status = userInput.readLine();
            System.out.println("Enter student gpa :");
            gpa = Double.parseDouble(userInput.readLine());
            System.out.println("Enter student email :");
            email = userInput.readLine();
            System.out.println("Enter student bdate (yyyy-MM-dd):");
        	String bdateString = userInput.readLine();
        	Date bdate;
        	try {
        	    bdate = dateFormat.parse(bdateString);
        	} catch (ParseException e) {
        	    System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
        	    return; // Exit the block if parsing fails
        	}
            	studentService.add_students(BNumber, firstname, lastname,status, gpa, email, bdate);
    	}
        	
        	else if(input.equals("3")) {
                try {
                    System.out.println("Enter the class ID:");
                    String classId = userInput.readLine();

                    // Invoke the method to list students in the class
                    studentService.listStudents(classId);
                } catch (IOException ex) {
                    System.out.println("An error occurred while reading input: " + ex.getMessage());
                }
        	}
        	
        	else if (input.equals("4")) {
        	    String deptCode;
        	    int courseNum;
        	    try {
        	        System.out.println("Enter Department Code: ");
        	        deptCode = userInput.readLine();
        	        System.out.println("Enter Course Number: ");
        	        courseNum = Integer.parseInt(userInput.readLine());
        	        
        	        // Invoke the method to show prerequisites
        	        courseService.showPrerequisites(deptCode, courseNum);
        	    } catch (IOException ex) {
        	        System.out.println("An error occurred while reading input: " + ex.getMessage());
        	    } catch (NumberFormatException ex) {
        	        System.out.println("Invalid input for course number: " + ex.getMessage());
        	    }
        	}
        	else if (input.equals("5")) {
        	    String BNumber;
        	    String classId;
        	    System.out.println("Enter BNumber: ");
        	    BNumber = userInput.readLine();
        	    System.out.println("Enter Class ID: ");
        	    classId = userInput.readLine();
        	    
        	    // Invoke the method to enroll the student into the class
        	    String enrollmentStatus = gEnrollmentsService.enrollStudentIntoClass(BNumber, classId);
        	    System.out.println(enrollmentStatus);
        	}
        	else if(input.equals("6")) {
				String s_id;
            String classid;
            System.out.println("Enter Student B-number: ");
            s_id = userInput.readLine();
            System.out.println("Enter classid: ");
            classid = userInput.readLine();
            String result = gEnrollmentsService.dropStudentFromClass(s_id, classid);
            System.out.println(result);      		
			}
        	
        	else if(input.equals("7")) {
        		System.out.println("Enter the B# of the student to delete: ");
                String bnum = userInput.readLine();
                
                studentService.deleteStudent(bnum); 		
			}
        	
        	
        
        	else if (input.equals("8")) {
        		String deptCode;
        		int courseNumber;
        		String title;
        		 System.out.println("Enter department code: ");
        		 deptCode = userInput.readLine();
        		 System.out.println("Enter course number");
        		 courseNumber =Integer.parseInt(userInput.readLine());
        		 System.out.println("Enter the title of course");
        		 title = userInput.readLine();
        		 courseService.addCourse(deptCode, courseNumber, title);
        		 
        		
        	}
        	else if (input.equals("9")) {
        		String deptCode;
        		int courseNumber;
        		String title;
        		 System.out.println("Enter department code: ");
        		 deptCode = userInput.readLine();
        		 System.out.println("Enter course number");
        		 courseNumber =Integer.parseInt(userInput.readLine());
        		 courseService.deleteCourse(deptCode, courseNumber);
        		 
        		
        	}
        	
        	else if (input.equals("10")) {
        		String classId;
        		String deptCode;
        		int courseNumber;
        		int sectionNumber;
        		int year;
        		String semester;
        		int limit;
        		int classSize;
        		String room;
        		System.out.print("Enter class ID: ");
                classId = userInput.readLine();

                System.out.print("Enter department code: ");
                deptCode = userInput.readLine();

                System.out.print("Enter course number: ");
                courseNumber = Integer.parseInt(userInput.readLine());

                System.out.print("Enter section number: ");
                sectionNumber =Integer.parseInt(userInput.readLine());

                System.out.print("Enter year: ");
                year = Integer.parseInt(userInput.readLine());

                System.out.print("Enter semester: ");
                semester = userInput.readLine();

                System.out.print("Enter limit: ");
                limit = Integer.parseInt(userInput.readLine());

                System.out.print("Enter class size: ");
                classSize = Integer.parseInt(userInput.readLine());

                System.out.print("Enter room: ");
                room = userInput.readLine();

                // Calling the service method to add the class
                classesService.addClass(classId, deptCode, courseNumber, sectionNumber, year, semester, limit, classSize, room);
            }
        		
        	else if (input.equals("11")) {
        		System.out.print("Enter the class ID to delete: ");
                String classIdToDelete = userInput.readLine();

                // Call the deleteClass method to delete a class
                classesService.deleteClass(classIdToDelete);
        		 
        		
        	}
        		
        	}
    	
        	
    	
    	
    
    }

	
    
   
    private void showStudents() {
        List<Map<String, Object>> students = studentService.showStudents();

        // Display the information of all students
        System.out.println("Students:");
        for (Map<String, Object> student : students) {
            System.out.println("Student ID: " + student.get("BNumber") +
                    ", First Name: " + student.get("FirstName") +
                    ", Last Name: " + student.get("LastName") +
                    ", Level: " + student.get("Level") +
                    ", GPA: " + student.get("Gpa") +
                    ", Email: " + student.get("Email") +
                    ", Birth Date: " + student.get("BirthDate"));
        }
    }
    
    private void showCourses() {
        try {
            // Fetch the list of courses from the service class
            List<Map<String, Object>> courses = courseService.showCourses();
            
            // Display the information of all courses
            System.out.println("All courses:");
            for (Map<String, Object> course : courses) {
                System.out.println("Department Code: " + course.get("deptCode") +
                        ", Course Number: " + course.get("courseNumber") +
                        ", Title: " + course.get("title"));
            }
        } catch (Exception e) {
            System.out.println("\n*** Exception caught while fetching courses ***\n");
            e.printStackTrace();
        }
    }
    
    private void showClasses() {
        try {
            // Call the showClasses method from the service class
            List<Map<String, Object>> classes = classesService.showClasses();

            // Display the information of all classes
            System.out.println("All classes:");
            for (Map<String, Object> classInfo : classes) {
                System.out.println("Class ID: " + classInfo.get("ClassId") +
                        ", Department Code: " + classInfo.get("DeptCode") +
                        ", Course Number: " + classInfo.get("CourseNumber") +
                        ", Section Number: " + classInfo.get("SectionNumber") +
                        ", Year: " + classInfo.get("Year") +
                        ", Semester: " + classInfo.get("Semester") +
                        ", Limit: " + classInfo.get("Limit") +
                        ", Class Size: " + classInfo.get("ClassSize") +
                        ", Room: " + classInfo.get("Room"));
            }
        } catch (Exception e) {
            System.out.println("\n*** Exception caught while fetching classes ***\n");
            e.printStackTrace();
        }
    }



    
    private void showScoreGrades() {
    	List<Map<String, Object>> scoreGrades = scoreGradeService.showScoreGrades();

         // Display the retrieved score grades
         System.out.println("All Score Grades:");
         for (Map<String, Object>  scoreGrade : scoreGrades) {
             System.out.println("score: " + scoreGrade.get("score") + ", lgrade: " + scoreGrade.get("lgrade"));
         }
    	
    }
    public void showEnrollments() {
        try {
            // Call the showEnrollments method from the service class
            List<Map<String, Object>> enrollments = gEnrollmentsService.showEnrollments();

            // Print the enrollments
            System.out.println("All graduate student enrollments:");
            for (Map<String, Object> enrollment : enrollments) {
                System.out.println("BNumber: " + enrollment.get("BNumber") +
                                   ", ClassId: " + enrollment.get("ClassId") +
                                   ", Score: " + enrollment.get("Score"));
            }
        } catch (Exception e) {
            System.out.println("\n*** Exception caught while fetching enrollments ***\n");
            e.printStackTrace();
        }
    }
    public void showCourseCredits() {
        try {
            // Call the show_enrollments method from the service class
        	 System.out.println("All course credits");
        	 courseCreditService.showCourseCredits();
        } catch (Exception e) {
            System.out.println("\n*** Exception caught while fetching enrollments ***\n");
            e.printStackTrace();
        }
    }
    
    public void showPrerequisite() {
        try {
            // Call the show_enrollments method from the service class
        	 System.out.println("All Prerequisites");
        	 PrerequisiteService.showPrerequisite();
        } catch (Exception e) {
            System.out.println("\n*** Exception caught while fetching enrollments ***\n");
            e.printStackTrace();
        }
    }
    
    public void showLogs() {
        try {
            // Call the showLogs method from the service class
            System.out.println("All Logs:");
            List<Map<String, Object>> logs = logService.showLogs();
            for (Map<String, Object> log : logs) {
                System.out.println("Log#: " + log.get("Log#") +
                        ", User Name: " + log.get("User Name") +
                        ", Operation Time: " + log.get("Operation Time") +
                        ", Table Name: " + log.get("Table Name") +
                        ", Operation: " + log.get("Operation") +
                        ", Tuple Key Value: " + log.get("Tuple Key Value"));
            }
        } catch (Exception e) {
            System.out.println("\n*** Exception caught while fetching logs ***\n");
            e.printStackTrace();
        }
    }

}
        