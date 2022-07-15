package vn.tpsc.it4u;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@SpringBootTest
class It4uApplicationTests {

	@Test
	void contextLoads() {
		System.setProperty("webdriver.chrome.driver", "C:\\Driver\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--ignore-certificate-errors");
		WebDriver driver = new ChromeDriver(options);

		// Get the login page
		driver.get("http://localhost:3000/");
		driver.findElement(By.xpath("//input[@name='usernameOrEmail']")).sendKeys("admin");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		// wait.until(ElemtDisappear("//li//div//span[contains(text(),'Dashboard')]"));
		WebElement clickDashboard = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//li//div//i[@class='anticon anticon-dashboard']")));
		clickDashboard.click();
		WebElement clickDashboard1 = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//li//a//span[contains(text(),'Dashboard 1')]/..")));
		clickDashboard1.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='anticon anticon-export']/..")));
		driver.findElement(By.xpath("//i[@class='anticon anticon-export']/..")).click();
		driver.findElement(By.xpath("//button[@type='button']")).click();

		driver.close();

	}

}
