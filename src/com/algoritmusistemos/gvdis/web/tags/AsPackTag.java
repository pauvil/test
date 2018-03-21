package com.algoritmusistemos.gvdis.web.tags;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.tagext.BodyTagSupport;

public class AsPackTag extends BodyTagSupport{
    
    private Map _parameters = new HashMap();
    
    public void setParameters(Map parameters){
        _parameters = parameters;
    }
    
    public Map getParameters(){
        return _parameters;
    }
    
    public void addParameter(String name, String val){
        _parameters.put(name, val);
    }
    
    public int doStartTag(){
        return EVAL_BODY_BUFFERED;
    }
    
    public int doAfterBody()
    {
        try{
            getBodyContent().writeOut(getPreviousOut());
        }catch(Exception e){
            e.printStackTrace();
        }
        _parameters.clear();
        return SKIP_BODY;
    }
       
    public void release(){
        _parameters.clear();
    }
}


