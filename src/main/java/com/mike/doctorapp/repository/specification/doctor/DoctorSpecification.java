package com.mike.doctorapp.repository.specification.doctor;

import com.mike.doctorapp.dto.doctor.DoctorFilter;
import com.mike.doctorapp.entity.Doctor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class DoctorSpecification {

    public static Specification<Doctor> build(DoctorFilter filter) {
        Specification<Doctor> specification = Specification.where(null);

        if(Objects.nonNull(filter.getSpecialization())){
            specification = specification.and(new DoctorSpecializationSpecification(filter.getSpecialization()));
        }
        return specification;
    }

}
