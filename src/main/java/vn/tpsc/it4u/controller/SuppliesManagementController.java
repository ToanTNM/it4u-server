package vn.tpsc.it4u.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import vn.tpsc.it4u.payload.supplies.ExportWarehouseSummary;
import vn.tpsc.it4u.payload.supplies.ImportWarehouseSummary;
import vn.tpsc.it4u.service.SuppliesManagementService;
import vn.tpsc.it4u.util.ApiResponseUtils;
import vn.tpsc.it4u.util.Calculator;

@RestController
@RequestMapping("${app.api.version}")
public class SuppliesManagementController {
    @Autowired
    SuppliesManagementService suppliesManagementService;

    @Autowired
    ApiResponseUtils apiResponse;
    
    @ApiOperation(value = "Create a list supplies")
    @PostMapping("/it4u/listSupplies")
    public String createListSupplies(@RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        suppliesManagementService.createListSupplies(convertDataToJson);
        JSONArray getData = new JSONArray(suppliesManagementService.findAllListSupplies());
        return getData.toString();
    }
    
    @ApiOperation(value = "Upload file list supplies")
    @PostMapping("/it4u/uploadListSupplies")
    public String uploadListSupplies(@RequestBody String data) {
        JSONArray convertDataToJson = new JSONArray(data);
        for (int i=0; i<convertDataToJson.length(); i++) {
            JSONObject getItem = (JSONObject) convertDataToJson.get(i);
            suppliesManagementService.uploadListSupplies(getItem);
            System.out.println(i);
        }
        JSONArray getListSupplies = new JSONArray(suppliesManagementService.findAllListSupplies());
        return getListSupplies.toString();
    }

    @ApiOperation(value = "Updated a list supplies by id")
    @PutMapping("/it4u/listSupplies/{id}")
    public String updatedListSuppliesById(@PathVariable(value="id") Long id,@RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        suppliesManagementService.updateListSuppliesById(id, convertDataToJson);
        JSONArray getData = new JSONArray(suppliesManagementService.findAllListSupplies());
        return getData.toString();
    }

    @ApiOperation(value = "Get a list supplies by id")
    @GetMapping("/it4u/listSupplies/{id}")
    public String getListSuppliesById(@PathVariable(value="id") long id) {
        JSONObject getData = new JSONObject(suppliesManagementService.findListSuppliesById(id));
        return getData.toString();
    }

    @ApiOperation(value = "Get all list supplies")
    @GetMapping("/it4u/listSupplies")
    public String getAllListSUpplies() {
        JSONArray getData = new JSONArray(suppliesManagementService.findAllListSupplies());
        return getData.toString();
    }

    @ApiOperation(value = "Delete a list supplies by id")
    @DeleteMapping("/it4u/listSupplies/{id}")
    public Boolean deleteListSuppliesById(@PathVariable(value="id") Long id) {
        suppliesManagementService.deleteListSuppliesById(id);
        return true;
    }

    @ApiOperation(value = "Upload file export warehouse")
    @PostMapping("/it4u/uploadExportWarehouse")
    public String uploadExportWarehouse(@RequestBody String data) {
        JSONArray convertDataToJson = new JSONArray(data);
        for (int i = 0; i < convertDataToJson.length(); i++) {
            JSONObject getItem = (JSONObject) convertDataToJson.get(i);
            suppliesManagementService.uploadExportWarehouse(getItem);
        }
        JSONArray getExportWarehouse = new JSONArray(suppliesManagementService.findAllExportWarehouse());
        return getExportWarehouse.toString();
    }

    @ApiOperation(value = "Create an export warehouse")
    @PostMapping("/it4u/exportWarehouse")
    public String createExportWarehouse(@RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        suppliesManagementService.createExportWarehouse(convertDataToJson);
        JSONArray getExportWarehouse = new JSONArray(suppliesManagementService.findAllExportWarehouse());
        return getExportWarehouse.toString();
    }

