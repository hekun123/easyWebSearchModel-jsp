package cn.java.ir.db;

public class news_info {
	public String title;
	public String url;
	public String cate;
	public String date;
	public String srcFrom;
	public String content;
	public String comment;
	public String hotIndex;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCate() {
        return cate;
    }
    public void setCate(String cate) {
        this.cate = cate;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getSrcFrom() {
        return srcFrom;
    }
    public void setSrcFrom(String srcFrom) {
        this.srcFrom = srcFrom;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getHotIndex() {
        return hotIndex;
    }
    public void setHotIndex(String hotIndex) {
        this.hotIndex = hotIndex;
    }
    public void printNews() {
    	System.out.println(this.title+":\n"
    			+this.content+"\n"
    			+this.url+"\t"+this.cate+"\t"+this.date+"\t"+this.comment+"\t"+this.srcFrom+"\t"+this.hotIndex);
    }
    
}
