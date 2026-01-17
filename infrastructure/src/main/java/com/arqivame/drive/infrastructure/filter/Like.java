package com.arqivame.drive.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.pagination.Filter;

@Component
public class Like extends SpecificationFilter {

    @Override
    public Filter.Type filterType() {
        return Filter.Type.LIKE;
    }

    @Override
    public <T> Specification<T> buildSpecification(Filter filter) {

        validateFilter(filter);

        return (root, query, criteriaBuilder) -> criteriaBuilder
                .like(
                        criteriaBuilder.upper(criteriaBuilder.toString(root.get(filter.field().value()))),
                        "%" + filter.value().toUpperCase() + "%");
    }

    private void validateFilter(final Filter filter) {
        if (filter.value() == null)
            throw new IllegalArgumentException("Value cannot be null");

        if (!Filter.Type.LIKE.equals(filter.type()))
            throw new IllegalArgumentException("Filter type must be LIKE");
    }

}
