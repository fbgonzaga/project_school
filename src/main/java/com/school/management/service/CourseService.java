package com.school.management.service;


import com.school.management.model.Course;
import com.school.management.model.Student;
import com.school.management.model.StudentCourse;
import com.school.management.model.StudentCourseView;
import com.school.management.model.dto.CourseDto;
import com.school.management.model.dto.StudentCourseDto;
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
public class CourseService {

	private final CourseRepository courseRepository;
	private final StudentRepository studentRepository;
	private final StudentCourseRepository studentCourseRepository;
	private final StudentCourseViewRepository studentCourseViewRepository;

	public CourseService(CourseRepository courseRepository, StudentRepository studentRepository, StudentCourseRepository studentCourseRepository, StudentCourseViewRepository studentCourseViewRepository) {
		this.courseRepository = courseRepository;
		this.studentRepository = studentRepository;
		this.studentCourseRepository = studentCourseRepository;
		this.studentCourseViewRepository = studentCourseViewRepository;
	}

	public List<CourseDto> getCourses() {
		return courseRepository.findAll().stream()
			.map(course -> new CourseDto(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	public List<CourseDto> getCoursesWithoutStudent() {
		return courseRepository.getCoursesWithoutStudent().stream()
			.map(course -> new CourseDto(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	public CourseDto getCourse(Long id) {
		return courseRepository.findById(id)
			.map(course -> new CourseDto(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt()))
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Course not found."));
	}

	@Transactional
	public CourseDto updateCourse(CourseDto courseDto) {
		Course course = courseRepository.findById(courseDto.getId()).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Course not found."));

		if (courseDto.getName() != null && !courseDto.getName().isBlank() && !courseDto.getName().equals(course.getName())) {
			course.setName(courseDto.getName());
			course.setUpdatedAt(Timestamp.from(Instant.now()));
			course = courseRepository.save(course);
		}

		return new CourseDto(course.getId(), course.getName(), course.getCreatedAt(), course.getUpdatedAt());
	}

	@Transactional
	public List<StudentCourseDto> updateCourseStudents(Long id, List<Long> studentIds) {
		if (studentIds.size() > 50) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "A course can not have more than 50 students.");
		}

		Course course = courseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
			HttpStatus.NOT_FOUND, "Course not found."));

		//building the students list
		List<StudentCourse> studentCourses = studentIds.stream()
			.map(studentId -> new StudentCourse(studentRepository.findById(studentId).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Student (id " + studentId + ") not found.")), course))
			.collect(Collectors.toList());

		//updating the course's timestamp
		course.setUpdatedAt(Timestamp.from(Instant.now()));
		courseRepository.save(course);
		//deleting current students
		studentCourseRepository.deleteStudentsByCourse(course);

		//saving new students
		studentCourses = studentCourseRepository.saveAll(studentCourses);

		return studentCourses.stream()
			.map(studentCourse -> new StudentCourseDto(studentCourse.getStudent().getId(), studentCourse.getStudent().getName(), studentCourse.getCourse().getId(), studentCourse.getCourse().getName()))
			.collect(Collectors.toList());
	}

	public List<CourseDto> createCourses(List<CourseDto> coursesDto) {
		if (coursesDto.size() > 50) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "A request can not contain more than 50 courses.");
		}
		Timestamp ts = Timestamp.from(Instant.now());
		List<Course> l = courseRepository.saveAll(coursesDto.stream()
			.filter(c -> c.getName() != null && !c.getName().isBlank())
			.map(courseDto -> new Course(courseDto.getName(),
				ts,
				ts,
				new HashSet<StudentCourse>()))
			.collect(Collectors.toList()));

		return l.stream()
			.map(course -> new CourseDto(course.getId(),
				course.getName(),
				course.getCreatedAt(),
				course.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteAllCourses(Boolean confirmDeletion) {
		if (confirmDeletion) {
			studentCourseRepository.deleteAll();
			courseRepository.deleteAll();
		} else {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"To delete ALL courses and courses-students relationships, inform confirm-deletion=true as a query param.");
		}
	}

	@Transactional
	public void deleteCourse(Long id, Boolean confirmDeletion) {
		if (confirmDeletion) {
			Course course = courseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Course not found."));

			studentCourseRepository.deleteStudentsByCourse(course);
			courseRepository.deleteById(id);
		} else {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"To delete the course and course-students relationships, inform confirm-deletion=true as a query param.");
		}
	}

	//--------------------------
	public List<CourseDto> getCoursesByStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found."));
		return courseRepository
			.getCoursesByStudent(student).stream()
			.map(course -> new CourseDto(course.getId(),
				course.getName(),
				course.getCreatedAt(),
				course.getUpdatedAt()))
			.collect(Collectors.toList());
	}

	public List<StudentCourseView> getCourseStudentRelationship() {
		return studentCourseViewRepository.getCourseStudentRelationship();
	}
}
