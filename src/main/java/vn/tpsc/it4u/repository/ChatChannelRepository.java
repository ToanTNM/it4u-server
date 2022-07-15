package vn.tpsc.it4u.repository;

import vn.tpsc.it4u.model.ChatChannel;
import vn.tpsc.it4u.model.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ChatChannelRepository extends CrudRepository<ChatChannel, String> {
  @Query(" FROM"
      + "    ChatChannel c"
      + "  WHERE"
      + "    c.userOne.id IN (:userOneId, :userTwoId) "
      + "  AND"
      + "    c.userTwo.id IN (:userOneId, :userTwoId)")
  public List<ChatChannel> findExistingChannel(
      @Param("userOneId") long userOneId, @Param("userTwoId") long userTwoId);
  
  @Query(" SELECT"
      + "    uuid"
      + "  FROM"
      + "    ChatChannel c"
      + "  WHERE"
      + "    c.userOne.id IN (:userIdOne, :userIdTwo)"
      + "  AND"
      + "    c.userTwo.id IN (:userIdOne, :userIdTwo)")
  public String getChannelUuid(
      @Param("userIdOne") long userIdOne, @Param("userIdTwo") long userIdTwo);

  @Query(" FROM"
      + "    ChatChannel c"
      + "  WHERE"
      + "    c.uuid IS :uuid")
  public ChatChannel getChannelDetails(@Param("uuid") String uuid);

  @Modifying
  @Query(value = " DELETE"
      + " FROM chat_channel"
      + " WHERE"
      + " user_id_one = :userIdOne", nativeQuery = true)
  int deleteByUserIdOne(@Param("userIdOne") Long userIdOne);

   @Query(value = " SELECT"
      + " count(*)>0"
      + " FROM chat_channel"
      + " WHERE"
      + " user_id_one=:userIdOne", nativeQuery = true)
  Boolean existsByUserIdOne(@Param("userIdOne") Long userIdOne);
}