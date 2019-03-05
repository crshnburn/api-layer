plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core-library"))

    testImplementation("junit:junit:4.12")
}


//dependencies {

//    compile(libraries.spring_boot_starter_web)
//    compile(libraries.commons_validator)
//    compile(libraries.spring_cloud_starter_eureka_server)
//    compile(libraries.jackson_databind)
//    compile(libraries.apacheCommons)
//    compile(libraries.http_client)
//    compile(libraries.http_core)
//    compile(libraries.jetty_websocket_client)
//    
//    compileOnly(libraries.spring_boot_configuration_processor)
//    
//    testCompile(libraries.javax_servlet_api)
//    testCompile(libraries.spring_boot_starter_test)
//    testCompile(libraries.powermock_api_mockito2)
//    testCompile(libraries.power_mock_junit4)
//    testCompile(libraries.power_mock_junit4_rule)
//    testCompile(libraries.gson)
//}
