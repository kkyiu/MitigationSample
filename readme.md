# Summary
This Java project utilizes the Veracode Web Service APIs to get a list of Flaws, if any, in the latest static scan of a given application. The sole purpose of this project is to demonstrate how to use the Veracode Web Service APIs. In this sample project, you will see how to chain multiple Veracode Web Service API calls together to get the desire information.

# Artifacts
mitigationSample-1.0.0.jar

# Dependencies
vosp-api-wrappers-java-17.9.4.7.jar which could be downloaded from the Veracode Help Center and install into the local maven repository of the build machine as:

group: 'com.veracode.vosp.api.wrappers'
name: 'vosp-api-wrappers-java'
version: '17.9.4.7'

# Build
gradlew assemble

# Usage
java -jar mitigationSample-1.0.0.jar <API ID> <API Key> <App Name>

where <API ID> is a valid Veracode API Credential ID
      <API Key> is a valid Veracode API Credential Key
      <App Name> is a application name that can be accessed with the given credential

Note that the vosp-api-wrappers-java-17.9.4.7.jar must be present in the same directory of the mitigationSample-1.0.0.jar

# Test
Although some manual testing are done, there is no tests written for this project yet :(

# More info
I strong recommend posting any questions or comments about this project (or integration with Veracode Analysis Center Service in general) in the Veracode Community at https://community.veracode.com/s/ to reach a wider audiance. Rather than posting them here where I could very well be the only person that will ever read them.