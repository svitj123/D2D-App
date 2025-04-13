package com.d2dapp.backend.controller;

import com.d2dapp.backend.entity.GeocodedAddress;
import com.d2dapp.backend.service.GeoService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/geo")
public class GeoUploadController {

    @Autowired
    private GeoService geoService;

    @PostMapping("/batch")
    public ResponseEntity<List<GeocodedAddress>> uploadExcel(@RequestParam("file") MultipartFile file) {
        List<String> addressList = new ArrayList<>();
        List<String> techList = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String ulica = getString(row.getCell(0));
                String stevilka = getString(row.getCell(1));
                String dodatek = getString(row.getCell(2));
                String obcina = getString(row.getCell(3));
                String tehnologija = getString(row.getCell(4));

                String naslov = ulica + " " + stevilka + (dodatek != null && !dodatek.isBlank() ? dodatek : "") + ", " + obcina;
                addressList.add(naslov);
                techList.add(tehnologija);
            }

            List<GeocodedAddress> result = geoService.getCoordinatesForAddresses(addressList);

            // dodamo tehnologijo (po indeksu)
            for (int i = 0; i < result.size(); i++) {
                GeocodedAddress g = result.get(i);
                g.setTehnologija(techList.get(i));
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    private String getString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int) cell.getNumericCellValue());
        return cell.getStringCellValue().trim();
    }
}