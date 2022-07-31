package com.school.management.service;


import com.school.management.model.Course;
import com.school.management.model.Student;
import com.school.management.model.StudentCourse;
import com.school.management.model.StudentCourseView;
import com.school.management.model.dto.CourseDto;
import com.school.management.model.dto.StudentCourseDto;
import com.school.management.model.dto.StudentDto;
import com.school.management.repository.CourseRepository;
import com.school.management.repository.StudentCourseRepository;
import com.school.management.repository.StudentCourseViewRepository;
import com.school.management.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final StudentCourseRepository studentCourseRepository;
	private final StudentCourseViewRepository studentCourseViewRepository;


	public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, StudentCourseRepository studentCourseRepository, StudentCourseViewRepository studentCourseViewRepository) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
		this.studentCourseRepository = studentCourseRepository;
		this.studentCourseViewRepository = studentCourseViewRepository;
	}

	public List<StudentDto> getStudents() {
		return studentRepository.findAll().stream()
			.map(student -> new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	public List<StudentDto> getStudentsWithoutCourse() {
		return studentRepository.getStudentsWithoutCourse().stream()
			.map(student -> new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	public StudentDto getStudent(Long id) {
		return studentRepository.findById(id)
			.map(student -> new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt()))
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Student not found."));
	}

	@Transactional
	public StudentDto updateStudent(StudentDto studentDto) {
		Student student = studentRepository.findById(studentDto.getId()).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Student not found."));
		student.setName(studentDto.getName());
		student.setAddress(studentDto.getAddress());
		student.setUpdatedAt(Timestamp.from(Instant.now()));
		student = studentRepository.save(student);
		return new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt());
	}

	@Transactional
	public List<StudentCourseDto> updateStudentCourses(Long id, List<Long> courseIds) {
		if (courseIds.size() > 5) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "A student can not enroll in more than five courses.");
		}

		Student student = studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Student not found."));

		//building the courses list
		List<StudentCourse> studentCourses = courseIds.stream()
			.map(courseId -> new StudentCourse(student, courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Course (id "+ courseId +") not found."))))
			.collect(Collectors.toList());

		//updating the student's timestamp
		student.setUpdatedAt(Timestamp.from(Instant.now()));
		studentRepository.save(student);
		//deleting current courses
		studentCourseRepository.deleteCoursesByStudent(student);

		//saving new courses
		studentCourses = studentCourseRepository.saveAll(studentCourses);

		return studentCourses.stream()
			.map(studentCourse -> new StudentCourseDto(studentCourse.getStudent().getId(), studentCourse.getStudent().getName(), studentCourse.getCourse().getId(),studentCourse.getCourse().getName()))
			.collect(Collectors.toList());
	}

	public List<StudentDto> createStudents(List<StudentDto> studentsDto) {
		if (studentsDto.size() > 50) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "A request can not contain more than 50 students.");
		}
		Timestamp ts = Timestamp.from(Instant.now());
		List<Student> l = studentRepository.saveAll(studentsDto.stream()
			.map(studentDto -> new Student(studentDto.getName(),
				studentDto.getAddress(),
				ts,
				ts,
				new HashSet<StudentCourse>()))
			.collect(Collectors.toList()));

		return l.stream()
			.map(student -> new StudentDto(student.getId(),
				student.getName(),
				student.getAddress(),
				student.getCreatedAt(),
				student.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteAllStudents(Boolean allStudents) {
		if (allStudents) {
			studentCourseRepository.deleteAll();
			studentRepository.deleteAll();
		} else {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"To delete ALL users and student-course relationships, inform all-students=true as a query param.");
		}
	}

	@Transactional
	public void deleteStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Student not found."));

		studentCourseRepository.deleteCoursesByStudent(student);
		studentRepository.deleteById(id);
	}

	//--------------------------
	public List<StudentDto> getStudentsByCourse(Long id) {
		Course course = courseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found."));
		return studentRepository
			.getStudentsByCourse(course).stream()
			.map(student -> new StudentDto(student.getId(),
				student.getName(),
				student.getAddress(),
				student.getCreatedAt(),
				student.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	public List<StudentCourseView> getStudentCourseRelationship() {
		return studentCourseViewRepository.getStudentCourseRelationship();
	}
}
