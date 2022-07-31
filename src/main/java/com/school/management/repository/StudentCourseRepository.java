package com.school.management.repository;

import com.school.management.model.Course;
import com.school.management.model.Student;
import com.school.management.model.StudentCourse;
import com.school.management.model.StudentCourseKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseKey> {

	@Modifying
	@Query("DELETE FROM StudentCourse sc " +
		"WHERE sc.student = :student")
	void deleteCoursesByStudent(Student student);

	@Modifying
	@Query("DELETE FROM StudentCourse sc " +
		"WHERE sc.course = :course")
	void deleteStudentsByCourse(Course course);
}
