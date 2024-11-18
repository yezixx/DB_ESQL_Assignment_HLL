import config.DBConfig;
import menu.Join;
import menu.Login;
import menu.MainMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws IOException {
        new MainMenu().mainMenu();
    }
}