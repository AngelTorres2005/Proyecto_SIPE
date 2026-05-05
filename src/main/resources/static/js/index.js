document.addEventListener('DOMContentLoaded', () => {
    sweetAlertError();
    sweetAlertSuccess();
    pacientesContenedor();
});


/////////////////////////////BACKEND/////////////////////////////////////////////
async function pacientesContenedor() {
    try {
        const contenedor = document.getElementById("pacientesContenedor");

        const response = await fetch("/pacientesContenedor");
        const pacientes = await response.json();

        let filasHTML = "";

        pacientes.forEach(paciente => {
            console.log(paciente.id_paciente);
            filasHTML += `
                <tr>
                    <td class="px-4 fw-bold">${paciente.id_paciente}</td>
                    <td>${paciente.nombre}</td>
                    <td>${paciente.fecha_nacimiento}</td>
                    
                    <td>Escala Paykel (PSS)</td>
                    <td class="text-center">4 / 5</td>
                    <td><span class="badge bg-danger rounded-0 px-2 py-1">Riesgo Alto</span></td>
                    
                    <td class="px-1">
                        <div class="d-flex justify-content-center gap-1" style="min-width: 180px;">
                            <button class="btn btn-sm btn-outline-secondary rounded-0" title="Ver Info Completa" onclick="informacionPaciente('${paciente.id_paciente}', '${paciente.nombre}','${paciente.fecha_nacimiento}','${paciente.genero}','${paciente.email}','${paciente.notas_clinicas}')">
                                <i class="fas fa-user"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-dark rounded-0" title="Editar Datos" onclick="modalEditarPaciente('${paciente.id_paciente}', '${paciente.nombre}','${paciente.fecha_nacimiento}','${paciente.genero}','${paciente.email}','${paciente.notas_clinicas}')">
                                <i class="fas fa-user-edit"></i>
                            </button>
                            <a href="/cuestionario" class="btn btn-sm btn-dark rounded-0 px-2" title="Empezar Test">
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
async function informacionPaciente(id,nombre,fecha,genero,email,notas){
    console.log(nombre, fecha);
    const modal = document.getElementById("modalVerPaciente")
    document.getElementById("informacion-id-value").textContent = id;
    document.getElementById("informacion-nombre-value").textContent = nombre;
    document.getElementById("informacion-fecha-value").textContent = fecha;
    document.getElementById("informacion-genero-value").textContent = genero;
    document.getElementById("informacion-email-value").textContent = email;
    document.getElementById("informacion-notas-value").textContent = notas;
    new bootstrap.Modal(modal).show();

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