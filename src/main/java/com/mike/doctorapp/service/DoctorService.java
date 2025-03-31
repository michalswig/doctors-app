package com.mike.doctorapp.service;

import com.mike.doctorapp.dto.doctor.DoctorFilter;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.mapper.DoctorMapper;
import com.mike.doctorapp.repository.DoctorRepository;
import com.mike.doctorapp.repository.specification.doctor.DoctorSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor with ID " + doctorId + " not found."));
    }

    public List<Doctor> getDoctorsByFilter(DoctorFilter filter) {
        Specification<Doctor> specification = DoctorSpecificationBuilder.build(filter);
        return doctorRepository.findAll(specification);
    }

}
