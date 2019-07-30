package lingxi.shop.item.service;

import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.item.mapper.SpecGroupMapper;
import lingxi.shop.item.mapper.SpecParamMapper;
import lingxi.shop.item.pojo.SpecGroup;
import lingxi.shop.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpeccificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> getListGroupList(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(list)) {
            throw new lingxiException(ExceptionEnum.SPEC_NOTf_fOND);
        }
        return list;
    }

    public void putListGroup(SpecGroup specGroup) {

        specGroupMapper.updateByPrimaryKey(specGroup);

    }

    public void DELETEGroup(Long id) {
        if (id == null) {
            throw new lingxiException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        int count = specGroupMapper.deleteByPrimaryKey(specGroup);
        if (count != 1) {
            throw new lingxiException(ExceptionEnum.SPEC_DELETE_fOND);
        }

    }

    public void SaveListGroup(SpecGroup specGroup) {
        List<SpecGroup> select = specGroupMapper.select(specGroup);
        if (!CollectionUtils.isEmpty(select)) {
            throw new lingxiException(ExceptionEnum.SPEC_SAVE_ERROR);
        }
        specGroupMapper.insert(specGroup);
    }

    public List<SpecParam> getListGidList(Long gid, Long cid, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        List<SpecParam> select = specParamMapper.select(param);
        if (CollectionUtils.isEmpty(select)) {
            throw new lingxiException(ExceptionEnum.SPEC_SAVE_ERROR);
        }
        return select;

    }

    public void SaveListspecParam(SpecParam specParam) {
        specParamMapper.insert(specParam);
    }

    public void PUTspecParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);
    }

    public void DELETEspecParam(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }

    public List<SpecGroup> querySpecGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(specGroupList)) {
            throw new lingxiException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
        return specGroupList;
    }

    public List<SpecGroup> querySpecsByCid(Long cid) {
        List<SpecGroup> specGroups = querySpecGroupByCid(cid);
        //查询当前分类下的参数
        List<SpecParam> specParams = querySpecParams(null, cid, null,null);
        //先把规格参数变成map，map的key是规格组的id,map的值是组下的所有参数
        Map<Long,List<SpecParam>> map = new HashMap<>();
        for (SpecParam specParam : specParams) {
            if (!map.containsKey(specParam.getGroupId())){
                map.put(specParam.getGroupId(),new ArrayList<>());
            }
            map.get(specParam.getGroupId()).add(specParam);
        }

        //填充param到group
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }


        return specGroups;
    }

    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        List<SpecParam> specParamList = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(specParamList)) {
            throw new lingxiException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
        return specParamList;
    }
}
