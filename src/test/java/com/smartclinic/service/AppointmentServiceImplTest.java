package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDao;
import com.smartclinic.dao.DoctorDao;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentDao appointmentDao;

    @Mock
    private DoctorDao doctorDao;

    @Mock
    private DoctorLeaveService doctorLeaveService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void cancelAppointmentMarksAppointmentCancelledAndStoresReason() {
        Appointment appointment = appointment(42L);
        appointment.setNotes("Original note");
        when(appointmentDao.findById(42L)).thenReturn(appointment);

        appointmentService.cancelAppointment(42L, "Patient called reception");

        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
        assertTrue(appointment.getNotes().contains("Original note"));
        assertTrue(appointment.getNotes().contains("Cancellation reason: Patient called reception"));
        verify(appointmentDao).update(appointment);
    }

    @Test
    void rescheduleAppointmentMovesAppointmentToSelectedDoctorAndSlot() {
        Appointment appointment = appointment(7L);
        Doctor newDoctor = new Doctor();
        LocalDateTime newSlot = LocalDateTime.of(2026, 6, 1, 10, 30);

        when(appointmentDao.findById(7L)).thenReturn(appointment);
        when(doctorDao.findById(3L)).thenReturn(newDoctor);

        appointmentService.rescheduleAppointment(7L, 3L, newSlot, Appointment.Priority.EMERGENCY, "Needs urgent review");

        assertEquals(newDoctor, appointment.getDoctor());
        assertEquals(newSlot, appointment.getSlotDatetime());
        assertEquals(Appointment.Priority.EMERGENCY, appointment.getPriority());
        assertEquals(Appointment.Status.SCHEDULED, appointment.getStatus());
        assertTrue(appointment.getNotes().contains("Needs urgent review"));
        assertTrue(appointment.getNotes().contains("Rescheduled from appointment #7"));
        verify(appointmentDao).update(appointment);
    }

    @Test
    void rescheduleAppointmentFailsWhenDoctorDoesNotExist() {
        Appointment appointment = appointment(7L);
        when(appointmentDao.findById(7L)).thenReturn(appointment);
        when(doctorDao.findById(99L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> appointmentService.rescheduleAppointment(
                        7L,
                        99L,
                        LocalDateTime.of(2026, 6, 1, 10, 30),
                        Appointment.Priority.NORMAL,
                        ""));
    }

    @Test
    void availableSlotsAreEmptyWhenDoctorIsOnLeave() {
        Doctor doctor = new Doctor();
        LocalDate leaveDate = LocalDate.of(2026, 6, 2);
        when(doctorDao.findById(5L)).thenReturn(doctor);
        when(doctorLeaveService.isDoctorOnLeave(5L, leaveDate)).thenReturn(true);

        assertTrue(appointmentService.getAvailableSlots(5L, leaveDate, Appointment.Priority.NORMAL).isEmpty());
    }

    private Appointment appointment(Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointment.setPriority(Appointment.Priority.NORMAL);
        return appointment;
    }
}
