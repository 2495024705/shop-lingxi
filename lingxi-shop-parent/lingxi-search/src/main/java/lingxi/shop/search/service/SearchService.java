package lingxi.shop.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.common.utils.JsonUtils;
import lingxi.shop.common.utils.NumberUtils;
import lingxi.shop.common.vo.PageResult;
import lingxi.shop.item.pojo.*;
import lingxi.shop.search.client.BrandClient;
import lingxi.shop.search.client.CategoryClient;
import lingxi.shop.search.client.GoodsClient;
import lingxi.shop.search.client.SpecClient;
import lingxi.shop.search.pojo.Goods;
import lingxi.shop.search.pojo.SearchRequest;
import lingxi.shop.search.pojo.SearchResult;
import lingxi.shop.search.repository.GoodsRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecClient specClient;

    public Goods queryList(Spu spu) {
        List<Category> list = categoryClient.queryCategoryById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(list)) {
            throw new lingxiException(ExceptionEnum.CATEGORY_NOTf_fOND);
        }
        List<String> collect = list.stream().map(Category::getName).collect(Collectors.toList());
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new lingxiException(ExceptionEnum.STORE_NOT_FOUND);
        }

        List<Sku> skus = goodsClient.querySpuList(spu.getId());
        if (CollectionUtils.isEmpty(skus)) {
            throw new lingxiException(ExceptionEnum.STORE_NOT_FOUND);
        }
        List<Map<String, Object>> skuss = new ArrayList<>();
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ","));

            skuss.add(map);
            priceList.add(sku.getPrice());

        }
        List<Long> collect1 = skus.stream().map(Sku::getPrice).collect(Collectors.toList());
        List<SpecParam> listGidList = specClient.getListGidList(null, spu.getCid3(), true);
        if (listGidList == null) {
            throw new lingxiException(ExceptionEnum.STORE_NOT_FOUND);
        }
        //查询商品详情
        SpuDetail spuDetail = goodsClient.queryDetailById(spu.getId());
        String json = spuDetail.getGenericSpec();
        Map<Long, String> stringStringMap = JsonUtils.parseMap(json, Long.class, String.class);
        String jsons = spuDetail.getSpecialSpec();
        Map<Long, List<String>> stringListMap = JsonUtils.nativeRead(jsons, new TypeReference<Map<Long, List<String>>>() {
        });
        //规格参数，key是规格参数的名字，值是规格参数的值
        Map<String, Object> spec = new HashMap<>();
        for (SpecParam param : listGidList) {
            String key = param.getName();
            Object value = "";
            if (param.getGeneric()) {
                value = stringStringMap.get(param.getId());
                if (param.getNumeric()) {
                    chooseSegment(value.toString(), param);
                }
            } else {
                value = stringListMap.get(param.getId());
            }
            spec.put(key, value);
        }

        String all = spu.getTitle() + StringUtils.join(collect, "") + brand.getName();
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(all);
        goods.setPrice(collect1);
        goods.setSkus(JsonUtils.serialize(skuss));
        goods.setSpecs(spec);
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest searchRequest) {
        int page = searchRequest.getPage() - 1;
        int size = searchRequest.getSize();
        String key = searchRequest.getKey();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page, size));
        QueryBuilder nativeSearchQueryBuilder1 = BasicRequestFilter(searchRequest);
        nativeSearchQueryBuilder.withQuery(nativeSearchQueryBuilder1);
        //聚合分类
        String categoryAggName = "category_agg";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //聚合品牌
        String brandAggName = "brand_agg";
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        AggregatedPage<Goods> search = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        //分页 结果
        long total = search.getTotalElements();
        long totalPage = search.getTotalPages();
        List<Goods> content = search.getContent();
        //聚合结果
        Aggregations aggregations = search.getAggregations();
        List<Category> categories = parseCategory(aggregations.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggregations.get(brandAggName));
//完成规格参数聚合
        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size() == 1) {
            //商品分类存在并且为1
            specs = buildSpecificationAgg(categories.get(0).getId(), nativeSearchQueryBuilder1);
        }
        return new SearchResult(total, totalPage, content, categories, brands, specs);

    }

    private QueryBuilder BasicRequestFilter(SearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //查询条件

        boolQueryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()));
        Map<String, String> filter = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            if (!"cid3".equals(key) && "brandId".equals(key)) {
                key = "specs." + key + ".keyward";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return boolQueryBuilder;
    }

    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder nativeSearchQueryBuilder1) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            //查询参数
            List<SpecParam> params = specClient.getListGidList(null, cid, true);
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            //聚合
            nativeSearchQueryBuilder.withQuery(nativeSearchQueryBuilder1);

            // 聚合规格参数
            for (SpecParam s : params) {
                String name = s.getName();
                nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));

            }

            // 查询
            AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);

            // 解析聚合结果
            Aggregations aggs = result.getAggregations();
            for (SpecParam p : params) {
                String name = p.getName();
                StringTerms aggregation = aggs.get(name);
                Map<String, Object> map = new HashMap<>();
                map.put("k", name);
                map.put("options", aggregation.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList()));
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            return null;
        }

    }

    private List<Brand> parseBrandAgg(LongTerms aggregation) {
        try {
            List<Long> collect = aggregation.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(collect);
            return brands;
        } catch (Exception e) {
            return null;
        }
    }

    private List<Category> parseCategory(LongTerms aggregation) {
        try {
            List<Long> collect = aggregation.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Category> brands = categoryClient.queryCategoryById(collect);
            return brands;
        } catch (Exception e) {
            return null;
        }
    }
}
