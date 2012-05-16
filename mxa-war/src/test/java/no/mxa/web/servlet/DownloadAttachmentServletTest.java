package no.mxa.web.servlet;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.mxa.dto.AttachmentDTO;
import no.mxa.service.AttachmentService;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class DownloadAttachmentServletTest {

	private DownloadAttachmentServlet servlet;
	private ServletConfig config;
	private ServletContext context;
	@Mock
	private WebApplicationContext webApplicationContext;
	@Mock
	private AttachmentService attachmentService;

	@Before
	public void setUp() throws Exception {
		Logger.getLogger(DownloadAttachmentServlet.class).setLevel(Level.TRACE);
		context = new MockServletContext(webApplicationContext);
		context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
		config = new MockServletConfig(context);
		servlet = new DownloadAttachmentServlet();
		when(webApplicationContext.getBean(DownloadAttachmentServlet.ATTACHMENT_SERVICE, AttachmentService.class)).thenReturn(
				attachmentService);
		servlet.init(config);

	}

	@Test
	public void testInit() throws ServletException {
		DownloadAttachmentServlet testServlet = new DownloadAttachmentServlet();
		when(webApplicationContext.getBean(DownloadAttachmentServlet.ATTACHMENT_SERVICE, AttachmentService.class)).thenReturn(
				attachmentService);
		testServlet.init(config);
	}

	@Test
	public void testDoGet() throws ServletException, IOException, SQLException {
		HttpServletResponse res = mock(HttpServletResponse.class);
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter(DownloadAttachmentServlet.REFERENCE_PARAMETER_KEY)).thenReturn("1234");
		AttachmentDTO dto = new AttachmentDTO();
		dto.setFileName("testName.xyz");
		dto.setMimeType("test/testType");
		when(attachmentService.searchById(1234L)).thenReturn(dto);
		when(res.getOutputStream()).thenReturn(new ServletOutputStream() {

			@Override
			public void write(int b) throws IOException {
				// Intentionally left blank.
			}
		});
		when(attachmentService.getAttachmentAsByteArray(1234L)).thenReturn(new byte[0]);

		servlet.doGet(req, res);
		verify(res, never()).sendError(HttpServletResponse.SC_NOT_FOUND,
				"Parameteren \"attachmentId\" er ikke en gyldig referanse.");
		verify(res).setHeader("Content-disposition", "attachment; filename=\"" + dto.getFileName() + "\"");
		verify(res).setContentType(dto.getMimeType());
	}

	@Test
	public void testDoGetNoLog() throws ServletException, IOException {
		Logger.getLogger(DownloadAttachmentServlet.class).setLevel(Level.ERROR);
		HttpServletResponse res = mock(HttpServletResponse.class);
		HttpServletRequest req = mock(HttpServletRequest.class);
		servlet.doGet(req, res);
		verify(res).sendError(HttpServletResponse.SC_NOT_FOUND, "Parameteren \"attachmentId\" er ikke en gyldig referanse.");
	}

	/**
	 * This test is supposed to test if the servlet survives serialization. At the moment it does not work since the mock
	 * objects used are not serializable themselfs.
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	@Test
	@Ignore
	public void testSerialization() throws ServletException, IOException, SQLException {
		byte[] serialize = SerializationUtils.serialize(servlet);
		Object deserializedObject = SerializationUtils.deserialize(serialize);
		assertTrue("Object should be a DownloadAttachementService.", deserializedObject instanceof DownloadAttachmentServlet);
		testDoGet();
	}

}
