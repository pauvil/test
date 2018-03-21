package lt.solver.gvdis.model;

import com.algoritmusistemos.gvdis.web.persistence.ReportResult;

public class Report02Result extends ReportResult {
	
	private long elektroninis;
	private long perGvdis;
	
	public long getElektroninis() {
		return elektroninis;
	}
	public void setElektroninis(long elektroninis) {
		this.elektroninis = elektroninis;
	}
	public long getPerGvdis() {
		return perGvdis;
	}
	public void setPerGvdis(long perGvdis) {
		this.perGvdis = perGvdis;
	}

}
