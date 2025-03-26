package com.mike.doctorapp.dto.doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorCreateRequest {
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private Long userId;
}
