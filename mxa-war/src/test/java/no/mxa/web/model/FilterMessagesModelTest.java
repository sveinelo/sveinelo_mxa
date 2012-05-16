package no.mxa.web.model;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import no.mxa.dto.MessageDTO;
import no.mxa.service.MessageService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FilterMessagesModelTest {
	@Mock
	private MessageService messageService;
	private FilterMessagesModel filterMessageModel;

	@Before
	public void setUp() {
		filterMessageModel = new FilterMessagesModel(messageService);
	}

	@Test
	public void testFilterMessagesModelSearchByCriteria() {
		String messageReference = "VM-Partial";

		MessageDTO criteria = new MessageDTO();
		criteria.setMessageStatus(10);
		filterMessageModel.setCriteria(criteria);
		filterMessageModel.setMessageReference(messageReference);

		String outcome = filterMessageModel.search();

		assertThat(outcome, is("success"));
		verify(messageService).searchFromGUI(criteria, null, null, null, messageReference);
	}
}
