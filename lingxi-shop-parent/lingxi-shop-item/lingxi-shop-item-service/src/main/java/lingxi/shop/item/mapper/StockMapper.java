package lingxi.shop.item.mapper;

import lingxi.shop.common.BaseMapper.BaseMapper;
import lingxi.shop.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface StockMapper extends BaseMapper<Stock> {
    @Update("UPDATE tb_stock SET stock=stock-#{num} WHERE sku_id=#{id} AND stock>=#{num}")
    int decreaseStock(@Param("id")Long id,@Param("num") Integer num);}
