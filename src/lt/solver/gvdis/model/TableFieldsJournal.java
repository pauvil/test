package lt.solver.gvdis.model;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


public class TableFieldsJournal {

	private String field1;
	private String field1DbColumnName;
	private String field1Url = "";
	private String field1OrderPix = null;

	private String field2;
	private String field2DbColumnName;
	private String field2Url = "";
	private String field2OrderPix = null;
	
	private String field3;
	private String field3DbColumnName;
	private String field3Url = "";
	private String field3OrderPix = null;

	private String field4;
	private String field4DbColumnName;

	private String field5;
	private String field5DbColumnName;
	private String field5Url = "";
	private String field5OrderPix = null;

	private String field6;
	private String field6DbColumnName;
	
	private Map sortColumsOrder = new HashMap();

	public String getDbSortColumns(String fieldName, String sortOrder) {
		String retVal = "";

		String dbClmn = getDbColumnName(fieldName);
		
		if (sortColumsOrder.isEmpty() || !sortColumsOrder.containsKey(dbClmn)) {
			retVal = dbClmn + " " + sortOrder;
		} else {
			String clmns = (String) sortColumsOrder.get(dbClmn);
			//priskiriama visiems stulpeliams tas pats rusiavimas
			retVal = MessageFormat.format(clmns, new Object[]{sortOrder}); 
		}
		
		return retVal;
	}
	
	private String getDbColumnName(String fieldName) {
		String retVal = "";
		if (fieldName.equalsIgnoreCase("field1")) {
			retVal = field1DbColumnName;
		}
		if (fieldName.equalsIgnoreCase("field2")) {
			retVal = field2DbColumnName;
		}
		if (fieldName.equalsIgnoreCase("field3")) {
			retVal = field3DbColumnName;
		}
		if (fieldName.equalsIgnoreCase("field4")) {
			retVal = field4DbColumnName;
		}
		if (fieldName.equalsIgnoreCase("field5")) {
			retVal = field5DbColumnName;
		}
		if (fieldName.equalsIgnoreCase("field6")) {
			retVal = field6DbColumnName;
		}
		return retVal;
	}
	
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}
	public String getField3() {
		return field3;
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}
	public String getField4() {
		return field4;
	}
	public void setField4(String field4) {
		this.field4 = field4;
	}
	public String getField5() {
		return field5;
	}
	public void setField5(String field5) {
		this.field5 = field5;
	}
	public String getField6() {
		return field6;
	}
	public void setField6(String field6) {
		this.field6 = field6;
	}
	public String getField1Url() {
		return field1Url;
	}
	public void setField1Url(String field1Url) {
		this.field1Url = field1Url;
	}
	public String getField2Url() {
		return field2Url;
	}
	public void setField2Url(String field2Url) {
		this.field2Url = field2Url;
	}
	public String getField3Url() {
		return field3Url;
	}
	public void setField3Url(String field3Url) {
		this.field3Url = field3Url;
	}
	public String getField5Url() {
		return field5Url;
	}
	public void setField5Url(String field5Url) {
		this.field5Url = field5Url;
	}
	public String getField1OrderPix() {
		return field1OrderPix;
	}
	public void setField1OrderPix(String field1OrderPix) {
		this.field1OrderPix = field1OrderPix;
	}
	public String getField2OrderPix() {
		return field2OrderPix;
	}
	public void setField2OrderPix(String field2OrderPix) {
		this.field2OrderPix = field2OrderPix;
	}
	public String getField3OrderPix() {
		return field3OrderPix;
	}
	public void setField3OrderPix(String field3OrderPix) {
		this.field3OrderPix = field3OrderPix;
	}
	public String getField5OrderPix() {
		return field5OrderPix;
	}
	public void setField5OrderPix(String field5OrderPix) {
		this.field5OrderPix = field5OrderPix;
	}
	public String getField1DbColumnName() {
		return field1DbColumnName;
	}
	public void setField1DbColumnName(String field1DbColumnName) {
		this.field1DbColumnName = field1DbColumnName;
	}
	public String getField2DbColumnName() {
		return field2DbColumnName;
	}
	public void setField2DbColumnName(String field2DbColumnName) {
		this.field2DbColumnName = field2DbColumnName;
	}
	public String getField3DbColumnName() {
		return field3DbColumnName;
	}
	public void setField3DbColumnName(String field3DbColumnName) {
		this.field3DbColumnName = field3DbColumnName;
	}
	public String getField4DbColumnName() {
		return field4DbColumnName;
	}
	public void setField4DbColumnName(String field4DbColumnName) {
		this.field4DbColumnName = field4DbColumnName;
	}
	public String getField5DbColumnName() {
		return field5DbColumnName;
	}
	public void setField5DbColumnName(String field5DbColumnName) {
		this.field5DbColumnName = field5DbColumnName;
	}
	public String getField6DbColumnName() {
		return field6DbColumnName;
	}
	public void setField6DbColumnName(String field6DbColumnName) {
		this.field6DbColumnName = field6DbColumnName;
	}


	public Map getSortColumsOrder() {
		return sortColumsOrder;
	}

	public void setSortColumsOrder(Map sortColumsOrder) {
		this.sortColumsOrder = sortColumsOrder;
	}


	
}
