package com.cdkj.baselibrary.model;

/**用于获取七牛TOken
 * Created by 李先俊 on 2017/6/14.
 */

public class AliTokenModel {


    /**
     * AccessKeyId : STS.NHEqJSsQu7owFALovwTDTj8U8
     * AccessKeySecret : BTAQafcHCXTc6ti9NJgLNFAE7iKVQaezDdQUJo3YNTGZ
     * SecurityToken : CAISmwJ1q6Ft5B2yfSjIr4vwOvDnno5UgK2cRGf9i3YiWMt4hf3+2jz2IHpPf3lhBOEasvUznmBS7P8Ylrh+W4NIX0rNaY5t9ZlN9wqkbtIwbUINafhW5qe+EE2/VjTJvqaLEdibIfrZfvCyESem8gZ43br9cxi7QlWhKufnoJV7b9MRLGbaAD1dH4UUXEgAzvUXLnzML/2gHwf3i27LdipStxF7lHl05NbYoKiV4QGMi0bhmK1H5dazAOD9MZI0bMwuCInsgLcuLfqf6kMKtUgWrpURpbdf5DLKsuuaB1Rs+BicO4LWiIY1d1QpN/VrR/IV8KKsz6ch4PagnoD22gtLOvpOTyPcSYavzc3JAuq1McwjcrL2K+5jcT4xuQOfGoABBlmlT2sNZXDfvXN1DV54o5HkEKhJLqlRNI12Vhlh/aNWT9Jb665ArlOqxWeJAVgbL9rmIgmT45oqQ4/7YzL7j4U2cUtgMA3GsdaLAxID9q4ELjnUCB9uD0bz3jmR0tJrCyBoEb6Sui6tnJh/l2rd8Z13V+qE4WefjsdD7t0V6nI=
     * Expiration : 2018-10-31T12:29:28Z
     */

    private String AccessKeyId;
    private String AccessKeySecret;
    private String SecurityToken;
    private String Expiration;

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String AccessKeyId) {
        this.AccessKeyId = AccessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String AccessKeySecret) {
        this.AccessKeySecret = AccessKeySecret;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String SecurityToken) {
        this.SecurityToken = SecurityToken;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String Expiration) {
        this.Expiration = Expiration;
    }
}
