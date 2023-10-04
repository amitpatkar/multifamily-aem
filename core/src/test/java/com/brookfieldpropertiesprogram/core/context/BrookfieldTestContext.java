/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.context;

/**
 *
 * @author patkara
 */
import com.brookfieldpropertiesprogram.core.services.EnvironmentSetting;
import com.brookfieldpropertiesprogram.core.services.impl.EnvironmentSettingImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import java.util.HashMap;
import java.util.Map;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Provides a context for unit tests.
 */
public final class BrookfieldTestContext {

    public static final String TEST_CONTENT_JSON = "/test-content.json";
    public static final String TEST_TAGS_JSON = "/test-tags.json";
    public static final String TEST_CONTENT_DAM_JSON = "/test-content-dam.json";
    public static final String TEST_APPS_JSON = "/test-apps.json";
    public static final String TEST_CONF_JSON = "/test-conf.json";
    public static final String TEST_CONFIG_P_EXPIRED_SPECIALS = "/config-properties-the-eugene-expiredSpecials.json"; //city

    public static final String HOME_P = "/content/portfolio/en/home";
    public static final String HOME_S = "/content/standalone/the-eugene/home";

    public static final String HOME_P_N = "/content/portfolio/en/neighborhoods/reston";
    public static final String HOME_P_C = "/content/portfolio/en/cities/oakland";
    public static final String HOME_P_M = "/content/portfolio/en/metroareas/chicago-metro-area";
    public static final String HOME_P_P = "/content/portfolio/en/properties/the-eugene";
    public static final String HOME_P_S = "/content/portfolio/en/states/ca";
    public static final String HOME_P_CL = "/content/portfolio/en/collections/mercantile-place";

    public static final String PROPERTY_TYPE_S = "/content/standalone/the-eugene/examples/apartment-type";
    public static final String PROPERTY_DETAIL_S = "/content/standalone/the-eugene/examples/apartment";

    public static final String CONFIG_S_S = "/content/siteconfig/sites/standalone/the-eugene"; //standalone site config
    public static final String CONFIG_S_P = "/content/siteconfig/sites/portfolio/master"; //portfolio site config

    public static final String CONFIG_C = "/content/siteconfig/cities/oakland"; //city
    public static final String CONFIG_STATE = "/content/siteconfig/states/ca"; //city
    public static final String CONFIG_N = "/content/siteconfig/neighborhoods/reston"; //city
    public static final String CONFIG_P = "/content/siteconfig/properties/the-eugene"; //city    
    public static final String CONFIG_M = "/content/siteconfig/metroareas/chicago-metro-area"; //city

    public static final String CONFIG_LINK_TYPES = "/content/siteconfig/link-types"; //city

    public static final String CONFIG_CL = "/content/siteconfig/collections/the-test-collection"; //city

    public static final String POLICY_PAGE_GENERIC = "/conf/common/settings/wcm/policies/brookfieldpropertiesprogram"; //city

    public static final String CMP_NAVIGATION_STANDALONE = "/content/experience-fragments/standalone/en/site/header/master/jcr:content/root/navigation_standalon"; //city

    public static final String CMP_FOOTER_STANDALONE = "/content/experience-fragments/standalone/en/site/footer/master/jcr:content/root/footer_standalone"; //city

    private BrookfieldTestContext() {
        // only static methods
        TestLoggerFactory.getInstance().setPrintLevel(Level.TRACE);
    }

    public static AemContext newAemContext() {
        AemContext aemContext = new AemContextBuilder()
                .plugin(CACONFIG)
                .resourceResolverType(ResourceResolverType.JCR_MOCK)
                .<AemContext>afterSetUp(context -> {
                    context.addModelsForPackage("com.brookfieldpropertiesprogram.core.models");
                }
                )
                .build();

        aemContext.load().json("/policy-page.json", POLICY_PAGE_GENERIC);

        //common configurations
        //aemContext.load().json("/config-neighborhoods-reston.json", "/content/siteconfig/neighborhoods/reston");     
        //aemContext.load().json("/config-metroareas-chicago-metro-area.json", "/content/siteconfig/metroareas/chicago-metro-area");            
        //aemContext.load().json("/config-cities-oakland.json", "/content/siteconfig/cities/oakland");
        //aemContext.load().json("/config-properties-the-eugene.json", "/content/siteconfig/properties/the-eugene");
        //aemContext.load().json("/config-collections-the-test-collection.json", "/content/siteconfig/collections/the-test-collection");
        //aemContext.load().json("/xf-portfolio-states.json", "/content/siteconfig/states");
        //standalone
        //aemContext.load().json("/siteconfig-the-eugene.json", "/content/siteconfig/sites/standalone/the-eugene");        
        //aemContext.load().json("/standalone-the-eugene-home.json", "/content/standalone/the-eugene/home");        
        aemContext.load().json("/standalone-the-eugene-hirarchy.json", "/content/standalone/the-eugene");

        aemContext.load().json("/standalone-demo-apartment-availability.json", "/content/standalone/demo/apartment-availability");

        //portfolio
        //aemContext.load().json("/siteconfig-portfolio.json", "/content/siteconfig/sites/portfolio/master");
        aemContext.load().json("/portfolio-en.json", "/content/portfolio/en");

        aemContext.load().json("/standalone-property-detail.json", PROPERTY_DETAIL_S);
        aemContext.load().json("/standalone-property-type.json", PROPERTY_TYPE_S);

        aemContext.load().json("/siteconfig.json", "/content/siteconfig");

        aemContext.load().json("/experience-fragments.json", "/content/experience-fragments");
        /*
        aemContext.load().json("/portfolio-home.json", "/content/portfolio/en/home");
        aemContext.load().json("/portfolio-cities-oakland.json", "/content/portfolio/en/cities/oakland");
        aemContext.load().json("/portfolio-metroareas-chicago-metro-area.json", "/content/portfolio/en/metroareas/chicago-metro-area");
        aemContext.load().json("/portfolio-neighborhood-reston.json", "/content/portfolio/en/neighborhoods/reston");
        aemContext.load().json("/portfolio-properties-the-eugene.json", "/content/portfolio/en/properties/the-eugene");
         */

        ///content/portfolio/en/home
        //component models
        aemContext.load().json("/navigationcomponentmodel.json", "/content/navigationcomponentmodel");
        //aemContext.load().json("/menu_links_left_right.json", "/content/experience-fragments/standalone/en/site/header/master/jcr:content/root/navigation_standalon");     

        aemContext.load().json("/model-multi-line.json", "/content/multi_model");

        aemContext.load().json("/cmp-section_50_50_carousel.json", "/content/cmp_section_50_50_carousel");

        EnvironmentSetting envSettingMockTrue;
        Map<String, Object> propsEnvSettingMockTrue = new HashMap<>();
        propsEnvSettingMockTrue.put("isPublishServer", true);
        envSettingMockTrue = new EnvironmentSettingImpl();
        aemContext.registerInjectActivateService(envSettingMockTrue, propsEnvSettingMockTrue);

        //aemContext.load().json("/footer_standalone.json", CMP_FOOTER_STANDALONE); 
        return aemContext;
    }
}
