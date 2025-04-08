package com.mike.doctorapp.repository.specification.doctor;

import com.mike.doctorapp.entity.Doctor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecializationSpecification implements Specification<Doctor> {

    private final String specialization;

    public DoctorSpecializationSpecification(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public Predicate toPredicate(Root<Doctor> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(specialization != null) {
            return criteriaBuilder.equal(root.get("specialization"), specialization);
        }
        return criteriaBuilder.conjunction();
    }

}
