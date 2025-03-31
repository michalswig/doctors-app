package com.mike.doctorapp.controller;

import com.mike.doctorapp.dto.doctor.DoctorFilter;
import com.mike.doctorapp.dto.doctor.DoctorResponse;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.mapper.DoctorMapper;
import com.mike.doctorapp.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    public DoctorController(DoctorService doctorService, DoctorMapper doctorMapper) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctorMapper.toResponse(doctor));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctorsByFilter(@RequestParam(required = false) String specialization) {

        DoctorFilter filter = DoctorFilter.builder()
                .specialization(specialization)
                .build();

        List<Doctor> doctors = doctorService.getDoctorsByFilter(filter);
        return ResponseEntity.ok(doctors.stream()
                .map(doctorMapper::toResponse)
                .toList());
    }

}
