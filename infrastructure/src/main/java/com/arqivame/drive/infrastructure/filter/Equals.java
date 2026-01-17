package com.arqivame.drive.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.pagination.Filter;

@Component
public class Equals extends SpecificationFilter {

    @Override
    public Filter.Type filterType() {
        return Filter.Type.EQUALS;
    }

    @Override
    public <T> Specification<T> buildSpecification(Filter filter) {
        return (root, query, criteriaBuilder) -> {
            final var field = root.get(filter.field().value());
            return criteriaBuilder.equal(field, SpecificationFilter.cast(filter.value(), field.getJavaType()));
        };
    }

}
