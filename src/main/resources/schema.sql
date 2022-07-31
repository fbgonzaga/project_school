CREATE VIEW `student_course_view` AS
SELECT student.id AS `student_id`, course.id AS `course_id`, student.name AS `student`, course.name AS `course` FROM (student, course) JOIN (student_course)
ON (student.id = student_course.student_id  AND student_course.course_id = course.id)
ORDER BY student.name, course.name;
