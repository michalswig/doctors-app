package com.mike.doctorapp.service;

import com.mike.doctorapp.dto.appointment.AppointmentCreateRequest;
import com.mike.doctorapp.dto.appointment.AppointmentFilter;
import com.mike.doctorapp.dto.appointment.AppointmentResponse;
import com.mike.doctorapp.entity.Appointment;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.entity.Patient;
import com.mike.doctorapp.enums.AppointmentStatus;
import com.mike.doctorapp.mapper.AppointmentMapper;
import com.mike.doctorapp.repository.AppointmentRepository;
import com.mike.doctorapp.repository.DoctorRepository;
import com.mike.doctorapp.repository.PatientRepository;
import com.mike.doctorapp.repository.specification.appointment.AppointmentSpecification;
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
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper mapper;
    private final Clock clock;

    public List<AppointmentResponse> getAppointmentsByDoctorAndStartDate(Long doctorId, LocalDateTime startDate) {

        AppointmentFilter filter = AppointmentFilter.builder()
                .doctorId(doctorId)
                .appointmentDate(startDate)
                .build();

        Specification<Appointment> specification = AppointmentSpecification.build(filter);

        return appointmentRepository.findAll(specification).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void updateAppointmentStatus(Long appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.valueOf(newStatus.toUpperCase()));
        appointment.setUpdatedAt(LocalDateTime.now(clock));

        appointmentRepository.save(appointment);
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentCreateRequest request) {
        ifAppointmentDateIsValid(request);
        Doctor doctor = getDoctor(request);
        Patient patient = getPatient(request);
        Appointment appointment = mapper.toEntity(request);
        setAppointmentRequest(request, appointment, doctor, patient);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapper.toResponse(savedAppointment);
    }

    private void ifAppointmentDateIsValid(AppointmentCreateRequest request) {
        LocalDateTime now = LocalDateTime.now(clock);
        if (request.getAppointmentDate() == null || request.getAppointmentDate().isBefore(now)) {
            throw new IllegalArgumentException("Appointment date must be in the future.");
        }
    }

    private void setAppointmentRequest(AppointmentCreateRequest request, Appointment appointment, Doctor doctor, Patient patient) {
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.APPOINTMENT_SCHEDULED);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setCreatedAt(LocalDateTime.now(clock));
    }

    private Patient getPatient(AppointmentCreateRequest request) {
        return patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));
    }

    private Doctor getDoctor(AppointmentCreateRequest request) {
        return doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
    }

}
