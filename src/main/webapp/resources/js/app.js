$(document).ready(function() {
    const contextPath = window.smartClinicContextPath || '';
    const apiBase = contextPath + '/api/appointments';
    
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
            $.get(apiBase + '/slots', { doctorId: docId, date: date, priority: priority }, function(response) {
                const data = response.data || response;
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
            $.get(apiBase + '/queue/' + docId, function(response) {
                const data = response.data || response;
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
                        `<a href="${contextPath}/doctor/consult/${appt.id}" class="btn btn-primary" style="padding:0.25rem 0.5rem;font-size:0.875rem">Consult</a>` : '-';
                        
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
    
    let itemIndex = $('.prescription-item').length;
    $('#addMedicineBtn').on('click', function() {
        const row = `
        <div class="prescription-item" style="display:grid;grid-template-columns:1.4fr 0.8fr 0.7fr 0.9fr 1.4fr auto;gap:10px;margin-bottom:10px;align-items:center;">
            <input type="text" name="items[`+itemIndex+`].medicineName" placeholder="Medicine" class="form-control medicine-name-input" required/>
            <input type="text" name="items[`+itemIndex+`].dosage" placeholder="Dosage" class="form-control" required/>
            <input type="number" min="1" name="items[`+itemIndex+`].quantity" value="1" placeholder="Qty" class="form-control" required/>
            <input type="text" name="items[`+itemIndex+`].duration" placeholder="Duration" class="form-control" required/>
            <input type="text" name="items[`+itemIndex+`].instructions" placeholder="Instructions" class="form-control"/>
            <button type="button" class="btn btn-remove" style="background:#EF4444;color:white;" onclick="$(this).parent().remove()">X</button>
        </div>
        `;
        $('#medicineItemsContainer').append(row);
        itemIndex++;
    });

    $(document).on('input', '.medicine-name-input', function() {
        const allergiesText = String($('#patientAllergies').data('allergies') || '').toLowerCase();
        const medicine = String($(this).val() || '').toLowerCase().trim();
        if (!allergiesText || allergiesText === 'none' || !medicine) {
            $('#allergyWarning').hide();
            return;
        }

        const terms = allergiesText.split(/[,\n;]/).map(term => term.trim()).filter(Boolean);
        const matched = terms.some(term => medicine.includes(term) || term.includes(medicine));
        $('#allergyWarning').toggle(matched);
    });
});
