package lingxi.shop.item.mapper;

import lingxi.shop.item.pojo.Category;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author bystander
 * @date 2018/9/15
 */
public interface CategoryMapper extends Mapper<Category> , IdListMapper<Category,Long> {

}
