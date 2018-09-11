package ua.kiev.prog;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

enum LoggerType {Console, File};

public class Main {
    static LoggerType loggerType = LoggerType.File;
    static boolean usePreprocessors = true;

    public static void main(String[] args) {
        // case #1
        System.out.println(">>> Sample #1:");

        LoggerAPI api = null;
        if (loggerType == LoggerType.Console)
            api = new ConsoleLoggerAPI();
        else if (loggerType == LoggerType.File)
            api = new FileLoggerAPI("log.txt");

        try {
            api.open();
            try {
                // optional functionality
                if (usePreprocessors) {
                    Preprocessor preprocessor = new DatePreprocessor();
                    api.setPreprocessor(preprocessor);
                }
                Notifier notifier = new Notifier(api);
                notifier.sendSms();
            } finally {
                api.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // case #2
        System.out.println(">>> Sample #2:");

        ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        try {
            Notifier notifier = ctx.getBean("notifier", Notifier.class);
            notifier.sendSms();
        } finally {
            ctx.close();
        }

        // case #3
        /*System.out.println(">>> Sample #3:");

        ctx = new ClassPathXmlApplicationContext("/spring-config.xml");
        try {
            Notifier notifier = ctx.getBean("notifier", Notifier.class);
            notifier.sendSms();
        } finally {
            ctx.close();
        }*/
    }
}



//>>> Sample #1:
//Open file
//Writing to file: [Tue Sep 11 19:41:43 EEST 2018] Sending sms...
//Writing to file: [Tue Sep 11 19:41:47 EEST 2018] Done!
//Close file
//>>> Sample #2:
//вер. 11, 2018 7:41:47 PM org.springframework.context.annotation.AnnotationConfigApplicationContext prepareRefresh
//INFO: Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@254989ff: startup date [Tue Sep 11 19:41:47 EEST 2018]; root of context hierarchy
//Open file
//Writing to file: [Tue Sep 11 19:41:48 EEST 2018] Sending sms...
//Writing to file: [Tue Sep 11 19:41:51 EEST 2018] Done!
//вер. 11, 2018 7:41:51 PM org.springframework.context.annotation.AnnotationConfigApplicationContext doClose
//Close file
//INFO: Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@254989ff: startup date [Tue Sep 11 19:41:47 EEST 2018]; root of context hierarchy
//
//Process finished with exit code 0