    @ApiOperation(value = "Update an export warehouse by id")
    @PutMapping("/it4u/exportWarehouse/{id}")
    public String updateExportWarehouseById(@PathVariable(value="id") Long id, @RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        suppliesManagementService.updateExportWarehouse(id, convertDataToJson);
        JSONArray getExportWarehouse = new JSONArray(suppliesManagementService.findAllExportWarehouse());
        return getExportWarehouse.toString();
    }

    @ApiOperation(value = "Get an export warehouse by id")
    @GetMapping("/it4u/exportWarehouse/{id}")
    public String getExportWarehouseById(@PathVariable(value="id") long id) {
        JSONObject getData = new JSONObject(suppliesManagementService.findExportWarehouseById(id));
        return getData.toString();
    }

    @ApiOperation(value = "get an export warehouse by mac")
    @GetMapping("/it4u/exportWarehouseBySerialNum/{serialNum}")
    public String getExportWarehouseByMAC(@PathVariable(value="serialNum") String serialNum) {
        JSONArray getData = new JSONArray(suppliesManagementService.findExportWarehouseBySerialNum(serialNum));
        return getData.toString();
    }

    @ApiOperation(value = "Get all export warehouse by date")
    @PostMapping("/it4u/exportWarehouseByDate")
    public String getExportWarehouseByDate(@RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        // Calculator getCalculator = new Calculator();
        Long fromDate = convertDataToJson.getLong("fromDate");
        Long toDate = convertDataToJson.getLong("toDate");
        // Timestamp convertFromDate = getCalculator.convertStringToTimestamp(fromDate);
        // Timestamp convertToDate = getCalculator.convertStringToTimestamp(toDate);
        List<ExportWarehouseSummary> getResult = suppliesManagementService.findExportWarehouseByDate(fromDate,
                toDate);
        JSONArray result = new JSONArray(getResult);
        return result.toString();
    }

    @ApiOperation(value = "Get all export warehouse")
    @GetMapping("/it4u/exportWarehouse")
    public String getAllExportWarehouse() {
        JSONArray getExportWarehouse = new JSONArray(suppliesManagementService.findAllExportWarehouse());
        return getExportWarehouse.toString();
    }

    @ApiOperation(value = "Delete an export warehouse by id")
    @DeleteMapping("/it4u/exportWarehouse/{id}")
    public Boolean deleteExportWarehouse(@PathVariable(value="id") Long id) {
        suppliesManagementService.deleteExportWarehouseById(id);
        return true;
    }

    @ApiOperation(value = "Upload file import warehouse")
    @PostMapping("/it4u/uploadImportWarehouse")
    public String uploadImportWarehouse(@RequestBody String data) {
        JSONArray convertDataToJson = new JSONArray(data);
        for (int i = 0; i < convertDataToJson.length(); i++) {
            JSONObject getItem = (JSONObject) convertDataToJson.get(i);
            suppliesManagementService.uploadImportWarehouse(getItem);
        }
        JSONArray getImportWarehouse = new JSONArray(suppliesManagementService.findAllImportWarehouse());
        return getImportWarehouse.toString();
    }

    @ApiOperation(value = "Post an import warehouse")
    @PostMapping("/it4u/importWarehouse")
    public String createImportWarehouse(@RequestBody String data){
        JSONObject convertDataToJson = new JSONObject(data);
        suppliesManagementService.createImportWarehouse(convertDataToJson);
        JSONArray getImportWarehouse = new JSONArray(suppliesManagementService.findAllImportWarehouse());
        return getImportWarehouse.toString();
    }

    @ApiOperation(value = "update an import warehouse by id")
    @PutMapping("/it4u/importWarehouse/{id}")
    public String updateImportWarehouseById(@PathVariable(value="id") Long id, @RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        suppliesManagementService.updateImportWarehouse(id, convertDataToJson);
        JSONArray getImportWarehouse = new JSONArray(suppliesManagementService.findAllImportWarehouse());
        return getImportWarehouse.toString();
    }

