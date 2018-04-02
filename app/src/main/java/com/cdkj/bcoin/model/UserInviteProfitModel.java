package com.cdkj.bcoin.model;

/**
 * Created by lei on 2018/3/14.
 */

public class UserInviteProfitModel {


    /**
     * coin : {"symbol":"BTC","ename":"bitcoin","cname":"比特币","type":"0","unit":8,"icon":"比特币@3x_1521170843181.png","pic1":"比特币图标@3x_1521170851717.png","pic2":"充值@3x_1521170848604.png","pic3":"提现@3x_1521170854543.png","orderNo":1,"status":"0"}
     * inviteProfit : 0
     */

    private CoinBean coin;
    private String inviteProfit;

    public CoinBean getCoin() {
        return coin;
    }

    public void setCoin(CoinBean coin) {
        this.coin = coin;
    }

    public String getInviteProfit() {
        return inviteProfit;
    }

    public void setInviteProfit(String inviteProfit) {
        this.inviteProfit = inviteProfit;
    }

    public static class CoinBean {
        /**
         * symbol : BTC
         * ename : bitcoin
         * cname : 比特币
         * type : 0
         * unit : 8
         * icon : 比特币@3x_1521170843181.png
         * pic1 : 比特币图标@3x_1521170851717.png
         * pic2 : 充值@3x_1521170848604.png
         * pic3 : 提现@3x_1521170854543.png
         * orderNo : 1
         * status : 0
         */

        private String symbol;
        private String ename;
        private String cname;
        private String type;
        private int unit;
        private String icon;
        private String pic1;
        private String pic2;
        private String pic3;
        private int orderNo;
        private String status;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPic1() {
            return pic1;
        }

        public void setPic1(String pic1) {
            this.pic1 = pic1;
        }

        public String getPic2() {
            return pic2;
        }

        public void setPic2(String pic2) {
            this.pic2 = pic2;
        }

        public String getPic3() {
            return pic3;
        }

        public void setPic3(String pic3) {
            this.pic3 = pic3;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
