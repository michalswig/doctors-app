package com.mike.doctorapp.service;

import com.mike.doctorapp.dto.appointment.AppointmentCreateRequest;
import com.mike.doctorapp.dto.appointment.AppointmentResponse;
import com.mike.doctorapp.entity.Appointment;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.entity.Patient;
import com.mike.doctorapp.enums.AppointmentStatus;
import com.mike.doctorapp.mapper.AppointmentMapper;
import com.mike.doctorapp.repository.AppointmentRepository;
import com.mike.doctorapp.repository.DoctorRepository;
import com.mike.doctorapp.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentMapper appointmentMapper;

    private Clock fixedClock;
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2025-03-01T10:00:00Z"), ZoneId.systemDefault());
        appointmentService = new AppointmentService(
                appointmentRepository,
                doctorRepository,
                patientRepository,
                appointmentMapper,
                fixedClock
        );
    }

    @Test
    void shouldCreateAppointmentWhenPassingValidData() {
        // given
        AppointmentCreateRequest request = AppointmentCreateRequest.builder()
                .appointmentDate(LocalDateTime.of(2025, 3, 16, 10, 0))
                .doctorId(1L)
                .patientId(1L)
                .build();

        Doctor doctor = Doctor.builder().id(1L).build();
        Patient patient = Patient.builder().id(1L).build();

        Appointment entity = new Appointment();
        AppointmentResponse expectedResponse = new AppointmentResponse();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentMapper.toEntity(request)).thenReturn(entity);
        when(appointmentRepository.save(entity)).thenReturn(entity);
        when(appointmentMapper.toResponse(entity)).thenReturn(expectedResponse);

        // when
        AppointmentResponse result = appointmentService.createAppointment(request);

        // then
        assertNotNull(result);
        assertSame(expectedResponse, result);
        
        verify(appointmentRepository).save(entity);

        assertEquals(request.getAppointmentDate(), entity.getAppointmentDate());
        assertEquals(AppointmentStatus.APPOINTMENT_SCHEDULED, entity.getStatus());
        assertEquals(doctor, entity.getDoctor());
        assertEquals(patient, entity.getPatient());
        assertEquals(LocalDateTime.now(fixedClock), entity.getCreatedAt());
    }
}