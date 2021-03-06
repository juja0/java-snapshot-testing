package au.com.origin.snapshots;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
class SnapshotFile {

    private static final String SPLIT_STRING = "\n\n\n";

    private final String fileName;
    private final Class<?> testClass;
    private final BiFunction<Class<?>, String, String> onSaveSnapshotFile;

    private String getDebugFilename() {
        return this.fileName + ".debug";
    }

    @Getter
    private Set<String> rawSnapshots;

    SnapshotFile( String srcDirPath, String fileName, Class<?> testClass, BiFunction<Class<?>, String, String> onSaveSnapshotFile) throws IOException {
        this.testClass = testClass;
        this.onSaveSnapshotFile = onSaveSnapshotFile;
        this.fileName = srcDirPath + File.separator + fileName;
        log.info("Snapshot File: " + this.fileName);

        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                fileContent.append(sCurrentLine + "\n");
            }

            String fileText = fileContent.toString();
            if (StringUtils.isNotBlank(fileText)) {
                rawSnapshots =
                    Stream.of(fileContent.toString().split(SPLIT_STRING))
                        .map(String::trim)
                        .collect(Collectors.toCollection(TreeSet::new));
            } else {
                rawSnapshots = new TreeSet<>();
            }
        } catch (IOException e) {
            createFile(this.fileName);
            rawSnapshots = new TreeSet<>();
        }
    }

    public File createDebugFile(String snapshot) {
        File file = null;
        try {
            file = new File(getDebugFilename());
            file.getParentFile().mkdirs();
            file.createNewFile();

            try (FileOutputStream fileStream = new FileOutputStream(file, false)) {
                fileStream.write(snapshot.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    @SneakyThrows
    public void deleteDebugFile() {
        Files.deleteIfExists(Paths.get(getDebugFilename()));
    }

    @SneakyThrows
    public void delete() {
        Files.deleteIfExists(Paths.get(this.fileName));
    }

    private File createFile(String fileName) throws IOException {
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }

    public void push(String snapshot) {

        rawSnapshots.add(snapshot);

        File file = null;

        try {
            file = createFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fileStream = new FileOutputStream(file, false)) {
            byte[] myBytes = StringUtils.join(rawSnapshots, SPLIT_STRING).getBytes();
            fileStream.write(myBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void cleanup() {
        Path path = Paths.get(this.fileName);
        if (Files.size(path) == 0) {
            deleteDebugFile();
            delete();
        } else {
            String content = new String(Files.readAllBytes(Paths.get(this.fileName)));
            String modified = onSaveSnapshotFile.apply(testClass, content);
            Files.write(path, modified.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
