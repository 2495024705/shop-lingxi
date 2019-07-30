package lingxi.shop.item.api;

import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.pojo.Brand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/brand")
public interface BrandApi {
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResult<Brand> selectByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                          @RequestParam(value = "sortBy", required = false) String sortBy,
                                          @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                          @RequestParam(value = "key", required = false) String key);

    //新增品牌
    @RequestMapping(method = RequestMethod.POST)
    public Void SaveBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    @RequestMapping(value = "cid/{cid}", method = RequestMethod.GET)
    public List<Brand> queryListByCid(@PathVariable("cid") Long cid);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Brand queryBrandById(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.GET)
    public List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
