package com.diploma.mindsupport.data;

import com.diploma.mindsupport.model.Option;
import com.diploma.mindsupport.model.PsychologistPatientMatch;
import com.diploma.mindsupport.model.Question;
import com.diploma.mindsupport.model.UserRole;
import com.diploma.mindsupport.service.OptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SurveyDataParser {

    public static List<Question> parseQuestionsThrowing(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<QuestionOptionUserRoleWrapper> questions = mapper.readValue(json, new TypeReference<>() {
        });
        return questions.stream()
                .map(q -> createQuestion(q.getQuestion(), q.getQuestionId(), q.getOptions(), q.getUserRole()))
                .toList();
    }

    public static List<PsychologistPatientMatch> parseMatches(String json, OptionService optionService) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> optionToOptionWrappers = mapper.readValue(json, new TypeReference<>() {
        });
        return optionToOptionWrappers
                .stream().flatMap(o -> o.entrySet().stream())
                .map(
                        entry -> PsychologistPatientMatch.builder()
                                .patientOption(optionService.getOptionById(Long.parseLong(entry.getKey())).orElseThrow())
                                .psychologistOption(optionService.getOptionById(Long.parseLong(entry.getValue())).orElseThrow())
                                .build()
                ).toList();
    }

    private static Question createQuestion(String questionText, String questionId, Map<String, String> options, String userRole) {
        Question question = Question.builder()
                .id(Long.parseLong(questionId))
                .questionText(questionText)
                .userRole(UserRole.valueOf(userRole.toUpperCase())).build();

        List<Option> optionsList = options.entrySet().stream()
                .map(e -> Option.builder()
                        .id(Long.parseLong(e.getKey()))
                        .optionText(e.getValue())
                        .question(question)
                        .build())
                .toList();

        question.setOptions(optionsList);
        return question;
    }
}
