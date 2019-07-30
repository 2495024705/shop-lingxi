package lingxi.shop.search.pojo;

import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.pojo.Brand;
import lingxi.shop.item.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author bystander
 * @date 2018/9/23
 */
@Data
public class SearchResult<Goods> extends PageResult<Goods> {

    public List<Brand> brands;
    public List<Category> categories;
    //规格参数过滤条件
    public List<Map<String, Object>> specs;

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Brand> brands, List<Category> categories) {
        super(total, totalPage, items);
        this.brands = brands;
        this.categories = categories;
        this.specs = specs;
    }

    public SearchResult(Long total,Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total,totalPage, items);
        this.brands = brands;
        this.categories = categories;
        this.specs = specs;
    }

    public SearchResult(List<Brand> brands, List<Category> categories, List<Map<String, Object>> specs) {
        this.brands = brands;
        this.categories = categories;
        this.specs = specs;
    }
}
