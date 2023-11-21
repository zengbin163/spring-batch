package com.zengbin.batch.service.reader;

import cn.hutool.core.collection.CollectionUtil;
import com.zengbin.batch.mapper.reader.FoodRMapper;
import com.zengbin.batch.response.QueryResponse;
import com.zengbin.batch.vo.reader.FoodR;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodReaderService {

    @Autowired
    private FoodRMapper foodRMapper;

    @Autowired
    @Qualifier("readerSqlSessionTemplate")
    private SqlSessionTemplate readerSqlSessionTemplate;

    public void insertBatch(List<FoodR> readerList) {
        if(CollectionUtil.isEmpty(readerList)) {
            return;
        }
        SqlSession sqlSession = readerSqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        FoodRMapper mapper = sqlSession.getMapper(FoodRMapper.class);
        try {
            for (FoodR foodR : readerList) {
                mapper.insert(foodR);
            }
            sqlSession.commit();
            sqlSession.clearCache();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    public QueryResponse<FoodR> findFoodListByOrder(int pageSize, Long beginUid) {
        if(0 == pageSize) {
            pageSize = 10;
        }
        List<FoodR> list = this.foodRMapper.findFoodListByOrder((pageSize + 1), beginUid);
        QueryResponse<FoodR> response = null;
        boolean hasNext = false;
        Long nextId = 0L;
        if(!CollectionUtil.isEmpty(list)) {
            if(list.size() > pageSize) {
                hasNext = true;
                nextId = list.get(list.size()-1).getUid();
                list.remove(list.size()-1);
                response = new QueryResponse<>(list, hasNext, nextId);
                return response;
            } else {
                hasNext = false;
                nextId = list.get(list.size()-1).getUid();
                response = new QueryResponse<>(list, hasNext, nextId);
                return response;
            }
        } else {
            response = new QueryResponse<>(null, hasNext, nextId);
            return response;
        }
    }

}
