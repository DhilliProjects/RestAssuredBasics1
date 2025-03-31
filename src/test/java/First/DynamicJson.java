package First;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Files.Payloads;
import Files.ReusableMethods;

//Library API

public class DynamicJson {

	@Test(dataProvider="Booksdata")
	public void addAndDeleteBook(String isbn, String aisle)
	{
		RestAssured.baseURI = "http://216.10.245.166"; //chanhe this to "https://rahulshettyacademy.com" if not works
		
		String addResponse = given().log().all().header("Content-Type","application/json").body(Payloads.addBook(isbn, aisle))
		.when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200).extract().asString();
		
		JsonPath js = ReusableMethods.rawToJson(addResponse);
		String id = js.get("ID"); //get() and getString() both are same
		System.out.println("Added Book ID: " + id);

	    // Delete Book
	    deleteBook(id); //calling the delete() method - to delete the ID, 
	    //so that everytime a new data will be generated
	    //so that we'll get the response as 'Successfully added' instead of ''Already added'
	}
	
	public void deleteBook(String id) //method to delete the ID - we called this method in addAndDeleteBook() method
	{
		RestAssured.baseURI = "http://216.10.245.166";
		
		String deleteResponse = given().log().all().header("Content-Type","application/json").body(Payloads.deleteBook(id))
		.when().post("Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200).extract().asString();
		
		JsonPath js = ReusableMethods.rawToJson(deleteResponse);
		String message = js.get("msg");
		System.out.println("Deleted Response: "+message);
	}

	
	//Parameterization - helpful for running a test/function with different sets of input values/data 
	@DataProvider(name="Booksdata")
	public Object[][] getData()
	{
		//An Array = is a collection of elements
		//Multidimensional array = Collection of Arrays
		return new Object[][] {{"hfh","7353"},{"jew","8972"},{"kww","2644"},{"wie","383"}};
		//here, we are passing for multiple data - here for 4books
		//we are passing two arguments/parameters, so in the 'addBook()' method also, we should provide two parameters
		//No. of TestCases showing in TestNG console depends on the no. of data values provided
	}
}
