package saechim.board.common.outboxmessagerelay;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class AssignedShardTest {

	@Test
	void ofTest() {
		// given
		Long shardCount = 64L;
		List<String> appList = List.of("appId1", "appId2", "appId3");
		// when
		AssignedShard assignedShard1 = AssignedShard.of(appList.get(0), appList, shardCount);
		AssignedShard assignedShard2 = AssignedShard.of(appList.get(1), appList, shardCount);
		AssignedShard assignedShard3 = AssignedShard.of(appList.get(2), appList, shardCount);
		AssignedShard assignedShard4 = AssignedShard.of("invalid", appList, shardCount);
		// then

		List<Long> result = Stream.of(assignedShard1.getShards(), assignedShard2.getShards(),
				assignedShard3.getShards(),
				assignedShard4.getShards())
			.flatMap(List::stream)
			.toList();

		assertThat(result).hasSize(shardCount.intValue());
	}
}