package com.framework.steps;

import com.framework.utils.Base;
import com.framework.utils.TestUtils;
import io.cucumber.java.Before;

import java.io.InputStream;
import java.util.Properties;

public class Hooks extends Base {
    InputStream stringsIs;

    @Before
    public void beforeTest() throws Exception {
        //feeding globalProps object from app.properties
        isGlobalProps = getGlobalPropertiesAsStream();
        globalProps = new Properties();
        globalProps.load(isGlobalProps);

        //feeding strings hashMap from strings.xml
        String stringsXmlFileName = "strings/strings.xml";
        stringsIs = getClass().getClassLoader().getResourceAsStream(stringsXmlFileName);
        TestUtils utils = new TestUtils();
        strings = utils.parseStringXML(stringsIs);
    }
}
