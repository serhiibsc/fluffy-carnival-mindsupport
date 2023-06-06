package com.diploma.mindsupport.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "option_text")
    private String optionText;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (!id.equals(option.id)) return false;
        if (!question.equals(option.question)) return false;
        return optionText.equals(option.optionText);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + question.hashCode();
        result = 31 * result + optionText.hashCode();
        return result;
    }
}