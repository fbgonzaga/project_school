package com.school.management.repository;

import com.school.management.model.Course;
import com.school.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("SELECT s FROM Student s LEFT JOIN s.studentCourse sc " +
		"WHERE sc.course = :course")
	List<Student> getStudentsByCourse(@Param("course") Course course);

	@Query("SELECT s FROM Student s LEFT JOIN s.studentCourse sc " +
		"WHERE sc.student IS NULL")
	List<Student> getStudentsWithoutCourse();
}
