package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.GEnrollment_service;


import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/enrollments")
public class G_EnrollmentController {
	
	   @Autowired
	    private GEnrollment_service gEnrollmentsService;
	
	   @GetMapping("/view")
	   public String viewEnrollments(Model model) {
	       try {
	           List<Map<String, Object>> enrollments = gEnrollmentsService.showEnrollments();
	           model.addAttribute("enrollments", enrollments);
	           return "viewEnrollment"; // Return the name of the HTML template
	       } catch (Exception e) {
	           // Handle exception if any
	           return "error"; // Return the name of the error page template
	       }
	   }
	   
	   @GetMapping("/enroll")
	    public String showEnrollmentForm() {
	        return "enrollment_page"; // Return the name of the HTML template for the enrollment form
	    }

	    // POST method to handle enrollment submission
	    @PostMapping("/enroll")
	    public String enrollStudent(@RequestParam String bNumber, @RequestParam String classId, Model model) {
	        // Call the service method to enroll the student into the class
	        String enrollmentStatus = gEnrollmentsService.enrollStudentIntoClass(bNumber, classId);
	        model.addAttribute("enrollmentStatus", enrollmentStatus);
	        return "enrollment_page"; // Return the name of the HTML template to display the enrollment status
	    }
	    
	    @GetMapping("/drop")
	    public String showDropStudentForm() {
	        return "drop_student"; // Return the name of the HTML template for the drop student form
	    }

	    @PostMapping("/drop")
	    public String dropStudent(@RequestParam String bNumber, @RequestParam String classId, Model model) {
	        String result;
	      
	            // Call the service method to drop the student from the class
	            result = gEnrollmentsService.dropStudentFromClass(bNumber, classId);
	        
	        model.addAttribute("message", result); // Add the result message to the model
	        return "drop_student"; // Return the name of the HTML template
	    }
	}


