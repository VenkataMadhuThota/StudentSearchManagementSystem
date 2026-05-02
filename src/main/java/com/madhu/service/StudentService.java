package com.madhu.service;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madhu.entity.StudentEntity;
import com.madhu.repository.StudentRepository;

@Service
public class StudentService 
{
	@Autowired
	private StudentRepository repository;

	public List<StudentEntity> getAllactiveStudents() 
	{
		return repository.findActiveStudents();
	}
	public List<StudentEntity> getAllinactiveStudents() 
	{
		return repository.findInActiveStudents();
	}
	public List<StudentEntity> getAllStudents() 
	{
		return repository.findAll();
	}
	public void addStudents(StudentEntity entity) 
	{
		entity.setStatus("ACTIVE");
		repository.save(entity);
	}
	public StudentEntity getStudentBYID(int id) {
		return repository.findById(id).orElseThrow(()->  new RuntimeException("Student not found with id: " + id));
	}

	public void updateStudentDetails(int id,StudentEntity entity) 
	{
		StudentEntity object = repository.findById(id).orElseThrow(()->  new RuntimeException("Student not found with id: " + id));
		object.setName(entity.getName());
		object.setFees(entity.getFees());
		object.setEmail(entity.getEmail());
		object.setCourse(entity.getCourse());
		object.setStatus(entity.getStatus());
		repository.save(object);
	}
	public void deleteStudent(int id) 
	{
		    StudentEntity student = getStudentBYID(id);
		    if(student!=null)
		    	student.setStatus("INACTIVE");
		    repository.save(student);
	}
	public void deleteStudent1(int id) 
	{
		 StudentEntity student = getStudentBYID(id);
		if(student==null)
			throw new RuntimeException("Student not found");
		else
			repository.deleteById(id);
	}
	public void setStudentActive(int id) 
	{
		 StudentEntity student = getStudentBYID(id);
			if(student==null)
				throw new RuntimeException("Student not found");
			else
			{
				student.setStatus("ACTIVE");
				repository.save(student);
			}
	}
	public List<StudentEntity> getStudentsByName(String name) {
	    return repository.findByName(name);
	}

	public StudentEntity getStudentByEmail(String email) {
	    return repository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
	}
}
