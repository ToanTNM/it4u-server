package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.channel.ChannelName;

@Repository
public interface ChannelNameRepository extends JpaRepository<ChannelName, Long> {

	ChannelName findByName(String name);

	Boolean existsByName(String name);
}
