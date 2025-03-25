package com.mike.doctorapp.dto.doctor;

import java.time.LocalDateTime;

public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}