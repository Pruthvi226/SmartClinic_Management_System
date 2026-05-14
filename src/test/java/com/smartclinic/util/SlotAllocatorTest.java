package com.smartclinic.util;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SlotAllocatorTest {

    @Test
    void normalPriorityReturnsNextThreeSlotsOnAvailableDay() {
        Doctor doctor = doctor("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY", 30);
        LocalDate date = next(DayOfWeek.MONDAY);

        List<LocalDateTime> slots = SlotAllocator.allocateSlots(
                doctor,
                date,
                Appointment.Priority.NORMAL,
                Collections.emptyList());

        assertEquals(3, slots.size());
        assertEquals(LocalTime.of(9, 0), slots.get(0).toLocalTime());
        assertEquals(LocalTime.of(9, 30), slots.get(1).toLocalTime());
        assertEquals(LocalTime.of(10, 0), slots.get(2).toLocalTime());
    }

    @Test
    void unavailableDoctorDayReturnsNoSlots() {
        Doctor doctor = doctor("MONDAY,WEDNESDAY,FRIDAY", 30);
        LocalDate date = next(DayOfWeek.TUESDAY);

        List<LocalDateTime> slots = SlotAllocator.allocateSlots(
                doctor,
                date,
                Appointment.Priority.NORMAL,
                Collections.emptyList());

        assertTrue(slots.isEmpty());
    }

    @Test
    void bookedSlotsAreExcludedFromRecommendations() {
        Doctor doctor = doctor("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY", 30);
        LocalDate date = next(DayOfWeek.MONDAY);
        Appointment booked = appointmentAt(date.atTime(9, 0), Appointment.Status.SCHEDULED);

        List<LocalDateTime> slots = SlotAllocator.allocateSlots(
                doctor,
                date,
                Appointment.Priority.EMERGENCY,
                List.of(booked));

        assertEquals(1, slots.size());
        assertEquals(LocalTime.of(9, 30), slots.get(0).toLocalTime());
    }

    @Test
    void cancelledAppointmentsDoNotBlockSlots() {
        Doctor doctor = doctor("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY", 30);
        LocalDate date = next(DayOfWeek.MONDAY);
        Appointment cancelled = appointmentAt(date.atTime(9, 0), Appointment.Status.CANCELLED);

        List<LocalDateTime> slots = SlotAllocator.allocateSlots(
                doctor,
                date,
                Appointment.Priority.EMERGENCY,
                List.of(cancelled));

        assertEquals(1, slots.size());
        assertEquals(LocalTime.of(9, 0), slots.get(0).toLocalTime());
    }

    private Doctor doctor(String availableDays, int slotDurationMins) {
        Doctor doctor = new Doctor();
        doctor.setAvailableDays(availableDays);
        doctor.setSlotDurationMins(slotDurationMins);
        return doctor;
    }

    private Appointment appointmentAt(LocalDateTime slot, Appointment.Status status) {
        Appointment appointment = new Appointment();
        appointment.setSlotDatetime(slot);
        appointment.setStatus(status);
        return appointment;
    }

    private LocalDate next(DayOfWeek dayOfWeek) {
        LocalDate today = LocalDate.now();
        LocalDate date = today.with(TemporalAdjusters.nextOrSame(dayOfWeek));
        return date.isEqual(today) ? date.plusWeeks(1) : date;
    }
}
