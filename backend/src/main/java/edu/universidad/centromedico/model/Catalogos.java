package edu.universidad.centromedico.model;

import java.util.Set;

/**
 * Catálogos cerrados de carreras y especialidades. Fuente de verdad para validar
 * que el admin no registre valores basura (p. ej. carrera "asd"). Debe mantenerse
 * en sintonía con `frontend/src/constants/catalogos.js`.
 */
public final class Catalogos {

    private Catalogos() {}

    public static final Set<String> CARRERAS = Set.of(
        "Ingenieria de Sistemas", "Ingenieria Industrial", "Ingenieria Civil",
        "Ingenieria Electronica", "Ingenieria Mecanica", "Ingenieria Quimica",
        "Ingenieria Economica", "Arquitectura", "Ciencias"
    );

    public static final Set<String> ESPECIALIDADES = Set.of(
        "Medicina General", "Cardiologia", "Dermatologia", "Endocrinologia",
        "Ginecologia", "Neurologia", "Odontologia", "Oftalmologia", "Pediatria",
        "Psicologia", "Radiologia", "Traumatologia"
    );

    public static final Set<String> TIPOS_SANGRE = Set.of(
        "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"
    );

    /** El tipo de sangre es opcional; si viene, debe ser uno de la lista. */
    public static void validarTipoSangre(String tipoSangre) {
        if (tipoSangre != null && !tipoSangre.isBlank() && !TIPOS_SANGRE.contains(tipoSangre.trim())) {
            throw new RuntimeException("El tipo de sangre no es válido");
        }
    }

    public static void validarCarrera(String carrera) {
        if (carrera == null || !CARRERAS.contains(carrera.trim())) {
            throw new RuntimeException("La carrera no es válida; selecciona una de la lista");
        }
    }

    public static void validarEspecialidad(String especialidad) {
        if (especialidad == null || !ESPECIALIDADES.contains(especialidad.trim())) {
            throw new RuntimeException("La especialidad no es válida; selecciona una de la lista");
        }
    }
}
