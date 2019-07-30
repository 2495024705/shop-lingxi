package lingxi.shop.item.api;

import lingxi.shop.item.pojo.Category;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/category")
public interface CategoryApi {
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Category> queryCategoryPid(@RequestParam("pid") Long pid);

    @RequestMapping(value = "/bid/{id}", method = RequestMethod.DELETE)
    public Void DeleteCategoryById(@PathVariable("id") Long id);

    @RequestMapping(value = "/list/ids", method = RequestMethod.GET)
    public List<Category> queryCategoryById(@RequestParam("ids") List<Long> ids);
}
