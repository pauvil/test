package com.algoritmusistemos.gvdis.web.delegators;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lt.solver.gvdis.model.Report02Result;
import lt.solver.gvdis.model.Report08Result;

import oracle.jdbc.OracleTypes;

import com.algoritmusistemos.gvdis.web.Constants;
import com.algoritmusistemos.gvdis.web.exceptions.DatabaseException;
import com.algoritmusistemos.gvdis.web.objects.Report07Result;
import com.algoritmusistemos.gvdis.web.objects.SkaiciausAtaskaitosEilute;
import com.algoritmusistemos.gvdis.web.objects.SkaiciausAtaskaitosEiluteGrupuota;
import com.algoritmusistemos.gvdis.web.persistence.Asmuo;
import com.algoritmusistemos.gvdis.web.persistence.ReportResult;
import com.algoritmusistemos.gvdis.web.utils.HibernateUtils;

public class AtaskaitosDelegator
{
	private static AtaskaitosDelegator instance;
	
	public static final int TYPE_0801 = 1;
	public static final int TYPE_0802 = 2;
	public static final int TYPE_0803 = 3;
	public static final int TYPE_0805 = 5;
	public static final int TYPE_0807 = 7;
	public static final int TYPE_0808 = 8;
	public static final int TYPE_0810 = 10;
	public static final int TYPE_0811 = 11;
	public static final int TYPE_0812 = 12;
	
	public static AtaskaitosDelegator getInstance() 
	{
		if (instance == null){
			instance =  new AtaskaitosDelegator();
		}
		return instance;
	}	

