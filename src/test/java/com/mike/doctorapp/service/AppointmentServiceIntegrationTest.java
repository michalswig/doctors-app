package com.mike.doctorapp.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppointmentServiceIntegrationTest {

    @Autowired private AppointmentService appointmentService;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;

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
        LocalDateTime appointmentDate = LocalDateTime.now().plusDays(1);
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

}
