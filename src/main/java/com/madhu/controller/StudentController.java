package com.madhu.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.madhu.entity.StudentEntity;
import com.madhu.service.StudentService;
import jakarta.validation.Valid;



@Controller
public class StudentController 
{
	@Autowired
	private StudentService service;

    @GetMapping("/students")
	public String getAllStudents(Model model)
	{
		model.addAttribute("students",service.getAllStudents());
		return "students";
	}
    @GetMapping("/inactivestudents")
	public String getInActiveStudents(Model model)
	{
		model.addAttribute("students",service.getAllinactiveStudents());
		return "students";
	}
  //Read Operation
	@GetMapping("/activestudents")
	public String getActiveStudents(Model model)
	{
		model.addAttribute("students",service.getAllactiveStudents());
		return "students";
	}
	//Create Operation
	@GetMapping("/addstudent")
	public String addStudent(Model model)
	{
		 model.addAttribute("student", new StudentEntity());
		return "add-student";
	}
	@PostMapping("/savestudent")
	public String addStudent(@Valid @ModelAttribute("student") StudentEntity entity,BindingResult result)
	{
		if(result.hasErrors()) {
	        return "add-student";
	    }
		service.addStudents(entity);
		return "redirect:/students";
	}
	//Update Operation
	@GetMapping("/editstudent/{id}")
	public String editStudent(@PathVariable int id,Model model)
	{
		model.addAttribute("student",service.getStudentBYID(id));
		return "edit-student";
	}
	@PostMapping("/updatestudent/{id}")
	public String updateStudent(@PathVariable int id,@ModelAttribute StudentEntity entity)
	{
		service.updateStudentDetails(id,entity);
		return "redirect:/students";
	}
	//Soft Delete Operation
	@GetMapping("/deletestudent/{id}")
	public String deleteStudent(@PathVariable int id) {
	    service.deleteStudent(id);
	    return "redirect:/students";
	}
	//Hard delete operation
	@GetMapping("/deletestudent1/{id}")
	public String deleteStudent1(@PathVariable int id) {
	    service.deleteStudent1(id);
	    return "redirect:/students";
	}
	@GetMapping("/setstudentactive/{id}")
	public String setStudentActive(@PathVariable int id)
	{
		service.setStudentActive(id);
		return "redirect:/students";
	}
	
	//find operations
	@GetMapping("/find")
	public String showFindPage() {
	    return "find";
	}

	@GetMapping("/users/findById")
	public String getByID(@RequestParam int id, Model model) {
	    StudentEntity entity = service.getStudentBYID(id);
	    model.addAttribute("student", entity);
	    return "find";
	}
	@GetMapping("/users/findByName")
	public String getByName(@RequestParam String name, Model model) {
	    List<StudentEntity> list = service.getStudentsByName(name);
	    model.addAttribute("students", list);
	    model.addAttribute("student", null);
	    return "find";
	}

	@GetMapping("/users/findByEmail")
	public String getByEmail(@RequestParam String email, Model model) {
	    StudentEntity entity = service.getStudentByEmail(email);
	    model.addAttribute("student", entity);
	    return "find";
	}
	
}
