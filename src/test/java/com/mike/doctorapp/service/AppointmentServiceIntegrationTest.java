package com.mike.doctorapp.service;

import com.mike.doctorapp.FixedClockTestConfig;
import com.mike.doctorapp.dto.appointment.AppointmentCreateRequest;
import com.mike.doctorapp.dto.appointment.AppointmentResponse;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.entity.Patient;
import com.mike.doctorapp.entity.User;
import com.mike.doctorapp.enums.AppointmentStatus;
import com.mike.doctorapp.enums.Gender;
import com.mike.doctorapp.repository.AppointmentRepository;
import com.mike.doctorapp.repository.DoctorRepository;
import com.mike.doctorapp.repository.PatientRepository;
import com.mike.doctorapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(FixedClockTestConfig.class)
class AppointmentServiceIntegrationTest {

    @Autowired private AppointmentService appointmentService;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private Clock clock;

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        // Clean DB state before each test
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        userRepository.deleteAll();

        // Create linked User entities
        User userDoctor = userRepository.save(User.builder().build());
        User userPatient = userRepository.save(User.builder().build());

        // Persist doctor and patient
        doctor = doctorRepository.save(Doctor.builder()
                .firstName("John")
                .lastName("Doe")
                .specialization("Dermatologist")
                .phone("1234567890")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .user(userDoctor)
                .build());

        patient = patientRepository.save(Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(2022, 12, 12))
                .gender(Gender.FEMALE)
                .phone("123456789")
                .address("123 Street")
                .user(userPatient)
                .build());
    }

    @Test
    void shouldCreateAppointmentWhenPassingValidData() {
        // Given
        LocalDateTime appointmentDate = LocalDateTime.now(clock).plusDays(1);
        AppointmentCreateRequest request = AppointmentCreateRequest.builder()
                .appointmentDate(appointmentDate)
                .doctorId(doctor.getId())
                .patientId(patient.getId())
                .build();

        // When
        AppointmentResponse response = appointmentService.createAppointment(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(AppointmentStatus.APPOINTMENT_SCHEDULED);
        assertThat(response.getAppointmentDate()).isEqualTo(appointmentDate);
        assertThat(response.getDoctorId()).isEqualTo(doctor.getId());
        assertThat(response.getPatientId()).isEqualTo(patient.getId());
        assertThat(appointmentRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenAppointmentDateIsInvalid() {
        // Given
        LocalDateTime pastDate = LocalDateTime.of(2025, 2, 28, 10, 0);
        LocalDateTime appointmentDate = pastDate;
        AppointmentCreateRequest request = AppointmentCreateRequest.builder()
                .appointmentDate(appointmentDate)
                .doctorId(doctor.getId())
                .patientId(patient.getId())
                .build();

        // When Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> appointmentService.createAppointment(request));
        assertThat(exception.getMessage()).isEqualTo("Appointment date must be in the future.");
    }

    @Test
    void shouldThrowExceptionWhenDoctorNotFound() {
        // Given
        AppointmentCreateRequest request = AppointmentCreateRequest.builder()
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .doctorId(999L)
                .patientId(patient.getId())
                .build();

        // When + Then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.createAppointment(request));
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        // Given
        AppointmentCreateRequest request = AppointmentCreateRequest.builder()
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .doctorId(doctor.getId())
                .patientId(999L)
                .build();

        // When + Then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.createAppointment(request));
    }

}