    @ApiOperation(value = "Get an import warehouse by id")
    @GetMapping("/it4u/importWarehouse/{id}")
    public String getImportWarehouseById(@PathVariable(value = "id") long id) {
        JSONObject getData = new JSONObject(suppliesManagementService.findImportWarehouseById(id));
        return getData.toString();
    }

    @ApiOperation(value = "Get all import warehouse by date")
    @PostMapping("/it4u/importWarehouseByDate")
    public String getImportWarehouseByDate(@RequestBody String data) {
        JSONObject convertDataToJson = new JSONObject(data);
        // Calculator getCalculator = new Calculator();
        Long fromDate = convertDataToJson.getLong("fromDate");
        Long toDate = convertDataToJson.getLong("toDate");
        // Timestamp convertFromDate = getCalculator.convertStringToTimestamp(fromDate);
        // Timestamp convertToDate = getCalculator.convertStringToTimestamp(toDate);
        List<ImportWarehouseSummary> getResult = suppliesManagementService.findImportWarehouseByDate(fromDate, toDate);
        JSONArray result = new JSONArray(getResult);
        return result.toString();
    }

    @ApiOperation(value = "Get all import warehouse")
    @GetMapping("/it4u/importWarehouse")
    public String getImportWarehouse() {
        JSONArray getImportWarehouse = new JSONArray(suppliesManagementService.findAllImportWarehouse());
        return getImportWarehouse.toString();
    }

    @ApiOperation(value = "Delete an improt warehouse by id")
    @DeleteMapping("/it4u/importWarehouse/{id}")
    public Boolean deleteImportWarehouse(@PathVariable(value="id") Long id) {
        suppliesManagementService.deleteImportWarehouseById(id);
        return true;
    }

    @ApiOperation(value = "Report supplies")
    @PostMapping("/it4u/reportSupplies")
    public String getReportSupplies(@RequestBody String data) {
        List<String> result = new ArrayList<>();
        JSONObject convertDataToJson = new JSONObject(data);
        // Calculator getCalculator = new Calculator();
        Long fromDate = convertDataToJson.getLong("fromDate");
        Long toDate = convertDataToJson.getLong("toDate");
        // Timestamp convertFromDate = getCalculator.convertStringToTimestamp(fromDate);
        // Timestamp convertToDate = getCalculator.convertStringToTimestamp(toDate);
        JSONArray getData = new JSONArray(suppliesManagementService.findAllListSupplies());
        for (int i=0; i< getData.length(); i++) {
            JSONObject createData = new JSONObject();
            JSONObject getItem = (JSONObject) getData.get(i);
            Long id = getItem.getLong("id"); 
            Long numExportLess = suppliesManagementService.findNumExportByLessDate(fromDate, id);
            Long numImportLess = suppliesManagementService.findNumImportByLessDate(fromDate, id);
            Long inventoryStart = numImportLess - numExportLess;
            Long numImportFromToDate = suppliesManagementService.findNumImportByFromToDate(fromDate, toDate, id);
            Long numExportFromToDate = suppliesManagementService.findNumExportByFromToDate(fromDate, toDate, id);
            // createData.put("numExportLess", numExportLess);
            Long inventoryEnd = numImportFromToDate + inventoryStart - numExportFromToDate;
            createData.put("itemCode", getItem.getString("itemCode"));
            createData.put("name", getItem.getString("name"));
            createData.put("unit", getItem.getString("unit"));
            createData.put("note", getItem.getString("note"));
            createData.put("inventoryStart", inventoryStart);
            createData.put("numImportFromToDate", numImportFromToDate);
            createData.put("numExportFromToDate", numExportFromToDate);
            createData.put("inventoryEnd", inventoryEnd);
            result.add(createData.toString());
        }
        return result.toString();
    }
}
