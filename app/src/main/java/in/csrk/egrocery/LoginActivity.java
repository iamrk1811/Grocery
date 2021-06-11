
package in.csrk.egrocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUserLoginPhoneNumber, editTextUserLoginPassword;
    private Button buttonUserLoginButton;
    private TextView textViewUserForgotPassword, textViewUserCreateAccount;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserLoginPhoneNumber = findViewById(R.id.userLoginPhoneNumberEditText);
        editTextUserLoginPassword = findViewById(R.id.userLoginPasswordEditText);
        buttonUserLoginButton = findViewById(R.id.userLoginButton);
        textViewUserCreateAccount = findViewById(R.id.userLoginCreateAccountTextView);
        textViewUserForgotPassword = findViewById(R.id.userForgotPasswordTextView);


//        firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

//      go to register page if already have an account
        textViewUserCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });


        buttonUserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMobileText = editTextUserLoginPhoneNumber.getText().toString().trim();
                String userPassword = editTextUserLoginPassword.getText().toString().trim();

                if (validateCredential(userMobileText, userPassword) && userExist(userMobileText, userPassword)) {
                    startActivity(new Intent(LoginActivity.this, UserDashBoard.class));
                    finish();
                }
            }
        });

    }
    private boolean validateCredential(String mobile, String password) {
        if (mobile.length() != 10) return false;
        if (password.length() < 8) return false;
        return true;
    }
    private boolean userExist(String mobile, String password) {

        return true;
    }
}