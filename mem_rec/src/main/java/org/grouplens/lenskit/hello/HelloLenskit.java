/*---------------------------------------------------------------------------
// AUTHOR:          (Put your name here)
// FILENAME:        Lab7.java
// SPECIFICATION:   This program creates objects of the Rectangle class and calculates the area
//					and perimeter of the rectangle. Complete coding the Rectangle class first.
// INSTRUCTIONS:    Read the following code skeleton and add your own code
//                  according to the comments.  Ask your TA or your class-
//                  mates for help and/or clarification.  When you see
//                  //--> that is where you need to add code.
//-------------------------------------------------------------------------*/

//Create a class Lab7 with a main method
public class Lab7{


	public static void main(String[] args) {

		//Create two Rectangle objects
		//r1 with length 10 and width 4
		//r2 with length 6 and width 6
		Rectangle r1 = new Rectangle(10, 4);
		Rectangle r2 = new Rectangle(6, 6);

		//Print the details of the rectangles with their respective toString() methods
		System.out.println("The two rectangles are");
		System.out.println("r1 "+r1.toString());
		System.out.println("r2 "+r2.toString());

		//Check if either of the rectangles is a square using the isSquare() method
		//and print the rectangle's details telling it is a square.
		if(r1.isSquare()){
			System.out.println(r1.toString()+ " is a square");
		}
		if(r2.isSquare()){
			System.out.println(r2.toString() + " is a square");
		}

		//Calculate the area of the rectangles using the area() method
		//and print it along with the details of the rectangle
		//For example:
		//"The area of <rectangle details> is <rectangle area> square units"
		//Use the toString() method to get the rectangle details and the area() method to get the area
		System.out.println("The area of "+ r1.toString() + " is "+ r1.area()+ " square units");
		System.out.println("The area of "+ r2.toString() + " is "+ r2.area()+ " square units");

		//Calculate the perimeter of the rectangles using the perimeter() method
		//and print it along with the details of the rectangle
		//For example:
		//"The perimeter of <rectangle details> is <rectangle perimeter> units"
		//Use the toString() method to get the rectangle details and the perimeter() method to get the area
		System.out.println("The perimeter of "+ r1.toString() + " is "+ r1.perimeter()+ " units");
		System.out.println("The perimeter of "+ r2.toString() + " is "+ r2.perimeter()+ " units");

		//Print "Changing the width of rectangle (r1) to 9" and change it using the setWidth(int) method
		System.out.println("Changing the width of rectangle (r1) to 9");
		r1.setWidth(9);
		//Print "Changing the length of rectangle (r2) to 15" and change it using the setLength(int) method
		System.out.println("Changing the length of rectangle (r2) to 15");
		r2.setLength(15);

		//Print the details of the new rectangles using the toString() methods.
		System.out.println("The new rectangles are: \nr1 "+r1.toString()+"\nr2 "+r2.toString());

		//Check if the areas of the rectangles are equal or not and print the information
		//if areas are equal then print
		//		'The rectangles are of equal area, <area of r1> square units'
		//if they are not equal then print
		//		'The areas of the rectangles are not equal.'
		//		'r1 has area = <area of r1> square units and r2 has area = <area of r2> square units.
		if(r1.area()==r2.area()){
			System.out.println("The rectangles are of equal area, " + r1.area()+ " square units");
		}
		else{
			System.out.println("The areas of the rectangles are not equal.");
			System.out.println("r1 has area = "+r1.area()+" square units and r2 has area = "+ r2.area()+" square units.");
		}

		//Check if the perimeters of the rectangles are equal or not and print the information
		//Use the same format as done while checking for area above
		if(r1.perimeter()==r2.perimeter()){
			System.out.println("The rectangles are of equal perimeter, " + r1.perimeter()+ " units");
		}
		else{
			System.out.println("The perimeters of the rectangles are not equal.");
			System.out.println("r1 has perimeter = "+r1.perimeter()+" units and r2 has perimeter = "+ r2.perimeter()+" units.");
		}

	}

}