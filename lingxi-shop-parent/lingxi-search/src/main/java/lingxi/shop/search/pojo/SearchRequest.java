package lingxi.shop.search.pojo;

import java.util.Map;

public class SearchRequest {
    private String key;
    private Integer page;
    private Map<String,String> filter;
    private static final int DEFAULT_SIZE = 20;//最大页
    private static final int DEFAULT_PAGE=1;//默认页

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }



    public Integer getPage() {
       if (page==null){
           return DEFAULT_PAGE;
       }
       return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }
public Integer getSize(){
        return DEFAULT_SIZE;
}

}
