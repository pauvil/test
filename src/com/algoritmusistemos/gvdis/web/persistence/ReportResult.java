package com.algoritmusistemos.gvdis.web.persistence;

public class ReportResult  extends GvdisBase 
{
	private long nr;
	private String name;
	private long count;
	
	public ReportResult()
	{
	}
	
	public ReportResult(long nr, String name, long count)
	{
		this.nr = nr;
		this.name = name;
		this.count = count;
	}

	public long getCount() 
	{
		return count;
	}

	public void setCount(long count) 
	{
		this.count = count;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public long getNr() 
	{
		return nr;
	}

	public void setNr(long nr) 
	{
		this.nr = nr;
	}
}
