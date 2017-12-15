package com.cdkj.ethereumdemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lei on 2017/10/31.
 */

public class OrderModel implements Serializable {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 10
     * totalCount : 2
     * totalPage : 1
     * list : [{"code":"JY201711171716012986371","type":"buy","adsCode":"ADSS201711161118209328091","buyUser":"U201711121556304486170","sellUser":"U201711102114095446687","leaveMessage":"哈哈哈哈","tradeCurrency":"CNY","tradeCoin":"ETH","tradePrice":2169.077,"tradeAmount":1,"fee":0,"count":0,"payType":"0","invalidDatetime":"Nov 17, 2017 5:26:01 PM","status":"0","createDatetime":"Nov 17, 2017 5:16:01 PM","updater":"U201711121556304486170","updateDatatime":"Nov 17, 2017 5:16:01 PM","remark":"新订单，等待支付","buyUserInfo":{"userId":"U201711121556304486170","loginName":"18984955240","mobile":"18984955240","photo":"ANDROID_1510823786678_580_580.jpg","nickname":"Lei","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 12, 2017 3:56:30 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false},"sellUserInfo":{"userId":"U201711102114095446687","loginName":"15268501482","mobile":"15268501482","nickname":"111","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 10, 2017 9:14:09 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}},{"code":"JY201711171747356586181","type":"buy","adsCode":"ADSS201711161118209328091","buyUser":"U201711121556304486170","sellUser":"U201711102114095446687","leaveMessage":"哈哈哈哈","tradeCurrency":"CNY","tradeCoin":"ETH","tradePrice":2164.702,"tradeAmount":1,"fee":0,"count":0,"payType":"0","invalidDatetime":"Nov 17, 2017 5:57:35 PM","status":"0","createDatetime":"Nov 17, 2017 5:47:35 PM","updater":"U201711121556304486170","updateDatatime":"Nov 17, 2017 5:47:35 PM","remark":"新订单，等待支付","buyUserInfo":{"userId":"U201711121556304486170","loginName":"18984955240","mobile":"18984955240","photo":"ANDROID_1510823786678_580_580.jpg","nickname":"Lei","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 12, 2017 3:56:30 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false},"sellUserInfo":{"userId":"U201711102114095446687","loginName":"15268501482","mobile":"15268501482","nickname":"111","loginPwdStrength":"1","kind":"C","level":"1","status":"0","createDatetime":"Nov 10, 2017 9:14:09 PM","companyCode":"CD-COIN000017","systemCode":"CD-COIN000017","tradepwdFlag":false}}]
     */

    private int pageNO;
    private int start;
    private int pageSize;
    private int totalCount;
    private int totalPage;
    private List<OrderDetailModel> list;

    public int getPageNO() {
        return pageNO;
    }

    public void setPageNO(int pageNO) {
        this.pageNO = pageNO;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<OrderDetailModel> getList() {
        return list;
    }

    public void setList(List<OrderDetailModel> list) {
        this.list = list;
    }

}
