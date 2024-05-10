package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.Courses_service;
import com.example.demo.services.Student_service;

@Controller
@RequestMapping("/courses")
public class CoursesController {
	
	@Autowired
    private Courses_service courseService;
	
	@GetMapping("/view")
    public String showCourses(Model model) {
        List<Map<String, Object>> courses = courseService.showCourses();
        model.addAttribute("courses", courses);
        return "view_courses"; // Return the name of the HTML template
    }
	
	@GetMapping("/add")
	public String showAddCourseForm() {
	    return "add_courses"; // Return the name of the HTML template for the add course form
	}

	@PostMapping("/add")
	public String addCourse(@RequestParam String deptCode, @RequestParam int courseNumber,
	                        @RequestParam String title, Model model) {
	    courseService.addCourse(deptCode, courseNumber, title);
	    // Add any necessary data to pass to the view
	    model.addAttribute("message", "Course added successfully.");
	    return "add_courses"; // Return the name of the HTML template
	}
	
	@GetMapping("/delete")
	public String showDeleteCourseForm() {
	    // Add any necessary data to pass to the view
	    return "delete_courses"; // Return the name of the HTML template for the delete course form
	}

	@PostMapping("/delete")
	public String deleteCourse(@RequestParam String deptCode, @RequestParam int courseNumber, Model model) {
	    courseService.deleteCourse(deptCode, courseNumber);
	    // Add any necessary data to pass to the view
	    model.addAttribute("message", "Course deleted successfully.");
	    return "redirect:/"; // Return the name of the HTML template
	}


}
