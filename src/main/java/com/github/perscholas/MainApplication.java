package com.github.perscholas;

/**
 * Created by leon on 2/18/2020.
 */
public class MainApplication {
    public static void main(String[] args) {
        JdbcConfigurator.initialize();
        Runnable applicationRunner = new ApplicationRunner();
        applicationRunner.run();
    }
}
