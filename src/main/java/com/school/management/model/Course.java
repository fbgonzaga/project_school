package com.school.management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	@OneToMany(mappedBy = "course")
	@JsonManagedReference
	Set<StudentCourse> studentCourse;

	public Course() {
	}

	public Course(Long id) {
		this.id = id;
	}

	public Course(String name, Timestamp createdAt, Timestamp updatedAt, Set<StudentCourse> studentCourse) {
		this.name = name;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.studentCourse = studentCourse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<StudentCourse> getStudentCourse() {
		return studentCourse;
	}

	public void setStudentCourse(Set<StudentCourse> studentCourse) {
		this.studentCourse = studentCourse;
	}
}
