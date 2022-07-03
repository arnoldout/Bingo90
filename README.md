# Bingo90 Strip Generator

# Running the program
When first using the program, you will need to run the command ```mvn clean install``` from command line. This will run all tests, ensure dependencies are downloaded, and compile the program. Run this command from the project root directory.

After this, you can open the Main.java class and modify the variable ```private static final int TOTAL_REQUIRED_STRIPS = 10000;``` It is currently set to 10,000, which will generate 10,000 strips of tickets. This algorithm should be able to generate this many tickets in less than 1 second, and depending on the system, I have seen it run in less than 0.4 seconds. 

Once required strips are set, please run the Main method and all strips will be output to Tickets.txt file. You will find this Tickets.txt in the project root directory, alongside the pom.xml file.
An example ticket will look like the following:
```[0, 3, 0][14, 15, 0][25, 26, 27][31, 0, 0][43, 47, 0][0, 0, 50][0, 60, 65][72, 0, 78][0, 80, 0]```. Each square bracket indicates a column of data for a ticket, with 0 indicating a blank space. A strip of tickets will be 6 lines of these generated tickets and will ensure all values within the 6 tickets are unique and in ascending order. 


# Requirements

-   Generate a strip of 6 tickets
    -   Tickets are created as strips of 6, because this allows every number from 1 to 90 to appear across all 6 tickets. If they buy a full strip of six it means that players are guaranteed to mark off a number every time a number is called.
-   A bingo ticket consists of 9 columns and 3 rows.
-   Each ticket row contains five numbers and four blank spaces
-   Each ticket column consists of one, two or three numbers and never three blanks.
    -   The first column contains numbers from 1 to 9 (only nine),
    -   The second column numbers from 10 to 19 (ten), the third, 20 to 29 and so on up until
    -   The last column, which contains numbers from 80 to 90 (eleven).
-   Numbers in the ticket columns are ordered from top to bottom (ASC).
-   There can be **no duplicate** numbers between 1 and 90 **in the strip** (since you generate 6 tickets with 15 numbers each)

**Please make sure you add unit tests to verify the above conditions and an output to view the strips generated (command line is ok).**

Try to also think about the performance aspects of your solution. How long does it take to generate 10k strips? The recommended time is less than 1s (with a lightweight random implementation)



# Algorithm
With the following, I have attempted to outline the algorithm by using a step by step example of the use case to generate a new strip. I will attempt to outline the parts of each step and why I chose to design them in such a way.

 ## Initial Loop of rows and columns
I begin the algorithm by looping rows first and columns second. The impact of this is that I will process an entire strip's worth of data at a time. For example, on the first run, we will place all the numbers ```[1,2,3,4,5,6,7,8,9]``` in their respective tickets. By doing this, I found the greatest benefit was that I could ensure each ticket followed the rule stating that no ticket column could contain only blanks, and it also allowed me to fully place all of the numbers at one time.

## Column Variations
I took the most advantage of attacking the columns together by utilizing a full column variation and implementing it entirely. For example, the first strip's columns can have different variations of data. ```{ 1, 2, 2, 2, 1, 1 }``` This array states that the first ticket should contain 1 number. The second ticket should have 2 numbers, and so on. I found there were only a few mathematical variations possible for tickets. The following table outlines the variations I found possible for a column of each length.
All of the outlined variations sum up to the required column length.
| Column Length | Column Variation |
|---|---|
| 9 | { 1, 2, 2, 2, 1, 1 }, { 3, 1, 1, 1, 1, 2 } |
| 10 | { 2, 2, 2, 2, 1, 1 }, { 3, 3, 1, 1, 1, 1 }, { 3, 2, 2, 1, 1, 1 } |
| 11 | { 3, 3, 2, 1, 1, 1 }, { 2, 2, 2, 2, 2, 1 } |


To add even further variation, I randomly select a variation from the list, and then shuffle that variation itself to have a huge amount of possible variations per strip column. 

## Linked List
Once a variation has been selected, we then have to pull values from the possible numbers and place them into the expected positions. I debated between the best way to store and retrieve the values, but in the end decided that the benefits of a linked list suited the problem very nicely. 
I can generate a linked list with all of the required values in ascending order. Removing an element is an O(n) operation, and it allows me to easily shrink my range of valid values as the list itself shrinks. This final point, I will go into in more detail now. 

### Random Selection of values
Let's take an initial selection of numbers for the first column ```[1,2,3,4,5,6,7,8,9].``` We may have to select 1, 2 or 3 values from this LinkedList to go into a column. Depending on the desired variation. 

If we required 2 numbers, we need to ensure that the first number is lower than 9. Our requirement clearly states that in a column we need to have our values in ascending order, **leaving a 9 off the field means if we randomly recieve 8, there is still a 9 available for us to put into our final place.** We do this by generating a random value between 1 and 8. Defined as ```maxRange``` and ```minRange```. This logic changes as our values are removed from the list. **If 8 and 9 are removed, we need to ensure 7 is not taken.** 

Using a LinkedList allows us to simply take the last element of the list out of the field, no matter what value it holds. Thus, it does not matter what size our list is, our solution will scale. 


After selecting our first number, we also need to ensure our next number is not lower. In order to do this, we change ```minRange``` to be our previously selected value, and keep ```maxRange``` as it is. Due to an element being removed from the list at this point, our ```maxRange``` is now actually pointing to the last node in our list.

This naturally shrinking boundary removes any potential extra logic checking values are larger. This solution scales in the same way for 3 required numbers as well. Except instead of taking 9 out of the possible values, we take 8 and 9 out. On our second iteration we put 8 back in, and finally we put 9 back in.

For a single required number it does not matter which number we take. 

## Placing our elements 
At this point in the algorithm, we have generated 1,2 or 3 numbers for our column. We now need to place them, allowing for the opposite amount of required blank spaces. 

If an array needs 1 number, we place it in any box. The remaining 2 boxes will have blanks. 

If an array needs 2 numbers, we have to be smart with our placement. For the first placement, it can only go into box 0 or box 1. We need to leave enough space for another item to be placed. Then on the second number, we have the final box free or if box 1 was not used previously, we can also use box 1.

3 numbers just need to be placed in order. So if it is the first number, it goes into box 0 and so on. 

## Rows
At this point of the algorithm we have our arrays created, and stored in a list of type ```int[]```. This first iteration of our loop will create arrays for each of our columns. We then increment our row and repeat the same steps. This second time, we generate the entire column of the values 10->19. 

Once finished, we will have a list of unique ascending ordered arrays sorted by column.

## Printing the ticket
When thinking about retrieving the output of our tickets, the first problem encountered is that the arrays are sorted by column. However, we actually need it to be sorted by row here. 
The solution for this was to have 2 loops. The outer one running by column length and the inner running by row length. 
Using a simple equation, we can access our items in the order we require. ```columnNo + (rowNo * 6);``` As there are 6 columns, we need to incrementally reach item 0, then 6, then 12 and so on to gather the data for our first ticket. Repeating this, we have reordered our list. I chose to use a ```StringBuilder``` to build up a single ticket, and then once a ticket has been generated. Output that string to a list of strings. 

The final step is then to loop through the list of strings and using a ```FileWriter``` output the data to a file named ```Tickets.txt```.
