package com.school.management.repository;

import com.school.management.model.StudentCourseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentCourseViewRepository extends JpaRepository<StudentCourseView, Long> {
	@Query("SELECT scv FROM StudentCourseView scv " +
		"ORDER BY student, course")
	List<StudentCourseView> getStudentCourseRelationship();

	@Query("SELECT scv FROM StudentCourseView scv " +
		"ORDER BY course, student")
	List<StudentCourseView> getCourseStudentRelationship();
}
