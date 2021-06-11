
package in.csrk.egrocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUserLoginPhoneNumber, editTextUserLoginPassword;
    Button buttonUserLoginButton;
    TextView textViewUserForgotPassword, textViewUserCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserLoginPhoneNumber = findViewById(R.id.userLoginPhoneNumberEditText);
        editTextUserLoginPassword = findViewById(R.id.userLoginPasswordEditText);
        buttonUserLoginButton = findViewById(R.id.userLoginButton);
        textViewUserCreateAccount = findViewById(R.id.userLoginCreateAccountTextView);
        textViewUserForgotPassword = findViewById(R.id.userForgotPasswordTextView);

//      go to register page if already have an account
        textViewUserCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

    }
}