package vn.tpsc.it4u.repository.supplies;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.supplies.ImportWarehouse;

@Repository
public interface ImportWarehouseRepository extends JpaRepository<ImportWarehouse, Long> {
	ImportWarehouse findById(long id);

	@Query(value = " SELECT"
			+ " number"
			+ " FROM import_warehouse i"
			+ " WHERE"
			+ " i.import_date < :lessDate AND i.list_supplies_id = :listSuppliesId", nativeQuery = true)
	List<Long> findNumberByLessDate(@Param("lessDate") Long lessDate, @Param("listSuppliesId") Long listSuppliesId);

	@Query(value = " SELECT"
			+ " number"
			+ " FROM import_warehouse i"
			+ " WHERE"
			+ " i.import_date >= :fromDate AND i.import_date <= :endDate AND i.list_supplies_id = :listSuppliesId", nativeQuery = true)
	List<Long> findNumberByFromToEndDate(@Param("fromDate") Long fromDate, @Param("endDate") Long endDate,
			@Param("listSuppliesId") Long listSuppliesId);

	@Query(value = " SELECT"
			+ " *"
			+ " FROM import_warehouse i"
			+ " WHERE"
			+ " i.import_date >= :fromDate AND i.import_date <= :endDate", nativeQuery = true)
	List<ImportWarehouse> findImportWarehouseToDate(@Param("fromDate") Long fromDate,
			@Param("endDate") Long endDate);

	@Query(value = " SELECT" + " *" + " FROM import_warehouse i" + " WHERE"
			+ " i.import_date >= :fromDate AND i.import_date <= :endDate", nativeQuery = true)
	List<ImportWarehouse> findImportWarehouseToImportDate(@Param("fromDate") Long fromDate,
			@Param("endDate") Long endDate);
}
