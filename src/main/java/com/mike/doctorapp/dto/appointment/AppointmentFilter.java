package com.mike.doctorapp.dto.appointment;


import com.mike.doctorapp.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentFilter {
    private Long id;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long doctorId;
    private Long patientId;
}
