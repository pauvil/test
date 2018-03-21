package lt.solver.gvdis.util;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import lt.solver.gvdis.model.UrlValueBean;

public class Helper {

	public static long string2Long (String longStr, long defaultValue) {
		long retVal = defaultValue;
		try {
			retVal = Long.parseLong(longStr);
		} catch (NumberFormatException e) {
			retVal = defaultValue;
		}
		return retVal;
	}
	
	public static int string2int(String integerStr, int defaultValue) {
		int retVal = defaultValue;
		try {
			retVal = Integer.parseInt(integerStr);
		} catch (NumberFormatException e) {
			retVal = defaultValue;
		}
		return retVal;
	}

	public static String noNulls(String str, String defaultValue) {
		String retVal = defaultValue;
		if (str != null) {
			retVal = str;
		}
		return retVal;
	}

	public static UrlValueBean[] makeFooterPages(HttpServletRequest req, int journalType, 
											int totalPage, int showPagesInFooter, int currentPage) {
		
		if (showPagesInFooter <= 0) showPagesInFooter = 1;
		int plius_minus_page = 0;
		if (showPagesInFooter < totalPage) {
			if (showPagesInFooter % 2 == 0) {
				//lyginis skaicius, turi buti visa laika nelyginis
				showPagesInFooter++; //padidiname
			}
			plius_minus_page = showPagesInFooter / 2;
		} else {
			showPagesInFooter = totalPage;
		}
		
		int fromPage = 1;
		int toPage = showPagesInFooter;
		
		if (showPagesInFooter != totalPage) {
			if (currentPage - plius_minus_page > 0 && currentPage + plius_minus_page < totalPage) {
				fromPage = currentPage - plius_minus_page; 
				toPage = currentPage + plius_minus_page;
			} else {
				if (currentPage - plius_minus_page < 0) {
					fromPage = 1; toPage = showPagesInFooter;
				}
				if (currentPage + plius_minus_page >= totalPage) {
					fromPage = totalPage - plius_minus_page * 2;
					toPage = totalPage;
				}
			}
		}
		//formuojami url
		String url = req.getContextPath() + "/journal.do?act=page&pageNr={0}&journalType=" + journalType;
		UrlValueBean[] retVal = new UrlValueBean[showPagesInFooter + 2]; //pradzioje ir pabaigoje bus po rodykles
		//zingsnis atgal
		retVal[0] = new UrlValueBean();
		int step = fromPage /2;
		if (step <= 0) { //currentPage == 1) {
			retVal[0].setMessage(""); //Nereikia ir taip pirmas puslapis
			retVal[0].setUrl(""); //
		} else {
			retVal[0].setMessage("«"); //<< &laquo;
			Integer t = new Integer(step);
			retVal[0].setUrl(MessageFormat.format(url, new Object[]{t})); //i ankstesni puslapi
		}
		
		//zingsnis i prieki
		retVal[showPagesInFooter + 1] = new UrlValueBean();
		step = toPage + ((totalPage - toPage) /2);
		if (step >= totalPage) {
			retVal[showPagesInFooter + 1].setMessage(""); //Nereikia ir taip paskutinis puslapis
			retVal[showPagesInFooter + 1].setUrl(""); //
		} else {
			retVal[showPagesInFooter + 1].setMessage("»"); //>> &raquo;
			Integer t = new Integer(step); // currentPage +1);
			retVal[showPagesInFooter + 1].setUrl(MessageFormat.format(url, new Object[]{t})); //i ankstesni puslapi
		}
		
		int p = fromPage;
		for (int i = 1; i< showPagesInFooter +1; i++) { //pirmas, paskutinis puslapis jau uzimtas
			String pageNr = String.valueOf(p);
			retVal[i] = new UrlValueBean();
			if (currentPage != p ) { //sitam reikia url'o
				retVal[i].setMessage(pageNr); //puslapis
				retVal[i].setUrl(MessageFormat.format(url, new Object[]{pageNr})); //url i puslapi
			} else {
				retVal[i].setMessage(pageNr); //neraikia url
				retVal[i].setUrl(""); //url i puslapi
			}
			p++;
		}
		
		return retVal;
	}
}
