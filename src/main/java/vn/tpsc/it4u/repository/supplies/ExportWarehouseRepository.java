package vn.tpsc.it4u.repository.supplies;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.supplies.ExportWarehouse;

@Repository
public interface ExportWarehouseRepository extends JpaRepository<ExportWarehouse, Long>{
    ExportWarehouse findById(long id);

    List<ExportWarehouse> findBySerialNum(String serialNum);

    Boolean existsBySerialNum(String serialNum);

    @Query(value = " SELECT"
    + " number"
    + " FROM export_warehouse e"
    + " WHERE"
    + " e.export_date < :lessDate AND e.list_supplies_id = :listSuppliesId", nativeQuery = true)
    List<Long> findNumberByLessDate(@Param("lessDate") Long lessDate, @Param("listSuppliesId") Long listSuppliesId);

    @Query(value = " SELECT"
    + " number"
    + " FROM export_warehouse e"
    + " WHERE"
    + " e.export_date >= :fromDate AND e.export_date <= :endDate AND e.list_supplies_id = :listSuppliesId", nativeQuery = true)
    List<Long> findNumberByFromToEndDate(@Param("fromDate") Long fromDate, @Param("endDate") Long endDate, @Param("listSuppliesId") Long listSuppliesId);

    @Query(value = " SELECT"
    + " *"
    + " FROM export_warehouse i"
    + " WHERE"
    + " i.export_date >= :fromDate AND i.export_date <= :endDate", nativeQuery = true)
    List<ExportWarehouse> findExportWarehouseToDate(@Param("fromDate") Long fromDate, @Param("endDate") Long endDate);

    @Query(value = " SELECT"
    + " *"
    + " FROM export_warehouse i"
    + " WHERE"
    + " i.export_date >= :fromDate AND i.export_date <= :endDate", nativeQuery = true)
    List<ExportWarehouse> findExportWarehouseToExportDate(@Param("fromDate") Long fromDate, @Param("endDate") Long endDate);

    @Query(value = " SELECT"
    + " *"
    + " FROM export_warehouse"
    + " ORDER BY status ASC", nativeQuery = true)
    List<ExportWarehouse> findAllSortByStatus();

}
