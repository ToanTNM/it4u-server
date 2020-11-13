package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.ChannelDetail;

@Repository
public interface ChannelDetailRepository extends JpaRepository<ChannelDetail, Long> {
    
}
