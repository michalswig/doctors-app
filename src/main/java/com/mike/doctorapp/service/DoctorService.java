package com.mike.doctorapp.service;

import com.mike.doctorapp.dto.doctor.DoctorCreateRequest;
import com.mike.doctorapp.dto.doctor.DoctorFilter;
import com.mike.doctorapp.dto.doctor.DoctorResponse;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.entity.User;
import com.mike.doctorapp.mapper.DoctorMapper;
import com.mike.doctorapp.repository.DoctorRepository;
import com.mike.doctorapp.repository.UserRepository;
import com.mike.doctorapp.repository.specification.doctor.DoctorSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;
    private final Clock clock;

    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        DoctorFilter filter = DoctorFilter.builder()
                .specialization(specialization)
                .build();

        Specification<Doctor> specification = DoctorSpecification.build(filter);

        return doctorRepository.findAll(specification).stream()
                .map(doctorMapper::toResponse)
                .toList();
    }

    @Transactional
    public DoctorResponse createDoctor(DoctorCreateRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User with ID " + request.getUserId() + " not found.")
        );
        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setUser(user);
        doctor.setCreatedAt(LocalDateTime.now(clock));
        return doctorMapper.toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void deleteDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(
                () -> new EntityNotFoundException("Doctor with ID " + doctorId + " not found.")
        );
        doctor.getAppointments().clear();
        doctorRepository.delete(doctor);
    }

}
