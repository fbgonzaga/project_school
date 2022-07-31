package com.school.management.model.dto;

public class StudentCourseDto {

	private Long studentId;
	private String studentName;
	private Long courseId;
	private String courseName;

	public StudentCourseDto() {
	}

	public StudentCourseDto(Long studentId, String studentName, Long courseId, String courseName) {
		this.studentId = studentId;
		this.studentName = studentName;
		this.courseId = courseId;
		this.courseName = courseName;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}
