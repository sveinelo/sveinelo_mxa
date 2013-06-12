/*
 * #%L
 * Altinn Webservice
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package no.mxa.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;

/**
 * An OutputStream that flushes out to a Category.
 * <p>
 * <p/>
 * Note that no data is written out to the Category until the stream is flushed or closed.
 * <p>
 * <p/>
 * Example:
 * 
 * <pre>
 * // make sure everything sent to System.err is logged
 * System.setErr(new PrintStream(new
 * LoggingOutputStream(Logger.getRootCategory(),
 * Level.WARN), true));
 * <p/>
 * // make sure everything sent to System.out is also logged
 * System.setOut(new PrintStream(new
 * LoggingOutputStream(Logger.getRootCategory(),
 * Level.INFO), true));
 * </pre>
 * 
 * @author <a href="[EMAIL PROTECTED]">Jim Moore</a>
 * @see org.apache.log4j.Category
 */

//
public class LoggingOutputStream extends OutputStream {
	public enum Level {
		ERROR {
			@Override
			void log(Logger logger, String msg) {
				logger.error(msg);
			}
		},
		WARN {
			@Override
			void log(Logger logger, String msg) {
				logger.warn(msg);
			}
		},
		INFO {
			@Override
			void log(Logger logger, String msg) {
				logger.info(msg);
			}
		},
		DEBUG {
			@Override
			void log(Logger logger, String msg) {
				logger.debug(msg);
			}
		},
		TRACE {
			@Override
			void log(Logger logger, String msg) {
				logger.trace(msg);
			}
		};
		abstract void log(Logger logger, String message);
	}

	/**
	 * Used to maintain the contract of [EMAIL PROTECTED] #close()}.
	 */
	private boolean hasBeenClosed = false;

	/**
	 * The internal buffer where data is stored.
	 */
	private byte[] buf;

	/**
	 * The number of valid bytes in the buffer. This value is always in the range <tt>0</tt> through <tt>buf.length</tt>;
	 * elements <tt>buf[0]</tt> through <tt>buf[count-1]</tt> contain valid byte data.
	 */
	private int count;

	/**
	 * Remembers the size of the buffer for speed.
	 */
	private int bufLength;

	/**
	 * The default number of bytes in the buffer. =2048
	 */
	public static final int DEFAULT_BUFFER_LENGTH = 2048;

	/**
	 * The category to write to.
	 */
	private final Logger logger;

	/**
	 * The priority to use when writing to the Category.
	 */
	private final Level level;

	/**
	 * Creates the LoggingOutputStream to flush to the given Category.
	 * 
	 * @param log
	 *            the Logger to write to
	 * @param level
	 *            the Level to use when writing to the Logger
	 * @throws IllegalArgumentException
	 *             if cat == null or priority == null
	 */
	public LoggingOutputStream(Logger log, Level level) throws IllegalArgumentException {
		if (log == null) {
			throw new IllegalArgumentException("cat == null");
		}
		if (level == null) {
			throw new IllegalArgumentException("priority == null");
		}

		this.level = level;
		logger = log;
		bufLength = DEFAULT_BUFFER_LENGTH;
		buf = new byte[DEFAULT_BUFFER_LENGTH];
		count = 0;
	}

	/**
	 * Closes this output stream and releases any system resources associated with this stream. The general contract of
	 * <code>close</code> is that it closes the output stream. A closed stream cannot perform output operations and cannot be
	 * reopened.
	 */
	@Override
	public void close() {
		flush();
		hasBeenClosed = true;
	}

	/**
	 * Writes the specified byte to this output stream. The general contract for <code>write</code> is that one byte is written
	 * to the output stream. The byte to be written is the eight low-order bits of the argument <code>b</code>. The 24
	 * high-order bits of <code>b</code> are ignored.
	 * 
	 * @param b
	 *            the <code>byte</code> to write
	 * @throws java.io.IOException
	 *             if an I/O error occurs. In particular, an <code>IOException</code> may be thrown if the output stream has
	 *             been closed.
	 */
	@Override
	public void write(final int b) throws IOException {
		if (hasBeenClosed) {
			throw new IOException("The stream has been closed.");
		}

		// would this be writing past the buffer?

		if (count == bufLength) {
			// grow the buffer
			final int newBufLength = bufLength + DEFAULT_BUFFER_LENGTH;
			final byte[] newBuf = new byte[newBufLength];

			System.arraycopy(buf, 0, newBuf, 0, bufLength);
			buf = newBuf;

			bufLength = newBufLength;
		}

		buf[count] = (byte) b;

		count++;
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be written out. The general contract of
	 * <code>flush</code> is that calling it is an indication that, if any bytes previously written have been buffered by the
	 * implementation of the output stream, such bytes should immediately be written to their intended destination.
	 */
	@Override
	public void flush() {

		if (count == 0) {
			return;
		}

		// don't print out blank lines; flushing from PrintStream puts

		// For linux system

		if (count == 1 && ((char) buf[0]) == '\n') {
			reset();
			return;
		}

		// For mac system

		if (count == 1 && ((char) buf[0]) == '\r') {
			reset();
			return;
		}

		// On windows system

		if (count == 2 && (char) buf[0] == '\r' && (char) buf[1] == '\n') {
			reset();
			return;
		}

		final byte[] theBytes = new byte[count];
		System.arraycopy(buf, 0, theBytes, 0, count);
		String message;
		try {
			message = new String(theBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Cannot encode the String", e);
			message = "";
		}
		level.log(logger, message);
		reset();

	}

	private void reset() {
		// not resetting the buffer -- assuming that if it grew then it will
		// likely grow similarly again
		count = 0;
	}

}
