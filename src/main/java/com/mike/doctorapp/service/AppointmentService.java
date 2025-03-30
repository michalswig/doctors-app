package com.mike.doctorapp.service;

import com.mike.doctorapp.dto.appointment.AppointmentFilter;
import com.mike.doctorapp.entity.Appointment;
import com.mike.doctorapp.enums.AppointmentStatus;
import com.mike.doctorapp.repository.AppointmentRepository;
import com.mike.doctorapp.repository.specification.appointment.AppointmentSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    public final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAppointmentsByFilter(AppointmentFilter filter) {
        Specification<Appointment> specification = AppointmentSpecificationBuilder.build(filter);
        return appointmentRepository.findAll(specification);
    }

    public void updateAppointmentStatus(Long appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.valueOf(newStatus.toUpperCase()));
        appointment.setUpdatedAt(LocalDateTime.now());

        appointmentRepository.save(appointment);
    }




}
