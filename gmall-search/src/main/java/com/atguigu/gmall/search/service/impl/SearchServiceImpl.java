package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.search.feign.GmallPmsClient;
import com.atguigu.gmall.search.feign.GmallWmsClient;
import com.atguigu.gmall.search.pojo.*;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.search.service.SearchService;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Cerosg
 * @create 2020年08月01日 14时52分
 */
@Service
public class SearchServiceImpl implements SearchService {

    // 注入elasticsearch的RestHighLevelClient客户端，实现【高亮】功能
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    // 实现反序列化
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private GmallPmsClient gmallPmsClient;
    @Autowired
    private GmallWmsClient gmallWmsClient;
    @Autowired
    private GoodsRepository goodsRepository;

    // ------------------------ 响应查询结果 【开始】 ------------------------
    @Override
    public SearchResponseVo search(SearchParamVo searchParamVo) {
        try {
            // 设置搜索索引库
            SearchRequest searchRequest = new SearchRequest().indices("goods");
            // 构建查询条件
            searchRequest.source(Objects.requireNonNull(getSearchSourceBuilder(searchParamVo)));
            // 指定查询，获取结果集
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 解析结果集
            SearchResponseVo searchResponseVo = parseResult(searchResponse);
            // 设置页面参数（页码+页面记录数）
            searchResponseVo.setPageNum(searchParamVo.getPageNum());
            searchResponseVo.setPageSize(searchParamVo.getPageSize());
            return searchResponseVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析elasticSearch的搜索结果集
     *
     * @return 解析后的响应对象
     */
    private SearchResponseVo parseResult(SearchResponse searchResponse) {
        SearchResponseVo searchResponseVo = new SearchResponseVo();
        SearchHits searchHits = searchResponse.getHits();
        // 命中记录总数
        searchResponseVo.setTotal(searchHits.totalHits);
        // 获取当前页数据
        SearchHit[] hits = searchHits.getHits();
        if (hits == null || hits.length == 0) {// 若当前页没有数据，直接返回
            return searchResponseVo;
        }
        // 页面商品数据
        List<Goods> goodsList = Stream.of(hits).map(hit -> {
            try {
                // 获取命中结果集中的_source
                String source = hit.getSourceAsString();
                // 将_source反序列化为Goods对象
                Goods goods = OBJECT_MAPPER.readValue(source, Goods.class);
                // 将_source中的普通的Title替换为高亮结果集中的title
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (!CollectionUtils.isEmpty(highlightFields)) {
                    HighlightField highlightField = highlightFields.get("title");
                    if (highlightField != null) {
                        Text[] fragments = highlightField.getFragments();
                        if (fragments != null && fragments.length > 0) {
                            goods.setTitle(fragments[0].toString());
                        }
                    }
                }
                return goods;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        searchResponseVo.setGoodsList(goodsList);
        // 获取【聚合】结果集
        Map<String, Aggregation> aggregationMap = searchResponse.getAggregations().asMap();
        // 一、解析--品牌--聚合结果集
        ParsedLongTerms brandIdAgg = (ParsedLongTerms) aggregationMap.get("brandIdAgg");
        List<? extends Terms.Bucket> buckets = brandIdAgg.getBuckets();
        List<BrandEntity> brandEntities = buckets.stream().map(bucket -> {
            BrandEntity brandEntity = new BrandEntity();
            // 解析出品牌桶中的id
            brandEntity.setId(bucket.getKeyAsNumber().longValue());
            // 获取桶中的name的子聚合
            Map<String, Aggregation> subAggregations = bucket.getAggregations().asMap();
            ParsedStringTerms brandNameAgg = (ParsedStringTerms) subAggregations.get("brandNameAgg");
            List<? extends Terms.Bucket> nameBuckets = brandNameAgg.getBuckets();
            if (!CollectionUtils.isEmpty(nameBuckets)) {
                Terms.Bucket nameBucket = nameBuckets.get(0);
                if (nameBucket != null) {
                    // 解析出子聚合中的name
                    brandEntity.setName(nameBucket.getKeyAsString());
                }
            }
            // 获取桶中的包含logo的子聚合
            ParsedStringTerms brandLogoAgg = (ParsedStringTerms) subAggregations.get("brandLogoAgg");
            if (brandLogoAgg != null) {
                List<? extends Terms.Bucket> logoBuckets = brandLogoAgg.getBuckets();
                if (!CollectionUtils.isEmpty(logoBuckets)) {
                    Terms.Bucket logoBucket = logoBuckets.get(0);
                    if (logoBucket != null) {
                        // 解析出子聚合中的name
                        brandEntity.setLogo(logoBucket.getKeyAsString());
                    }
                }
            }
            return brandEntity;
        }).collect(Collectors.toList());
        searchResponseVo.setBrands(brandEntities);
        // 二、解析--分类--聚合结果集
        ParsedLongTerms categoryIdAgg = (ParsedLongTerms) aggregationMap.get("categoryIdAgg");
        List<? extends Terms.Bucket> categoryIdBuckets = categoryIdAgg.getBuckets();
        List<CategoryEntity> categoryEntities = categoryIdBuckets.stream().map(bucket -> {
            CategoryEntity categoryEntity = new CategoryEntity();
            // 解析category的id
            categoryEntity.setId(bucket.getKeyAsNumber().longValue());
            // 获取分类子聚合
            ParsedStringTerms categoryNameAgg = bucket.getAggregations().get("categoryNameAgg");
            if (categoryNameAgg != null) {
                List<? extends Terms.Bucket> nameAggBuckets = categoryNameAgg.getBuckets();
                if (!CollectionUtils.isEmpty(nameAggBuckets)) {
                    categoryEntity.setName(nameAggBuckets.get(0).getKeyAsString());
                }
            }
            return categoryEntity;
        }).collect(Collectors.toList());
        searchResponseVo.setCategories(categoryEntities);
        // 三、解析--规格参数--聚合结果集
        ParsedNested attrAgg = (ParsedNested) aggregationMap.get("attrAgg");
        ParsedLongTerms attrIdAdd = attrAgg.getAggregations().get("attrIdAgg");
        if (attrIdAdd != null) {
            List<? extends Terms.Bucket> attrIdAggBuckets = attrIdAdd.getBuckets();
            if (!CollectionUtils.isEmpty(attrIdAggBuckets)) {
                List<SearchResponseAttrVo> attrs = attrIdAggBuckets.stream().map(bucket -> {
                    SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();
                    // 解析桶中的key，获取规格参数的id
                    searchResponseAttrVo.setAttrId(bucket.getKeyAsNumber().longValue());
                    // 获取该参数所有的子聚合
                    Map<String, Aggregation> subAggregationMap = bucket.getAggregations().asMap();
                    // 获取规格参数名的子聚合
                    ParsedStringTerms attrNameAgg = (ParsedStringTerms) subAggregationMap.get("attrNameAgg");
                    if (attrNameAgg != null) {
                        List<? extends Terms.Bucket> attrNameAggBuckets = attrNameAgg.getBuckets();
                        if (!CollectionUtils.isEmpty(attrNameAggBuckets)) {
                            Terms.Bucket nameBucket = attrNameAggBuckets.get(0);
                            if (nameBucket != null) {
                                searchResponseAttrVo.setAttrName(nameBucket.getKeyAsString());
                            }
                        }
                    }
                    // 获取规格参数值的子聚合
                    ParsedStringTerms attrValueAgg = (ParsedStringTerms) subAggregationMap.get("attrValueAgg");
                    if (attrValueAgg != null) {
                        List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();
                        if (!CollectionUtils.isEmpty(attrValueAggBuckets)) {
                            searchResponseAttrVo.setAttrValues(attrValueAggBuckets
                                    .stream()
                                    .map(Terms.Bucket::getKeyAsString)
                                    .collect(Collectors.toList())
                            );
                        }
                    }
                    return searchResponseAttrVo;
                }).collect(Collectors.toList());
                searchResponseVo.setFilters(attrs);
            }
        }
        return searchResponseVo;
    }

    /**
     * 构建查询条件（DSL语句）
     *
     * @param searchParamVo 搜索参数对象
     * @return 封装有查询条件的SearchSourceBuilder
     */
    private SearchSourceBuilder getSearchSourceBuilder(SearchParamVo searchParamVo) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 1.构建关键字查询
        String keyword = searchParamVo.getKeyword();
        if (StringUtils.isEmpty(keyword))
            // 展示推荐商品
            // TODO: 2020/8/3  打广告
            return null;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        searchSourceBuilder.query(boolQueryBuilder);
        boolQueryBuilder.must(QueryBuilders.matchQuery("title", keyword).operator(Operator.AND));
        // 2.构建条件过滤
        // 2.1品牌过滤
        List<Long> brandIds = searchParamVo.getBrandId();
        if (!CollectionUtils.isEmpty(brandIds)) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", brandIds));
        }
        // 2.2分类过滤
        List<Long> cid = searchParamVo.getCid();
        if (!CollectionUtils.isEmpty(cid)) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("categoryId", cid));
        }
        // 2.3价格区间过滤
        Double priceFrom = searchParamVo.getPriceFrom();
        Double priceTo = searchParamVo.getPriceTo();
        if (priceFrom != null || priceTo != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            if (priceFrom != null)
                rangeQuery.gte(priceFrom);
            if (priceTo != null)
                rangeQuery.lte(priceTo);
            boolQueryBuilder.filter(rangeQuery);
        }
        // 2.4库存过滤
        Boolean stock = searchParamVo.getStock();
        if (stock != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("stock", stock));
        }
        // 2.5规格参数过滤：props=8:8G-12G&props=9:128G-256G ===>>> "8:8G-12G","9:128G-256G"
        List<String> props = searchParamVo.getProps();
        if (!CollectionUtils.isEmpty(props)) {
            props.forEach(prop -> {
                // 以“:”为分割，从“8:8G-12G”中获取规格参数的id 8以及值8G-12G
                String[] attrs = StringUtils.split(prop, ":");
                if (attrs != null && attrs.length == 2) {
                    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                    boolQuery.must(QueryBuilders.termQuery("searchAttrs.attrId", attrs[0]));
                    // 以“-”为分割，从“8G-12G”中获取8G和12G
                    String[] attrValues = StringUtils.split(attrs[1], "-");
                    if (attrValues != null && attrValues.length > 0)
                        boolQuery.must(QueryBuilders.termsQuery("searchAttrs.attrId", attrValues));
                    boolQueryBuilder.filter(QueryBuilders.nestedQuery("searchAttrs", boolQuery, ScoreMode.None));
                }
            });
        }
        // System.out.println(searchSourceBuilder.toString());
        // 3.排序
        Integer sort = searchParamVo.getSort();
        if (sort != null) {
            // 1：价格升序；2：价格降序；3：销量降序；4：新品降序
            switch (sort) {
                case 1:
                    searchSourceBuilder.sort("price", SortOrder.ASC);
                    break;
                case 2:
                    searchSourceBuilder.sort("price", SortOrder.DESC);
                    break;
                case 3:
                    searchSourceBuilder.sort("sales", SortOrder.DESC);
                    break;
                case 4:
                    searchSourceBuilder.sort("createTime", SortOrder.DESC);
                    break;
            }
        }
        // 4.分页
        Integer pageSize = searchParamVo.getPageSize();
        searchSourceBuilder.from((searchParamVo.getPageNum() - 1) * pageSize);
        searchSourceBuilder.size(pageSize);
        // 5.高亮
        searchSourceBuilder.highlighter(new HighlightBuilder()
                .field("title")
                .preTags("<font style='color:red'>")
                .postTags("</font>"));
        // 6.聚合
        // 6.1品牌聚合
        searchSourceBuilder.aggregation(AggregationBuilders.terms("brandIdAgg").field("brandId")
                .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName"))
                .subAggregation(AggregationBuilders.terms("brandLogoAgg").field("logo"))
        );
        // 6.2分类聚合
        searchSourceBuilder.aggregation(AggregationBuilders.terms("categoryIdAgg").field("categoryId")
                .subAggregation(AggregationBuilders.terms("categoryNameAgg").field("categoryName"))
        );
        // 6.3规格参数聚合
        searchSourceBuilder.aggregation(AggregationBuilders.nested("attrAgg", "searchAttrs")
                .subAggregation(AggregationBuilders.terms("attrIdAgg").field("searchAttrs.attrId")
                        .subAggregation(AggregationBuilders.terms("attrNameAgg").field("searchAttrs.attrName"))
                        .subAggregation(AggregationBuilders.terms("attrValueAgg").field("searchAttrs.attrValue"))
                )
        );
        // 7.指定包含的字段
        searchSourceBuilder.fetchSource(new String[]{"skuId", "title", "price", "image", "subTitle"}, null);
        // System.out.println(searchSourceBuilder.toString()); // 测试DSL语句
        return searchSourceBuilder;
    }
    // ------------------------ 响应查询结果 【结束】 ------------------------

