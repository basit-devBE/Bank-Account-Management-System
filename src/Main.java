import controllers.AccountController;
import controllers.MenuController;
import controllers.TransactionController;
import services.AccountManager;
import services.TransactionManager;
import utils.CustomTestRunner;

public class Main{
    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager();
        AccountController accountController = new AccountController(accountManager, transactionManager);
        TransactionController transactionController = new TransactionController(accountManager, transactionManager);
        CustomTestRunner testRunner = new CustomTestRunner();
        
        MenuController menu = new MenuController(accountController, transactionController,accountManager, transactionManager,testRunner);
        menu.initializeDemoData(accountManager, transactionManager);
        menu.start();
    }

}
