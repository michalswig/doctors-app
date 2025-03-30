package com.mike.doctorapp.mapper;

import com.mike.doctorapp.dto.appointment.AppointmentResponse;
import com.mike.doctorapp.entity.Appointment;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AppointmentMapper {

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentResponse toResponse(Appointment appointment);

    @Mapping(target = "doctor", expression = "java(mapDoctor(response.getDoctorId()))")
    @Mapping(target = "patient", expression = "java(mapPatient(response.getPatientId()))")
    @Mapping(source = "status", target = "status")
    Appointment toEntity(AppointmentResponse response);

    default Doctor mapDoctor(Long id) {
        if (id == null) return null;
        return Doctor.builder().id(id).build();
    }

    default Patient mapPatient(Long id) {
        if (id == null) return null;
        return Patient.builder().id(id).build();
    }

}
