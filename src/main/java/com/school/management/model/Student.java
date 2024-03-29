package com.school.management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	@OneToMany(mappedBy = "student")
	@JsonManagedReference
	Set<StudentCourse> studentCourse;

	public Student() {
	}

	public Student(Long id) {
		this.id = id;
	}

	public Student(String name, String address, Timestamp createdAt, Timestamp updatedAt, Set<StudentCourse> studentCourse) {
		this.name = name;
		this.address = address;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.studentCourse = studentCourse;
	}

	public Student(Long id, String name, String address, Timestamp createdAt, Timestamp updatedAt, Set<StudentCourse> studentCourse) {
		this(name, address, createdAt, updatedAt, studentCourse);
		this.id = id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
