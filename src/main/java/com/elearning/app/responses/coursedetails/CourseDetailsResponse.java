package com.elearning.app.responses.coursedetails;

import java.util.List;

public class CourseDetailsResponse {
    private CourseDetailsCourseResponse course;
    private List<CourseDetailsExamResponse> exams;
    private List<CourseDetailsLessonResponse> lessons;
    private List<CourseDetailsStudentResponse> students;

    public CourseDetailsCourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseDetailsCourseResponse course) {
        this.course = course;
    }

    public List<CourseDetailsLessonResponse> getLessons() {
        return lessons;
    }

    public void setLessons(List<CourseDetailsLessonResponse> lessons) {
        this.lessons = lessons;
    }

    public List<CourseDetailsStudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<CourseDetailsStudentResponse> students) {
        this.students = students;
    }

    public List<CourseDetailsExamResponse> getExams() {
        return exams;
    }

    public void setExams(List<CourseDetailsExamResponse> exams) {
        this.exams = exams;
    }
}
