package com.school.management.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StudentCourseKey implements Serializable {

	@Column(name = "student_id")
	Long studentId;

	@Column(name = "course_id")
	Long courseId;

	public StudentCourseKey(){}
	public StudentCourseKey(Long studentId, Long courseId) {
		this.studentId = studentId;
		this.courseId = courseId;
	}
}
