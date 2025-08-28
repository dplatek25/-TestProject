package pl.synektik.medview.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCore {

    private final String fileName;
    private List<SettingsDto> settings = new ArrayList<>();

    private static final Pattern LINE = Pattern.compile(
            "^\\s*set\\s*\\{([^}]+)}\\s*=\\s*(.*)\\s*$",
            Pattern.CASE_INSENSITIVE
    );

    protected AbstractCore(String fileName) {
        this.fileName = fileName;
        load();
    }

    public String getFileName() { return fileName; }
    public List<SettingsDto> getSettings() { return settings; }
    public void setSettings(List<SettingsDto> settings) { this.settings = settings; }

    protected void load() {
        Path path = Path.of(fileName);
        List<SettingsDto> result = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                Matcher m = LINE.matcher(line);
                if (m.matches()) {
                    String name = m.group(1).trim();
                    String value = m.group(2).trim();
                    result.removeIf(s -> s.getName().equals(name));
                    result.add(new SettingsDto(name, value));
                }
            }
            this.settings = result;
        } catch (IOException e) {
            throw new UncheckedIOException("Nie można odczytać pliku: " + fileName, e);
        }
    }

    public String findSettings(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nazwa ustawienia nie może być pusta.");
        }
        if (settings == null) {
            throw new IllegalStateException("Lista ustawień nie została zainicjowana.");
        }
        return settings.stream()
                .filter(s -> name.equals(s.getName()))
                .findFirst()
                .map(SettingsDto::getValue)
                .orElseThrow(() -> new NoSuchElementException("Brak ustawienia: " + name));
    }

    protected boolean isWindowsServiceInstalled(String serviceName) throws IOException, InterruptedException {
        Process p = new ProcessBuilder("sc", "query", serviceName)
                .redirectErrorStream(true)
                .start();
        p.waitFor(10, TimeUnit.SECONDS);
        int code = p.exitValue(); // 0 – istnieje, 1060 – brak
        return code == 0;
    }

    protected boolean isWindowsServiceRunning(String serviceName) throws IOException, InterruptedException {
        Process p = new ProcessBuilder("sc", "query", serviceName)
                .redirectErrorStream(true)
                .start();
        p.waitFor(10, TimeUnit.SECONDS);
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream(), Charset.defaultCharset()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("STATE")) {
                    return line.contains("RUNNING");
                }
            }
        }
        return false;
    }

    /**
     * Sprawdza usługę z pliku konfiguracyjnego.
     * Zwraca true, jeśli działa; rzuca wyjątek, jeśli coś jest nie tak.
     */
    protected boolean loadsConfigAndChecksService(String settingKey) throws Exception {
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            throw new UnsupportedOperationException("Sprawdzanie usługi działa tylko na Windows.");
        }

        if (getSettings() == null || getSettings().isEmpty()) {
            throw new IllegalStateException("Lista ustawień nie została zainicjowana albo plik jest pusty.");
        }

        String serviceName = findSettings(settingKey);
        if (serviceName == null || serviceName.isBlank()) {
            throw new IllegalArgumentException("Nazwa usługi nie może być pusta.");
        }

        if (!isWindowsServiceInstalled(serviceName)) {
            throw new IllegalStateException("Usługa Windows '" + serviceName + "' nie jest zainstalowana.");
        }
        if (!isWindowsServiceRunning(serviceName)) {
            throw new IllegalStateException("Usługa Windows '" + serviceName + "' nie jest w stanie RUNNING.");
        }

        return true;
    }
}
