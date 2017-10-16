package com.veracode.sample.apiwrapper;

import com.veracode.sample.apiwrapper.models.Flaw;
import com.veracode.sample.apiwrapper.utils.WrapperUtils;
import com.veracode.sample.apiwrapper.utils.XmlUtils;

import java.io.PrintStream;
import java.util.Set;
import java.util.stream.Collectors;

import com.veracode.util.lang.StringUtility;

public class MitigationSample {

    /**
     * Get mitigation info about the flaws found, if any, in the latest build of the given application using the
     * given API credentials.
     * 
     * @param apiID - API ID
     * @param apiKey - API Key
     * @param appName - Application name
     * @return XML from the getMitigationInfo.do web service API
     * @throws Exception
     */
    public static final String getMitigationInfo(String apiID, String apiKey, String appName) throws Exception {
        if (StringUtility.isNullOrEmpty(apiID) ||
            StringUtility.isNullOrEmpty(apiKey) ||
            StringUtility.isNullOrEmpty(appName)) {
            throw new IllegalArgumentException("API credentials or the application name is invalid.");
        }

        WrapperUtils.initApiCredentials(apiID, apiKey);
        String lastBuildId = XmlUtils.parseBuildId(WrapperUtils.getBuildInfo(appName));
        // TODO: Check the build status is "Results Ready" before getting the detailed report.
        //       The best way is to create a BuildInfo POJO that will contain both ID and
        //       status (as an Enum), and then change parseBuildId to return an instance of
        //       that POJO, instead of parsing pieces of info one at a time.
        Set<Flaw> flaws = XmlUtils.parseFlaws(WrapperUtils.getDetailedReport(lastBuildId));
        String result = "";
        if (null != flaws) {
            String flawIdsCSV = flaws.stream().map((f) -> f.getID()).collect(Collectors.joining(","));

            if (!StringUtility.isNullOrEmpty(flawIdsCSV)) {
                result = WrapperUtils.getMitigationInfo(lastBuildId, flawIdsCSV);
            }
        }
        return result;
    }

    public static void printUsage(PrintStream ps) {
        if (null != ps) {
            ps.println("Usage: <API ID> <API Key> <Application name>");
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            MitigationSample.printUsage(System.out);
            return;
        }

        String apiID = args[0];
        if (!apiID.matches("[a-zA-Z0-9]{32,32}")) {
            System.err.println("API ID is invalid.");
            System.exit(1);
        }

        String apiKey = args[1];
        if (!apiKey.matches("[a-zA-Z0-9]{128,128}")) {
            System.err.println("API Key is invalid.");
            System.exit(1);
        }

        String appName = args[2];
        if (!appName.matches("[a-zA-Z0-9 ]+")) {
            System.err.println("Application name is invalid.");
            System.exit(1);
        }

        try {
            String mitigationInfoXml = MitigationSample.getMitigationInfo(apiID, apiKey, appName); 
            System.out.println(StringUtility.isNullOrEmpty(mitigationInfoXml) ? "No flaws found in the latest scan." : mitigationInfoXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
