package com.cdkj.bcoin.model;

/**
 * Created by lei on 2017/11/23.
 */

public class InviteModel {

    private int inviteCount;
    private String inviteProfitEth;
    private String inviteProfitSc;

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public String getInviteProfitEth() {
        return inviteProfitEth;
    }

    public void setInviteProfitEth(String inviteProfitEth) {
        this.inviteProfitEth = inviteProfitEth;
    }

    public String getInviteProfitSc() {
        return inviteProfitSc;
    }

    public void setInviteProfitSc(String inviteProfitSc) {
        this.inviteProfitSc = inviteProfitSc;
    }
}
