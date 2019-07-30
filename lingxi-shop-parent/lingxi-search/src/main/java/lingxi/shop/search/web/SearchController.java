package lingxi.shop.search.web;

import lingxi.shop.common.vo.PageResult;
import lingxi.shop.search.pojo.Goods;
import lingxi.shop.search.pojo.SearchRequest;
import lingxi.shop.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(searchService.search(searchRequest));
    }
}
