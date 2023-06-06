package com.diploma.mindsupport.data;

import com.diploma.mindsupport.model.PsychologistPatientMatch;
import com.diploma.mindsupport.model.Question;
import com.diploma.mindsupport.service.OptionService;
import com.diploma.mindsupport.service.PsychologistPatientMatchService;
import com.diploma.mindsupport.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.diploma.mindsupport.data.SurveyDataParser.parseMatches;
import static com.diploma.mindsupport.data.SurveyDataParser.parseQuestionsThrowing;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final QuestionService questionService;
    private final OptionService optionService;
    private final PsychologistPatientMatchService matchService;

    @Override
    public void run(String... args) throws Exception {
        List<Question> questionList =
                parseQuestionsThrowing(readFileFromClasspath("questions_to_use.json"));
        questionList.forEach(questionService::saveQuestion);
//        questionService.saveQuestionList(questionList); // DO NOT USE save ALL, because there is a problem with correct IDs

        List<PsychologistPatientMatch> matches =
                parseMatches(readFileFromClasspath("option_to_option.json"), optionService);
        matchService.saveMatches(matches);
    }

    public String readFileFromClasspath(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        Path path = resource.getFile().toPath();
        return Files.readString(path);
    }
}
