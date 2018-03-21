package lt.solver.gvdis.model;

public class PagingInitReturnObj {

	private boolean error = false;
	private String errorText = "";
	private long irasuKiekis = 0;
	private long puslapiuKiekis = 0;
	private long puslapiuKiekisRodyti = 0;
	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	public long getIrasuKiekis() {
		return irasuKiekis;
	}
	public void setIrasuKiekis(long irasuKiekis) {
		this.irasuKiekis = irasuKiekis;
	}
	public long getPuslapiuKiekis() {
		return puslapiuKiekis;
	}
	public void setPuslapiuKiekis(long puslapiuKiekis) {
		this.puslapiuKiekis = puslapiuKiekis;
	}
	public long getPuslapiuKiekisRodyti() {
		return puslapiuKiekisRodyti;
	}
	public void setPuslapiuKiekisRodyti(long puslapiuKiekisRodyti) {
		this.puslapiuKiekisRodyti = puslapiuKiekisRodyti;
	}
	
}
