package com.school.management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class StudentCourse {
	@EmbeddedId
	StudentCourseKey id;

	@ManyToOne
	@JsonBackReference
	@MapsId("studentId")
	Student student;

	@ManyToOne
	@JsonBackReference
	@MapsId("courseId")
	Course course;

	public StudentCourse(){}

	public StudentCourse(Student student, Course course) {
		this.id = new StudentCourseKey(student.getId(), course.getId());
		this.student = student;
		this.course = course;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
