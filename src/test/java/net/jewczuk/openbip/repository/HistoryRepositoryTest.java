package net.jewczuk.openbip.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.entity.HistoryEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HistoryRepositoryTest {
	
	@Autowired
	HistoryRepository historyRepository;
	
	@Test
	public void shouldReturnCorrectNumerOfLogs() {
		List<HistoryEntity> history = historyRepository.getAllLogEntriesByEditor(2L);
		
		assertThat(history.size()).isEqualTo(2);
		assertThat(history.get(0).getAction()).isEqualTo(TestConstants.LOG_ENTRY_2);
	}
	
	@Test
	public void shouldReturnEmptyListWhenNoLogsGiven() {
		List<HistoryEntity> history = historyRepository.getAllLogEntriesByEditor(1L);
		
		assertThat(history).isEmpty();
	}

}
