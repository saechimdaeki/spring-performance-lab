package saechim.board.common.outboxmessagerelay;

import java.util.List;
import java.util.stream.LongStream;

import lombok.Getter;

@Getter
public class AssignedShard {
	private List<Long> shards;

	public static AssignedShard of(String appId, List<String> appIds, long shardCount) {
		AssignedShard assignedShard = new AssignedShard();
		assignedShard.shards = assign(appId, appIds, shardCount);
		return assignedShard;
	}

	private static List<Long> assign(String appId, List<String> appIds, long shardCount) {
		int appIndex = findAppIndex(appId, appIds);
		if (appIndex == -1) {
			return List.of();
		}
		long start = appIndex * shardCount / appIds.size();
		long end = (appIndex + 1) * shardCount / appIds.size() - 1;

		return LongStream.rangeClosed(start, end).boxed().toList();
	}

	private static int findAppIndex(String appId, List<String> appIds) {
		for (int i = 0; i < appIds.size(); i++) {
			if (appId.equals(appIds.get(i))) {
				return i;
			}
		}
		return -1;
	}
}
