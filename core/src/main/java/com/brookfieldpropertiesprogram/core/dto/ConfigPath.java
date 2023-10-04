/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.dto;

/**
 *
 * @author amitpatkar
 */
public class ConfigPath {
    String contentFragmentPath;
    
    String siteConfigPath;
    String siteConfigPathSecondary;
    String propertyConfigPath;
    String cityConfigPath;
    String neighborhoodConfigPath;
    String metroConfigPath;
    String collectionConfigPath;
    String stateConfigPath;
    
    boolean siteConfigPathValid;
    boolean propertyConfigPathValid;
    boolean cityConfigPathValid;
    boolean neighborhoodConfigPathValid;
    boolean metroConfigPathValid;
    boolean collectionConfigPathValid;
    boolean stateConfigPathValid;

    public String getSiteConfigPath() {
        return siteConfigPath;
    }

    public void setSiteConfigPath(String siteConfigPath) {
        this.siteConfigPath = siteConfigPath;
    }

    public String getSiteConfigPathSecondary() {
        return siteConfigPathSecondary;
    }

    public void setSiteConfigPathSecondary(String siteConfigPathSecondary) {
        this.siteConfigPathSecondary = siteConfigPathSecondary;
    }
    
    
    public String getPropertyConfigPath() {
        return propertyConfigPath;
    }

    public void setPropertyConfigPath(String propertyConfigPath) {
        this.propertyConfigPath = propertyConfigPath;
    }

    public String getCityConfigPath() {
        return cityConfigPath;
    }

    public void setCityConfigPath(String cityConfigPath) {
        this.cityConfigPath = cityConfigPath;
    }

    public String getNeighborhoodConfigPath() {
        return neighborhoodConfigPath;
    }

    public void setNeighborhoodConfigPath(String neighborhoodConfigPath) {
        this.neighborhoodConfigPath = neighborhoodConfigPath;
    }

    public String getMetroConfigPath() {
        return metroConfigPath;
    }

    public void setMetroConfigPath(String metroConfigPath) {
        this.metroConfigPath = metroConfigPath;
    }

    public String getCollectionConfigPath() {
        return collectionConfigPath;
    }

    public void setCollectionConfigPath(String collectionConfigPath) {
        this.collectionConfigPath = collectionConfigPath;
    }

    public String getStateConfigPath() {
        return stateConfigPath;
    }

    public void setStateConfigPath(String stateConfigPath) {
        this.stateConfigPath = stateConfigPath;
    }

    public boolean isSiteConfigPathValid() {
        return siteConfigPathValid;
    }

    public void setSiteConfigPathValid(boolean siteConfigPathValid) {
        this.siteConfigPathValid = siteConfigPathValid;
    }

    public boolean isPropertyConfigPathValid() {
        return propertyConfigPathValid;
    }

    public void setPropertyConfigPathValid(boolean propertyConfigPathValid) {
        this.propertyConfigPathValid = propertyConfigPathValid;
    }

    public boolean isCityConfigPathValid() {
        return cityConfigPathValid;
    }

    public void setCityConfigPathValid(boolean cityConfigPathValid) {
        this.cityConfigPathValid = cityConfigPathValid;
    }

    public boolean isNeighborhoodConfigPathValid() {
        return neighborhoodConfigPathValid;
    }

    public void setNeighborhoodConfigPathValid(boolean neighborhoodConfigPathValid) {
        this.neighborhoodConfigPathValid = neighborhoodConfigPathValid;
    }

    public boolean isMetroConfigPathValid() {
        return metroConfigPathValid;
    }

    public void setMetroConfigPathValid(boolean metroConfigPathValid) {
        this.metroConfigPathValid = metroConfigPathValid;
    }

    public boolean isCollectionConfigPathValid() {
        return collectionConfigPathValid;
    }

    public void setCollectionConfigPathValid(boolean collectionConfigPathValid) {
        this.collectionConfigPathValid = collectionConfigPathValid;
    }

    public boolean isStateConfigPathValid() {
        return stateConfigPathValid;
    }

    public void setStateConfigPathValid(boolean stateConfigPathValid) {
        this.stateConfigPathValid = stateConfigPathValid;
    }

    public String getContentFragmentPath() {
        return contentFragmentPath;
    }

    public void setContentFragmentPath(String contentFragmentPath) {
        this.contentFragmentPath = contentFragmentPath;
    }
    
    
    
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("siteConfigPath:").append(siteConfigPath).append(System.lineSeparator());
        sb.append("propertyConfigPath:").append(propertyConfigPath).append(System.lineSeparator());
        sb.append("cityConfigPath:").append(cityConfigPath).append(System.lineSeparator());
        sb.append("neighborhoodConfigPath:").append(neighborhoodConfigPath).append(System.lineSeparator());
        sb.append("metroConfigPath:").append(metroConfigPath).append(System.lineSeparator());
        sb.append("collectionConfigPath:").append(collectionConfigPath).append(System.lineSeparator());
        sb.append("stateConfigPath:").append(stateConfigPath).append(System.lineSeparator());
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
