package com.diploma.mindsupport.service;


import com.diploma.mindsupport.model.Course;
import com.diploma.mindsupport.model.Feedback;
import com.diploma.mindsupport.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    public Feedback getFeedback(Long id) {
        return feedbackRepository.findById(id).orElseThrow();
    }

    public Feedback getFeedbackByCourse(Course course) {
        return feedbackRepository.findByCourse(course).orElseThrow();
    }
}
