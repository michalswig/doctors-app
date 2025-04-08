package com.mike.doctorapp.repository.specification.appointment;

import com.mike.doctorapp.dto.appointment.AppointmentFilter;
import com.mike.doctorapp.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class AppointmentSpecification {

    public static Specification<Appointment> build(AppointmentFilter filter) {
        Specification<Appointment> specification = Specification.where(null);

        if (Objects.nonNull(filter.getDoctorId())) {
            specification = specification.and(new AppointmentByDoctorIdSpecification(filter.getDoctorId()));
        }
        if(Objects.nonNull(filter.getAppointmentDate())) {
            specification = specification.and(new AppointmentByDateSpecification("appointmentDate", filter.getAppointmentDate()));
        }
        return specification;
    }

}
