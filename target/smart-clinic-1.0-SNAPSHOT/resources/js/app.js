$(document).ready(function() {
    
    $('#doctorSelect, #prioritySelect').on('change', function() {
        fetchSlots();
    });
    
    $('#dateSelect').on('change', function() {
        fetchSlots();
    });
    
    function fetchSlots() {
        const docId = $('#doctorSelect').val();
        const date = $('#dateSelect').val();
        const priority = $('#prioritySelect').val();
        
        if(docId && date && priority) {
            $.get(window.location.origin + '/appointments/api/slots', { doctorId: docId, date: date, priority: priority }, function(data) {
                const select = $('#slotTimeSelect');
                select.empty();
                if(!data || data.length === 0) {
                    select.append('<option value="">No slots available</option>');
                } else {
                    data.forEach(slot => {
                        select.append(`<option value="${slot}">${new Date(slot).toLocaleString()}</option>`);
                    });
                }
            });
        }
    }

    if($('#queueDoctorSelect').length > 0) {
        let queueInterval;
        
        $('#queueDoctorSelect').on('change', function() {
            clearInterval(queueInterval);
            const docId = $(this).val();
            if(docId) {
                loadQueue(docId);
                queueInterval = setInterval(() => loadQueue(docId), 30000);
            } else {
                $('#queueTable tbody').html('<tr><td colspan="5">Select a doctor to view queue</td></tr>');
            }
        });
        
        function loadQueue(docId) {
            $.get(window.location.origin + '/appointments/api/queue/' + docId, function(data) {
                const tbody = $('#queueTable tbody');
                tbody.empty();
                if(!data || data.length === 0) {
                    tbody.append('<tr><td colspan="5">No upcoming appointments</td></tr>');
                    return;
                }
                
                data.forEach(appt => {
                    let badgeClass = 'badge-normal';
                    if(appt.priority === 'EMERGENCY') badgeClass = 'badge-emergency';
                    if(appt.priority === 'SENIOR') badgeClass = 'badge-senior';
                    let status = appt.status;
                    let patientName = appt.patient ? appt.patient.name : 'Unknown';
                    
                    const time = new Date(appt.slotDatetime).toLocaleString();
                    const actionBtn = appt.status === 'SCHEDULED' ? 
                        `<a href="/doctor/consult/${appt.id}" class="btn btn-primary" style="padding:0.25rem 0.5rem;font-size:0.875rem">Consult</a>` : '-';
                        
                    tbody.append(`
                        <tr>
                            <td>${time}</td>
                            <td>${patientName}</td>
                            <td><span class="badge ${badgeClass}">${appt.priority}</span></td>
                            <td><b>${status}</b></td>
                            <td>${actionBtn}</td>
                        </tr>
                    `);
                });
            });
        }
    }
    
    let itemIndex = 0;
    $('#addMedicineBtn').on('click', function() {
        const row = `
        <div class="prescription-item" style="display:flex;gap:10px;margin-bottom:10px;">
            <input type="text" name="items[`+itemIndex+`].medicineName" placeholder="Medicine" class="form-control" required/>
            <input type="text" name="items[`+itemIndex+`].dosage" placeholder="Dosage" class="form-control" required/>
            <input type="text" name="items[`+itemIndex+`].duration" placeholder="Duration" class="form-control" required/>
            <input type="text" name="items[`+itemIndex+`].instructions" placeholder="Instructions" class="form-control"/>
            <button type="button" class="btn btn-remove" style="background:#EF4444;color:white;" onclick="$(this).parent().remove()">X</button>
        </div>
        `;
        $('#medicineItemsContainer').append(row);
        itemIndex++;
    });
});
