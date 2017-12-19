package com.svlada.entity.news;

import com.svlada.entity.parent.NewsParent;

import java.io.Serializable;
//
//@Entity
//@Table(name="News")
public class News extends NewsParent implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 查询条件
	 */
	private String createtimeEnd;// 页面查询条件
	private int readerCount;
	private String status;//文章是否显示到门户。y:显示；n：不显示；默认是n
	private String catalogID;// 目录ID
	private String lableID;// 标签ID


	public String getCatalogID() {
		return catalogID;
	}

	public void setCatalogID(String catalogID) {
		this.catalogID = catalogID;
	}

	public String getCreatetimeEnd() {
		return createtimeEnd;
	}

	public void setCreatetimeEnd(String createtimeEnd) {
		this.createtimeEnd = createtimeEnd;
	}

	public int getReaderCount() {
		return readerCount;
	}

	public void setReaderCount(int readerCount) {
		this.readerCount = readerCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLableID() {
		return lableID;
	}

	public void setLableID(String lableID) {
		this.lableID = lableID;
	}


	@Override
	public String toString() {
		return "News [createtimeEnd=" + createtimeEnd + ", readerCount="
				+ readerCount + ", status=" + status + ", catalogID="
				+ catalogID + ", lableID=" + lableID + "]";
	}
	
	
}
