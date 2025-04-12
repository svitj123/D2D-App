package com.d2dapp.backend.service;

import com.d2dapp.backend.dto.AddressDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelReaderService {

    public List<AddressDto> readAddressesFromExcel(MultipartFile file) {
        List<AddressDto> addresses = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String ulica = getString(row.getCell(0));
                String hisnaStevilka = getString(row.getCell(1));
                String dodatek = getString(row.getCell(2));
                String obcina = getString(row.getCell(3));
                String tehnologija = getString(row.getCell(4));

                // Obvezna polja
                if (ulica.isEmpty() || hisnaStevilka.isEmpty() || obcina.isEmpty()) {
                    System.err.println("Preskakujem vrstico " + (i + 1) + " - manjkajo obvezna polja");
                    continue;
                }

                addresses.add(new AddressDto(ulica, hisnaStevilka, dodatek, obcina, tehnologija));
            }

        } catch (Exception e) {
            throw new RuntimeException("Napaka pri branju Excel datoteke: " + e.getMessage(), e);
        }

        return addresses;
    }

    private String getString(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
}