package pe.edu.uni.centromedico.service;
import java.io.InputStream;
import java.util.Scanner;

import pe.edu.uni.centromedico.models.Estudiante;


public class EstudiantesBuscar {
    public EstudiantesBuscar() {
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
}
