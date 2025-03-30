package com.mike.doctorapp.dto.appointment;

import com.mike.doctorapp.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAppointmentStatusRequest {
    private AppointmentStatus status;
}
