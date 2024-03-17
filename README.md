# 13033-Helper-App
A government assisting tool for handling the Covid Outbreak - Movement Messages

The goal of this project was to create an Android application used for sending movement messages to 13033 during the pandemic. The application consists of the following:

In the Signup page, the user is prompted to enter their login credentials as well as their full name and address for ease of use later on. These details are saved under a unique ID in the Realtime database on Firebase.

We start the first activity with a simple layout, containing two fields where the user can enter an email and password to authenticate via Firebase and proceed to the program. If the user's email is in the list of existing users on Firebase, they can log in and continue normally. After filling in the details and clicking log in, the onClick function of the button will check if the fields are empty or not, and if not, it will call the logIn function, passing the email and password as arguments. The logIn function will use the signInWithEmailAndPassword function of Firebase, and if successful, it will redirect the user to the main page.

To avoid repeated login, the onStart function checks the user's login status.

In the Signup page, the user is prompted to enter their login credentials as well as their full name and address for ease of use later on. These details are saved under a unique ID in the Realtime database on Firebase. In the signup class, when the confirm button is clicked, the user's details are gathered, and a check is performed for empty fields. Then, the signup function is called with these details, which creates the user in the database and assigns them a UID. After that, an object of type User is created, which holds the user's name and address information, and it is also stored as a child node in Firebase. Finally, the user is redirected to the main page.

Next, we have the main activity of the application. Here, the user's details are automatically retrieved from the database, but they can choose to change and save them with the "update info" button. Additionally, they can select one of the radio buttons corresponding to the reason for their exit and send the message to 13033 using the "Send Message" button. For exercise purposes, the messages are sent to a fake number to avoid causing any issues with the service.
