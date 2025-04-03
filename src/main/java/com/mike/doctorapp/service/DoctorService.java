package com.mike.doctorapp.service;

import com.mike.doctorapp.dto.doctor.DoctorCreateRequest;
import com.mike.doctorapp.dto.doctor.DoctorFilter;
import com.mike.doctorapp.dto.doctor.DoctorResponse;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.entity.User;
import com.mike.doctorapp.mapper.DoctorMapper;
import com.mike.doctorapp.repository.DoctorRepository;
import com.mike.doctorapp.repository.UserRepository;
import com.mike.doctorapp.repository.specification.doctor.DoctorSpecificationBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public DoctorResponse createDoctor(DoctorCreateRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new RuntimeException("User with ID " + request.getUserId() + " not found.")
        );
        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setUser(user);
        doctor.setCreatedAt(LocalDateTime.now());
        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void deleteDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(
                () -> new RuntimeException("Doctor with ID " + doctorId + " not found.")
        );
        doctor.getAppointments().clear();
        doctorRepository.delete(doctor);
    }

}
