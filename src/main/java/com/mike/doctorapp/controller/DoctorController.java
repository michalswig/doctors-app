package com.mike.doctorapp.controller;

import com.mike.doctorapp.dto.doctor.DoctorCreateRequest;
import com.mike.doctorapp.dto.doctor.DoctorFilter;
import com.mike.doctorapp.dto.doctor.DoctorResponse;
import com.mike.doctorapp.entity.Doctor;
import com.mike.doctorapp.mapper.DoctorMapper;
import com.mike.doctorapp.service.DoctorService;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getDoctorsByFilter(@RequestParam(required = false) String specialization) {
        List<DoctorResponse> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@RequestBody DoctorCreateRequest request) {
        DoctorResponse savedDoctor = doctorService.createDoctor(request);
        return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DoctorResponse> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return ResponseEntity.noContent().build();
    }

}
