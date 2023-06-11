package com.diploma.mindsupport.service;

import com.diploma.mindsupport.model.Course;
import com.diploma.mindsupport.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final FeedbackService feedbackService;
    private final UserService userService;

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow();
    }

    public Course updateCourse(Course course, Long id) {
        return courseRepository.save(course);
    }
}
