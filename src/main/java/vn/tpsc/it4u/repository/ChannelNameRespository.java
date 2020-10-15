package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.ChannelName;

@Repository
public interface ChannelNameRespository extends JpaRepository<ChannelName, Long>{
    ChannelName findByName(String name);
}
