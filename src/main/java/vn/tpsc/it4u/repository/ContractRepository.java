package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import vn.tpsc.it4u.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Contract findByCustomId(String customId);

    Boolean existsByCustomId(String customId);
}
