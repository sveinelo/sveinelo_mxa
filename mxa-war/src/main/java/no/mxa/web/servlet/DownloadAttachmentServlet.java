package no.mxa.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.mxa.dto.AttachmentDTO;
import no.mxa.service.AttachmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet which sends an attachment as a response.
 */
public class DownloadAttachmentServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadAttachmentServlet.class);
	static final String ATTACHMENT_SERVICE = "attachmentService";

	private static final long serialVersionUID = 1L;

	public static final String REFERENCE_PARAMETER_KEY = "attachmentId";
	private transient AttachmentService attachmentService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		loadAttachementService();
	}

	private void loadAttachementService() {
		final WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		attachmentService = webApplicationContext.getBean(ATTACHMENT_SERVICE, AttachmentService.class);
	}

	/**
	 * Sends an attachment, with the gives id, to the response.
	 * 
	 * @param id
	 *            attachment id
	 * @param response
	 *            response.
	 * @throws IOException
	 * @throws SQLException
	 */
	void sendFile(Long id, HttpServletResponse response) throws IOException, SQLException {
		AttachmentDTO attachmentDTO = attachmentService.searchById(id);
		response.setContentType(attachmentDTO.getMimeType());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Response header fileName:" + attachmentDTO.getFileName());
		}
		response.setHeader("Content-disposition", "attachment; filename=\"" + attachmentDTO.getFileName() + "\"");

		response.getOutputStream().write(attachmentService.getAttachmentAsByteArray(id));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		LOGGER.debug("doGet=" + REFERENCE_PARAMETER_KEY);
		String reference = req.getParameter(REFERENCE_PARAMETER_KEY);
		LOGGER.debug(REFERENCE_PARAMETER_KEY + " get success. Value:" + reference);
		try {
			Long id = Long.parseLong(reference);
			sendFile(id, res);
		} catch (Exception x) {
			LOGGER.debug("Exception caught in doGet().", x);
			String message = "Parameteren \"" + REFERENCE_PARAMETER_KEY + "\" er ikke en gyldig referanse.";
			res.sendError(HttpServletResponse.SC_NOT_FOUND, message);
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		loadAttachementService();
	}
}
