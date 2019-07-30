package lingxi.shop.item.web;

import lingxi.shop.item.pojo.Category;
import lingxi.shop.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> queryCategoryPid(@RequestParam("pid") Long pid) {

        return ResponseEntity.ok(categoryService.queryCategoryList(pid));
    }

    @RequestMapping(value = "/bid/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> DeleteCategoryById(@PathVariable("id") Long id) {
        categoryService.DeleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @RequestMapping(value = "/list/ids", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> queryCategoryById(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }
}
