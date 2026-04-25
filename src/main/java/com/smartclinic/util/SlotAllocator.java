package com.smartclinic.util;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Appointment.Priority;
import com.smartclinic.model.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SlotAllocator {

    private static final LocalTime CLINIC_START = LocalTime.of(9, 0);
    private static final LocalTime CLINIC_END = LocalTime.of(17, 0);

    public static List<LocalDateTime> allocateSlots(Doctor doctor, LocalDate date, Priority priority, List<Appointment> bookedAppointments) {
        List<LocalDateTime> allPossibleSlots = new ArrayList<>();
        int slotDuration = doctor.getSlotDurationMins() != null ? doctor.getSlotDurationMins() : 30;

        // 2. Generate all possible slots within clinic working hours
        LocalTime current = CLINIC_START;
        while (current.plusMinutes(slotDuration).isBefore(CLINIC_END) || current.plusMinutes(slotDuration).equals(CLINIC_END)) {
            LocalDateTime slotTime = LocalDateTime.of(date, current);
            // Only add if it's a future time (e.g. they can't book a past slot if booking for today)
            if (slotTime.isAfter(LocalDateTime.now())) {
                allPossibleSlots.add(slotTime);
            }
            current = current.plusMinutes(slotDuration);
        }

        // 1 & 3. Remove booked slots
        List<LocalDateTime> bookedTimes = bookedAppointments.stream()
                .filter(a -> a.getStatus() != Appointment.Status.CANCELLED)
                .map(Appointment::getSlotDatetime)
                .collect(Collectors.toList());

        allPossibleSlots.removeAll(bookedTimes);

        // 4, 5, 6. Apply priority rules for selecting slots.
        if (allPossibleSlots.isEmpty()) {
            return new ArrayList<>();
        }

        List<LocalDateTime> recommended = new ArrayList<>();
        
        switch (priority) {
            case EMERGENCY:
                // Return first available slot always
                recommended.add(allPossibleSlots.get(0));
                break;
            case SENIOR:
                // Return earliest morning slots
                List<LocalDateTime> morningSlots = allPossibleSlots.stream()
                        .filter(s -> s.toLocalTime().isBefore(LocalTime.of(12, 0)))
                        .collect(Collectors.toList());
                if (!morningSlots.isEmpty()) {
                    recommended.add(morningSlots.get(0));
                } else {
                    recommended.add(allPossibleSlots.get(0)); // fallback to earliest in afternoon
                }
                break;
            case NORMAL:
            default:
                // Return up to 3 next chronological slots
                int limit = Math.min(3, allPossibleSlots.size());
                recommended.addAll(allPossibleSlots.subList(0, limit));
                break;
        }

        return recommended;
    }
}
