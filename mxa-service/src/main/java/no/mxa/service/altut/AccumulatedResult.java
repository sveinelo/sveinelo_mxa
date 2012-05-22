/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2012 Patentstyret
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
package no.mxa.service.altut;

public class AccumulatedResult {
	private int succeed = 0;
	private int failed = 0;
	private int exception = 0;

	public int getSucceeded() {
		return succeed;
	}

	public int getFailed() {
		return failed;
	}

	public int getException() {
		return exception;
	}

	public void addSucceed() {
		succeed++;
	}

	public void addFailed() {
		failed++;
	}

	public void addException() {
		exception++;
	}

}
