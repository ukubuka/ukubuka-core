package com.ukubuka.core.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ukubuka.core.parser.impl.UkubukaDFileParser;

/**
 * Ukubuka Root
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaLauncher {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaLauncher.class);

    /********************************** Application Context *********************************/
    private static final String APP_CONTEXT_NAME = "ukubuka-config.xml";
    private static final ApplicationContext APP_CONTEXT = new ClassPathXmlApplicationContext(
            APP_CONTEXT_NAME);

    /*********************************** Main Entry Point ***********************************/
    public static void main(String[] args) throws Exception {
        /* Execute Program */
        LOGGER.info("Starting Ukubuka...");
        new UkubukaLauncher().execute(args);
    }

    /**
     * Execute Program
     * 
     * @throws Exception
     */
    private void execute(String[] arguments) throws Exception {
        /* Read File*/
        UkubukaDFileParser parser = (UkubukaDFileParser) getAppContext()
                .getBean(UkubukaDFileParser.class);
        System.out.println(parser.getParserInfo());
    }

    /**
     * Fetch Application Context
     * 
     * @return the appContext
     */
    public static ApplicationContext getAppContext() {
        return APP_CONTEXT;
    }
}
