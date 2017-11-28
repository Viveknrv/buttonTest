    package com.barefoot.test.usebutton;

    import org.junit.Test;
    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import java.util.Random;
    import java.util.concurrent.TimeUnit;

    import static org.junit.Assert.assertTrue;

    public class SmokeTest {

        //Initiate webdriver
        static WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        static WebElement newEmailusedElement;

        static String userName = "name1";
        String exisitingemail = "a@a.com";
        static String newEmailused;

        @Test
        public void smokeSuite() {

            //Launch challenge site
            driver.get("http://qa-challenge.herokuapp.com/");

            //Tap on add user button
            WebElement adduserButton = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[contains(text(), 'Add User')]"))));
            adduserButton.click();

            //wait implicityly
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            //declare save button
            String cssSaveButton = "button[class='save-user']";
            WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSaveButton)));

            //Save state of save button
            final boolean saveState = saveButton.isEnabled();

            //verify save button state disabled
            assertTrue(!saveState);


            //add a new user name
            String cssEnterFullName = "input[placeholder='Full name']";
            WebElement enteruserName = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssEnterFullName)));
            enteruserName.sendKeys(userName);

            //save button should now still be disabled
            assertTrue(!saveState);


            //add new users email
            String cssEmailField = "input[placeholder='Email']";
            WebElement enterEmailField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssEmailField)));
            enterEmailField.sendKeys(exisitingemail);

            //save button should now be enabled, try to save with existing email
            if(saveButton.isEnabled())
            {
                saveButton.click();
                //user should see error
                assertTrue(addAccountError());
            }

            //clear that field enter a new unique email
            enterEmailField.clear();
            newEmailused = newEmail();
            enterEmailField.sendKeys(newEmailused);

                if (saveButton.isEnabled())
                {
                    saveButton.click();
                }
            //Check if you new accoutn has been added
                assertTrue(verifyNewAccountAdded(newEmailused));
       }

       @Test
       public void smokeUserDetails()
       {
           //go view the new user you created
           clickOnNewUser(newEmailusedElement);

           //verify user details that were used
           verifyUserDetails();

           //Tap on add transaction button
           WebElement addTransaction = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[contains(text(), 'Add Transfer')]"))));
           addTransaction.click();

           //declare save button
           String cssSaveXnButton = "button[class='save-button']";
           WebElement saveXnButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSaveXnButton)));

           //add new amount
           String cssAmountField = "input[placeholder='Amount']";
           WebElement enterAmountField = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssAmountField)));
           enterAmountField.sendKeys("1123");

           saveXnButton.click();

       }

       //Helper to check if add account error is displayed
        public static boolean addAccountError()
        {
            WebElement errorTextField = driver.findElement(By.xpath("//*[contains(text(), 'A user with that email already exists')]"));
            return errorTextField.isDisplayed();
        }

        //Helper to check new account is added to list
        public static boolean verifyNewAccountAdded(String Emailused)
        {
            //kinda hacky or is it
            StringBuilder xpathBuilder = new StringBuilder();
            xpathBuilder.append("//*[contains(text(), '");
            xpathBuilder.append(Emailused);
            xpathBuilder.append("')]");

            newEmailusedElement = driver.findElement(By.xpath(xpathBuilder.toString()));

            return newEmailusedElement.isDisplayed();
        }

        public static boolean verifyUserDetails()
        {
            boolean userNameCorrect, emailCorrect, allGood;

            //kinda hacky or is it
            StringBuilder xpathBuilder = new StringBuilder();
            xpathBuilder.append("//*[contains(text(), '");
            xpathBuilder.append(userName);
            xpathBuilder.append("')]");

            WebElement newUserEntry = driver.findElement(By.xpath(xpathBuilder.toString()));

            return newUserEntry.isDisplayed();
        }

        public static void clickOnNewUser(WebElement element)
        {
            element.click();
        }



        //generate unique email
        public static String newEmail()
        {
            Random rand = new Random();
            int value = rand.nextInt(1000);
            StringBuilder str = new StringBuilder();
            str.append("new");
            str.append(value);
            str.append("@button.com");
            return str.toString();
        }

    }