package com.diploma.mindsupport.matching;

import com.diploma.mindsupport.model.User;

import java.util.Collection;

public interface Criteria {
    Collection<User> meetCriteria(Collection<User> psychologists);
}
