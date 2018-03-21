package lt.solver.gvdis.util;


import java.util.HashMap;
import java.util.Map;

import lt.solver.gvdis.model.TableFieldsJournal;

public class JournalHelper {
	
	
	public static TableFieldsJournal makeTableHeaderType6() {
		TableFieldsJournal retVal = new TableFieldsJournal();
		retVal.setField1("Reg. nr.");
		retVal.setField1DbColumnName("reg_nr");
		
		//rusiavimo tvarka
		Map map = new HashMap();
		map.put("reg_nr", "reg_nr {0}, pazymos_data {0}"); //rusiavimo tvarka
		map.put("pazymos_data", "pazymos_data {0}, reg_nr {0}"); //rusiavimo tvarka
		map.put("pilnas_pavadinimas", "pilnas_pavadinimas {0}, pazymos_data desc"); //rusiavimo tvarka
		map.put("adresas", "adresas {0}"); //rusiavimo tvarka
		retVal.setSortColumsOrder(map);
		
		retVal.setField2("Data");
		retVal.setField2DbColumnName("pazymos_data");
		retVal.setField3("Adresas");
		retVal.setField3DbColumnName("adresas");
		retVal.setField4(""); //Sito nera
		retVal.setField4DbColumnName("");
		retVal.setField5("Savivaldybë, seniûnija");
		retVal.setField5DbColumnName("pilnas_pavadinimas");
		retVal.setField6("Veiksmai");
		retVal.setField6DbColumnName("");
		
		return retVal;
	}
	
	public static TableFieldsJournal makeTableHeaderType3() {
		TableFieldsJournal retVal = new TableFieldsJournal();
		retVal.setField1("Reg. nr.");
		retVal.setField1DbColumnName("reg_nr");
		//rusiavimo tvarka
		Map map = new HashMap();
		map.put("reg_nr", "reg_nr {0}, sprendimo_data {0}"); //rusiavimo tvarka
		map.put("sprendimo_data", "sprendimo_data {0}, reg_nr {0}"); //rusiavimo tvarka
		map.put("pilnas_pavadinimas", "pilnas_pavadinimas {0}, sprendimo_data desc"); //rusiavimo tvarka
		map.put("tipas", "tipas {0}, sprendimo_data {0}"); //rusiavimo tvarka
		retVal.setSortColumsOrder(map);
		
		retVal.setField2("Data");
		retVal.setField2DbColumnName("sprendimo_data");
		retVal.setField3("Tipas");
		retVal.setField3DbColumnName("tipas");
		retVal.setField4(""); //Sito nera
		retVal.setField4DbColumnName("");
		retVal.setField5("Savivaldybë, seniûnija");
		retVal.setField5DbColumnName("pilnas_pavadinimas");
		retVal.setField6("Veiksmai");
		retVal.setField6DbColumnName("");
		
		return retVal;
	}

	public static TableFieldsJournal makeTableHeaderType2_5() {
		TableFieldsJournal retVal = new TableFieldsJournal();
		retVal.setField1("Reg. nr.");
		retVal.setField1DbColumnName("reg_nr");
		//rusiavimo tvarka
		Map map = new HashMap();
		map.put("reg_nr", "reg_nr {0}, pazymos_data {0}"); //rusiavimo tvarka
		map.put("pazymos_data", "pazymos_data {0}, reg_nr {0}"); //rusiavimo tvarka
		map.put("pilnas_pavadinimas", "pilnas_pavadinimas {0}, pazymos_data desc"); //rusiavimo tvarka
		map.put("Asmuo", "Asmuo {0}"); //rusiavimo tvarka
		retVal.setSortColumsOrder(map);
		
		retVal.setField2("Data");
		retVal.setField2DbColumnName("pazymos_data");
		retVal.setField3("Asmuo");
		retVal.setField3DbColumnName("Asmuo");
		retVal.setField4(""); //Sito nera
		retVal.setField4DbColumnName("");
		retVal.setField5("Savivaldybë, seniûnija");
		retVal.setField5DbColumnName("pilnas_pavadinimas");
		retVal.setField6("Veiksmai");
		retVal.setField6DbColumnName("");
		
		return retVal;
	}
	
	public static TableFieldsJournal makeTableHeaderType0_1_4() {
		TableFieldsJournal retVal = new TableFieldsJournal();
		
		retVal.setField1("Reg. nr.");
		retVal.setField1DbColumnName("reg_nr");
		//rusiavimo tvarka
		Map map = new HashMap();
		map.put("reg_nr", "reg_nr {0}, deklaravimo_data {0}"); //rusiavimo tvarka
		map.put("deklaravimo_data", "deklaravimo_data {0}, reg_nr {0}"); //rusiavimo tvarka
		map.put("pilnas_pavadinimas", "pilnas_pavadinimas {0}, deklaravimo_data desc"); //rusiavimo tvarka
		map.put("Asmuo", "Asmuo {0}"); //rusiavimo tvarka
		retVal.setSortColumsOrder(map);
		
		retVal.setField2("Data");
		retVal.setField2DbColumnName("deklaravimo_data");
		retVal.setField3("Asmuo");
		retVal.setField3DbColumnName("Asmuo");
		retVal.setField4("Bûsena");
		retVal.setField4DbColumnName("BusenaWeb");
		retVal.setField5("Savivaldybë, seniûnija");
		retVal.setField5DbColumnName("pilnas_pavadinimas");
		retVal.setField6("Veiksmai");
		retVal.setField6DbColumnName("");
		
		return retVal;
		
	}
}
