package com.fiora.note2.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.util.ByteSource;

//public class CustomRealm extends AuthorizingRealm {
public class CustomRealm extends IniRealm {
    public CustomRealm (String resourcePath) {
        super(resourcePath);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        SimpleAccount account = getUserByUserName(username);
        if (account == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, (String) account.getCredentials(),
                "customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));
        return authenticationInfo;
    }

    private SimpleAccount getUserByUserName(String username) {
        return this.getUser(username);
    }

}
