package com.svlada.component.repository;

import com.svlada.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Partner findFirstByUserId(Long userId);

    Partner findOneByUserIdAndShareUserId(Long userId,Long share_user_id);

    List<Partner> findAllByShareUserId(Long shareUserId);
}
