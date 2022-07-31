package com.school.management.model;


import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;

@Entity
@Immutable
@Subselect("SELECT student.id AS `student_id`, course.id AS `course_id`, student.name AS `student`, course.name AS `course` FROM (student, course) JOIN (student_course) " +
	" ON (student.id = student_course.student_id  AND student_course.course_id = course.id) ")
public class StudentCourseView {

	@EmbeddedId
	StudentCourseKey id;
	private String student;
	private String course;

	public String getStudent() {
		return student;
	}

	public String getCourse() {
		return course;
	}
}
