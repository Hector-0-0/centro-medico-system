package pe.edu.uni.centromedico.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public final class Validador {

    private static final Pattern CODIGO_USUARIO = Pattern.compile("^[a-zA-Z0-9]{9}$");
    private static final Pattern CODIGO_PACIENTE = Pattern.compile("^[Uu][a-zA-Z0-9]{8}$");
    private static final Pattern CODIGO_DOCTOR = Pattern.compile("^[Dd][a-zA-Z0-9]{8}$");
    private static final Pattern EMAIL = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    private static final Pattern DOSIS = Pattern.compile("^\\d+(\\.\\d+)?\\s*(mg|g|ml|ui|l|mcg|%|mg/ml)?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern TIENE_NUMEROS = Pattern.compile("\\d");

    private static final String[] PALABRAS_OFENSIVAS = {
        "puta", "puto", "mierda", "carajo", "concha", "verga", "webada",
        "huevon", "cojudo", "idiota", "estupido", "tonto", "imbecil",
        "pendejo", "caca", "culo", "marica", "maricon", "flaite"
    };

    private Validador() {}

    public static boolean esCodigoUsuarioValido(String codigo) {
        return codigo != null && CODIGO_USUARIO.matcher(codigo.trim()).matches();
    }

    public static String validarCodigoPaciente(String codigo) {
        if (codigo == null || !CODIGO_PACIENTE.matcher(codigo.trim()).matches())
            return "El código del paciente debe empezar con 'U' y tener 9 caracteres.";
        return null;
    }

    public static String validarCodigoDoctor(String codigo) {
        if (codigo == null || !CODIGO_DOCTOR.matcher(codigo.trim()).matches())
            return "El código del médico debe empezar con 'D' y tener 9 caracteres.";
        return null;
    }

    public static String validarNombreSinNumeros(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;
        if (TIENE_NUMEROS.matcher(nombre).find())
            return "El nombre no puede contener números.";
        return null;
    }

    public static boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return false;
        return calcularEdad(fechaNacimiento) >= 18;
    }

    public static int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public static String validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) return "El email no puede estar vacío.";
        if (!EMAIL.matcher(email.trim()).matches()) return "Formato de email inválido.";
        return null;
    }

    public static String validarTextoLibre(String texto) {
        if (texto == null || texto.trim().isEmpty()) return null;
        String minus = texto.toLowerCase();
        for (String ofensa : PALABRAS_OFENSIVAS) {
            if (minus.contains(ofensa)) return "El texto contiene palabras no permitidas.";
        }
        return null;
    }

    public static String validarStock(int stock) {
        if (stock < 0) return "El stock no puede ser negativo.";
        return null;
    }

    public static String validarDosis(String dosis) {
        if (dosis == null || dosis.trim().isEmpty()) return null;
        if (!DOSIS.matcher(dosis.trim()).matches()) return "Formato de dosis inválido. Ej: 500mg, 1g, 5ml";
        return null;
    }
}
