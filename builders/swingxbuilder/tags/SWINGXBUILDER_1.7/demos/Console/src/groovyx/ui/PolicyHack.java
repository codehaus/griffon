package groovyx.ui;

import java.security.*;

/**
 * Created by IntelliJ IDEA.
 * User: Danno
 * Date: Nov 2, 2007
 * Time: 3:13:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PolicyHack {

    public static void setPolicy() {
        Policy.setPolicy(new Policy() {
            public PermissionCollection getPermissions(CodeSource
                codesource) {
                Permissions perms = new Permissions();
                perms.add(new AllPermission());
                return (perms);
            }

            public void refresh() {
            }
        });
    }

}
