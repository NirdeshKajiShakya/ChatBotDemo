# 🔑 How to Get an OpenAI API Key

This guide will walk you through creating an OpenAI API key. OpenAI provides a free tier with a starting credit that is more than enough for testing this application.

---

## Step 1: Sign Up for OpenAI

1.  Go to the OpenAI platform website:
    [https://platform.openai.com/](https://platform.openai.com/)

2.  Click on **"Sign up"** to create a new account. You can use your Google, Microsoft, Apple account, or a standard email/password.

3.  You may need to verify your phone number to complete the setup. This is for security purposes.

---

## Step 2: Go to the API Keys Section

1.  Once you are logged in, navigate directly to the API keys page:
    [https://platform.openai.com/api-keys](https://platform.openai.com/api-keys)

    ![OpenAI API Keys Page](https://i.imgur.com/H9g2k6E.png)

---

## Step 3: Create a New Secret Key

1.  On the API keys page, click the button that says **"+ Create new secret key"**.

2.  A dialog box will appear. You can give the key a name for your own reference, for example, `TestingProjectKey`. This is optional.

3.  Click the **"Create secret key"** button.

    ![Create Secret Key](https://i.imgur.com/T3B3d5c.png)

4.  **This is the only time you will see the full key.** A dialog will show your new key. Click the copy icon to copy it to your clipboard.

    **Do not close this window until you have pasted the key somewhere safe.**

---

## Step 4: Add the New Key to Your Project

Now, let's put your new key into the app.

1.  Go back to your Android Studio project.
2.  Open the `local.properties` file.
3.  You will see the placeholder:
    ```properties
    OPENAI_API_KEY=your_openai_api_key_here
    ```
4.  **Delete the placeholder** and paste your **newly copied key**:
    ```properties
    # Make sure there are no extra spaces before or after the key
    OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    ```

---

## Step 5: Rebuild and Run

To ensure the app uses the new key:

1.  In Android Studio, go to **Build → Clean Project**.
2.  Then, go to **Build → Rebuild Project**.
3.  **Uninstall the old app** from your phone.
4.  Click the **Run** (▶️) button to install the fresh app.

---

## Step 6: Test

1.  Open the app.
2.  Type "hello" and click send.

The app should now communicate successfully with the OpenAI API and give you a proper response.

