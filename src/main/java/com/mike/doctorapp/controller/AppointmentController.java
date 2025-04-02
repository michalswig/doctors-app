package com.mike.doctorapp.controller;

import com.mike.doctorapp.dto.appointment.AppointmentCreateRequest;
import com.mike.doctorapp.dto.appointment.AppointmentFilter;
import com.mike.doctorapp.dto.appointment.AppointmentResponse;
import com.mike.doctorapp.dto.appointment.UpdateAppointmentStatusRequest;
import com.mike.doctorapp.mapper.AppointmentMapper;
import com.mike.doctorapp.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctorIdAndStartDate(
            @PathVariable Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
    ) {

        AppointmentFilter filter = AppointmentFilter.builder()
                .doctorId(doctorId)
                .appointmentDate(startDate)
                .build();

        List<AppointmentResponse> response = appointmentService.getAppointmentsByFilter(filter).stream()
                .map(appointmentMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateAppointmentStatusRequest request
    ) {
        appointmentService.updateAppointmentStatus(id, request.getStatus().name());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody AppointmentCreateRequest request
    ) {
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }


}