	/**
	 * Gyvenamàjà vietà deklaravusiø gyventojø skaièiaus ataskaita 
	 */
	public List report0801(HttpServletRequest req, Date dataNuo, Date dataIki, Long savivaldybe, Long seniunija, 
			Long deklTipas, Date gimDataNuo, Date gimDataIki, Long grupavimas)
		throws DatabaseException
	{
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0801(?, ?, ?, ?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (dataNuo != null) ? sdf.format(dataNuo) : null);
            stmt.setString(3, (dataIki != null) ? sdf.format(dataIki) : null);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(4, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(4, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            stmt.setLong(5, (deklTipas != null) ? deklTipas.longValue() : 0);
            stmt.setString(6, (gimDataNuo != null) ? sdf.format(gimDataNuo): null);
            stmt.setString(7, (gimDataIki != null) ? sdf.format(gimDataIki) : null);
            stmt.setLong(8, grupavimas != null ? grupavimas.longValue() : 0);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
                ReportResult result = new ReportResult();
                result.setName(rs.getString("NAME"));
                result.setCount(rs.getLong("COUNT"));
                if(null != rs.getString("NAME"))l.add(result);
                //System.out.println(">"+rs.getString("NAME")+"<");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}

	/**
	 * Iðvykimà ið LR deklaravusiø gyventojø skaièiaus ataskaita 
	 */
	public List report0802(HttpServletRequest req, Date dataNuo, Date dataIki,  Long savivaldybe, Long seniunija,
			Date gimDataNuo, Date gimDataIki, Long grupavimas)
		throws DatabaseException
	{
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0802(?, ?, ?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (dataNuo != null) ? sdf.format(dataNuo) : null);
            stmt.setString(3, (dataIki != null) ? sdf.format(dataIki) : null);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(4, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(4, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            stmt.setString(5, (gimDataNuo != null) ? sdf.format(gimDataNuo): null);
            stmt.setString(6, (gimDataIki != null) ? sdf.format(gimDataIki) : null);
            stmt.setLong(7, grupavimas != null ? grupavimas.longValue() : 0);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
                Report02Result result = new Report02Result();
                result.setName(rs.getString("NAME"));
                result.setCount(rs.getLong("COUNT"));
                result.setElektroninis(rs.getLong("ELEKTRONINIS"));
                result.setPerGvdis(rs.getLong("PER_GVDIS"));
                
                l.add(result);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}

	/**
	 * Nuolat gyvenanèiø uþsienyje gyventojø skaièiaus ataskaita 
	 */
	public List report0803(HttpServletRequest req, Date dataNuo, Date dataIki, Long savivaldybe, Long seniunija,
			Date gimDataNuo, Date gimDataIki, Long grupavimas)
		throws DatabaseException
	{
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0803(?, ?, ?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (dataNuo != null) ? sdf.format(dataNuo) : null);
            stmt.setString(3, (dataIki != null) ? sdf.format(dataIki) : null);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(4, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(4, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            //naujoje ataskaitoje parametras GIM_DATA_NUO naudojamas kaip GALIOJIMO_DATA
            stmt.setString(5, (gimDataNuo != null) ? sdf.format(gimDataNuo): null);
            stmt.setString(6, (gimDataIki != null) ? sdf.format(gimDataIki) : null);
            stmt.setLong(7, grupavimas != null ? grupavimas.longValue() : 0);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
                ReportResult result = new ReportResult();
                result.setName(rs.getString("NAME"));
                result.setCount(rs.getLong("COUNT"));
                l.add(result);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}

	/**
	 * Iðduotø paþymø apie asmens gyvenamàjà vietà skaièiaus ataskaita 
	 */
	public List report0805(HttpServletRequest req, Date dataNuo, Date dataIki,  
			Long savivaldybe, Long seniunija, Long grupavimas)
		throws DatabaseException
	{
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0805(?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (dataNuo != null) ? sdf.format(dataNuo) : null);
            stmt.setString(3, (dataIki != null) ? sdf.format(dataIki) : null);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(4, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(4, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            stmt.setLong(5, grupavimas != null ? grupavimas.longValue() : 0);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
                ReportResult result = new ReportResult();
                result.setName(rs.getString("NAME"));
                result.setCount(rs.getLong("COUNT"));
                l.add(result);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}

	/**
	 * Priimtø sprendimø dël deklaravimo duomenø taisymo, keitimo ir naikinimo ataskaita 
	 * @param grupavimas 
	 */
	public List report0807(HttpServletRequest req, Date dataNuo, Date dataIki,  
			Long savivaldybe, Long seniunija, Long grupavimas)
		throws DatabaseException
	{
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0807b(?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (dataNuo != null) ? sdf.format(dataNuo) : null);
            stmt.setString(3, (dataIki != null) ? sdf.format(dataIki) : null);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(4, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(4, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            stmt.setLong(5, grupavimas != null ? grupavimas.longValue() : 0);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            int suma = 0;
            if (grupavimas == null || grupavimas.longValue() == 0) {
				while (rs.next()) {
					ReportResult result = new ReportResult();
					result.setName(rs.getString("NAME"));
					result.setCount(rs.getLong("COUNT"));
					suma += rs.getLong("COUNT");
					l.add(result);
				}
				ReportResult viso = new ReportResult();
	            viso.setName("Ið viso");
	            viso.setCount(suma);
	            l.add(viso);
			}
            else{
            	int suma0 = 0, suma1 = 0, suma2 = 0;
            	while (rs.next()) {
            		String name = rs.getString("NAME");
            		boolean rastas = false;
            		Report07Result result = null;
            	 	for (Iterator iter = l.iterator(); iter.hasNext();) {
						Report07Result elementTemp = (Report07Result) iter.next();
						if(name.equals(elementTemp.getName())){
							rastas = true;
							result = elementTemp;
							break;
						}						
					}
            	 	if(!rastas){
            	 		result = new Report07Result();
            	 		result.setName(name);
            	 	}
					switch(rs.getInt("TIPAS")){
					case 0:
						result.setTaisymo(rs.getLong("COUNT"));
						suma0 += rs.getLong("COUNT");
						break;
					case 1:
						result.setKeitimo(rs.getLong("COUNT"));
						suma1 += rs.getLong("COUNT");
						break;
					case 2:
						result.setNaikinimo(rs.getLong("COUNT"));
						suma2 += rs.getLong("COUNT");
					}
					result.setViso(result.getViso() + rs.getLong("COUNT"));
					//result.setName(rs.getString("NAME"));
					//result.setCount(rs.getLong("COUNT"));					
					if(!rastas){
						l.add(result);
					}
				}
            	Report07Result viso = new Report07Result();
	            viso.setName("Ið viso");
	            viso.setTaisymo(suma0);
	            viso.setKeitimo(suma1);
	            viso.setNaikinimo(suma2);
	            viso.setViso(suma0+suma1+suma2);
	            Collections.sort(l, new Comparator(){
					public int compare(Object arg0, Object arg1) {
						return ((Report07Result) arg0).getName().compareTo(((Report07Result) arg1).getName());
					}});
	            l.add(viso);
            }
            rs.close();
            stmt.close();
            
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}

	/**
	 * Átrauktø á GVNA apskaità gyventojø skaièiaus ataskaita 
	 */
	public List report0808(HttpServletRequest req, Date dataNuo, Date dataIki, Long savivaldybe, Long seniunija, 
			Date gimDataNuo, Date gimDataIki, Long grupavimas)
		throws DatabaseException
	{
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0808(?, ?, ?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (dataNuo != null) ? sdf.format(dataNuo) : null);
            stmt.setString(3, (dataIki != null) ? sdf.format(dataIki) : null);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(4, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(4, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            stmt.setString(5, (gimDataNuo != null) ? sdf.format(gimDataNuo): null);
            stmt.setString(6, (gimDataIki != null) ? sdf.format(gimDataIki) : null);
            stmt.setLong(7, grupavimas != null ? grupavimas.longValue() : 0);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            
            while(rs.next()){
            	Report08Result result = new Report08Result();
                result.setName(rs.getString("NAME"));
                result.setCount(rs.getLong("COUNT")); 
                result.setGalioja(rs.getLong("GALIOJA"));
                l.add(result);
            }
            rs.close();           
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}
	
	/**
	 * Iðduotø paþymø apie átraukimà á GVNA apskaità skaièiaus ataskaita 
	 */
	public List report0810(HttpServletRequest req, Date dataNuo, Date dataIki,  
			Long savivaldybe, Long seniunija, Long grupavimas)
		throws DatabaseException
	{
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0810(?, ?, ?, ?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, (dataNuo != null) ? sdf.format(dataNuo) : null);
            stmt.setString(3, (dataIki != null) ? sdf.format(dataIki) : null);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(4, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(4, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(4, OracleTypes.NUMBER);
            }
            stmt.setLong(5, grupavimas != null ? grupavimas.longValue() : 0);
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){
                ReportResult result = new ReportResult();
                result.setName(rs.getString("NAME"));
                result.setCount(rs.getLong("COUNT"));
                l.add(result);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}
	
	/**
	 * Gyventojø, kuriø deklaravimo duomenys turi bûti naikinami, ataskaita  
	 */
	public List report0811(HttpServletRequest req, Long savivaldybe, Long seniunija)
		throws DatabaseException
	{
        List l = new ArrayList();
        try {
            String qry = "{? = call gvdis_rep.gvdis_0811(?) }";
            Connection conn = HibernateUtils.currentSession(req).connection();
            CallableStatement stmt = conn.prepareCall(qry);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            if (seniunija != null && seniunija.longValue() > 0){
            	stmt.setLong(2, seniunija.longValue());
            }
            else if (savivaldybe != null && savivaldybe.longValue() > 0){
            	stmt.setLong(2, savivaldybe.longValue());
            }
            else {
            	stmt.setNull(2, OracleTypes.NUMBER);
            }
            stmt.execute();
            ResultSet rs = (ResultSet)stmt.getObject(1);
            while(rs.next()){            
            	Asmuo asmuo = new Asmuo();
            	asmuo.setAsmNr(rs.getLong("asm_nr"));
            	asmuo.setAsmKodas(new Long(rs.getLong("asm_kodas")));
            	asmuo.setVardas(rs.getString("asm_vardas"));
            	asmuo.setPavarde(rs.getString("asm_pavarde"));
            	asmuo.setAsmGimData(rs.getTimestamp("asm_gim_data"));
            	
                l.add(asmuo);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}

	public List report0812(HttpServletRequest request, Date dataNuo, Long savivaldybe, Long seniunija, String tip, Long grupavimas, int userStatus) 
		throws DatabaseException
	{
		List l = new ArrayList();
        try {
        	if (/*(savivaldybe == null || savivaldybe.longValue() == 0) || */(seniunija == null || seniunija.longValue() == 0) || !tip.equals("K")) {
	        	SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
	            String qry = "{? = call gvdis_rep.Get_Gyv_sk_0812(?,?,?,?,?) }";
	            Connection conn = HibernateUtils.currentSession(request).connection();
	            CallableStatement stmt = conn.prepareCall(qry);
	            stmt.registerOutParameter(1, OracleTypes.CURSOR);
	            stmt.setString(2, sdf.format(dataNuo));
	            if (seniunija != null && seniunija.longValue() > 0){
	            	stmt.setLong(4, seniunija.longValue());
	            }
	            else {
	            	stmt.setNull(4, OracleTypes.NUMBER);
	            }
	            if (savivaldybe != null && savivaldybe.longValue() > 0){
	            	stmt.setLong(3, savivaldybe.longValue());
	            }
	            else {
	            	stmt.setNull(3, OracleTypes.NUMBER);
	            }
	            if (grupavimas != null) {
	            	stmt.setLong(5, grupavimas.longValue());
	            }
	            else {
	            	stmt.setNull(5, OracleTypes.NUMBER);
	            }
	            stmt.setString(6, tip);
	            stmt.execute();
	            ResultSet rs = (ResultSet)stmt.getObject(1);
	            
	            boolean notgroupedGVNA = true;
	            if ((savivaldybe != null && savivaldybe.longValue()!=0) && (seniunija == null || seniunija.longValue() == 0) && grupavimas!=null && grupavimas.longValue()!=0 && "R".equals(tip)) {
	            	notgroupedGVNA = false;
	            }
	            
	            if (((savivaldybe != null && savivaldybe.longValue()!=0) || (seniunija != null && seniunija.longValue()!=0)) && notgroupedGVNA)
	            {
	            	int total = 0;
	            	int totalV = 0;
	            	int totalM = 0;
	            	while (rs.next()) {
	            		SkaiciausAtaskaitosEilute eilute = new SkaiciausAtaskaitosEilute();
	            		int viso = rs.getInt("Moteru") + rs.getInt("Vyru");
	            		total+=viso;
	            		totalV+=rs.getInt("Vyru");
	            		totalM+=rs.getInt("Moteru");
	            		if (rs.getString("grupe").equals("ið viso")) {
	            			eilute.setGrupe(rs.getString("grupe"));
	            			eilute.setMoteru(totalM);
	            			eilute.setVyru(totalV);
	            			eilute.setViso(total);
	            		}
	            		else{
		            		eilute.setGrupe(rs.getString("grupe"));
		            		eilute.setMoteru(rs.getInt("Moteru"));
		            		eilute.setVyru(rs.getInt("Vyru"));            		
		            		eilute.setViso(viso);
	            		}
	            		l.add(eilute);
	            	}
	            }
	            else {
		            while(rs.next()){       
		            	SkaiciausAtaskaitosEiluteGrupuota eilute = new SkaiciausAtaskaitosEiluteGrupuota();
		            	eilute.setGrupe(rs.getString("savivaldybe"));
		            	eilute.setM0_7(rs.getInt("0 - 7"));
		            	eilute.setM7_16(rs.getInt("7 - 16"));
		            	eilute.setM16_18(rs.getInt("16 - 18"));
		            	eilute.setM18_25(rs.getInt("18 - 25"));
		            	eilute.setM25_45(rs.getInt("25 - 45"));
		            	eilute.setM45_65(rs.getInt("45 - 65"));
		            	eilute.setM65_85(rs.getInt("65 - 85"));
		            	eilute.setM85_0(rs.getInt("85 - *"));
		            	eilute.setViso(rs.getInt("0 - 7")+rs.getInt("7 - 16")+
		            				   rs.getInt("16 - 18")+rs.getInt("18 - 25")+
		            				   rs.getInt("25 - 45")+rs.getInt("45 - 65")+
		            				   rs.getInt("65 - 85")+rs.getInt("85 - *"));
		        	
		                l.add(eilute);
		            }
	            }
	            rs.close();
	            stmt.close();
        	}
        }
        catch (SQLException sqlEx) {
            throw new DatabaseException(sqlEx);
        }
		return l.isEmpty() ? null : l;
	}
	/*private List report0812(HttpServletRequest request, Date dataNuo, Long savivaldybe, Long seniunija, String tip)
	{	
		if((savivaldybe == null || savivaldybe.longValue()==0)&& (seniunija == null || seniunija.longValue()==0)) return null;
		if(dataNuo == null) return null;
		long ist = (seniunija != null && seniunija.longValue() != 0)? seniunija.longValue():savivaldybe.longValue(); 
		if("K".equals(tip))
			ist = savivaldybe.longValue();
		
		
		List lll = HibernateUtils.currentSession(request).createSQLQuery("select a.gvt_tipas, a.lytis, a.amzius,a.skaicius from gvdis_ataskaita a " +
		"where trunc(a.laikotarpis)=:laik and a.gvist_id=:ist and a.gvt_tipas = :tip").
		addScalar("GVT_TIPAS", Hibernate.STRING)				
		.addScalar("LYTIS", Hibernate.STRING)
		.addScalar("AMZIUS", Hibernate.STRING)
		.addScalar("SKAICIUS", Hibernate.INTEGER)
		.setDate("laik", dataNuo).setLong("ist", ist).setString("tip", tip).list();
		System.out.println("SIZE:"+lll.size());
		Iterator iter = lll.iterator();
		
		System.out.println("data:"+dataNuo);
		System.out.println("ist:"+ist);
		System.out.println("tip:"+tip);
		
		ArrayList atas = new ArrayList();
		while ( iter.hasNext()) {
			Object[] obj = (Object[]) iter.next();			
			String lytis = (String) obj[1];
			String amzius = (String) obj[2];
			Integer skaicius = (Integer) obj[3];
			boolean rastas = false;
			for (Iterator iterator = atas.iterator(); iterator.hasNext();) {
				SkaiciausAtaskaitosEilute element = (SkaiciausAtaskaitosEilute) iterator.next();
				if(element.getGrupe().equals(amzius)){
					if(lytis.equals("V"))
						element.setVyru(element.getVyru() + skaicius.intValue());
					else element.setMoteru(element.getMoteru() + skaicius.intValue());					
				}				
			}
			if(!rastas){
				SkaiciausAtaskaitosEilute element = new SkaiciausAtaskaitosEilute();
				element.setGrupe(amzius); 
				if(lytis.equals("V"))
					element.setVyru(skaicius.intValue());
				else element.setMoteru(skaicius.intValue());
				atas.add(element);
			}
		}
		String[][] grupes = {{METAI_0_7,"0 - 7"},{METAI_7_16,"7 - 16"},{METAI_16_18,"16 - 17"},
				{METAI_18_25,"18 - 25"}, {METAI_25_45,"25 - 45"}, {METAI_45_65,"45 - 65"},
				{METAI_65_85,"65 - 85"},{METAI_85_0,"85 - *"},{METAI_IS_VISO,""}};
		ArrayList rezult = new ArrayList();
		for (int i = 0; i < grupes.length; i++) {
			boolean rastas = false;
			for (Iterator iterator = atas.iterator(); iterator.hasNext();) {
				SkaiciausAtaskaitosEilute element = (SkaiciausAtaskaitosEilute) iterator.next();
				if(element.getGrupe().equals(grupes[i][1])){
					element.setGrupe(grupes[i][0]);
					element.setViso(element.getMoteru() + element.getVyru());
					rezult.add(element);
					rastas = true; break;
				}
			}
			if(!rastas){
				SkaiciausAtaskaitosEilute eil = new SkaiciausAtaskaitosEilute();
				eil.setGrupe(grupes[i][0]); eil.setMoteru(0); eil.setVyru(0); eil.setViso(0);
				rezult.add(eil);
			}			
		}
		SkaiciausAtaskaitosEilute eil = (SkaiciausAtaskaitosEilute) rezult.get(rezult.size()-1);
		int motViso = 0, vyruViso = 0;
		for (Iterator iterator = rezult.iterator(); iterator.hasNext();) {
			SkaiciausAtaskaitosEilute element = (SkaiciausAtaskaitosEilute) iterator.next();
			motViso += element.getMoteru(); vyruViso += element.getVyru();
		}
		eil.setMoteru(motViso); eil.setVyru(vyruViso); eil.setViso(motViso + vyruViso);
		return rezult;
	}
public List report0812(HttpServletRequest request, Date ataskaita12data, Long savivaldybe, Long seniunija, 
		String tip, Long grupavimas, int userStatus) {
	
	List rezult = null;
	
	if (userStatus == UserDelegator.USER_GLOBAL){
		if ((seniunija == null || seniunija.longValue() == 0) && (savivaldybe == null || savivaldybe.longValue()== 0)){
			List istaigos = new ArrayList();
			if(grupavimas != null && grupavimas.intValue()==3 && tip.equals("R")){
				istaigos = JournalDelegator.getInstance().getSeniunijos(request);				
			}
			else
				istaigos = JournalDelegator.getInstance().getSavivaldybes(request);					
			
			rezult = AtaskaitosDelegator.getInstance().report0812Grouped(request, ataskaita12data, istaigos, tip, true);			
		}
		else if ((savivaldybe != null && savivaldybe.longValue()!= 0)&& (seniunija == null || seniunija.longValue() == 0)){
			List seniunijos = null;
			if (grupavimas != null && grupavimas.intValue() == 3 && tip.equals("R")) {
				try {
					Istaiga objSavivaldybe = JournalDelegator.getInstance().getIstaiga(request,savivaldybe.longValue());
					seniunijos = JournalDelegator.getInstance().getSeniunijos(request,objSavivaldybe);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				rezult = AtaskaitosDelegator.getInstance().report0812Grouped(request,ataskaita12data, seniunijos, tip, false);
			}
			else
				rezult = AtaskaitosDelegator.getInstance().report0812(request, ataskaita12data,	savivaldybe, seniunija, tip);
		}
		else if ((savivaldybe != null && savivaldybe.longValue()!= 0)&& (seniunija != null && seniunija.longValue() != 0)){
			if(tip.equals("R"))
				rezult = AtaskaitosDelegator.getInstance().report0812(request, ataskaita12data,	savivaldybe, seniunija, tip);
		}
	}
	else if (userStatus == UserDelegator.USER_SEN){
		Istaiga sen = (Istaiga)UtilDelegator.getInstance().getIstaiga(seniunija.longValue(), request);
		long sav = sen.getIstaiga().getId();
		
		rezult = AtaskaitosDelegator.getInstance().report0812(request, ataskaita12data, new Long(sav), seniunija, tip);
		
	}
	else if (userStatus == UserDelegator.USER_SAV){
		if ((savivaldybe != null && savivaldybe.longValue()!= 0)&& (seniunija == null || seniunija.longValue() == 0)){
			List seniunijos = null;
			if (grupavimas != null && grupavimas.intValue() == 3 && tip.equals("R")) {
				try {
					Istaiga objSavivaldybe = JournalDelegator.getInstance().getIstaiga(request,savivaldybe.longValue());
					seniunijos = JournalDelegator.getInstance().getSeniunijos(request,objSavivaldybe);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				rezult = AtaskaitosDelegator.getInstance().report0812Grouped(request,ataskaita12data, seniunijos, tip, false);
			}
			else
				rezult = AtaskaitosDelegator.getInstance().report0812(request, ataskaita12data,	savivaldybe, seniunija, tip);
		}
		else if ((savivaldybe != null && savivaldybe.longValue()!= 0)&& (seniunija != null && seniunija.longValue() != 0)){
			if(tip.equals("R"))
				rezult = AtaskaitosDelegator.getInstance().report0812(request, ataskaita12data,	savivaldybe, seniunija, tip);
		}
	}	
	
	return rezult;
}

	private List report0812Grouped(HttpServletRequest request, Date ataskaita12data, List savivaldybes, String tip, boolean pilnaPavadinima) {
		List rezult = new ArrayList();
		
		for (Iterator iter = savivaldybes.iterator(); iter.hasNext();) {
			Istaiga ist = (Istaiga) iter.next();
			List savAtaskaita = report0812(request, ataskaita12data, new Long(ist.getId()), null, tip);
			SkaiciausAtaskaitosEiluteGrupuota eilGr = new SkaiciausAtaskaitosEiluteGrupuota();
			if(ist.getTipas()==2 && pilnaPavadinima)
				eilGr.setGrupe(ist.getPilnasPavadinimas());				
			else
				eilGr.setGrupe(ist.getPavadinimas());
			//int m0_7 = 0, m7_16 = 0, m16_18 = 0, m18_25 = 0, m25_45 = 0, m45_65 = 0, m65_85 = 0 , m85_0 = 0;
			int isViso = 0;
			for (Iterator iterator = savAtaskaita.iterator(); iterator.hasNext();) {
				SkaiciausAtaskaitosEilute eilute = (SkaiciausAtaskaitosEilute) iterator.next();	
				String grupe = eilute.getGrupe(); int viso = eilute.getViso();
				if(grupe.equals(METAI_0_7))		
					eilGr.setM0_7(viso);
				else if(grupe.equals(METAI_7_16))
					eilGr.setM7_16(viso);
				else if(grupe.equals(METAI_16_18))
					eilGr.setM16_18(viso);
				else if(grupe.equals(METAI_18_25))
					eilGr.setM18_25(viso);
				else if(grupe.equals(METAI_25_45))
					eilGr.setM25_45(viso);
				else if(grupe.equals(METAI_45_65))
					eilGr.setM45_65(viso);
				else if(grupe.equals(METAI_65_85))
					eilGr.setM65_85(viso);
				else if(grupe.equals(METAI_85_0))
					eilGr.setM85_0(viso);
				/////
				if(!grupe.equals(METAI_IS_VISO)) 
					isViso += viso;
				////
			}	
			eilGr.setViso(isViso);
			rezult.add(eilGr);
		}
		return rezult;
	}*/
}