package com.example.customerserviceapp.repository.specification.customer;

import com.example.customerserviceapp.model.Customer;
import com.example.customerserviceapp.repository.specification.SpecificationProvider;
import javax.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CustomerLastNameIn implements SpecificationProvider<Customer> {
    private static final String FILTER_KEY = "lastNameIn";
    private static final String FIELD_NAME = "lastName";

    @Override
    public Specification<Customer> getSpecification(String[] params) {
        return (root, query, cb) -> {
            CriteriaBuilder.In<String> predicate = cb.in(root.get(FIELD_NAME));
            for (String value : params) {
                predicate.value(value);
            }
            return cb.and(predicate, predicate);
        };
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
