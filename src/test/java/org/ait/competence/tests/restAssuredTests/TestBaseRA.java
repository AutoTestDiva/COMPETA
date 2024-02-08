package org.ait.competence.tests.restAssuredTests;

import io.restassured.RestAssured;
import org.ait.competence.DataBaseRA;
import org.ait.competence.fwRA.AdminHelperRA;
import org.ait.competence.fwRA.DeleteUserHelperRA;
import org.ait.competence.fwRA.UserHelperRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TestBaseRA {
    final static Logger logger = LoggerFactory.getLogger(TestBaseRA.class);
    protected static DataBaseRA db;
    protected static UserHelperRA user = new UserHelperRA();
    protected static AdminHelperRA admin = new AdminHelperRA();
    protected static DeleteUserHelperRA deleteUser = new DeleteUserHelperRA();

    @BeforeMethod
    public void preconditionRA(Method method, Object[] parameters) {
        RestAssured.baseURI = "http://localhost:5173";
        logger.info("Start test " + method.getName() + " with parameters " + Arrays.asList(parameters));
    }

    @AfterMethod
    public void quitRA(ITestResult result) {
        if (result.isSuccess()) {
            logger.info("PASSED: " + result.getMethod().getMethodName());
        } else {
            logger.info("FAILED: " + result.getMethod().getMethodName());
        }
        logger.info("Stop test");
        logger.info("==============================");
    }
}