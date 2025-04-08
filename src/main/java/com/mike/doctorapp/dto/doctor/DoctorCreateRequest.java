package com.mike.doctorapp.dto.doctor;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorCreateRequest {
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private Long userId;
}
