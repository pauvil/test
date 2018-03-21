package com.algoritmusistemos.gvdis.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AsParamTag extends BodyTagSupport {
    
    private String _param=null;
    private String _value=null;
    
    public void setParam(String param){ _param=param; }
    public void setValue(String value){ _value=value; }
       
    public int doStartTag() {
         return EVAL_BODY_BUFFERED;
     }

    public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            setValue(value);
        }
        return (SKIP_BODY);
    }    
    
    public int doEndTag() throws JspException {
         AsPackTag pack = (AsPackTag)getParent();
         pack.addParameter(_param, _value);
         return EVAL_PAGE;
    }
}


