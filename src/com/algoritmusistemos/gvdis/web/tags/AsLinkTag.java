package com.algoritmusistemos.gvdis.web.tags;

import javax.servlet.jsp.JspException;

public class AsLinkTag extends org.apache.struts.taglib.html.LinkTag{
	   
    public int doStartTag() throws JspException {
        AsPackTag pack = (AsPackTag)getParent(); 
        pageContext.setAttribute("parameter-map", pack.getParameters());
        setName("parameter-map");
        
        return super.doStartTag();
    }
    
    protected String calculateURL() throws JspException {
        String s = super.calculateURL();
        return s;
    }
    
    public void release() 
    {
        pageContext.removeAttribute("parameter-map");
        super.release();
    }
}

