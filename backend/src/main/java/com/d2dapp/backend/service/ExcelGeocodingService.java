package com.d2dapp.backend.service;

import com.d2dapp.backend.dto.GeocodedAddress;
import com.d2dapp.backend.dto.GeocodingResult;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class ExcelGeocodingService {

    private final GeoService geoService;

    public ExcelGeocodingService(GeoService geoService) {
        this.geoService = geoService;
    }

    public GeocodingResult processExcelFile(MultipartFile file) {
        List<GeocodedAddress> successful = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            while (rows.hasNext()) {
                Row row = rows.next();

                String ulica = getCellValue(row.getCell(0));
                String hs = getCellValue(row.getCell(1));
                String dodatek = getCellValue(row.getCell(2));
                String obcina = getCellValue(row.getCell(3));
                String tehnologija = getCellValue(row.getCell(4));

                String naslov = ulica + " " + hs + (dodatek != null && !dodatek.isBlank() ? dodatek : "") + ", " + obcina;

                try {
                    GeocodedAddress g = geoService.getCoordinates(naslov);
                    g.setTehnologija(tehnologija);
                    successful.add(g);
                } catch (Exception e) {
                    failed.add(naslov);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Napaka pri obdelavi Excel datoteke: " + e.getMessage(), e);
        }

        return new GeocodingResult(successful, failed);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int) cell.getNumericCellValue());
        return cell.toString().trim();
    }
}