package pe.edu.uni.centromedico.service;
import java.io.InputStream;
import java.util.Scanner;

import pe.edu.uni.centromedico.models.*;

public class BuscarPersona {
    public BuscarPersona() {
        // Constructor vacío
    }
    public Estudiante buscarEstudiantePorCodigo(String codigo) {
        Estudiante estudiante=new Estudiante(); 
        InputStream data_datosTXT = getClass().getResourceAsStream("/data/data_estudiante.txt");
        try(Scanner myreader = new Scanner(data_datosTXT)){
            while(myreader.hasNextLine()){
                String data=myreader.nextLine();
                String[] data_split=data.split("\\|");
                if(data_split[0].equals(codigo)){
                    estudiante.codigo=data_split[0];
                    estudiante.password=data_split[1];
                    estudiante.name=data_split[2];
                    estudiante.age=Integer.parseInt(data_split[3]);
                    estudiante.especialidad=data_split[4];
                    break;
                }
            }
        }
        return estudiante;
    }
    public Doctor buscarDoctorPorCodigo(String codigo) {
        Doctor doctor=new Doctor(); 
        InputStream data_datosTXT = getClass().getResourceAsStream("/data/data_doctor.txt");
        try(Scanner myreader = new Scanner(data_datosTXT)){
            while(myreader.hasNextLine()){
                String data=myreader.nextLine();
                String[] data_split=data.split("\\|");
                if(data_split[0].equals(codigo)){
                    doctor.codigo=data_split[0];
                    doctor.password=data_split[1];
                    doctor.especialidad=data_split[2];
                    doctor.name=data_split[3];
                    break;
                }
            }
        }
        return doctor;
    }
    public Admin buscarAdminPorCodigo(String codigo) {
        Admin admin=new Admin(); 
        InputStream data_datosTXT = getClass().getResourceAsStream("/data/data_admin.txt");
        try(Scanner myreader = new Scanner(data_datosTXT)){
            while(myreader.hasNextLine()){
                String data=myreader.nextLine();
                String[] data_split=data.split("\\|");
                if(data_split[0].equals(codigo)){
                    admin.codigo=data_split[0];
                    admin.password=data_split[1];
                    admin.name=data_split[2];
                    break;
                }
            }
        }
        return admin;
    }
    public Farmacia buscarFarmaciaPorCodigo(String codigo) {
        Farmacia farmacia=new Farmacia(); 
        InputStream data_datosTXT = getClass().getResourceAsStream("/data/data_farmacia.txt");
        try(Scanner myreader = new Scanner(data_datosTXT)){
            while(myreader.hasNextLine()){
                String data=myreader.nextLine();
                String[] data_split=data.split("\\|");
                if(data_split[0].equals(codigo)){
                    farmacia.codigo=data_split[0];
                    farmacia.password=data_split[1];
                    farmacia.name=data_split[2];
                    break;
                }
            }
        }
        return farmacia;
    }
    public Persona buscarPersonaPorCodigo(String codigo) {
        Estudiante estudiante=buscarEstudiantePorCodigo(codigo);
        if(estudiante.codigo!=null){
            return estudiante;
        }
        Doctor doctor=buscarDoctorPorCodigo(codigo);
        if(doctor.codigo!=null){
            return doctor;
        }
        Admin admin=buscarAdminPorCodigo(codigo);
        if(admin.codigo!=null){
            return admin;
        }
        Farmacia farmacia=buscarFarmaciaPorCodigo(codigo);
        if(farmacia.codigo!=null){
            return farmacia;
        }
        return null;
    }
}
