package fall18_207project.GameCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String emailValue;
    public final static String ACCOUNT_MANAGER_DATA = "accountManager.ser";
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountManager = new AccountManager();
        readFromSer(ACCOUNT_MANAGER_DATA);
        saveToFile(ACCOUNT_MANAGER_DATA);
        firebaseAuth = FirebaseAuth.getInstance();
        addLoginButtonListener();
        addRegisterButtonListener();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String emailValue = currentUser.getEmail();

            GlobalScoreBoardActivity.userEmail = emailValue;
            GameCentreActivity.userEmail = emailValue;
            SavedGamesActivity.userEmail = emailValue;
            Game2048StartActivity.userEmail = emailValue;
            Game2048Activity.userEmail = emailValue;
            StartingActivity.userEmail = emailValue;
            GameActivity.userEmail = emailValue;
            MatchingCardStartActivity.userEmail = emailValue;
            MatchingCardsGameActivity.userEmail = emailValue;
            GameFinishActivity.userEmail = emailValue;
            UserHistoryActivity.userEmail = emailValue;

            firebaseAuth.signOut();
//            Intent goToCenter = new Intent(getApplicationContext(), GameCentreActivity.class);
//            startActivity(goToCenter);
        }
    }

    private void addLoginButtonListener() {
        Button login = findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = findViewById(R.id.EmailLogin);
                EditText password = findViewById(R.id.passwordtext);
                emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                userLogin(emailValue, passwordValue);
            }
        });
    }

    private void addRegisterButtonListener() {
        Button register = findViewById(R.id.registerbtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(goToRegister);
            }
        });
    }

    private void readFromSer(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream in = new ObjectInputStream(inputStream);
                accountManager = (AccountManager) in.readObject();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("Login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("Login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(accountManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void userLogin(final String email, String password) {
        if (!validateForm()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LoginActivity", "sign in successful!");
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            GlobalScoreBoardActivity.userEmail = email;
                            GameCentreActivity.userEmail = email;
                            SavedGamesActivity.userEmail = email;
                            Game2048StartActivity.userEmail = email;
                            Game2048Activity.userEmail = email;
                            StartingActivity.userEmail = email;
                            GameActivity.userEmail = email;
                            MatchingCardStartActivity.userEmail = email;
                            MatchingCardsGameActivity.userEmail = email;
                            GameFinishActivity.userEmail = email;
                            UserHistoryActivity.userEmail = email;

                            Intent goToCenter = new Intent(getApplicationContext(), GameCentreActivity.class);
//                    goToCenter.putExtra("userEmail", accountManager.getAccount(emailValue).getUserName());
                            startActivity(goToCenter);
                        } else {
                            Log.d("LoginActivity", "sign in failed");
                            Toast.makeText(LoginActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        EditText emailValue = findViewById(R.id.EmailLogin);
        String email = emailValue.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailValue.setError("Required.");
            valid = false;
        } else {
            emailValue.setError(null);
        }

        EditText passwordValue = findViewById(R.id.passwordtext);
        String password = passwordValue.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordValue.setError("Required.");
            valid = false;
        } else {
            passwordValue.setError(null);
        }

        return valid;
    }
}
