package saechim.board.common.outboxmessagerelay;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
	List<Outbox> findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
		Long shardKey,
		LocalDateTime from,
		Pageable pageable
	);
}
