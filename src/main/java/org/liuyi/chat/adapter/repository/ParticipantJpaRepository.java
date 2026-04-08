package org.liuyi.chat.adapter.repository;

import org.liuyi.chat.adapter.persistence.ParticipantDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantJpaRepository extends JpaRepository<ParticipantDO, Long> {

}
