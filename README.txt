Title: Appointment Scheduler

Purpose: Provides an interface for creating/deleting Customer records and creating/deleting appointments associated
with a customer. This program also catalogs and stores all customer and appointment creations/updates to a remote
database.

Author: Brenden M. DeLong
Contact Information: email-> bdelon3@wgu.edu phone-> 509-797-1329
Application Version: 1.25
Date: 1-2-2023
IDE: IntelliJ Community 2022.2.3
JDK Version: Java SE 17.0.4
JavaFX Version: JavaFX 19.0

Directions to run the program:
1. Delete .idea folder then open project with "file->open" and select project folder
2. Download JavaFX 19.0.
3. Extract the zipped folder.
4. Configure VM options to
--module-path ${PATH_TO_FX_19} --add-modules javafx.fxml,javafx.controls, javafx.base.
5. Create PATH_TO_FX_19 in IntelliJ's path variables and set the value to "your extracted folder"/lib.
6. Add "your extracted folder"/lib to your project libraries under "File->ProjectStructure->Libraries"
7. Download mysql-connector-java-8.0.25.
8. Extract the zipped folder.
9 . Add "your extracted folder"/mysql-connector-java-8.0.25/mysql-connector-java-8.0.25 to your project libraries under
"File->ProjectStructure->Libraries"

Description of the additional report:
The additional report section is implemented within the "Reports" controller which
can be launched by clicking the "Generate Report" button in the main schedule window. The report calculates the number of
customers who have appointments scheduled for a selected contact. The user only selects a contact from a combo box in
order to view the report.

MySql Driver version: 8.0.25