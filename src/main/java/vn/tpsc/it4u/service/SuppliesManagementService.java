package vn.tpsc.it4u.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.google.api.client.json.Json;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.StringUtils;
import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.model.Contract;
import vn.tpsc.it4u.repository.ContractRepository;
import vn.tpsc.it4u.repository.supplies.*;
import vn.tpsc.it4u.util.Calculator;
import vn.tpsc.it4u.model.supplies.*;
import vn.tpsc.it4u.payload.supplies.ExportWarehouseSummary;
import vn.tpsc.it4u.payload.supplies.ImportWarehouseSummary;

@Service
@ExtensionMethod({StringUtils.class})
public class SuppliesManagementService {
    @Autowired
    private ImportWarehouseRepository importWarehouseRepository;

    @Autowired 
    private ExportWarehouseRepository exportWarehouseRepository;

    @Autowired
    private ListSuppliesRepository listSuppliesRepository;

    @Autowired
    private ContractRepository contractRepository;

    public boolean createListSupplies(JSONObject data){
        ListSupplies listSupplies = new ListSupplies(
            data.getString("itemCode"),
            data.getString("name"),
            data.getString("unit"),
            data.getLong("number"),
            data.getLong("value"),
            data.getString("note")
        );
        listSuppliesRepository.save(listSupplies);    
        return true;
    }

    public Boolean uploadListSupplies(JSONObject data) {
        if (!listSuppliesRepository.existsByItemCode(data.getString("itemCode"))) {
            long number = 0;
            long value = 0;
            String note ="";
            try {
                number = data.getLong("number");
            } catch (Exception e) {
            }
            try {
                value = data.getLong("value");
            } catch (Exception e) {
            }
            try {
                note = data.getString("note");
            } catch (Exception e) {
            }
            ListSupplies createListSupplies = new ListSupplies(
                data.getString("itemCode"),
                data.getString("name"),
                data.getString("unit").toLowerCase(),
                number,
                value,
                note
            );
            listSuppliesRepository.save(createListSupplies);
        }
        return true;
    }

    public Boolean updateListSuppliesById(long id, JSONObject data) {
        ListSupplies listSupplies = listSuppliesRepository.findById(id);
        listSupplies.setItemCode(data.getString("itemCode"));
        listSupplies.setName(data.getString("name"));
        listSupplies.setUnit(data.getString("unit"));
        listSupplies.setNumber(data.getLong("number"));
        listSupplies.setValue(data.getLong("value"));
        listSupplies.setNote(data.getString("note"));
        listSuppliesRepository.save(listSupplies);
        return true;
    }

    public Boolean deleteListSuppliesById(Long id) {
        listSuppliesRepository.deleteById(id);
        return true;
    }

    public ListSupplies findListSuppliesById(long id) {
        ListSupplies listSupplies = listSuppliesRepository.findById(id);
        return listSupplies;
    }

    public List<ListSupplies> findAllListSupplies() {
        List<ListSupplies> listSupplies = listSuppliesRepository.findAll();
        return listSupplies;
    }

    public Boolean createImportWarehouse(JSONObject data) {
        ListSupplies listSupplies = listSuppliesRepository.findByItemCode(data.getString("itemCode"));
        ImportWarehouse importWarehouse = new ImportWarehouse(
            data.getString("licence"),
            data.getLong("number"),
            data.getLong("value"),
            data.getLong("totalAmount"),
            data.getString("supplier"),
            data.getString("serialNum"),
            data.getString("MAC"),
            data.getLong("warrantyPeriod"),
            data.getLong("storageTerm"),
            data.getLong("warrantyLandmark"),
            data.getLong("importDate"),
            data.getString("note")
        );
        importWarehouse.setListSupplies(listSupplies);
        importWarehouseRepository.save(importWarehouse);
        return true;
    }

    public Boolean updateImportWarehouse(long id, JSONObject data) {
        ListSupplies listSupplies = listSuppliesRepository.findByItemCode(data.getString("itemCode"));
        ImportWarehouse importWarehouse = importWarehouseRepository.findById(id);
        importWarehouse.setLicence(data.getString("licence"));
        importWarehouse.setNumber(data.getLong("number"));
        importWarehouse.setValue(data.getLong("value"));
        importWarehouse.setTotalAmount(data.getLong("totalAmount"));
        importWarehouse.setSupplier(data.getString("supplier"));
        importWarehouse.setSerialNum(data.getString("MAC"));
        importWarehouse.setMAC(data.getString("MAC"));
        importWarehouse.setWarrantyPeriod(data.getLong("warrantyPeriod"));
        importWarehouse.setStorageTerm(data.getLong("storageTerm"));
        importWarehouse.setWarrantyLandmark(data.getLong("warrantyLandmark"));
        importWarehouse.setNote(data.getString("note"));
        importWarehouse.setListSupplies(listSupplies);
        importWarehouseRepository.save(importWarehouse);
        return true;
    }

