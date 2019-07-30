package lingxi.shop.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.mapper.*;
import lingxi.shop.item.pojo.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private DetailMapper detailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Spu.class);
        //搜索字段过滤
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        example.setOrderByClause("last_update_time DESC");

        List<Spu> spus = goodsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)) {
            throw new lingxiException(ExceptionEnum.GOODS_FIND_NOTFOUND);
        }
        LoadCategoryAndBrandName(spus);
        PageInfo<Spu> info = new PageInfo<>(spus);

        return new PageResult<>(info.getTotal(), spus);
        //排序
    }

    private void LoadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            List<String> collect = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(collect, "/"));
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }

    }

    @Transactional
    public void saveGoods(Spu spu) {
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int insert = spuMapper.insert(spu);

        if (insert != 1) {
            throw new lingxiException(ExceptionEnum.SPU_INSET_ERROR);
        }
        //新增detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        detailMapper.insert(spuDetail);
        //新增sku
        List<Sku> skus = spu.getSkus();
        List<Stock> list = new ArrayList<>();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());

            int insert1 = skuMapper.insert(sku);
            if (insert1 != 1) {
                throw new lingxiException(ExceptionEnum.SPU_INSET_ERROR);

            }
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            list.add(stock);
        }
        int i = stockMapper.insertList(list);

        if (i != list.size()) {
            throw new lingxiException(ExceptionEnum.SPU_INSET_ERROR);
        }
    }

    public SpuDetail queryDetailById(Long spuId) {
        SpuDetail spuDetail = detailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null) {
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        return spuDetail;
    }

    public List<Sku> querySpuList(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> select = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(select)) {
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        // for (Sku sku1 : select) {
        //     Stock stock = stockMapper.selectByPrimaryKey(sku1.getId());
        //     if (stock==null){
        //         throw new lingxiException(ExceptionEnum.STORE_NOT_FOUND);
        //     }
        //     sku1.setStock(stock.getStock());
        // }
        List<Long> collect = select.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stocks = stockMapper.selectByIdList(collect);
        if (CollectionUtils.isEmpty(stocks)) {
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        Map<Long, Integer> collect1 = stocks.stream().
                collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        select.forEach(sku1 -> sku1.setStock(collect1.get(sku1.getId())));
        return select;
    }
@Transactional
    public void updateGoods(Spu spu) {
//删除sku
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> select = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(select)){
            skuMapper.delete(sku);
            List<Long> collect = select.stream().map(Sku::getId).collect(Collectors.toList());
            //删除stock
            stockMapper.deleteByIdList(collect);
        }
//修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        int i = spuMapper.updateByPrimaryKeySelective(spu);
        if (i!=1){
            throw new lingxiException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
        i= detailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if (i!=1){
            throw new lingxiException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
        saveGoods(spu);
    amqpTemplate.convertAndSend("item.update", spu.getId());
    }

    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu==null){
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        spu.setSkus(querySpuList(id));
        spu.setSpuDetail(queryDetailById(id));
        return spu;
    }

    public List<Sku> querySkuBySpuId(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)){
            throw new lingxiException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
        //填充库存
        fillStock(ids, skus);
return skus;
    }
    private void fillStock(List<Long> ids, List<Sku> skus) {
        //批量查询库存
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stocks)) {
            throw new lingxiException(ExceptionEnum.SPEC_UPDATE_ERROR);
        }
        //首先将库存转换为map，key为sku的ID
        Map<Long, Integer> map = stocks.stream().collect(Collectors.toMap(s -> s.getSkuId(), s -> s.getStock()));

        //遍历skus，并填充库存
        for (Sku sku : skus) {
            sku.setStock(map.get(sku.getId()));
        }
    }

    public void decreaseStock(List<CartDto> carts) {
        for (CartDto cartDto:carts){
            int i = stockMapper.decreaseStock(cartDto.getSkuId(), cartDto.getNum());
       if (i!=1){
           throw new lingxiException(ExceptionEnum.Stock_NOT_ERROR);
       }
        }
    }
}
