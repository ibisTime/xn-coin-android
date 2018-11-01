package com.cdkj.bcoin.model;

import java.util.List;

/**
 * @updateDts 2018/11/1
 */

public class ZfqrImage {

    private List<ZfbQrListBean> zfbQrList;

    public List<ZfbQrListBean> getZfbQrList() {
        return zfbQrList;
    }

    public void setZfbQrList(List<ZfbQrListBean> zfbQrList) {
        this.zfbQrList = zfbQrList;
    }

    public static class ZfbQrListBean {
        /**
         * amount : 0
         * zfbQrUrl : ANDROID_1541075422092_3456_4608.jpg
         */

        private String amount;
        private String zfbQrUrl;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getZfbQrUrl() {
            return zfbQrUrl;
        }

        public void setZfbQrUrl(String zfbQrUrl) {
            this.zfbQrUrl = zfbQrUrl;
        }
    }
}
