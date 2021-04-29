package com.framework.runners;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        plugin = {"pretty",
                "html:target/htmlReport/cucumber.html",
                //"json:target/jsonReport/cucumber.json"},
                "de.monochromata.cucumber.report.PrettyReports:target/pretty-cucumber"
        },
        features = "src/test/resources/features",
        glue = {"com.framework.steps"}
        // tags = "@add"
        //monochrome = true
)
public class runner {

}
