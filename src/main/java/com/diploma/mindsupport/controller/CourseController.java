package com.diploma.mindsupport.controller;


import com.diploma.mindsupport.model.Course;
import com.diploma.mindsupport.model.Feedback;
import com.diploma.mindsupport.service.CourseService;
import com.diploma.mindsupport.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {
    private final CourseService courseService;
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.saveCourse(course));
    }

    @PatchMapping("/{id}/feedback")
    public ResponseEntity<Course> addFeedbackToCourse(
            @RequestBody Feedback feedback, @PathVariable("id") Long id) {
        feedbackService.saveFeedback(feedback);
        return ResponseEntity.ok(courseService.getCourseById(id));
    }
}