    public ImportWarehouse findImportWarehouseById(long id) {
        ImportWarehouse importWarehouse = importWarehouseRepository.findById(id);
        return importWarehouse;
    }

    public List<ImportWarehouseSummary> findAllImportWarehouse() {
        List<ImportWarehouse> importWarehouses = importWarehouseRepository.findAll();
        List<ImportWarehouseSummary> listImportWarehouse = importWarehouses.stream()
            .map(importWarehouse -> new ImportWarehouseSummary(
                importWarehouse.getId(), 
                importWarehouse.getLicence(),
                importWarehouse.getListSupplies(),
                importWarehouse.getNumber(), 
                importWarehouse.getValue(),
                importWarehouse.getTotalAmount(),
                importWarehouse.getSupplier(),
                importWarehouse.getSerialNum(),
                importWarehouse.getMAC(), 
                importWarehouse.getWarrantyPeriod(),
                importWarehouse.getStorageTerm(), 
                importWarehouse.getWarrantyLandmark(),
                importWarehouse.getImportDate(),
                importWarehouse.getNote(),
                importWarehouse.getCreatedAt()
            )).collect(Collectors.toList());
        return listImportWarehouse;
    }

    public Boolean deleteImportWarehouseById(Long id) {
        importWarehouseRepository.deleteById(id);
        return true;
    }

    public Boolean createExportWarehouse(JSONObject data) {
        String status = "Đang sử dụng";
        if (exportWarehouseRepository.existsBySerialNum(data.getString("serialNum"))) {
            List<ExportWarehouse> getListExportWarehouse = exportWarehouseRepository.findBySerialNum(data.getString("serialNum"));
            for (int i=0; i<getListExportWarehouse.size(); i++) {
                ExportWarehouse exportWarehouse = getListExportWarehouse.get(i);
                exportWarehouse.setStatus("Đã dịch chuyển");
                exportWarehouseRepository.save(exportWarehouse);
            }
        }
        ExportWarehouse exportWarehouse = new ExportWarehouse(
            data.getString("licence"),
            data.getLong("number"),
            status,
            data.getString("supplier"),
            data.getString("serialNum"),
            data.getString("MAC"),
            data.getLong("warrantyPeriod"),
            data.getLong("storageTerm"),
            data.getLong("warrantyLandmark"),
            data.getLong("exportDate"),
            data.getString("note")
        );
        ListSupplies listSupplies = listSuppliesRepository.findByItemCode(data.getString("itemCode"));
        Contract contract = contractRepository.findByClientName(data.getString("clientName"));
        exportWarehouse.setListSupplies(listSupplies);
        exportWarehouse.setContract(contract);
        exportWarehouseRepository.save(exportWarehouse);
        return true;
    }

    public Boolean uploadExportWarehouse(JSONObject data) {
        Calculator getCalculator = new Calculator();
        String status = "Đang sử dụng";
        Long convertExportDate = getCalculator.ConvertStringToSecond(data.getString("exportDate"));
        long convertWarrantyLandmark = 0;
        long number = 0;
        try {
            convertWarrantyLandmark = getCalculator.ConvertStringToSecond(data.getString("warrantyLandmark"));
        } catch (Exception e) {
            //TODO: handle exception
        }
        try {
            number = data.getLong("number");
        } catch (Exception e) {
            System.out.println(number);
        }
        ExportWarehouse exportWarehouse = new ExportWarehouse(
            data.getString("licence"),
            data.getLong("number"),
            status,
            data.getString("supplier"),
            data.getString("serialNum"),
            data.getString("MAC"),
            data.getLong("warrantyPeriod"),
            data.getLong("storageTerm"),
            convertWarrantyLandmark,
            convertExportDate,
            data.getString("note")
        );
        if (listSuppliesRepository.existsByItemCode(data.getString("itemCode"))) {
            ListSupplies listSupplies = listSuppliesRepository.findByItemCode(data.getString("itemCode"));
            exportWarehouse.setListSupplies(listSupplies);
        }
        else {
            ListSupplies createListSupplies = new ListSupplies(
                data.getString("itemCode"),
                data.getString("name"),
                data.getString("unit"),
                data.getLong("number"),
                data.getLong("value"),
                data.getString("note")
            );
            listSuppliesRepository.save(createListSupplies);
            exportWarehouse.setListSupplies(createListSupplies);
        }
        if (!data.getString("clientName").isEmpty()) {
            if (contractRepository.existsByClientName(data.getString("clientName"))) {
                try {
                    Contract contract = contractRepository.findByClientName(data.getString("clientName"));
                    exportWarehouse.setContract(contract);
                } catch (Exception e) {
                    String[] getNumContract = data.getString("numContract").split("\\s",0);
                    String numContract = getNumContract[0];
                    Contract contract = contractRepository.findByNumContract(data.getString("numContract"));
                    exportWarehouse.setContract(contract);
                }
            }
            else if (contractRepository.existsByNumContract(data.getString("numContract"))) {
                Contract contract = contractRepository.findByNumContract(data.getString("numContract"));
                exportWarehouse.setContract(contract);
            }
            else {
                Contract contract = new Contract(
                    null, 
                    data.getString("numContract"),
                    data.getString("clientName"), 
                    null,
                    data.getString("street"),
                    null
                );
                contractRepository.save(contract);
                exportWarehouse.setContract(contract);
            }
        }
        exportWarehouseRepository.save(exportWarehouse);
        return true;
    }

