package com.nettactic.mall.bean;

import java.util.Comparator;

import com.nettactic.mall.base.BaseBean;

public class SelectpicBean extends BaseBean implements
		Comparator<SelectpicBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 图片的名字 （用GUI生成的压缩后保存到SD中的名字）
	private String imgid;
	// 图片所在的作品id
	private String workid;
	// 图片 所在的作品的总页数
	private String totalpage;
	// SD中图片原来的路径
	private String oldpath;
	// 压缩后保存到SD中的新路径
	private String newpath;

	// 图片 所在的作品的第几页
	private String workpage = "-1";
	// 图片是否被选中
	private String isselect = "false";
	// 图片是否被压缩
	private String iscondense = "false";
	// 图片是否保存
	private String issave = "false";
	// 图片是否上传到服务器成功
	private String isnet = "false";
	// 图片是否正在上传到服务器
	private String isupload = "false";
	// 图片压缩保存在SD中是否存在
	private String isexist = "false";

	// 是否计算用户图片相对mask居中铺满
	private String iscompute = "false";
	// 计算用户图片相对mask居中铺满 的 x
	private String translatex;
	// 计算用户图片相对mask居中铺满 的 y
	private String translatey;
	// 计算用户图片相对mask居中铺满 初始缩放值
	private String initscale;
	// 用户图片相对mask 当前初始缩放值
	private String scale;
	// Mask 布局文件高 （像素）
	private String masklayoutheight;
	// Mask 物理文件高 （像素）
	private String maskheight;
	// Mask 物理文件宽 （像素）
	private String maskwidth;
	// 用户图片 物理文件高 （像素）
	private String picheight;
	// 用户图片 物理文件宽 （像素）
	private String picwidth;
	// 计算用户图片相对mask居中铺满 的 x （服务器需要）
	private String cutx;
	// 是否计算用户图片相对mask居中铺满 的 y （服务器需要）
	private String cuty;
	// 用户图片 处理后 高（服务器需要）
	private String cutwidth;
	// 用户图片 处理后 宽 （服务器需要）
	private String cutheight;
	// 单张牌的布局信息
	private String cardinfo;

	public String getImgid() {
		return imgid;
	}

	public void setImgid(String imgid) {
		this.imgid = imgid;
	}

	public String getWorkid() {
		return workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}

	public String getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}

	public String getOldpath() {
		return oldpath;
	}

	public void setOldpath(String oldpath) {
		this.oldpath = oldpath;
	}

	public String getNewpath() {
		return newpath;
	}

	public void setNewpath(String newpath) {
		this.newpath = newpath;
	}

	public String getWorkpage() {
		return workpage;
	}

	public void setWorkpage(String workpage) {
		this.workpage = workpage;
	}

	public String getIsselect() {
		return isselect;
	}

	public void setIsselect(String isselect) {
		this.isselect = isselect;
	}

	public String getIscondense() {
		return iscondense;
	}

	public void setIscondense(String iscondense) {
		this.iscondense = iscondense;
	}

	public String getIssave() {
		return issave;
	}

	public void setIssave(String issave) {
		this.issave = issave;
	}

	public String getIsnet() {
		return isnet;
	}

	public void setIsnet(String isnet) {
		this.isnet = isnet;
	}

	public String getIsupload() {
		return isupload;
	}

	public void setIsupload(String isupload) {
		this.isupload = isupload;
	}

	public String getIsexist() {
		return isexist;
	}

	public void setIsexist(String isexist) {
		this.isexist = isexist;
	}

	public String getIscompute() {
		return iscompute;
	}

	public void setIscompute(String iscompute) {
		this.iscompute = iscompute;
	}

	public String getTranslatex() {
		return translatex;
	}

	public void setTranslatex(String translatex) {
		this.translatex = translatex;
	}

	public String getTranslatey() {
		return translatey;
	}

	public void setTranslatey(String translatey) {
		this.translatey = translatey;
	}

	public String getInitscale() {
		return initscale;
	}

	public void setInitscale(String initscale) {
		this.initscale = initscale;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getMasklayoutheight() {
		return masklayoutheight;
	}

	public void setMasklayoutheight(String masklayoutheight) {
		this.masklayoutheight = masklayoutheight;
	}

	public String getMaskheight() {
		return maskheight;
	}

	public void setMaskheight(String maskheight) {
		this.maskheight = maskheight;
	}

	public String getMaskwidth() {
		return maskwidth;
	}

	public void setMaskwidth(String maskwidth) {
		this.maskwidth = maskwidth;
	}

	public String getPicheight() {
		return picheight;
	}

	public void setPicheight(String picheight) {
		this.picheight = picheight;
	}

	public String getPicwidth() {
		return picwidth;
	}

	public void setPicwidth(String picwidth) {
		this.picwidth = picwidth;
	}

	public String getCutx() {
		return cutx;
	}

	public void setCutx(String cutx) {
		this.cutx = cutx;
	}

	public String getCuty() {
		return cuty;
	}

	public void setCuty(String cuty) {
		this.cuty = cuty;
	}

	public String getCutwidth() {
		return cutwidth;
	}

	public void setCutwidth(String cutwidth) {
		this.cutwidth = cutwidth;
	}

	public String getCutheight() {
		return cutheight;
	}

	public void setCutheight(String cutheight) {
		this.cutheight = cutheight;
	}

	public String getCardinfo() {
		return cardinfo;
	}

	public void setCardinfo(String cardinfo) {
		this.cardinfo = cardinfo;
	}

	public SelectpicBean() {
		super();
	}

	public SelectpicBean(String imgid, String workid, String totalpage,
			String oldpath, String newpath, String workpage, String isselect,
			String iscondense, String issave, String isnet, String isupload,
			String isexist, String iscompute, String translatex,
			String translatey, String initscale, String scale,
			String masklayoutheight, String maskheight, String maskwidth,
			String picheight, String picwidth, String cutx, String cuty,
			String cutwidth, String cutheight, String cardinfo) {
		super();
		this.imgid = imgid;
		this.workid = workid;
		this.totalpage = totalpage;
		this.oldpath = oldpath;
		this.newpath = newpath;
		this.workpage = workpage;
		this.isselect = isselect;
		this.iscondense = iscondense;
		this.issave = issave;
		this.isnet = isnet;
		this.isupload = isupload;
		this.isexist = isexist;
		this.iscompute = iscompute;
		this.translatex = translatex;
		this.translatey = translatey;
		this.initscale = initscale;
		this.scale = scale;
		this.masklayoutheight = masklayoutheight;
		this.maskheight = maskheight;
		this.maskwidth = maskwidth;
		this.picheight = picheight;
		this.picwidth = picwidth;
		this.cutx = cutx;
		this.cuty = cuty;
		this.cutwidth = cutwidth;
		this.cutheight = cutheight;
		this.cardinfo = cardinfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cardinfo == null) ? 0 : cardinfo.hashCode());
		result = prime * result
				+ ((cutheight == null) ? 0 : cutheight.hashCode());
		result = prime * result
				+ ((cutwidth == null) ? 0 : cutwidth.hashCode());
		result = prime * result + ((cutx == null) ? 0 : cutx.hashCode());
		result = prime * result + ((cuty == null) ? 0 : cuty.hashCode());
		result = prime * result + ((imgid == null) ? 0 : imgid.hashCode());
		result = prime * result
				+ ((initscale == null) ? 0 : initscale.hashCode());
		result = prime * result
				+ ((iscompute == null) ? 0 : iscompute.hashCode());
		result = prime * result
				+ ((iscondense == null) ? 0 : iscondense.hashCode());
		result = prime * result + ((isexist == null) ? 0 : isexist.hashCode());
		result = prime * result + ((isnet == null) ? 0 : isnet.hashCode());
		result = prime * result + ((issave == null) ? 0 : issave.hashCode());
		result = prime * result
				+ ((isselect == null) ? 0 : isselect.hashCode());
		result = prime * result
				+ ((isupload == null) ? 0 : isupload.hashCode());
		result = prime * result
				+ ((maskheight == null) ? 0 : maskheight.hashCode());
		result = prime
				* result
				+ ((masklayoutheight == null) ? 0 : masklayoutheight.hashCode());
		result = prime * result
				+ ((maskwidth == null) ? 0 : maskwidth.hashCode());
		result = prime * result + ((newpath == null) ? 0 : newpath.hashCode());
		result = prime * result + ((oldpath == null) ? 0 : oldpath.hashCode());
		result = prime * result
				+ ((picheight == null) ? 0 : picheight.hashCode());
		result = prime * result
				+ ((picwidth == null) ? 0 : picwidth.hashCode());
		result = prime * result + ((scale == null) ? 0 : scale.hashCode());
		result = prime * result
				+ ((totalpage == null) ? 0 : totalpage.hashCode());
		result = prime * result
				+ ((translatex == null) ? 0 : translatex.hashCode());
		result = prime * result
				+ ((translatey == null) ? 0 : translatey.hashCode());
		result = prime * result + ((workid == null) ? 0 : workid.hashCode());
		result = prime * result
				+ ((workpage == null) ? 0 : workpage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectpicBean other = (SelectpicBean) obj;
		if (cardinfo == null) {
			if (other.cardinfo != null)
				return false;
		} else if (!cardinfo.equals(other.cardinfo))
			return false;
		if (cutheight == null) {
			if (other.cutheight != null)
				return false;
		} else if (!cutheight.equals(other.cutheight))
			return false;
		if (cutwidth == null) {
			if (other.cutwidth != null)
				return false;
		} else if (!cutwidth.equals(other.cutwidth))
			return false;
		if (cutx == null) {
			if (other.cutx != null)
				return false;
		} else if (!cutx.equals(other.cutx))
			return false;
		if (cuty == null) {
			if (other.cuty != null)
				return false;
		} else if (!cuty.equals(other.cuty))
			return false;
		if (imgid == null) {
			if (other.imgid != null)
				return false;
		} else if (!imgid.equals(other.imgid))
			return false;
		if (initscale == null) {
			if (other.initscale != null)
				return false;
		} else if (!initscale.equals(other.initscale))
			return false;
		if (iscompute == null) {
			if (other.iscompute != null)
				return false;
		} else if (!iscompute.equals(other.iscompute))
			return false;
		if (iscondense == null) {
			if (other.iscondense != null)
				return false;
		} else if (!iscondense.equals(other.iscondense))
			return false;
		if (isexist == null) {
			if (other.isexist != null)
				return false;
		} else if (!isexist.equals(other.isexist))
			return false;
		if (isnet == null) {
			if (other.isnet != null)
				return false;
		} else if (!isnet.equals(other.isnet))
			return false;
		if (issave == null) {
			if (other.issave != null)
				return false;
		} else if (!issave.equals(other.issave))
			return false;
		if (isselect == null) {
			if (other.isselect != null)
				return false;
		} else if (!isselect.equals(other.isselect))
			return false;
		if (isupload == null) {
			if (other.isupload != null)
				return false;
		} else if (!isupload.equals(other.isupload))
			return false;
		if (maskheight == null) {
			if (other.maskheight != null)
				return false;
		} else if (!maskheight.equals(other.maskheight))
			return false;
		if (masklayoutheight == null) {
			if (other.masklayoutheight != null)
				return false;
		} else if (!masklayoutheight.equals(other.masklayoutheight))
			return false;
		if (maskwidth == null) {
			if (other.maskwidth != null)
				return false;
		} else if (!maskwidth.equals(other.maskwidth))
			return false;
		if (newpath == null) {
			if (other.newpath != null)
				return false;
		} else if (!newpath.equals(other.newpath))
			return false;
		if (oldpath == null) {
			if (other.oldpath != null)
				return false;
		} else if (!oldpath.equals(other.oldpath))
			return false;
		if (picheight == null) {
			if (other.picheight != null)
				return false;
		} else if (!picheight.equals(other.picheight))
			return false;
		if (picwidth == null) {
			if (other.picwidth != null)
				return false;
		} else if (!picwidth.equals(other.picwidth))
			return false;
		if (scale == null) {
			if (other.scale != null)
				return false;
		} else if (!scale.equals(other.scale))
			return false;
		if (totalpage == null) {
			if (other.totalpage != null)
				return false;
		} else if (!totalpage.equals(other.totalpage))
			return false;
		if (translatex == null) {
			if (other.translatex != null)
				return false;
		} else if (!translatex.equals(other.translatex))
			return false;
		if (translatey == null) {
			if (other.translatey != null)
				return false;
		} else if (!translatey.equals(other.translatey))
			return false;
		if (workid == null) {
			if (other.workid != null)
				return false;
		} else if (!workid.equals(other.workid))
			return false;
		if (workpage == null) {
			if (other.workpage != null)
				return false;
		} else if (!workpage.equals(other.workpage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SelectpicBean [imgid=" + imgid + ", workid=" + workid
				+ ", totalpage=" + totalpage + ", oldpath=" + oldpath
				+ ", newpath=" + newpath + ", workpage=" + workpage
				+ ", isselect=" + isselect + ", iscondense=" + iscondense
				+ ", issave=" + issave + ", isnet=" + isnet + ", isupload="
				+ isupload + ", isexist=" + isexist + ", iscompute="
				+ iscompute + ", translatex=" + translatex + ", translatey="
				+ translatey + ", initscale=" + initscale + ", scale=" + scale
				+ ", masklayoutheight=" + masklayoutheight + ", maskheight="
				+ maskheight + ", maskwidth=" + maskwidth + ", picheight="
				+ picheight + ", picwidth=" + picwidth + ", cutx=" + cutx
				+ ", cuty=" + cuty + ", cutwidth=" + cutwidth + ", cutheight="
				+ cutheight + ", cardinfo=" + cardinfo + "]";
	}

	@Override
	public int compare(SelectpicBean bean0, SelectpicBean bean1) {
		int num = 0;
		String workpage0 = bean0.getWorkpage();
		int pageNum0 = Integer.parseInt(workpage0);

		String workpage1 = bean1.getWorkpage();
		int pageNum1 = Integer.parseInt(workpage1);
		int value = pageNum1 - pageNum0;
		if (value > 0) {
			num = -1;
		} else if (value == 0) {
			num = 0;
		} else if (value < 0) {
			num = 1;
		}
		return num;
	}

}
