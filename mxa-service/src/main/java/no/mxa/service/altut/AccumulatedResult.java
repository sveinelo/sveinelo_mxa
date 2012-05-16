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
