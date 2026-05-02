package com.madhu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.madhu.entity.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer>
{
	@Query(value = "SELECT * FROM student_entity WHERE status='ACTIVE'", nativeQuery = true)
	public 	List<StudentEntity> findActiveStudents();
	
	@Query(value = "SELECT * FROM student_entity WHERE status='INACTIVE'", nativeQuery = true)
	public 	List<StudentEntity> findInActiveStudents();

	List<StudentEntity> findByName(String name);

	Optional<StudentEntity> findByEmail(String email);
     
}
