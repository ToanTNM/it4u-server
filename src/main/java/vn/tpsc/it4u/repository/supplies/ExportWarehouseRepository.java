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

    @Query(value = " SELECT"
    + " number"
    + " FROM export_warehouse e"
    + " WHERE"
    + " e.created_at < :lessDate AND e.list_supplies_id = :listSuppliesId", nativeQuery = true)
    List<Long> findNumberByLessDate(@Param("lessDate") Timestamp lessDate, @Param("listSuppliesId") Long listSuppliesId);

    @Query(value = " SELECT"
    + " number"
    + " FROM export_warehouse e"
    + " WHERE"
    + " e.created_at >= :fromDate AND e.created_at <= :endDate AND e.list_supplies_id = :listSuppliesId", nativeQuery = true)
    List<Long> findNumberByFromToEndDate(@Param("fromDate") Timestamp fromDate, @Param("endDate") Timestamp endDate, @Param("listSuppliesId") Long listSuppliesId);

    @Query(value = " SELECT"
    + " *"
    + " FROM export_warehouse i"
    + " WHERE"
    + " i.created_at >= :fromDate AND i.created_at <= :endDate", nativeQuery = true)
    List<ExportWarehouse> findExportWarehouseToDate(@Param("fromDate") Timestamp fromDate, @Param("endDate") Timestamp endDate);
}
