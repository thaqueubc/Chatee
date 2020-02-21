# Android Development: Building a Multi-View App with Cloud Storage
1. [ Introduction ](#1)
2. [ Getting Started ](#2)
3. [ Create a Google Firebase App ](#3)
4. [ Enable Firebase Authentication and Realtime Database ](#4)
5. [ Login / Register Buttons ](#5)
6. [ Guidelines ](#6)
7. [ Material Design ](#7)
8. [ Adding Authentication Screens ](#8)
9. [ Refactor Form as Login AND Registration ](#9)
10. [ Authenticating a User with Firebase ](#10) 
11. [ Adding the Chat Screen ](#11)
12. [ Add Message Elements to Chat Screen ](#12)
13. [ Submit Messages to Firebase Realtime Database ](#13)
14. [ Recycler View to Display Chat Messages ](#14)
15. [ Read and Render Messages from Firebase ](#15)
16. [ Add Styling to Chat Messages ](#16)
17. [ Add Drawables to Customize Shapes ](#17)

<a name="1"></a>
## 1) Introduction

During this lesson we'll be building a realtime chat app called Chatee. We're going to use Firebase for user authentication and as a realtime cloud database for our app's storage.

<a name="2"></a>
## 2) Getting Started

Create an "empty activity" project in Android Studio called Chatee.

<img src="https://i.imgur.com/EP9aaaP.png" />

<a name="3"></a>
## 3) Create a Google Firebase App
Firebase is a collection of products for web and mobile development. Today we are going to use both Firebase Authentication and Firebase Realtime Database that are hosted in the cloud so we do not have to manage them.

Log into the Firebase Console. https://firebase.google.com/

Create a project called Chatee


Click the "Add App" button, and select android as the platform:

<img src="https://i.imgur.com/OoSLo2D.png" />

Enter your package name, ie. (com.yourname.chatee):

<img src="https://i.imgur.com/Mf2N2if.png" width="400" />

Download the google-services.json file to the app directory of your Chatee project.

Follow the instructions to add the Firebase SDK to your project.

When you add the firebase dependencies to your build.gradle file, add firebase-auth and firebase-database:

```
implementation 'com.google.firebase:firebase-database:19.2.1'
implementation 'com.google.firebase:firebase-auth:19.2.0'
```

Once Gradle has synced, run your app and ensure firebase can detect it.

<img src="https://i.imgur.com/cDkd8dY.png" />

<a name="4"></a>
## 4) Enable Firebase Authentication and Realtime Database

Within your project in the Firebase Console click on Authentication from the left menu. Enable Email/Password auth (NOT Email link passwordless sign-in).

![](https://i.imgur.com/cpOA5Nb.png)

While we are here we can also create our Realtime Database that will store our chat messages. Below Authentication on the left menu select Databases, scroll down to the Realtime Database section shown below and Create database.

![](https://i.imgur.com/ViAaNqI.png)

Enable open security settings for read and write permissions.

![](https://i.imgur.com/Apqpzrr.png)

<a name="5"></a>
## 5) Login / Register Buttons

### Exercise

Add a "Log In" button and a "Register" button to the activity_main layout file.

<a name="6"></a>
## 6) Guidelines

Guidelines are a visual guide which will not be seen at runtime. We use these to help us align views and view groups.

Let's try using a horizontal guideline to center our buttons.

```xml
<androidx.constraintlayout.widget.Guideline
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:id="@+id/center_guideline"
    android:orientation="horizontal"
    app:layout_constraintGuide_percent="0.5" />

<Button
    android:id="@+id/login_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/login"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintBottom_toBottomOf="@id/center_guideline"
    app:layout_constraintEnd_toEndOf="parent" />

<Button
    android:id="@+id/register_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/register"
    app:layout_constraintTop_toTopOf="@id/center_guideline"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent" />
```

### Adding Custom Colour Scheme

Navigate to `res/src/values` and look at the `colors.xml` file.

Let's try updating our primary colour scheme:

```xml
<resources>
    <color name="colorPrimary">#F391A0</color>
    <color name="colorPrimaryDark">#B0228C</color>
    <color name="colorAccent">#CCFF66</color>
</resources>
```

<img src="https://i.imgur.com/KhebQXo.png" width="300" />


<a name="7"></a>
## 7) Material Design

[Material Design](https://material.io/) is a set of tools and components created by Gooogle to support user experience best practices for digital interfaces.

Let's use Material Design in our app.

Add the following to your build.gradle file:

```text
    implementation 'com.google.android.material:material:1.1.0'
```

Be sure to sync.

Open the `styles.xml` file at `res/src/values` and update our parent theme to be `Theme.MaterialComponents.Light`.

```xml
<style name="AppTheme" parent="Theme.MaterialComponents.Light">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
</style>
```

Now our buttons will be styles like Material buttons.

<img src="https://i.imgur.com/cdcxrMv.png" width="300" />

<a name="8"></a>
## 8) Adding Authentication Screens

When a user selects either Log In or Register we will need an activity to handle their input and connect to Firebase.

Let's start by adding log in and registration forms.

Add an activity file called `LoginActivity.kt` in the same directory as MainActivity.
```kotlin
class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
```
Add a corresponding layout file called `activity_login.xml` in the `res/layouts` folder.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/center_guideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.5" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fillViewport="true"
        android:isScrollContainer="false"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:layout_marginStart="30dp"
            android:layout_marginEnd="30dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <EditText
                android:id="@+id/email_input"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="@string/email"
                android:inputType="textEmailAddress"
                app:layout_constraintBottom_toTopOf="@id/center_guideline"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent" />

            <EditText
                android:id="@+id/password_input"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="15dp"
                android:hint="@string/password"
                android:inputType="textPassword"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@id/center_guideline" />

            <Button
                android:id="@+id/login_button"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="24dp"
                android:padding="12dp"
                android:text="@string/login"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@id/password_input" />

        </LinearLayout>

    </ScrollView>

</androidx.constraintlayout.widget.ConstraintLayout>
```
Don't forget to add your activity to the manifest:

```xml
<activity
    android:name=".LoginActivity"
    android:windowSoftInputMode="adjustResize" />
```

`android:windowSoftInputMode="adjustResize"` prevents our keyboard from obstructing our input fields. 


To test your new LoginActivity, add an OnClickListener to the login_button within the onCreate of your MainActivity:

```kotlin
login_button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
```

<img src="https://i.imgur.com/QuziZe2.png" width="300" /> </br>

<a name="9"></a>
## 9) Refactor Form as Login AND Registration

Before we get too far, let's consider that the register form is going to be essentially the same as this Login form, where we will ask for the user's email and password and send it to Firebase. It makes sense to rename this activity to AuthActivity and use this activity for both login and register.

Let's rename LoginActivity to AuthActivity, and activity_login to activity_auth. Use the Refactor->Rename tool by right clicking the file in Android Studio.

We will need to know whether the user is registering or logging in. To do this, we can store a property called authMode. We could use a boolean for this and call it `isRegistering` or such, but let's use a sealed class for more declarative code.

Let's also update MainActivity to pass an AuthMode when starting the AuthActivity (by using the companion object we added to AuthActivity).

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button.setOnClickListener {
            startActivity(AuthActivity.newIntent(AuthMode.Login, this))
        }

        register_button.setOnClickListener {
            startActivity(AuthActivity.newIntent(AuthMode.Register, this))
        }
    }
}

sealed class AuthMode() : Parcelable {
    @Parcelize
    object Login : AuthMode()

    @Parcelize
    object Register : AuthMode()
}
```

Update the `AuthActivity` to handle the AuthMode:

```kotlin
class AuthActivity: AppCompatActivity() {
    private lateinit var authMode: AuthMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authMode = intent?.extras?.getParcelable(AuthModeExtraName)!!

        when(authMode) {
            is AuthMode.Login -> { auth_button.text = getString(R.string.login)}
            is AuthMode.Register -> { auth_button.text = getString(R.string.register) }
        }
    }

    companion object {
        const val AuthModeExtraName = "AUTH_MODE"

        fun newIntent(authMode: AuthMode, context: Context): Intent {
            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(AuthModeExtraName, authMode)
            return intent
        }
    }
}
```

Don't forget to validate that the manifest file was updated with our new activity. It should look like this:

```xml
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />

            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    <activity
        android:name=".AuthActivity"
        android:windowSoftInputMode="adjustResize" />
```

<img src="https://i.imgur.com/idSrybW.png" width="300" />

<img src="https://i.imgur.com/3nLkooP.png" width="300" />

<a name="10"></a>
## 10) Authenticating a User with Firebase

When a user enters their email and password, we want to have these credentials authenticaed by Firebase.

Let's update the AuthActivity to do this for us:

```kotlin
class AuthActivity: AppCompatActivity() {
    // lateinit is 'lazy'
    private lateinit var auth: FirebaseAuth
    private lateinit var authMode: AuthMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authMode = intent?.extras?.getParcelable(AuthModeExtraName)!!
        auth = FirebaseAuth.getInstance()

        when(authMode) {
            is AuthMode.Login -> { auth_button.text = getString(R.string.login)}
            is AuthMode.Register -> { auth_button.text = getString(R.string.register) }
        }

        auth_button.setOnClickListener {
            when(authMode) {
                is AuthMode.Login -> {
                    auth.signInWithEmailAndPassword(email_input.text.toString(), password_input.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                startActivity(Intent(this, ChatActivity::class.java))
                            }

                            // Handle error here
                        }
                }

                is AuthMode.Register -> {
                    auth.createUserWithEmailAndPassword(email_input.text.toString(), password_input.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                startActivity(Intent(this, ChatActivity::class.java))
                            }

                            // Handle error here
                        }
                }
            }
        }
    }

    companion object {
        const val AuthModeExtraName = "AUTH_MODE"

        fun newIntent(authMode: AuthMode, context: Context): Intent {
            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(AuthModeExtraName, authMode)
            return intent
        }
    }
}
```
<a name="11"></a>
## 11) Adding the Chat Screen

Let's add a chat activity and navigate here when the user successfully authenticates.

Add a file called `ChatActivity` and it's corresponding layout file.

```kotlin
class ChatActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="CHAT SCREEN" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

Now when we successfully register (with new credentials) or login (with previously registered credentials). For simplicity, we're not going to handle the error case when the authentication fails. In a real application, you should show the user an error message.

When a user successfully authenticates, you should see the chat screen:

<img src="https://i.imgur.com/ubLvc46.png" width="300" />

<a name="12"></a>
## 12) Add Message Elements to Chat Screen

Let's build out the chat screen to look more like it should with a collection of messages, a text area to write new messages and a button to save the message to Firebase. First we will try to write new messages.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/three_quarters_horizontal_guideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.75" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toBottomOf="@id/three_quarters_horizontal_guideline">

        <EditText
            android:id="@+id/message_input"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginStart="10dp"
            android:layout_marginEnd="10dp"
            android:gravity="center_vertical"
            android:hint="@string/message"/>

        <Button
            android:id="@+id/send_message_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/send"
            android:textAlignment="center"
            android:layout_marginEnd="10dp" />

    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```
<img src="https://i.imgur.com/vueGhAG.png" width="300" />

Update strings as well:

```xml
<resources>
    <string name="app_name">Chatee</string>
    <string name="login">Log In</string>
    <string name="register">Register</string>
    <string name="email">Email</string>
    <string name="password">Password</string>
    <string name="message">Message</string>
    <string name="send">Send</string>
</resources>
```

<a name="13"></a>
## 13) Submit Messages to Firebase Realtime Database

Make sure you have 2 emulators that are running google play services.

Pixel 3 and Pixel 3A for instance:

![](https://i.imgur.com/hB7PODf.png)

![](https://i.imgur.com/wWlhUK0.png)

```kotlin
class ChatActivity: AppCompatActivity() {
    private lateinit var messagesDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)
        messagesDB = FirebaseDatabase.getInstance().getReference("Messages")
        send_message_button.setOnClickListener {
            val sender = FirebaseAuth.getInstance().currentUser?.email
            val message = message_input.text.toString()

            if (sender != null) {
                saveMessage(sender, message)
            }
        }
    }

    private fun saveMessage(sender: String, messageBody: String) {
        val key = messagesDB.push().key
        key ?: return

        val message = Message(sender, messageBody)

        messagesDB.child(key).setValue(message)
    }
}
data class Message(
        var sender: String = "",
        var messageBody: String = "")
```


<a name="14"></a>
## 14) Recycler View to Display Chat Messages
Let's hook up a recycler view so we can lay out the chat messages.

Update the activity_chat.xml to include the following:
```xml
<androidx.recyclerview.widget.RecyclerView
        android:id="@+id/message_list"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/three_quarters_horizontal_guideline"
        tools:listitem="@layout/item_message" />
```

Add a layout file called item_message:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/center_guideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.50" />

    <TextView
        android:id="@+id/sender_label"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:textAlignment="viewStart"
        android:layout_marginStart="20dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/center_guideline" />

    <TextView
        android:id="@+id/message_body_label"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginEnd="20dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toEndOf="@id/center_guideline"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
```kotlin
class ChatActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        message_list.layoutManager = LinearLayoutManager(this)

        val messages = listOf(
            Message("someguy@example.com", "Oh hai! Blah blah blah blah blahblahblah."),
            Message("someotherguy@example.com", "Yaya, blee blee bleeee.")
        )

        message_list.adapter = MessagesAdapter(messages, this)
    }
}

private class MessagesAdapter(private val messages: List<Message>, val context: Context): RecyclerView.Adapter<MessagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        return MessagesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message, parent, false))
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messages[position]

        holder.itemView.sender_label.text = message.sender
        holder.itemView.message_body_label.text = message.messageBody
    }
}

class MessagesViewHolder(view: View): RecyclerView.ViewHolder(view)

data class Message(val sender: String, val messageBody: String)
```

<img src="https://i.imgur.com/0gsQEWT.png" width="300" />


<a name="15"></a>
## 15) Read and Render Messages from Firebase

Now we need to replace the hardcoded messages with messages that are pulled from Firebase.

```kotlin
class ChatActivity: AppCompatActivity() {
    private lateinit var messagesDB: DatabaseReference
    
    // create an instance level messages collection
    var messages: MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        messagesDB = FirebaseDatabase.getInstance().getReference("Messages")
        
        // add the event listener to receive data from Firebase
        messagesDB.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages = mutableListOf()
                dataSnapshot.children.forEach {
                    val message = it.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }

                update()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read values: handle error
            }
        })

        send_message_button.setOnClickListener {
            val sender = FirebaseAuth.getInstance().currentUser?.email
            val message = message_input.text.toString()

            if (sender != null) {
                saveMessage(sender, message)
            }
        }

        message_list.layoutManager = LinearLayoutManager(this)
    }

    // update method to be called when UI needs to be refreshed
    private fun update(){
        message_list.adapter = MessagesAdapter(messages, this)
    }

    private fun saveMessage(sender: String, messageBody: String) {
        val key = messagesDB.push().key
        key ?: return

        val message = Message(sender, messageBody)

        messagesDB.child(key).setValue(message)
    }


}
```

<a name="16"></a>
## 16) Add Styling to Chat Messages

Let's improve the look and feel of the chat screen:

Change item_message.xml to the following which will add a sender image to distinguish between messages from the current user and those from other users, as well as stack the user email and message vertically.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/one_quarter_guideline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.25" />

    <ImageView
        android:id="@+id/sender_image"
        android:layout_width="75dp"
        android:layout_height="75dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/one_quarter_guideline"
        app:layout_constraintTop_toTopOf="parent" />

    <LinearLayout
        android:id="@+id/message_container"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_marginTop="10dp"
        android:layout_marginBottom="10dp"
        android:layout_marginEnd="10dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@id/one_quarter_guideline"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:id="@+id/sender_label"
            android:textSize="14sp"
            android:layout_marginTop="10dp"
            android:layout_marginStart="10dp"
            android:layout_marginEnd="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textAlignment="viewStart" />

        <TextView
            android:id="@+id/message_body_label"
            android:textSize="17sp"
            android:layout_marginBottom="10dp"
            android:layout_marginStart="10dp"
            android:layout_marginEnd="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />

    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

Add a couple new colors, again to distinguish between current and other user messages:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="colorPrimary">#F391A0</color>
    <color name="colorPrimaryDark">#B0228C</color>
    <color name="colorAccent">#CCFF66</color>
    <color name="colorCurrent">#FF6666</color>
</resources>
```

<a name="17"></a>
## 17) Add Drawables to Customize Shapes

Add the following 2 drawable files that will help us create rounded text bubbles for our messages.

rounded_background1:
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">

    <corners android:radius="8dp" />

    <gradient
        android:endColor="@color/colorCurrent"
        android:startColor="@color/colorCurrent" />
</shape>
```

rounded_background2:
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">

    <corners android:radius="8dp" />

    <gradient
        android:endColor="@color/colorAccent"
        android:startColor="@color/colorAccent" />
</shape>
```
In `MessagesAdapter` update `onBindViewHolder` to the following:

```kotlin
override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
    val message = messages[position]

    holder.itemView.sender_label.text = message.sender
    holder.itemView.message_body_label.text = message.messageBody

    if (FirebaseAuth.getInstance().currentUser?.email == message.sender) {
        holder.itemView.sender_image.setImageResource(R.drawable.smile)
        holder.itemView.message_container.setBackgroundResource(R.drawable.rounded_background1)
    } else {
        holder.itemView.sender_image.setImageResource(R.drawable.stars)
        holder.itemView.message_container.setBackgroundResource(R.drawable.rounded_background2)
    }
}
```
Add the smile and [stars images](https://github.com/philweier/ssd-android-6day/blob/master/Day4Assets.zip) from todays assets as drawables through the Resource Manager.

![](https://i.imgur.com/cPhK8cN.png)
