package com.example.demo.controller;

import com.example.demo.BaseDatosEstudiantes;
import com.example.demo.Estudiante;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EstudianteController {

    private final BaseDatosEstudiantes baseDatos;

    // Constructor para inicializar la base de datos
    public EstudianteController() {
        baseDatos = new BaseDatosEstudiantes();
    }

    @GetMapping("/")
    public String redirigirARaiz() {
        return "redirect:/estudiantes/buscarFormulario";
    }

    // Ruta para mostrar el formulario de búsqueda en /estudiantes/buscarFormulario
    @GetMapping("/estudiantes/buscarFormulario")
    public String mostrarFormularioDeBusqueda() {
        return "buscar-estudiantes";  // Devuelve la vista del formulario de búsqueda
    }

    // Ruta para realizar la búsqueda de estudiantes por nombre o legajo
    @GetMapping("/estudiantes/buscar")
    public String buscarEstudiantes(@RequestParam(required = false) String texto,
                                    @RequestParam(required = false) String legajo,
                                    @RequestParam(defaultValue = "Todos") String anio,
                                    @RequestParam(defaultValue = "Todos") String cuatrimestre,
                                    Model model) {
        List<Estudiante> estudiantes;

        if ((texto != null && !texto.isEmpty()) || (legajo != null && !legajo.isEmpty())) {
            if (texto != null && !texto.isEmpty()) {
                estudiantes = baseDatos.buscarPorNombre(texto);
            } else {
                estudiantes = baseDatos.buscarPorLegajo(legajo);
            }
        } else {
            // Si no hay texto ni legajo, filtra solo por año y cuatrimestre
            estudiantes = baseDatos.buscarPorPeriodo(cuatrimestre, anio);
        }

        // Aplicar filtro adicional por cuatrimestre y año si es necesario
        estudiantes = estudiantes.stream()
                .filter(e -> cuatrimestre.equals("Todos") || e.getCuatrimestre().equalsIgnoreCase(cuatrimestre))
                .filter(e -> anio.equals("Todos") || e.getAnio().equals(anio))
                .toList();

        if (estudiantes.isEmpty()) {
            model.addAttribute("mensaje", "No se encontraron estudiantes.");
        } else {
            model.addAttribute("estudiantes", estudiantes);
        }

        return "resultados-de-busqueda";
    }

}
