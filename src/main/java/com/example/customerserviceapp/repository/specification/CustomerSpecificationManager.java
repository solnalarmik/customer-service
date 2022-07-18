package com.example.customerserviceapp.repository.specification;

import com.example.customerserviceapp.model.Customer;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CustomerSpecificationManager implements SpecificationManager<Customer> {
    private final Map<String, SpecificationProvider<Customer>> providersMap;

    @Autowired
    public CustomerSpecificationManager(List<SpecificationProvider<Customer>> specifications) {
        this.providersMap = specifications.stream()
                .collect(Collectors.toMap(SpecificationProvider::getFilterKey,
                        Function.identity()));
    }

    @Override
    public Specification<Customer> get(String filterKey, String[] params) {
        if (!providersMap.containsKey(filterKey)) {
            throw new RuntimeException("Key " + filterKey + " is not supported for data filtering");
        }
        return providersMap.get(filterKey).getSpecification(params);
    }
}
