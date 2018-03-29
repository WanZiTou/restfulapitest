package com.blc.qa.dataprovider;

public interface IData {


	public Object[][] getData(String caseName, String dataFile);

	public Object[][] getData(String caseName, String dataFile, int startRowNum);

	public Object[][] getData(String caseName, String dataFile, int beginNum, int endNum);


}
