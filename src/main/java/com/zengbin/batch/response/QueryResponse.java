package com.zengbin.batch.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengbin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse<T> implements Serializable {

    //返回值
    private List<T> list;
    //是否有下一页
    private boolean hasNext;
    //下一页第一个ID
    private Long nextId;

}
