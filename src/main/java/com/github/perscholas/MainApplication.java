package com.github.perscholas;
/**
 * Created by leon on 8/13/2020.
 */
public class MainApplication {
    public static void main(String[] args) {
        JdbcConfigurator.initialize();
        Runnable sms = new ApplicationRunner();
        sms.run();
    }
}
