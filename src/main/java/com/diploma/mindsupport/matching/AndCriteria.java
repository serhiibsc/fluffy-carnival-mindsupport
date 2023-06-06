package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.User;
import lombok.Builder;

import java.util.Collection;
import java.util.List;

@Builder
public class AndCriteria implements Criteria {
    private List<Criteria> criteriaList;

    public AndCriteria(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public Collection<User> meetCriteria(Collection<User> psychologists) {
        return criteriaList.stream()
                .reduce((criteria, accumulated) -> users -> criteria.meetCriteria(accumulated.meetCriteria(users)))
                .orElse(user -> List.of())
                .meetCriteria(psychologists);
    }
}
