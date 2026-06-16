package edu.universidad.centromedico.config;

import edu.universidad.centromedico.model.*;
import edu.universidad.centromedico.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Inicializa datos de prueba al arrancar la aplicación.
 * Solo crea registros si no existen (idempotente).
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository    usuarioRepository;
    private final EspecialidadRepository especialidadRepository;
    private final MedicoRepository     medicoRepository;
    private final PacienteRepository   pacienteRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final PasswordEncoder      passwordEncoder;

    @Override
    public void run(String... args) {
        // ── Usuarios ──────────────────────────────────────────────
        Usuario admin  = crearUsuario("admin",    "admin123", "admin@uni.edu.pe",    Rol.ADMIN);
        Usuario uMed1  = crearUsuario("medico1",  "admin123", "medico1@uni.edu.pe",  Rol.MEDICO);
        Usuario uMed2  = crearUsuario("medico2",  "admin123", "medico2@uni.edu.pe",  Rol.MEDICO);
        Usuario uRecep = crearUsuario("recep1",   "admin123", "recep1@uni.edu.pe",   Rol.RECEPCIONISTA);
        Usuario uPac1  = crearUsuario("paciente1","admin123", "pac1@uni.edu.pe",     Rol.PACIENTE);
        Usuario uPac2  = crearUsuario("paciente2","admin123", "pac2@uni.edu.pe",     Rol.PACIENTE);

        // ── Especialidades ────────────────────────────────────────
        Especialidad med   = crearEspecialidad("Medicina General", "Atención primaria de salud");
        Especialidad odont = crearEspecialidad("Odontología",       "Salud bucal y dental");
        Especialidad psic  = crearEspecialidad("Psicología",        "Salud mental y bienestar");
        Especialidad nutr  = crearEspecialidad("Nutrición",         "Orientación nutricional");

        // ── Médicos ───────────────────────────────────────────────
        Medico mGarcia = crearMedico("CMP-12345", "Carlos",   "García",  "987654321", med,   uMed1);
        Medico mTorres = crearMedico("CMP-67890", "Patricia", "Torres",  "976543210", odont, uMed2);

        // ── Pacientes vinculados a sus usuarios ───────────────────
        crearPaciente("70123456", "Juan",  "Pérez",  LocalDate.of(2000,5,15), "991234567", "O+",  null,          uPac1);
        crearPaciente("70654321", "María", "López",  LocalDate.of(1999,11,20),"992345678", "A+",  "Penicilina",  uPac2);

        // ── Medicamentos (inventario de farmacia) ─────────────────
        if (medicamentoRepository.count() == 0) {
            crearMedicamento("Paracetamol 500mg", "Paracetamol", "Tabletas", 120, 20, "tableta");
            crearMedicamento("Ibuprofeno 400mg",  "Ibuprofeno",  "Tabletas",  80, 20, "tableta");
            crearMedicamento("Amoxicilina 500mg", "Amoxicilina", "Cápsulas",  15, 20, "cápsula");
            crearMedicamento("Loratadina 10mg",   "Loratadina",  "Tabletas",  60, 15, "tableta");
            crearMedicamento("Suero oral",        "Electrolitos","Sobre",      8, 10, "sobre");
        }

        // ── Disponibilidades de los médicos ───────────────────────
        if (disponibilidadRepository.count() == 0 && mGarcia != null && mTorres != null) {
            crearDisponibilidad(mGarcia, "LUNES",     LocalTime.of(8, 0),  LocalTime.of(12, 0), "Consultorio 1");
            crearDisponibilidad(mGarcia, "MIERCOLES", LocalTime.of(14, 0), LocalTime.of(18, 0), "Consultorio 1");
            crearDisponibilidad(mTorres, "MARTES",    LocalTime.of(9, 0),  LocalTime.of(13, 0), "Consultorio 2");
            crearDisponibilidad(mTorres, "JUEVES",    LocalTime.of(8, 0),  LocalTime.of(12, 0), "Consultorio 2");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────

    private Usuario crearUsuario(String username, String password, String email, Rol rol) {
        return usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            u.setEmail(email);
            u.setRol(rol);
            u.setActivo(true);
            Usuario guardado = usuarioRepository.save(u);
            System.out.println("✅ Usuario creado: " + username + " [" + rol + "]");
            return guardado;
        });
    }

    private Especialidad crearEspecialidad(String nombre, String descripcion) {
        return especialidadRepository.findAll().stream()
            .filter(e -> e.getNombre().equals(nombre))
            .findFirst()
            .orElseGet(() -> {
                Especialidad e = new Especialidad();
                e.setNombre(nombre);
                e.setDescripcion(descripcion);
                return especialidadRepository.save(e);
            });
    }

    private Medico crearMedico(String cmp, String nombre, String apellido,
                              String tel, Especialidad esp, Usuario usuario) {
        return medicoRepository.findAll().stream()
            .filter(m -> cmp.equals(m.getCmp()))
            .findFirst()
            .orElseGet(() -> {
                Medico m = new Medico();
                m.setCmp(cmp);
                m.setNombre(nombre);
                m.setApellido(apellido);
                m.setTelefono(tel);
                m.setEspecialidad(esp);
                m.setUsuario(usuario);
                Medico guardado = medicoRepository.save(m);
                System.out.println("✅ Médico creado: Dr. " + nombre + " " + apellido);
                return guardado;
            });
    }

    private void crearMedicamento(String nombre, String principio, String presentacion,
                                  int stock, int minimo, String unidad) {
        Medicamento m = new Medicamento();
        m.setNombre(nombre);
        m.setPrincipioActivo(principio);
        m.setPresentacion(presentacion);
        m.setStockActual(stock);
        m.setStockMinimo(minimo);
        m.setUnidad(unidad);
        m.setActivo(true);
        medicamentoRepository.save(m);
    }

    private void crearDisponibilidad(Medico medico, String dia,
                                     LocalTime inicio, LocalTime fin, String consultorio) {
        Disponibilidad d = new Disponibilidad();
        d.setMedico(medico);
        d.setDiaSemana(dia);
        d.setHoraInicio(inicio);
        d.setHoraFin(fin);
        d.setConsultorio(consultorio);
        d.setActivo(true);
        disponibilidadRepository.save(d);
    }

    private void crearPaciente(String dni, String nombre, String apellido,
                                LocalDate fechaNac, String tel, String grupo,
                                String alergias, Usuario usuario) {
        if (!pacienteRepository.existsByDni(dni)) {
            Paciente p = new Paciente();
            p.setDni(dni);
            p.setNombre(nombre);
            p.setApellido(apellido);
            p.setFechaNacimiento(fechaNac);
            p.setTelefono(tel);
            p.setGrupoSanguineo(grupo);
            p.setAlergias(alergias);
            p.setUsuario(usuario);  // ← vínculo con la cuenta de acceso
            pacienteRepository.save(p);
            System.out.println("✅ Paciente creado: " + nombre + " " + apellido + " (DNI: " + dni + ")");
        }
    }
}