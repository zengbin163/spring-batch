package com.zengbin.batch.service.writer;

import cn.hutool.core.collection.CollectionUtil;
import com.zengbin.batch.mapper.writer.FoodWMapper;
import com.zengbin.batch.vo.writer.FoodW;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodWriterService {

    @Autowired
    @Qualifier("writerSqlSessionTemplate")
    private SqlSessionTemplate writerSqlSessionTemplate;

    public void insertBatch(List<FoodW> writerList) {
        if(CollectionUtil.isEmpty(writerList)) {
            return;
        }
        SqlSession sqlSession = writerSqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        FoodWMapper mapper = sqlSession.getMapper(FoodWMapper.class);
        try {
            for (FoodW foodW : writerList) {
                mapper.insert(foodW);
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

}
