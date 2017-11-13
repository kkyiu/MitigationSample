# Summary
This Java project utilizes the Veracode Web Service APIs to get a list of Flaws, if any, in the latest static scan of a given application. The sole purpose of this project is to demonstrate how to use the Veracode Web Service APIs. In this sample project, you will see how to chain multiple Veracode Web Service API calls together to get the desire information.

# Artifacts
mitigationSample-1.0.0.jar

# Dependencies
vosp-api-wrappers-java-17.10.4.8.jar which could be downloaded from the Veracode Help Center and install into the local maven repository of the build machine as:

group: com.veracode.vosp.api.wrappers
name: vosp-api-wrappers-java
version: 17\.10\.4\.8

# Build
gradlew assemble

# Usage
java -jar mitigationSample-1.0.0.jar &lt;API ID&gt; &lt;API Key&gt; &lt;App Name&gt;

where &lt;API ID&gt; is a valid Veracode API Credential ID
      &lt;API Key&gt; is a valid Veracode API Credential Key
      &lt;App Name&gt; is a application name that can be accessed with the given credential

Note that this project requires the vosp-api-wrappers-java-17.10.4.8.jar

# Test
Although some manual testing are done, there is no tests written for this project yet.

# More info
I recommend posting any questions or comments about this project (or integration with Veracode Analysis Center Service in general) in the Veracode Community at https://community.veracode.com/s/ to reach a wider audience. Rather than posting them here where I could very well be the only person that will ever read them.
