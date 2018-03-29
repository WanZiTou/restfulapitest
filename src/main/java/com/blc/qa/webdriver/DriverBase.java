package com.blc.qa.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.blc.qa.util.Logger;

public class DriverBase {
	private WebDriver dr = null;
	public static WebDriverPlus driver = null;
	public static int implicitlyWait = 5;
	public static int webDriverWait = 5;

	/**
	 * 启动浏览器
	 */
	public void launch() {
		/*ProfilesIni allProfiles = new ProfilesIni();   
		FirefoxProfile profile = allProfiles.getProfile("selenium");  */
		  
		String browser = System.getProperty("WebDriver.Browser");
		String browserLocation = System
				.getProperty("WebDriver.Browser.Location");
		// launch browser
		if (browser.equalsIgnoreCase("Firefox")) {
			Logger.log("打开Firefox浏览器");
			if (browserLocation != null && !browserLocation.isEmpty()) {
				System.setProperty("webdriver.firefox.bin", browserLocation);
			}
			dr = new FirefoxDriver();
			//dr =new FirefoxDriver(profile); 
		} else if (browser.equalsIgnoreCase("Chrome")) {
			Logger.log("打开Chrome浏览器");
			if (browserLocation != null && !browserLocation.isEmpty()) {
				System.setProperty("webdriver.chrome.driver", browserLocation);
			}
			dr = new ChromeDriver();
		} else {
			Logger.log("打开IE浏览器");
			if (browserLocation != null && !browserLocation.isEmpty()) {
				System.setProperty("webdriver.ie.driver", browserLocation);
			}
			dr = new InternetExplorerDriver();
		}
		driver = new WebDriverPlus(dr);
		implicitlyWait = Integer.parseInt(System.getProperty(
				"WebDriver.ImplicitlyWait").trim());
		webDriverWait = Integer.parseInt(System.getProperty(
				"WebDriver.WebDriverWait").trim());
		Logger.log("设置全局显示等待:" + implicitlyWait);
		driver.manage().timeouts()
				.implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}



}
