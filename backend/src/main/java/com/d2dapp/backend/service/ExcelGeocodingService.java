package com.d2dapp.backend.service;

import com.d2dapp.backend.entity.GeocodedAddress;
import com.d2dapp.backend.repository.GeocodedAddressRepository;
import com.d2dapp.backend.service.GeoService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelGeocodingService {

    private final GeoService geoService;

    public ExcelGeocodingService(GeoService geoService) {
        this.geoService = geoService;
    }

    public List<GeocodedAddress> processExcelFile(MultipartFile file) {
        List<GeocodedAddress> result = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next(); // skip header

            List<String> addressStrings = new ArrayList<>();
            List<String> techList = new ArrayList<>();

            while (rows.hasNext()) {
                Row row = rows.next();

                String ulica = getCellValue(row.getCell(0));
                String hs = getCellValue(row.getCell(1));
                String dodatek = getCellValue(row.getCell(2));
                String obcina = getCellValue(row.getCell(3));
                String tehnologija = getCellValue(row.getCell(4));

                String address = String.format("%s %s%s, %s",
                        ulica,
                        hs,
                        dodatek != null && !dodatek.isEmpty() ? dodatek : "",
                        obcina);

                addressStrings.add(address);
                techList.add(tehnologija);
            }

            List<GeocodedAddress> geoResults = geoService.getCoordinatesForAddresses(addressStrings);

            // dodamo tehnologijo ročno
            for (int i = 0; i < geoResults.size(); i++) {
                GeocodedAddress g = geoResults.get(i);
                g.setTehnologija(techList.get(i));
                result.add(g);
            }

        } catch (Exception e) {
            throw new RuntimeException("Napaka pri obdelavi Excel datoteke: " + e.getMessage(), e);
        }

        return result;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue().trim();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int) cell.getNumericCellValue());
        return cell.toString().trim();
    }
}
