package main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger;

    public static void main(String[] args) {
        if (!setupLogger()) utils.UtilsForAll.exitFromProgram();
    }

    private static boolean setupLogger() {
        if (!utils.UtilsForAll.setMyLoggerConfiguration()) {
            System.out.println("Ошибка доступа к настройкам логгера");
            return false;
        }
        if (!utils.XMLProgramSettings.isXMLSettingsFile()){
            System.out.println("Ошибка файла настроек программы");
            return false;
        }

        logger = Logger.getLogger(utils.UtilsForAll.getMainClass().getName());
        if (utils.XMLProgramSettings.isLogInSettings()){
                logger.setLevel(Level.INFO);
        } else logger.setLevel(Level.OFF);

        logger.info("Запуск программы");
        return true;
    }
}
