package lingxi.shop.item.api;

import lingxi.shop.item.pojo.SpecGroup;
import lingxi.shop.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/spec")
public interface SpecApi {
    @RequestMapping(value = "/groups/{cid}", method = RequestMethod.GET)
    public List<SpecGroup> getListGroupList(@PathVariable("cid") Long cid);

    @RequestMapping(value = "/group", method = RequestMethod.PUT)
    public Void putListGroup(@RequestBody SpecGroup specGroup);
    @RequestMapping(value = "/group/{id}", method = RequestMethod.DELETE)
    public Void DELETEGroup(@PathVariable("id")Long id);
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public Void SaveListGroup(@RequestBody SpecGroup specGroup);
    @RequestMapping(value = "/params", method = RequestMethod.GET)
    public List<SpecParam> getListGidList(@RequestParam(value = "gid",required = false) Long gid
            , @RequestParam(value = "cid",required = false) Long cid, @RequestParam(value = "searching",required = false) Boolean searching
    );
    @RequestMapping(value = "/param", method = RequestMethod.POST)
    public Void SaveListspecParam(@RequestBody SpecParam specParam);
    @RequestMapping(value = "/param", method = RequestMethod.PUT)
    public Void PUTspecParam(@RequestBody SpecParam specParam);
    @RequestMapping(value = "/param/{id}", method = RequestMethod.DELETE)
    public Void DELETEspecParam(@PathVariable Long id);
@RequestMapping(value = "/group",method = RequestMethod.GET)
    public List<SpecGroup> queryGroupByCid(@RequestParam("cid")Long cid);
    @GetMapping("/{cid}")
    List<SpecGroup> querySpecsByCid(@PathVariable("cid") Long cid);
}
