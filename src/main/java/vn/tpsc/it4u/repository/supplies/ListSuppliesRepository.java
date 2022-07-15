package vn.tpsc.it4u.repository.supplies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.supplies.ListSupplies;

@Repository
public interface ListSuppliesRepository extends JpaRepository<ListSupplies, Long> {
    ListSupplies findById(long id);

    ListSupplies findByItemCode(String itemCode);

    Boolean existsByItemCode(String itemCode);
}
