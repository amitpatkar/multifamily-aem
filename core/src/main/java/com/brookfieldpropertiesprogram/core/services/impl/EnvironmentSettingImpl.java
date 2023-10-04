/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.EnvironmentSetting;
import com.brookfieldpropertiesprogram.core.services.EnvironmentSettingImplConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

/**
 * @author amitpatkar
 */
@Component(service = EnvironmentSetting.class,configurationPolicy = ConfigurationPolicy.OPTIONAL,immediate = true)
@Designate(ocd = EnvironmentSettingImplConfig.class)
public class EnvironmentSettingImpl implements EnvironmentSetting {
    EnvironmentSettingImplConfig config;
    
    @Activate
    @Modified
    protected void activate(EnvironmentSettingImplConfig config) {
        this.config = config;
    }

    @Override
    public boolean isPublishServer() {
        return (config != null ? config.isPublishServer() : false);
    }
   
}
