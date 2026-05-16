document.addEventListener('DOMContentLoaded', () => {
    sweetAlertError();
    sweetAlertSuccess();
    pacientesContenedor();
    selectInstrumento();
});

let pacienteActualId = null;
/////////////////////////////BACKEND/////////////////////////////////////////////
async function pacientesContenedor() {
    try {
        const contenedor = document.getElementById("pacientesContenedor");
        const response = await fetch("/pacientesContenedor");
        const pacientes = await response.json();
        console.log(pacientes);
        let filasHTML = "";

        pacientes.forEach(paciente => {
            const ultimaEva = paciente.evaluaciones && paciente.evaluaciones.length > 0
                ? paciente.evaluaciones[paciente.evaluaciones.length - 1]
                : null;

            const puntuacion = ultimaEva ? `${ultimaEva.puntuacion} / 5` : "N/A";
            const riesgo = ultimaEva ? ultimaEva.nivel_riesgo : "Sin evaluación";

            let badgeClass = "bg-secondary";
            if (ultimaEva) {
                badgeClass = ultimaEva.nivel_riesgo === 'Riesgo Alto' ? 'bg-danger' : 'bg-warning text-dark';
            }

            filasHTML += `
        <tr>
            <td class="px-4 fw-bold">${paciente.id_paciente}</td>
            <td>${paciente.nombre}</td>
            <td>${paciente.fecha_nacimiento}</td>
            
            <td>${ultimaEva ? 'Escala Paykel (PSS)' : 'Ninguno'}</td>
            <td class="text-center">${puntuacion}</td>
            <td><span class="badge ${badgeClass} rounded-0 px-2 py-1">${riesgo}</span></td>
            
            <td class="px-1">
                <div class="d-flex justify-content-center gap-1" style="min-width: 180px;">
                    <button class="btn btn-sm btn-outline-secondary rounded-0" title="Ver Info Completa" onclick="verPaciente('${paciente.id_paciente}', '${paciente.nombre}','${paciente.fecha_nacimiento}','${paciente.genero}','${paciente.email}','${paciente.notas_clinicas}')">
                        <i class="fas fa-user"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-dark rounded-0" title="Editar Datos" onclick="modalEditarPaciente('${paciente.id_paciente}', '${paciente.nombre}','${paciente.fecha_nacimiento}','${paciente.genero}','${paciente.email}','${paciente.notas_clinicas}')">
                        <i class="fas fa-user-edit"></i>
                    </button>
                    <a onclick="modalContenedor(${paciente.id_paciente})" class="btn btn-sm btn-dark rounded-0 px-2" title="Empezar Test">
                        <i class="fas fa-play small me-1"></i> Test
                    </a>
                    <button onclick="eliminarPaciente(${paciente.id_paciente})" class="btn btn-sm btn-outline-danger rounded-0" title="Eliminar">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </div>
            </td>
        </tr>
    `;
        });
        contenedor.innerHTML = filasHTML;

    } catch (error) {
        console.error("Error al cargar los pacientes:", error);
    }
}
async function eliminarPaciente(id) {
    const resultado = await Swal.fire({
        title: '¿Estás seguro?',
        text: "Se eliminarán todos los horarios asociados a esta materia.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    });

    if (resultado.isConfirmed) {
        try {
            const res = await fetch(`/eliminarPaciente/${id}`, {
                method: 'POST'
            });

            if (res.ok) {
                window.alertaSuccess = "Paciente eliminado correctamente";
                sweetAlertSuccess();

                pacientesContenedor();
            } else {
                window.alertaError = "No se pudo eliminar el paciente";
                sweetAlertError();
            }
        } catch (error) {
            console.error("Error al eliminar:", error);
        }
    }
}
async function verPaciente(id,nombre,fecha,genero,email,notas){
    const modal = document.getElementById("modalVerPaciente")
    document.getElementById("informacion-id-value").textContent = id;
    document.getElementById("informacion-nombre-value").textContent = nombre;
    document.getElementById("informacion-fecha-value").textContent = fecha;
    document.getElementById("informacion-genero-value").textContent = genero;
    document.getElementById("informacion-email-value").textContent = email;
    document.getElementById("informacion-notas-value").textContent = notas;
    new bootstrap.Modal(modal).show();
    informacionPaciente(id,nombre,fecha,genero,email,notas);
}
function modalEditarPaciente(id,nombre,fecha,genero,email,notas){
    const modal = document.getElementById('modalEditarPaciente');
    document.getElementById('editar-id_paciente').value = id;
    document.getElementById('editar-nombre').value = nombre;
    document.getElementById('editar-genero').value = genero;
    document.getElementById('editar-fecha-nacimiento').value = fecha;
    document.getElementById('editar-email').value = email;
    document.getElementById('editar-notas_clinicas').value = notas;
    new bootstrap.Modal(modal).show();
}
function modalContenedor(idPaciente){
    pacienteActualId = idPaciente;
    const modal = document.getElementById("modalSeleccionarTest");
    new bootstrap.Modal(modal).show();
}
function empezarCuestionario(){
    const selectSeleccionado = document.getElementById("selectInstrumento").value;
    if(!selectSeleccionado) {
        window.alertaError = "Seleccione un instrumento primero";
        sweetAlertError();
        return;
    }
    window.location.href = `/empezarCuestionario?instrumentoId=${selectSeleccionado}&pacienteId=${pacienteActualId}`;
}
async function selectInstrumento(){
    const select = document.getElementById("selectInstrumento");

    const response = await fetch("/selectInstrumento");
    const instrumentos = await response.json();

    select.innerHTML = ""
    select.innerHTML = '<option value="" selected disabled>Seleccione una prueba a aplicar...</option>'

    instrumentos.forEach(instrumento => {
        const option = document.createElement("option");
        option.value = instrumento.id_instrumento;
        option.textContent = instrumento.nombre;
        select.appendChild(option);
    })
}
async function informacionPaciente(id, nombre, fecha, genero, email, notas) {
    const iaContenedor = document.getElementById("ia-recomendacion-value");
    iaContenedor.innerHTML = '<div class="spinner-border spinner-border-sm text-primary"></div> Cargando...';

    try {
        const res = await fetch(`/ia/recomendacion?pacienteId=${id}`);
        const data = await res.json();
        iaContenedor.innerHTML = `<p class="small m-0">${data.recomendacion}</p>`;
    } catch (error) {
        iaContenedor.innerHTML = '<span class="text-danger small">No se pudo cargar la recomendación.</span>';
    }
}


//////////////////////////FRONTEND///////////////////////////////////////////////
function sweetAlertError() {
    if (window.alertaError) {

        const Toast = Swal.mixin({
            toast: true,
            position: 'top',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        });

        Toast.fire({
            icon: 'error',
            title: window.alertaError
        });
    }
}
function sweetAlertSuccess() {
    if (window.alertaSuccess) {

        const Toast = Swal.mixin({
            toast: true,
            position: 'top',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer)
                toast.addEventListener('mouseleave', Swal.resumeTimer)
            }
        });

        Toast.fire({
            icon: 'success',
            title: window.alertaSuccess
        });
    }
}