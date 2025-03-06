package saechim.board.hotarticle.service;

import static org.mockito.BDDMockito.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import saechim.board.common.event.Event;
import saechim.board.hotarticle.repository.ArticleCreatedTimeRepository;
import saechim.board.hotarticle.repository.HotArticleListRepository;
import saechim.board.hotarticle.service.eventhandler.EventHandler;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreUpdaterTest {

	@InjectMocks
	HotArticleScoreUpdater hotArticleScoreUpdater;

	@Mock
	HotArticleListRepository hotArticleListRepository;
	@Mock
	HotArticleScoreCalculator hotArticleScoreCalculator;
	@Mock
	ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Test
	void updateIfArticleNotCreatedTodayTest() {
		// given
		Long articleId = 1L;
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(eventHandler.findArticleId(event)).willReturn(articleId);

		LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
		given(articleCreatedTimeRepository.read(articleId)).willReturn(createdTime);

		// when
		hotArticleScoreUpdater.update(event, eventHandler);

		// then
		verify(eventHandler, never()).handle(event);
		verify(hotArticleListRepository, never())
			.add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));
	}
	
}