    public Boolean uploadImportWarehouse(JSONObject data) {
        Calculator getCalculator = new Calculator();
        long warrantyLandmark = 0;
        long importDate = 0;
        warrantyLandmark = getCalculator.ConvertStringToSecond(data.getString("warrantyLandmark"));
        try {
          warrantyLandmark = getCalculator.ConvertStringToSecond(data.getString("warrantyLandmark"));
        } catch (Exception e) {
        }
        try {
            
            importDate = getCalculator.ConvertStringToSecond(data.getString("importDate"));
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        ImportWarehouse importWarehouse = new ImportWarehouse(
            data.getString("licence"),
            data.getLong("number"),
            data.getLong("value"),
            data.getLong("totalAmount"),
            data.getString("supplier"),
            data.getString("serialNum"),
            data.getString("MAC"),
            data.getLong("warrantyPeriod"),
            data.getLong("storageTerm"),
            warrantyLandmark,
            importDate,
            data.getString("note")
        );
        
        if (listSuppliesRepository.existsByItemCode(data.getString("itemCode"))) {
            ListSupplies listSupplies = listSuppliesRepository.findByItemCode(data.getString("itemCode"));
            importWarehouse.setListSupplies(listSupplies);
        }
        else {
            ListSupplies createListSupplies = new ListSupplies(
                data.getString("itemCode"),
                data.getString("name"),
                data.getString("unit"),
                data.getLong("number"),
                data.getLong("value"),
                data.getString("note")
            );
            listSuppliesRepository.save(createListSupplies);
            importWarehouse.setListSupplies(createListSupplies);
        }
        importWarehouseRepository.save(importWarehouse);
        return true;
    }

    public Boolean updateExportWarehouse(long id, JSONObject data) {
        ListSupplies listSupplies = listSuppliesRepository.findByItemCode(data.getString("itemCode"));
        Contract contract = contractRepository.findByClientName(data.getString("clientName"));
        ExportWarehouse exportWarehouse = exportWarehouseRepository.findById(id);
        exportWarehouse.setLicence(data.getString("licence"));
        exportWarehouse.setListSupplies(listSupplies);
        long number = 0;
        String getStatus = data.getString("status");
        exportWarehouse.setStatus(getStatus);
        if (getStatus.equals("Thu hồi")) {
            exportWarehouse.setNumber(number);
        }
        else {
            exportWarehouse.setNumber(data.getLong("number"));
        }
        exportWarehouse.setSupplier(data.getString("supplier"));
        exportWarehouse.setMAC(data.getString("MAC"));
        exportWarehouse.setWarrantyPeriod(data.getLong("warrantyPeriod"));
        exportWarehouse.setStorageTerm(data.getLong("storageTerm"));
        exportWarehouse.setWarrantyLandmark(data.getLong("warrantyLandmark"));
        exportWarehouse.setContract(contract);
        exportWarehouse.setNote(data.getString("note"));
        exportWarehouseRepository.save(exportWarehouse);
        return true;
    }

    public ExportWarehouse findExportWarehouseById(long id) {
        ExportWarehouse exportWarehouse = exportWarehouseRepository.findById(id);
        return exportWarehouse;
    }

    public List<ExportWarehouse> findExportWarehouseBySerialNum(String serialNum) {
        List<ExportWarehouse> exportWarehouse = exportWarehouseRepository.findBySerialNum(serialNum);
        return exportWarehouse;
    }

    public List<ExportWarehouseSummary> findAllExportWarehouse() {
        List<ExportWarehouse> exportWarehouses = exportWarehouseRepository.findAllSortByStatus();
        List<ExportWarehouseSummary> listExportWarehouse = exportWarehouses.stream()
            .map(exportWarehouse -> new ExportWarehouseSummary(
                exportWarehouse.getId(),
                exportWarehouse.getLicence(), 
                exportWarehouse.getListSupplies(), 
                exportWarehouse.getNumber(),
                exportWarehouse.getStatus(),
                exportWarehouse.getSupplier(),
                exportWarehouse.getSerialNum(), 
                exportWarehouse.getMAC(),
                exportWarehouse.getWarrantyPeriod(), 
                exportWarehouse.getStorageTerm(),
                exportWarehouse.getWarrantyLandmark(),
                exportWarehouse.getContract(),
                exportWarehouse.getExportDate(),
                exportWarehouse.getNote(),
                exportWarehouse.getCreatedAt()
        )).collect(Collectors.toList());
        return listExportWarehouse;
    }

    public Boolean deleteExportWarehouseById(Long id) {
        exportWarehouseRepository.deleteById(id);
        return true;
    }

    public Long findNumExportByLessDate(Long fromDate, Long id) {
        long result = 0;
        List<Long> numExportWarehouse = exportWarehouseRepository.findNumberByLessDate(fromDate, id);
        for (int i=0; i<numExportWarehouse.size(); i++) {
            result = result + numExportWarehouse.get(i);
        }
        return result;
    }

    public Long findNumImportByLessDate(Long fromDate, Long id) {
        long result = 0;
        List<Long> numImportWarehouse = importWarehouseRepository.findNumberByLessDate(fromDate, id);
        for (int i = 0; i < numImportWarehouse.size(); i++) {
            result = result + numImportWarehouse.get(i);
        }
        return result;
    }

    public Long findNumImportByFromToDate(Long fromDate, Long endDate, Long id) {
        long result = 0;
        List<Long> numImportWarehouse = importWarehouseRepository.findNumberByFromToEndDate(fromDate, endDate, id);
        for (int i = 0; i < numImportWarehouse.size(); i++) {
            result = result + numImportWarehouse.get(i);
        }
        return result;
    }

    public Long findNumExportByFromToDate(Long fromDate, Long endDate, Long id) {
        long result = 0;
        List<Long> numExportWarehouse = exportWarehouseRepository.findNumberByFromToEndDate(fromDate, endDate, id);
        for (int i = 0; i < numExportWarehouse.size(); i++) {
            result = result + numExportWarehouse.get(i);
        }
        return result;
    }

    public List<ExportWarehouseSummary> findExportWarehouseByDate(Long fromDate, Long endDate) {
        List<ExportWarehouse> exportWarehouses = exportWarehouseRepository.findExportWarehouseToExportDate(fromDate, endDate);
        List<ExportWarehouseSummary> listExportWarehouse = exportWarehouses.stream()
            .map(exportWarehouse -> new ExportWarehouseSummary(
                exportWarehouse.getId(),
                exportWarehouse.getLicence(), 
                exportWarehouse.getListSupplies(), 
                exportWarehouse.getNumber(),
                exportWarehouse.getStatus(),
                exportWarehouse.getSupplier(),
                exportWarehouse.getSerialNum(), 
                exportWarehouse.getMAC(),
                exportWarehouse.getWarrantyPeriod(), 
                exportWarehouse.getStorageTerm(),
                exportWarehouse.getWarrantyLandmark(),
                exportWarehouse.getContract(), 
                exportWarehouse.getExportDate(),
                exportWarehouse.getNote(),
                exportWarehouse.getCreatedAt()
        )).collect(Collectors.toList());
        return listExportWarehouse;
    }

     public List<ImportWarehouseSummary> findImportWarehouseByDate(Long fromDate, Long endDate) {
        List<ImportWarehouse> importWarehouses = importWarehouseRepository.findImportWarehouseToImportDate(fromDate, endDate);
        List<ImportWarehouseSummary> listExportWarehouse = importWarehouses.stream()
            .map(importWarehouse -> new ImportWarehouseSummary(
                importWarehouse.getId(), 
                importWarehouse.getLicence(),
                importWarehouse.getListSupplies(),
                importWarehouse.getNumber(), 
                importWarehouse.getValue(),
                importWarehouse.getTotalAmount(),
                importWarehouse.getSupplier(),
                importWarehouse.getSerialNum(),
                importWarehouse.getMAC(), 
                importWarehouse.getWarrantyPeriod(),
                importWarehouse.getStorageTerm(), 
                importWarehouse.getWarrantyLandmark(),
                importWarehouse.getImportDate(),
                importWarehouse.getNote(),
                importWarehouse.getCreatedAt()
        )).collect(Collectors.toList());
        return listExportWarehouse;
    }
}