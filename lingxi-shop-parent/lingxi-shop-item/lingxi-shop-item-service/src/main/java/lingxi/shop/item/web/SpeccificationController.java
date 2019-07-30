package lingxi.shop.item.web;

import lingxi.shop.item.pojo.SpecGroup;
import lingxi.shop.item.pojo.SpecParam;
import lingxi.shop.item.service.SpeccificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpeccificationController {
    @Autowired
    private SpeccificationService speccificationService;

    @RequestMapping(value = "/groups/{cid}", method = RequestMethod.GET)
    public ResponseEntity<List<SpecGroup>> getListGroupList(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(speccificationService.getListGroupList(cid));
    }


    @RequestMapping(value = "/group", method = RequestMethod.PUT)
    public ResponseEntity<Void> putListGroup(@RequestBody SpecGroup specGroup) {
        speccificationService.putListGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @RequestMapping(value = "/group/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> DELETEGroup(@PathVariable("id")Long id) {
        speccificationService.DELETEGroup(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public ResponseEntity<Void> SaveListGroup(@RequestBody SpecGroup specGroup) {
        speccificationService.SaveListGroup(specGroup);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @RequestMapping(value = "/params", method = RequestMethod.GET)
    public ResponseEntity<List<SpecParam>> getListGidList(@RequestParam(value = "gid",required = false) Long gid
    ,@RequestParam(value = "cid",required = false) Long cid,@RequestParam(value = "searching",required = false) Boolean searching
    ) {
        return ResponseEntity.ok(speccificationService.getListGidList(gid,cid,searching));
    }

    @RequestMapping(value = "/param", method = RequestMethod.POST)
    public ResponseEntity<Void> SaveListspecParam(@RequestBody SpecParam specParam) {
        speccificationService.SaveListspecParam(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @RequestMapping(value = "/param", method = RequestMethod.PUT)
    public ResponseEntity<Void> PUTspecParam(@RequestBody SpecParam specParam) {
        speccificationService.PUTspecParam(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @RequestMapping(value = "/param/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> DELETEspecParam(@PathVariable Long id) {
        speccificationService.DELETEspecParam(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @RequestMapping(value = "/group",method = RequestMethod.GET)
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@RequestParam("cid")Long cid){
        return ResponseEntity.ok(speccificationService.querySpecGroupByCid(cid));
    }
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid) {
        //todo
        return ResponseEntity.ok(speccificationService.querySpecsByCid(cid));
    }
    @GetMapping("/1")
    public ResponseEntity<String> querySpecsByCids() {
        //todo
        return ResponseEntity.ok("223233");
    }
}
