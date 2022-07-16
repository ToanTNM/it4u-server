package vn.tpsc.it4u.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.ChannelAttribute;
import vn.tpsc.it4u.models.ChannelValue;

/**
 * ChannelValueRepository
 */
@Repository
public interface ChannelAttributeRepository extends JpaRepository<ChannelAttribute, Long> {

	Boolean existsByChannelValue(ChannelValue channelValue);

	ChannelAttribute findById(long id);

	List<ChannelAttribute> findByStatus(String status);

	List<ChannelAttribute> findByChannelValue(ChannelValue channelValue);

	@Query(value = " SELECT"
			+ " id"
			+ " FROM channel_attribute c"
			+ " WHERE"
			+ " virtual_num=:virtualNum AND usernamepppoe=:usernamePPPoE", nativeQuery = true)
	Long getIdByCondition(@Param("virtualNum") String virtualNum, @Param("usernamePPPoE") String usernamePPPoE);

	@Query(value = " SELECT"
			+ " COUNT(*) > 0"
			+ " FROM channel_attribute c"
			+ " WHERE"
			+ " virtual_num=:virtualNum AND usernamepppoe=:usernamePPPoE", nativeQuery = true)
	Boolean existsIdByCondition(@Param("virtualNum") String virtualNum, @Param("usernamePPPoE") String usernamePPPoE);

	@Query(value = "SELECT"
			+ " *"
			+ " FROM channel_attribute c"
			+ " WHERE"
			+ " c.created_at >= :fromDate AND c.created_at <= :toDate", nativeQuery = true)
	List<ChannelAttribute> findChannelAttributes(@Param("fromDate") Timestamp fromDate,
			@Param("toDate") Timestamp toDate);

}
