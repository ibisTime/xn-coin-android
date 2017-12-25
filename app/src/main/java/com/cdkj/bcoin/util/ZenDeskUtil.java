package com.cdkj.bcoin.util;

import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;

/**
 * Created by lei on 2017/12/22.
 */

public class ZenDeskUtil {

    /**
     * 设置ZenDesk的用户信息
     * @param name
     * @param email
     */
    public static void initZenDeskIdentity(String name, String email){
        // 设置ZenDesk的昵称
        Identity identity = new AnonymousIdentity.Builder()
                .withNameIdentifier(name)
                .withEmailIdentifier(email)
                .build();
        ZendeskConfig.INSTANCE.setIdentity(identity);

        // Init Zopim Visitor info
//        final VisitorInfo.Builder build = new VisitorInfo.Builder()
//                .email(email)
//                .name(name);
//        ZopimChat.setVisitorInfo(build.build());
    }

}
