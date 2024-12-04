package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractBaseDao {
    protected static Map<String, Map<Long, Object>> poorMansDatabase = new HashMap<>();
    protected abstract String getEntityName();

    protected Map<Long, Object> getInMemoryDatabase() {
        if (poorMansDatabase.get(getEntityName()) == null) {
            poorMansDatabase.put(getEntityName(),new HashMap<>());
        }
        return poorMansDatabase.get(getEntityName());
    }
    // ! IMPORTANTE: este método es para usarlo en el DAO de Cliente, Cuenta y Movimiento.
    public static long generateId(String FILE_PATH) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));

        if (lines.isEmpty()) throw new IOException("El archivo está vacío.");

        // Obtener la primera línea y dividirla en partes
        String[] parts = lines.get(0).split(";");
        if (parts.length == 0) throw new IOException("Formato de archivo incorrecto: no se encontró el ID.");
        // Leer y retornar el ID incrementado sin escribir en el archivo
        try {
            long currentId = Long.parseLong(parts[0].trim());
            return currentId + 1;
        } catch (NumberFormatException e) {
            throw new IOException("El ID en el archivo no es un número válido.", e);
        }
    }
}