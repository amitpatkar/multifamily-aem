/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.utils;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author amitpatkar
 */
@ExtendWith(AemContextExtension.class)
public class JsonLdUtilsTest {
    
    private final AemContext context = BrookfieldTestContext.newAemContext();
    private PageInfoRequestModel pageInfoRequestModel;

    /**
     * Test of getJsonLd method, of class JsonLdUtils.
     */
    @Test
    public void testGetJsonLd() {
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);
        Assertions.assertNotNull(pageInfoRequestModel);
        
        Map<String, Object> in = pageInfoRequestModel.getCombinedProps();
        JsonObject expResult = new Gson().fromJson("{\"googleAddress\":\"407 W 31st St, New York, NY 10001, USA\",\"zipCode\":\"94087\",\"specials\":\"<p>test</p>\\r\\n\",\"twitterURL\":\"test\",\"textIsRich\":\"true\",\"site.footer_cta_url\":\"/\",\"site.footer_cta_heading\":\"Features & Amenities\",\"phoneNumberFormatted\":\"(844) 396-6697\",\"metaDescription\":\"The Eugene is a spectacular high-rise residential tower at Hudson Yards, with expansive lifestyle and recreational amenities. Click to see available apartments.\",\"metaName\":\"The Eugene | Luxury Manhattan Apartments for Rent near Hudson Yards\",\"logo\":\"/content/dam/common/properties/the-eugune/logo.svg\",\"site.theme\":\"theme-1\",\"site.reportSuiteBaseName\":\"brookfieldpropertiesportfolio\",\"mapLink\":\"https://www.google.com/maps/search/?api=1&query=435+West+31st+Street%2CSunnyvale%2Cnull\",\"specialsExpiry\":\"2021-08-17T00:00:00.000-07:00\",\"site.getInTouch\":\"<div class=\\\"contact-info contact-info--address\\\">\\r\\n                <p>${address}</p>\\r\\n                <p>${city}, ${stateCode} ${zipCode}</p>\\r\\n            </div>\\r\\n            <div class=\\\"contact-info contact-info--direction\\\">\\r\\n                <img src=\\\"/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/icons/map_light.svg\\\" alt=\\\"\\\" aria-hidden=\\\"true\\\"/>\\r\\n                <a href=\\\"#\\\" >Get Directions</a>\\r\\n            </div>\\r\\n            <div class=\\\"contact-info contact-info--phone\\\"> \\r\\n                <img src=\\\"/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/icons/phone_light.svg\\\" alt=\\\"\\\" aria-hidden=\\\"true\\\"/>\\r\\n                <a href=\\\"tel:+01-877-245-1215\\\" >${phoneNumberFormatted}</a>\\r\\n            </div>\\r\\n            <div class=\\\"contact-info contact-info--chat\\\"> \\r\\n                <img src=\\\"/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/icons/chat_light.svg\\\" alt=\\\"\\\" aria-hidden=\\\"true\\\"/>\\r\\n                <a href=\\\"#\\\" >Chat Now</a>\\r\\n            </div>\",\"phoneNumber\":\"8443966697\",\"featuredImage\":\"/content/dam/common/properties/the-eugune/logo.svg\",\"specialsDisclaimer\":\"<p>test</p>\\r\\n\",\"name\":\"The Eugene\",\"activeVariation\":\"back_to_school\",\"scheduleTourURL\":\"test\",\"nodeName\":\"the-eugene\",\"city\":\"Sunnyvale\",\"description\":\"Located in Midtown West, Manhattan, residents of The Eugene have a haven in the middle of one of the most dynamic cities in the world. Each penthouse apartment boasts floor-to-ceiling views of New York’s iconic skyline. An NBA-size basketball court, climbing wall, yoga room, and game room are just a few of many ways to unwind without putting on a coat. There’s a library lounge ideal for distraction-free reading or working. There’s even a rooftop club sixty-two stories above the city complete with a sunroom, bar, and outdoor grilling stations. Pets get love, too, thanks to an on-site grooming station. And when residents want to explore, there are dining and shopping options, galore at nearby Hudson Yards. \",\"tourInfo\":\"<p>test</p>\\r\\n\",\"custServiceEmailAddress\":\"RS@TheEugeneNYC.com\",\"site.feature_disable_neighborhood\":\"false\",\"preferredLeaseTerm\":\"test\",\"site.footer_cta_openLinkInNewTab\":\"false\",\"site.feature_disable_amenities\":\"false\",\"facebookURL\":\"https://www.instagram.com/theeugenenyc/\",\"site.name\":\"The Eugene\",\"metaKeywords\":\"test\",\"websiteURL\":\"https://www.theeugenenyc.com\",\"lroPricing\":\"yes\",\"scheduleTourJavaScriptSnippet\":\"test\",\"theme\":\"theme-1\",\"selfGuidedTourURL\":\"test\",\"headline\":\"IT'S TIME TO DISCOVER THE EUGENE\",\"residentPortalURL\":\"http://theeugenenyc.activebuilding.com/\",\"address\":\"435 West 31st Street\",\"anyoneHomeEmailAddress\":\"amitpatkar@gmail.com\",\"affordableDisclaimer\":\"Test\",\"legalText\":\"&copy; 2021 The Eugene, All Rights Reserved.\",\"page.name\":\"Home\",\"page.nodeName\":\"home\",\"onesiteID\":\"4545111\",\"site.footer_cta_label\":\"Explore\",\"embedAnyoneHome\":\"<link rel=\\\"stylesheet\\\" type=\\\"text/css\\\" href=\\\"https://www.myshowing.com/css/schedule_a_tour_webform_popup.css\\\" media=\\\"all\\\">\\r\\n<div id=\\\"schedule-tour-modal\\\" class=\\\"schedule-tour-modal\\\">\\r\\n <div class=\\\"schedule-tour-modal-content\\\">\\r\\n<div class=\\\"schedule-tour-modal-body\\\">\\r\\n<div class=\\\"self_schedule_popup_close\\\"><span class=\\\"schedule_popup_close\\\">&times;</span></div>\\r\\n<div class='embed-container'><iframe id='ifrm' src='https://www.myshowing.com/Brookfield_Properties/Guild/scheduletourwidget/a0F0H00000d3cusUAA/' frameborder='0' style='border:0;' frameborder='0' ></iframe>\\r\\n</div></div></div></div>\\r\\n<script src=\\\"https://www.myshowing.com/js/properties/schedule_a_tour_webform_popup.js\\\"></script>\\r\\n\",\"site.nodeName\":\"the-eugene\",\"instagramURL\":\"https://www.instagram.com/theeugenenyc/\"}", JsonObject.class);
        JsonObject result = JsonLdUtils.getJsonLd(in);
        
        Assertions.assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of buildJsonLd method, of class JsonLdUtils.
     */
    @Test
    public void testBuildJsonLd() {
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);
        Assertions.assertNotNull(pageInfoRequestModel);
        
        Map<String, Object> in = null;
        JsonObject jsonObj = pageInfoRequestModel.getPropertyConfigModel().getJsonLdObject();
        JsonLdUtils.buildJsonLd(in, jsonObj);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
