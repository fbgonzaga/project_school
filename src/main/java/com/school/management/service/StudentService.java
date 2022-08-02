package com.school.management.service;


import com.school.management.model.Course;
import com.school.management.model.Student;
import com.school.management.model.StudentCourse;
import com.school.management.model.StudentCourseView;
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

		Boolean updated = false;
		if (studentDto.getName() != null && !studentDto.getName().isBlank() && !studentDto.getName().equals(student.getName())) {
			student.setName(studentDto.getName());
			updated = true;
		}
		if (studentDto.getAddress() != null && !studentDto.getAddress().isBlank() && !studentDto.getAddress().equals(student.getAddress())) {
			student.setAddress(studentDto.getAddress());
			updated = true;
		}

		if (updated) {
			student.setUpdatedAt(Timestamp.from(Instant.now()));
			student = studentRepository.save(student);
		}

		return new StudentDto(student.getId(), student.getName(), student.getAddress(), student.getCreatedAt(), student.getUpdatedAt());
	}

	@Transactional
	public List<StudentCourseDto> updateStudentCourses(Long id, List<Long> courseIds) {
		//Trying to enroll in more than five courses.
		if (courseIds.size() > 5) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "A student can not enroll in more than five courses.");
		}

		//Invalid student id.
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Student not found."));

		//For each course verifies:
		//1) Does the Course exist?
		//2) Considering the student is not enrolled in the course, does the course already has 50 students enrolled?
		List<StudentCourse> studentCourses = courseIds.stream()
			.filter(courseId -> {
				if (studentCourseRepository
					.getTotalOtherStudentsByCourse(courseRepository.findById(courseId)
						.orElseThrow(() -> new ResponseStatusException(
							HttpStatus.NOT_FOUND, "Course (id " + courseId + ") not found.")), student) >= 50)
					throw new ResponseStatusException(
						HttpStatus.FORBIDDEN, "The course (id " + courseId + ") has already 50 students enrolled.");
				return true;
			})
			.map(courseId -> new StudentCourse(student, courseRepository.findById(courseId).get()))
			.collect(Collectors.toList());

		//updating the student's timestamp
		student.setUpdatedAt(Timestamp.from(Instant.now()));
		studentRepository.save(student);
		//deleting current student's courses
		studentCourseRepository.deleteCoursesByStudent(student);

		//saving new courses
		studentCourses = studentCourseRepository.saveAll(studentCourses);

		return studentCourses.stream()
			.map(studentCourse -> new StudentCourseDto(studentCourse.getStudent().getId(), studentCourse.getStudent().getName(), studentCourse.getCourse().getId(), studentCourse.getCourse().getName()))
			.collect(Collectors.toList());
	}

	public List<StudentDto> createStudents(List<StudentDto> studentsDto) {
		if (studentsDto.size() > 50) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "A request can not contain more than 50 students.");
		}

		Timestamp ts = Timestamp.from(Instant.now());
		List<Student> l = studentRepository.saveAll(studentsDto.stream()
			.filter(s -> s.getName() != null && !s.getName().isBlank() && s.getAddress() != null && !s.getAddress().isBlank())
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
	public void deleteAllStudents(Boolean confirmDeletion) {
		if (confirmDeletion) {
			studentCourseRepository.deleteAll();
			studentRepository.deleteAll();
		} else {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"To delete ALL students and students-courses relationships, inform confirm-deletion=true as a query param.");
		}
	}

	@Transactional
	public void deleteStudent(Long id, Boolean confirmDeletion) {
		if (confirmDeletion) {
			Student student = studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Student not found."));
			studentCourseRepository.deleteCoursesByStudent(student);
			studentRepository.deleteById(id);
		} else {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"To delete the student and student-courses relationships, inform confirm-deletion=true as a query param.");
		}


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
