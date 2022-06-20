package ink.rayin.app.web.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Created by tangyongmao on 2019-6-4 14:35:44.
 */
public class CustomObjectInputStream extends ObjectInputStream {
    ClassLoader customLoader;

    public CustomObjectInputStream(InputStream in, ClassLoader loader ) throws IOException, SecurityException {
        super( in );
        customLoader= loader;
    }

    protected Class resolveClass( ObjectStreamClass v ) throws IOException, ClassNotFoundException {
        if ( customLoader == null )
            return super.resolveClass( v );
        else
            return Class.forName( v.getName(), true, customLoader);
    }
}
