package lingxi.shop.item.service;

import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.item.mapper.CategoryMapper;
import lingxi.shop.item.pojo.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryList(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> select = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(select)){
throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        return select;
    }

    public void DeleteCategoryById(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
    public List<Category> queryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)){
            throw new lingxiException(ExceptionEnum.GOODS_FIND_NOTFOUND);
        }
        return list;
    }
}
