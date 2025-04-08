package com.mike.doctorapp.dto.doctor;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorUpdateRequest {
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
}
