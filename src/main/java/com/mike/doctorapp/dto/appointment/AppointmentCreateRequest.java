package com.mike.doctorapp.dto.appointment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCreateRequest {
    private LocalDateTime appointmentDate;
    private Long doctorId;
    private Long patientId;
}
