package lingxi.shop.item.web;

import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.pojo.Brand;
import lingxi.shop.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<PageResult<Brand>> selectByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                          @RequestParam(value = "sortBy", required = false) String sortBy,
                                                          @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                          @RequestParam(value = "key", required = false) String key) {
        PageResult<Brand> result = brandService.selectByPage(page, rows, sortBy, desc, key);
        if (result == null || result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    //新增品牌
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> SaveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {

        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
@RequestMapping(value = "cid/{cid}",method = RequestMethod.GET)
    public ResponseEntity<List<Brand>> queryListByCid(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(brandService.queryListByCid(cid));
}
@RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandService.queryById(id));
}
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }
}
