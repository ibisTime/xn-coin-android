package com.cdkj.baseim.interfaces;

/**
 * Created by lq on 2017/11/27.
 */

public interface TxImLoginInterface {

    void keyRequestOnNoNet(String msg);

    void onError(int i, String s);

    void onSuccess();

}
