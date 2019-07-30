package lingxi.shop.item.mapper;

import lingxi.shop.common.BaseMapper.BaseMapper;
import lingxi.shop.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand> {
@Insert("INSERT INTO tb_category_brand (category_id,brand_id) VALUES (#{cid},#{bid})")
int insertCategoryBrand(@Param("cid")Long cid,@Param("bid")Long bid);
@Select("SELECT b.* from tb_brand b INNER JOIN tb_category_brand c on b.id=c.brand_id WHERE c.category_id=#{cid}")
    List<Brand> queryListByCid(@Param("cid")Long cid);
}
