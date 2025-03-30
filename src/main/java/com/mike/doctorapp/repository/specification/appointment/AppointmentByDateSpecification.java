package com.mike.doctorapp.repository.specification.appointment;

import com.mike.doctorapp.entity.Appointment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AppointmentByDateSpecification implements Specification<Appointment> {
    private final String fieldName;
    private final LocalDateTime from;

    public AppointmentByDateSpecification(String fieldName, LocalDateTime from) {
        this.fieldName = fieldName;
        this.from = from;

    }

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (from != null) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), from);
        }
        return criteriaBuilder.conjunction();
    }
}
