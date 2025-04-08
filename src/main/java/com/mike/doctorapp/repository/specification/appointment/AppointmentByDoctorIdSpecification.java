package com.mike.doctorapp.repository.specification.appointment;

import com.mike.doctorapp.entity.Appointment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentByDoctorIdSpecification implements Specification<Appointment> {
    private final Long doctorId;

    public AppointmentByDoctorIdSpecification(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(doctorId == null) {
            return criteriaBuilder.conjunction();
        }
        return criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
    }


}
