package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import vn.tpsc.it4u.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Contract findByCustomId(String customId);

    Boolean existsByCustomId(String customId);

    Boolean existsByNumContract(String numContract);

    @Query(value = "SELECT"
    + " count(*)>0"
    + " FROM contract c"
    + " WHERE"
    + " c.client_name"
    + " LIKE %:clientName%", nativeQuery = true)
    Boolean existsByClientName(@Param("clientName") String clientName);

    @Query(value = "SELECT"
    + " *"
    + " FROM contract c"
    + " WHERE"
    + " c.client_name"
    + " LIKE :clientName%", nativeQuery = true)
    Contract findByClientName(@Param("clientName") String clientName);

    @Query(value = "SELECT"
    + " *"
    + " FROM contract c"
    + " WHERE"
    + " c.street"
    + " LIKE :street%", nativeQuery = true)
    Contract findByStreet(@Param("street") String street);

    @Query(value = "SELECT"
    + " *"
    + " FROM contract c"
    + " WHERE"
    + " c.client_name LIKE :clientName% OR"
    + " c.street LIKE :street% OR"
    + " c.num_contract LIKE :numContract% OR"
    + " c.custom_id LIKE :customId%", nativeQuery = true)
    Contract findByParamContract(@Param("customId") String customId, @Param("clientName") String clientName, @Param("street") String street, @Param("numContract") String numContract);

    @Query(value = "SELECT"
    + "  count(*)>0"
    + " FROM contract c"
    + " WHERE"
    + " c.client_name LIKE :clientName% OR"
    + " c.street LIKE :street% OR"
    + " c.num_contract LIKE :numContract% OR"
    + " c.custom_id LIKE :customId%", nativeQuery = true)
    Boolean existsByParamContract(@Param("customId") String customId, @Param("clientName") String clientName, @Param("street") String street, @Param("numContract") String numContract);
    
    Contract findByNumContract(String numContract);
}
