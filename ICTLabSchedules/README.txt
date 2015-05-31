On start up the app displays a custom gridView showing The Room or Person Names and the description for each Room or Person.
In Portrait mode One column is displayed.
In landscape mode Two columns are displayed.
All items are color coded.

When an item is clicked the Schedule Activity page is opened

This page displays a custom Grid view and control buttons for switching between Schedule types
All schedules Display a header showing the Days of the week.
Below the header the numbers for the days of the month are displayed.
The left hand column displays the hours from 0800 to 1700.
In the body of the schedule the classes are displayed.
All classes are color coded and span the rows in which there time is alloted.

When any cell in the grid is clicked identifying information for that cell is displayed.
For class cells you may enter text that is temporarily displayed on the grid. This demonstrates
the read write ability of the custom gridView. This capability could be extended in the future
with a back end service for persisting the data. 

The column for the current day of the week is high-lighted.
If the current day of the week is either Saturday or Sunday it is not high-lighted.
Cells for the current time of day are high-lighted. 

Unnecessary network traffic is prevented by caching all down loaded JSON strings.
To make sure that information does not get stale the time stamp for opening is saved and
program logic assures that data is refreshed after a predetermined amount of time has passed between
opening and closing of the app.

All days of the week are represented.
All days of the month are represented in their correct locations. 
Consecutive cells with same course are merged.
The custom grid view is built using components and can be easily programmed to represent a wide
range of possible views.
In this respect I composed two different types of Calendar views, one showing all time rows and class information
and one showing only the days of the month and whether there are classes occurring in their respective cells.
All cells in these grids support read write capability by tracking the pertinent information.
It would be possible with a minimum amount of effort and a back end service the grid could be used for editing class schedules.

The current presentations of the lab's schedule are:

Day
Day +/-
Week
Month with all information
Month with days and weeks only

Note all of the above presentations are fully prepared for read  and write operations
Click on any cell and a simple dialog demonstrates this. 

The following specifications are met:
Day: display the schedule for the current day; high-light the column.
Day +/-: display 3 columns: the day before the current day, the current day, the next day; high-light the column for the current day.
Week: display the selected lab's schedule for the entire week (Sun to Sat); high-light the current day.
Month: display the selected lab's schedule for the current month; high-light the current day.
Colour Code Classes

This was a very complex project especially since the current adapters that I researched did not support the grid view
column and row spanning capabilities. It could be possible to do this if there was an advanced adapter that allowed this
although with this capability comes many problems. To illustrate this consider the problem of high lighting the
class that is in the current hour. When this class spans more than one row it is necessary to query the leading and following
cells to determine if the class should be high lighted. This could probably be solved with the current adapters. A harder
problem to solve is actually creating the spanned cells. With the current adapters I did not see any easy way of doing this.

My solution to the problems above was to take complete control of building the grid view. All components were dynamically
created therefore I was able to ignore certain cells that were part of a spanned area and search freely through all
the components created.

THis was my first venture into the android view components and the many different techniques for manipulating them.
I can see right away that many of my original assumptions and logic were somewhat flawed; however the experience taught me much
about the android visual components and their ecosystem.

 