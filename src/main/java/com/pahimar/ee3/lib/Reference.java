package com.pahimar.ee3.lib;

import java.io.InputStream;
import java.util.Properties;

import com.google.common.base.Throwables;

/**
 * Equivalent-Exchange-3
 * 
 * Reference
 * 
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class Reference {
    
    static {
        
        Properties prop = new Properties();
        
        try {
            InputStream stream = Reference.class.getClassLoader().getResourceAsStream("version.properties");
            prop.load(stream);
            stream.close();
        }
        catch(Exception e) {
            Throwables.propagate(e); // just throw it...
        }
        
        VERSION_NUMBER = prop.getProperty("version") + " (build " +prop.getProperty("build_number") + ")";
    }

    // General Mod related constants
    public static final String MOD_ID = "EE3";
    public static final String MOD_NAME = "Equivalent Exchange 3";
    public static final String VERSION_NUMBER;
    public static final String CHANNEL_NAME = MOD_ID;
    public static final String DEPENDENCIES = "required-after:Forge@[9.10.1.849,)";
    public static final String FINGERPRINT = "@FINGERPRINT@";
    public static final int SECOND_IN_TICKS = 20;
    public static final int SHIFTED_ID_RANGE_CORRECTION = 256;
    public static final String SERVER_PROXY_CLASS = "com.pahimar.ee3.core.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "com.pahimar.ee3.core.proxy.ClientProxy";
    public static final int VERSION_CHECK_ATTEMPTS = 3;
}