    @Override
    public void createIndex(Long spuId) {
        List<SkuEntity> skuEntities = gmallPmsClient.querySkuBySpuId(spuId).getData();
        if (!CollectionUtils.isEmpty(skuEntities)) {
            List<Goods> goodsList = skuEntities.stream().map(skuEntity -> {
                Goods goods = new Goods();
                Long skuId = skuEntity.getId();
                // sku相关属性值设置
                goods.setSkuId(skuId);
                goods.setImage(skuEntity.getDefaultImage());
                goods.setPrice(skuEntity.getPrice().doubleValue());
                goods.setTitle(skuEntity.getTitle());
                goods.setSubTitle(skuEntity.getSubtitle());
                // spu相关属性设置
                SpuEntity spuEntity = gmallPmsClient.querySpuById(spuId).getData();
                goods.setCreateTime(spuEntity.getCreateTime());
                // brand相关属性设置
                Long brandId = skuEntity.getBrandId();
                goods.setBrandId(brandId);
                BrandEntity brandEntity = gmallPmsClient.queryBrandById(brandId).getData();
                if (brandEntity != null) {
                    goods.setBrandName(brandEntity.getName());
                    goods.setLogo(brandEntity.getLogo());
                }
                // wms库存相关属性设置
                List<WareSkuEntity> wareSkuEntities = gmallWmsClient.queryWareSkuBySkuId(skuId).getData();
                if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                    goods.setStock(wareSkuEntities
                            .stream()
                            .anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
                    goods.setSales(wareSkuEntities
                            .stream()
                            .map(WareSkuEntity::getSales)
                            .reduce(Long::sum)
                            .get());
                }
                // category相关属性设置
                Long categoryId = skuEntity.getCatagoryId();
                CategoryEntity categoryEntity = gmallPmsClient.queryCategoryById(categoryId).getData();
                if (categoryEntity != null) {
                    goods.setCategoryId(categoryId);
                    goods.setCategoryName(categoryEntity.getName());
                }
                // searchAttrs相关属性设置
                List<SearchAttrValue> searchAttrValues = new ArrayList<>();
                List<SpuAttrValueEntity> spuAttrValueEntities = gmallPmsClient.querySearchAttrValueBySpuId(spuId).getData();
                if (!CollectionUtils.isEmpty(spuAttrValueEntities)) {
                    searchAttrValues.addAll(spuAttrValueEntities
                            .stream()
                            .map(spuAttrValueEntity -> {
                                SearchAttrValue searchAttrValue = new SearchAttrValue();
                                searchAttrValue.setAttrId(spuAttrValueEntity.getAttrId());
                                searchAttrValue.setAttrName(spuAttrValueEntity.getAttrName());
                                searchAttrValue.setAttrValue(spuAttrValueEntity.getAttrValue());
                                return searchAttrValue;
                            }).collect(Collectors.toList()));
                }
                List<SkuAttrValueEntity> skuAttrValueEntities = gmallPmsClient.querySearchAttrValueBySkuId(skuId).getData();
                if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
                    searchAttrValues.addAll(skuAttrValueEntities
                            .stream()
                            .map(skuAttrValueEntity -> {
                                SearchAttrValue searchAttrValue = new SearchAttrValue();
                                searchAttrValue.setAttrId(skuAttrValueEntity.getAttrId());
                                searchAttrValue.setAttrName(skuAttrValueEntity.getAttrName());
                                searchAttrValue.setAttrValue(skuAttrValueEntity.getAttrValue());
                                return searchAttrValue;
                            }).collect(Collectors.toList()));
                }
                goods.setSearchAttrs(searchAttrValues);
                return goods;
            }).collect(Collectors.toList());
            goodsRepository.saveAll(goodsList);
        }
    }
}
