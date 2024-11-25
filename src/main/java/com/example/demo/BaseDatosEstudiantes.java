package com.example.demo;

// BaseDatosEstudiantes.java
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BaseDatosEstudiantes {
    private List<Estudiante> estudiantes;
    private static final String RUTA_ARCHIVO = "/csv/estudiantes.csv";

    public BaseDatosEstudiantes() {
        estudiantes = new ArrayList<>();
        cargarDatos();
    }

    private void cargarDatos() {
        String cuatrimestreActual = "Primer";
        String anioActual = "2024";

        // Verificación del archivo
        InputStream testStream = getClass().getResourceAsStream(RUTA_ARCHIVO);
        if (testStream == null) {
            System.err.println("ERROR: No se pudo encontrar el archivo: " + RUTA_ARCHIVO);
            return;
        }
        System.out.println("Archivo encontrado exitosamente");

        try (InputStream is = getClass().getResourceAsStream(RUTA_ARCHIVO);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

            String linea;
            System.out.println("\n=== Comenzando lectura línea por línea ===");
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                // Limpiamos las comillas extras y espacios
                linea = linea.replace("\"\"\"", "").trim();
                System.out.println("\nProcesando línea " + numeroLinea + ": " + linea);

                // Saltar líneas vacías o encabezados específicos
                if (linea.isEmpty() || linea.startsWith("Nombre y apellido")) {
                    System.out.println("Línea saltada (vacía o encabezado)");
                    continue;
                }

                // Detectar cambio de período
                if (linea.contains("Cuatrimestre")) {
                    if (linea.contains("Primer")) {
                        cuatrimestreActual = "Primer";
                    } else if (linea.contains("Segundo")) {
                        cuatrimestreActual = "Segundo";
                    }
                    if (linea.matches(".*\\d{4}.*")) {
                        anioActual = linea.replaceAll(".*?(\\d{4}).*", "$1");
                    }
                    System.out.println("Cambio de período detectado -> " +
                            cuatrimestreActual + " Cuatrimestre " + anioActual);
                    continue;
                }

                try {
                    // Dividir la línea usando punto y coma como separador
                    String[] datos = linea.split(";");
                    System.out.println("Campos encontrados: " + datos.length);

                    if (datos.length >= 3) {
                        String nombre = datos[0].trim().replace(",", ""); // Eliminamos la coma del nombre
                        String legajo = datos[1].trim();
                        String condicion = datos[2].trim();
                        String nota = datos.length > 3 ? datos[3].trim() : "";

                        System.out.println("Datos procesados:");
                        System.out.println("- Nombre: " + nombre);
                        System.out.println("- Legajo: " + legajo);
                        System.out.println("- Condición: " + condicion);
                        System.out.println("- Nota: " + nota);
                        System.out.println("- Cuatrimestre: " + cuatrimestreActual);
                        System.out.println("- Año: " + anioActual);

                        if (!nombre.isEmpty()) {
                            Estudiante estudiante = new Estudiante(
                                    nombre, legajo, condicion, nota,
                                    cuatrimestreActual, anioActual
                            );
                            estudiantes.add(estudiante);
                            System.out.println("✓ Estudiante agregado exitosamente");
                        } else {
                            System.out.println("✗ Estudiante no agregado (nombre vacío)");
                        }
                    } else {
                        System.out.println("✗ Línea ignorada (insuficientes campos)");
                    }
                } catch (Exception e) {
                    System.err.println("Error procesando línea " + numeroLinea + ": " + linea);
                    e.printStackTrace();
                }
            }

            System.out.println("\n=== Resumen final ===");
            System.out.println("Total de líneas procesadas: " + numeroLinea);
            System.out.println("Total de estudiantes cargados: " + estudiantes.size());

            if (estudiantes.size() > 0) {
                System.out.println("\nPrimeros 3 estudiantes cargados:");
                estudiantes.stream().limit(3).forEach(e -> {
                    System.out.println(String.format("- %s (%s) - %s %s - %s - Nota: %s",
                            e.getNombre(), e.getLegajo(),
                            e.getCuatrimestre(), e.getAnio(),
                            e.getCondicion(), e.getNota()));
                });
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public List<Estudiante> buscarPorCriterios(String texto, String cuatrimestre, String anio) {
        String textoNormalizado = normalizarTexto(texto);

        return estudiantes.stream()
                .filter(e -> (textoNormalizado.isEmpty() ||
                        normalizarTexto(e.getNombre()).contains(textoNormalizado) ||
                        e.getLegajo().contains(textoNormalizado))
                )
                .filter(e -> cuatrimestre.equals("Todos") || e.getCuatrimestre().equalsIgnoreCase(cuatrimestre))
                .filter(e -> anio.equals("Todos") || e.getAnio().equals(anio))
                .collect(Collectors.toList());
    }



    public List<Estudiante> buscarPorNombre(String nombre) {
        // Normalizar el nombre de búsqueda
        String nombreBusqueda = normalizarTexto(nombre.trim());

        return estudiantes.stream()
                .filter(estudiante ->
                        normalizarTexto(estudiante.getNombre()).contains(nombreBusqueda)
                )
                .collect(Collectors.toList());
    }

    private String normalizarTexto(String texto) {
        if (texto == null) return "";
        // Normaliza el texto eliminando los acentos y convirtiendo a minúsculas
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }

    public List<Estudiante> buscarPorLegajo(String legajo) {
        List<Estudiante> estudiantesEncontrados = new ArrayList<>();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getLegajo().contains(legajo)) {
                estudiantesEncontrados.add(estudiante);  // Asegúrate de agregar el estudiante a la lista
            }
        }
        return estudiantesEncontrados;  // Asegúrate de devolver una lista
    }

    public List<Estudiante> buscarPorPeriodo(String cuatrimestre, String anio) {
        return estudiantes.stream()
                .filter(e -> e.getCuatrimestre().equalsIgnoreCase(cuatrimestre) &&
                        e.getAnio().equals(anio))
                .toList();
    }

    // Método para debugging
    public void imprimirTodosLosEstudiantes() {
        System.out.println("\nLista completa de estudiantes:");
        estudiantes.forEach(e -> {
            System.out.println(String.format("Nombre: %-30s | Legajo: %-10s | Condición: %-10s | " +
                            "Nota: %-5s | Período: %s %s",
                    e.getNombre(), e.getLegajo(), e.getCondicion(), e.getNota(),
                    e.getCuatrimestre(), e.getAnio()));
        });

    }
    // Método público para obtener la lista completa de estudiantes
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

}
