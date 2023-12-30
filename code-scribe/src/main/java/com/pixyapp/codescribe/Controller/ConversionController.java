package com.pixyapp.codescribe.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixyapp.codescribe.service.ConversionService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api-conversion")
public class ConversionController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConversionService conversionService;


    @PostMapping("/convertJsonToExcel")
    public ResponseEntity<?> convertJsonToExcel(@RequestPart("file") MultipartFile jsonFile) {
        try {

            JsonNode jsonNode = conversionService.convertJsonId(objectMapper.readTree(jsonFile.getBytes()));

            if (!jsonNode.isArray()) {
                return new ResponseEntity<>("Invalid JSON format. Expected an array.", HttpStatus.BAD_REQUEST);
            }

            // Create Excel workbook and sheet
            try (Workbook workbook = new XSSFWorkbook()) {

                Sheet sheet = workbook.createSheet("Sheet 1");

                // Create header row
                Row headerRow = sheet.createRow(0);
                int cellIndex = 0;
                for (JsonNode header : jsonNode.get(0)) {
                    Cell cell = headerRow.createCell(cellIndex++);
                    cell.setCellValue(header.asText()); // Corrected line
                }

                // Create data rows
                int rowIndex = 1;
                for (JsonNode data : jsonNode) {
                    Row dataRow = sheet.createRow(rowIndex++);
                    cellIndex = 0;
                    for (JsonNode value : data) {
                        Cell cell = dataRow.createCell(cellIndex++);
                        cell.setCellValue(value.asText());
                    }
                }

                // Save the workbook to a ByteArrayOutputStream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);

                // Convert ByteArrayOutputStream to byte array
                byte[] excelBytes = outputStream.toByteArray();

                // Set response headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "output.xlsx");

                return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error during conversion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
