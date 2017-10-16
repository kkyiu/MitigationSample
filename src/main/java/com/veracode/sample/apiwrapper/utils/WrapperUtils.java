package com.veracode.sample.apiwrapper.utils;

import com.veracode.apiwrapper.wrappers.MitigationAPIWrapper;
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper;
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper;
import com.veracode.util.lang.StringUtility;

/**
 * An utility class that provides methods to work with API Wrappers
 */
public class WrapperUtils {
    private static WrapperFactory wrapperFactory = null;

    /**
     * This method must be called before using other methods in this class to
     * initialize the API credentials
     * 
     * @param apiID - API ID
     * @param apiKey - API Key
     */
    public static final void initApiCredentials(String apiID, String apiKey) {
        if ((StringUtility.isNullOrEmpty(apiID) || !apiID.matches("[a-zA-Z0-9]{32,32}")) ||
            (StringUtility.isNullOrEmpty(apiKey) || !apiKey.matches("[a-zA-Z0-9]{128,128}"))) {
            throw new RuntimeException("The API credential is invalid.");
        }

        wrapperFactory = WrapperFactory.newInstance(apiID, apiKey);
    }

    /**
     * Get Detailed Report of the given build
     * 
     * @param buildId - Build ID
     * @return XML of the detailed report of the build
     * @throws Exception
     */
    public static final String getDetailedReport(String buildId) throws Exception {
        if (null == wrapperFactory) {
            throw new RuntimeException("The API credential is not initialized.");
        }

        if (StringUtility.isNullOrEmpty(buildId)) {
            throw new IllegalArgumentException("Build ID is invalid.");
        }

        ResultsAPIWrapper resultsApiWrapper = wrapperFactory.createWrapper(ResultsAPIWrapper.class);
        String detailedReportXml = resultsApiWrapper.detailedReport(buildId); 
        String errMsg = XmlUtils.getErrorString(detailedReportXml);
        if (!StringUtility.isNullOrEmpty(errMsg)) {
            throw new RuntimeException(errMsg);
        }
        return detailedReportXml;
    }

    /**
     * Get the latest build information of the given application
     * 
     * @param appName - Application name
     * @return XML of the build info of the latest build
     * @throws Exception
     */
    public static final String getBuildInfo(String appName) throws Exception {
        if (null == wrapperFactory) {
            throw new RuntimeException("The API credential is not initialized.");
        }

        if (StringUtility.isNullOrEmpty(appName)) {
            throw new IllegalArgumentException("Application name is invalid.");
        }

        UploadAPIWrapper uploadApiWrapper = wrapperFactory.createWrapper(UploadAPIWrapper.class);
        String appListXML = uploadApiWrapper.getAppList();
        String errMsg = XmlUtils.getErrorString(appListXML);
        if (!errMsg.isEmpty()) {
            throw new RuntimeException(errMsg);
        }

        String appId = XmlUtils.parseAppId(appName, appListXML);
        if (StringUtility.isNullOrEmpty(appId)) {
            throw new RuntimeException(String.format("Cannot find the ID for application %s", appName));
        }

        String buildInfoXml = uploadApiWrapper.getBuildInfo(appId, null, null);
        errMsg = XmlUtils.getErrorString(buildInfoXml);
        if (!StringUtility.isNullOrEmpty(errMsg)) {
            throw new RuntimeException(errMsg);
        }

        return buildInfoXml;
    }

    /**
     * Get the mitigation info of the flaws in the given build
     * 
     * @param buildId - Build ID
     * @param flawIdsCSV - Flaws IDs (as a csv string) in the given build
     * @return XML of the mitigation info
     * @throws Exception
     */
    public static final String getMitigationInfo(String buildId, String flawIdsCSV) throws Exception {
        if (null == wrapperFactory) {
            throw new RuntimeException("The API credential is not initialized.");
        }

        MitigationAPIWrapper mitigateApiWrapper = wrapperFactory.createWrapper(MitigationAPIWrapper.class);
        String mitigationInfoXml = mitigateApiWrapper.getMitigationInfo(buildId, flawIdsCSV);
        String errMsg = XmlUtils.getErrorString(mitigationInfoXml);
        if (!StringUtility.isNullOrEmpty(errMsg)) {
            throw new RuntimeException(errMsg);
        }
        return mitigationInfoXml;
    }
}
