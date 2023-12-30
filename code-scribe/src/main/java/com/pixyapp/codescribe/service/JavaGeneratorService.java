package com.pixyapp.codescribe.service;

import com.pixyapp.codescribe.utils.GenerationData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class JavaGeneratorService {
    public ResponseEntity<byte[]> javaGenerator(String packageName, String className, List<String> properties) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        try {
            String templateLocation = new ClassPathResource("static/spring-templates/").getFile().getAbsolutePath();

            cfg.setDirectoryForTemplateLoading(new File(templateLocation));

            Path tempDirectory = Files.createTempDirectory("tempFiles");

            GenerationData data = new GenerationData();
            data.setPackageName(packageName);
            data.setClassName(className);
            data.setProperties(properties);

            generateClass(cfg, "model.ftl", data, tempDirectory, className);
            generateClass(cfg, "controller.ftl", data, tempDirectory, className + "Controller");
            generateClass(cfg, "service.ftl", data, tempDirectory, className + "Service");
            generateClass(cfg, "repository.ftl", data, tempDirectory, className + "Repository");
            generateClass(cfg, "serviceimpl.ftl", data, tempDirectory, className + "ServiceImpl");

            // Create a zip file in the temporary directory
            String zipFileName = className+".zip";
            Path zipFilePath = tempDirectory.resolve(zipFileName);
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
                File[] filesToZip = tempDirectory.toFile().listFiles();
                if (filesToZip != null) {
                    for (File file : filesToZip) {
                        addToZip(file, file.getName(), zipOutputStream);
                    }
                }
            }

            // Read the zip file into a byte array
            byte[] zipFileBytes = Files.readAllBytes(zipFilePath);

            // Set up HTTP headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", zipFileName);

            // Clean up the temporary directory
            Files.walk(tempDirectory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

            // Return the zip file as a ResponseEntity
            return new ResponseEntity<>(zipFileBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void generateClass(Configuration cfg, String templateFile, GenerationData data, Path outputDir, String fileName) {
        try {
            Template template = cfg.getTemplate(templateFile);

            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("data", data); // Provide the data model to the template

            File packageDir = new File(outputDir + "/" + data.getPackageName().replace(".", "/"));
            packageDir.mkdirs();

            File file = new File(packageDir, fileName + ".java");
            Writer writer = new FileWriter(file);
            template.process(dataModel, writer);
            writer.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private static void addToZip(File file, String entryName, ZipOutputStream zipOutputStream) throws IOException {
        if (file.isDirectory()) {
            // For directories, recursively add their contents
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    addToZip(subFile, entryName + "/" + subFile.getName(), zipOutputStream);
                }
            }
        } else {
            // For files, add the file to the zip
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                ZipEntry zipEntry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }
                zipOutputStream.closeEntry();
            }
        }

    }
}
