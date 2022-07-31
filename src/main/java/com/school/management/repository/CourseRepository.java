package com.school.management.repository;

import com.school.management.model.Course;
import com.school.management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

	@Query("SELECT c FROM Course c LEFT JOIN c.studentCourse sc " +
		"WHERE sc.student = :student")
	List<Course> getCoursesByStudent(@Param("student") Student student);

	@Query("SELECT c FROM Course c LEFT JOIN c.studentCourse sc " +
		"WHERE sc.course IS NULL")
	List<Course> getCoursesWithoutStudent();
}
