package vn.tpsc.it4u.repository.supplies;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.supplies.ImportWarehouse;

@Repository
public interface ImportWarehouseRepository extends JpaRepository<ImportWarehouse, Long>{
    ImportWarehouse findById(long id);

    @Query(value = " SELECT"
    + " number"
    + " FROM import_warehouse i"
    + " WHERE"
    + " i.created_at < :lessDate AND i.list_supplies_id = :listSuppliesId", nativeQuery = true)
    List<Long> findNumberByLessDate(@Param("lessDate") Timestamp lessDate, @Param("listSuppliesId") Long listSuppliesId);

    @Query(value = " SELECT"
    + " number"
    + " FROM import_warehouse i"
    + " WHERE"
    + " i.created_at >= :fromDate AND i.created_at <= :endDate AND i.list_supplies_id = :listSuppliesId", nativeQuery = true)
    List<Long> findNumberByFromToEndDate(@Param("fromDate") Timestamp fromDate, @Param("endDate") Timestamp endDate, @Param("listSuppliesId") Long listSuppliesId);

}
