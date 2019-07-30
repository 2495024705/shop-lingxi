package lingxi.shop.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.mapper.BrandMapper;
import lingxi.shop.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> selectByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtil.isNotEmpty(key)) {
            example.createCriteria().orLike("name", "%" + key + "%").orEqualTo("letter", key.toUpperCase());

        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClasue = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClasue);
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        //查询
        if (CollectionUtils.isEmpty(brands)) {
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        PageInfo<Brand> info = new PageInfo<Brand>(brands);
        return new PageResult<Brand>(info.getTotal(), brands);
    }
@Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count != 1) {
            throw new lingxiException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
        //新增中间表
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1) {
                throw new lingxiException(ExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }
    public Brand queryById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand==null){
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        return brand;
    }

    public List<Brand> queryListByCid(Long cid) {

        List<Brand> brands = brandMapper.queryListByCid(cid);
        if (CollectionUtils.isEmpty(brands)){
            throw new lingxiException(ExceptionEnum.GOODS_FIND_NOTFOUND);
        }
        return brands;
    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)){
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        return brands;
    }
}
