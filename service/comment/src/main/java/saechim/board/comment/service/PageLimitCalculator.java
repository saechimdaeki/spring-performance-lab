package saechim.board.comment.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageLimitCalculator {

	public static Long calculatePageLimit(final Long page, final Long pageSize, final Long movablePageCount) {
		return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1;
	}
}
