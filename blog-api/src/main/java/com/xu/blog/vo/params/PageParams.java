package com.xu.blog.vo.params;

import lombok.Data;

@Data
public class PageParams {
    //承担着返回页数和数量的类
    private int page = 1;

    private int pageSize =10;

    private Long categoryId;

    private Long tagId;

    private String year;

    private String month;

    public String getMonth(){
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }
        return this.month;
    }

}
