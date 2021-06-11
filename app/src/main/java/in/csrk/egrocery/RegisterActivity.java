package in.csrk.egrocery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    TextView alreadyRegisterButton;
    EditText userFullName, userPassword, userReenterPassword, userMobile, userEmail, userOTP;
    Button userRegisterButton;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiester);

        //        Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


//        if user already registered then redirect to user dashboard
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
        }

        alreadyRegisterButton = findViewById(R.id.alreadyRegisterButton);
        userEmail = findViewById(R.id.userEmail);
        userFullName = findViewById(R.id.userFullName);
        userMobile = findViewById(R.id.userMobile);
        userPassword = findViewById(R.id.userPassword);
        userReenterPassword = findViewById(R.id.userReenterPassword);
        userRegisterButton = findViewById(R.id.userRegisterButton);


//        already have account then switch to login page
        alreadyRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });



//        handle user register button
        userRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uFullName = userFullName.getText().toString().trim();
                String uEmail = userEmail.getText().toString().trim();
                String uMobile = userMobile.getText().toString().trim();
                String uPassword = userPassword.getText().toString();
                String uReenterPassword = userReenterPassword.getText().toString();

//                user validation
//                if (userValidate(uFullName, uEmail, uMobile, uPassword, uReenterPassword)) {
                if (true) {
                    if (!isUserAlreadyExist(uEmail, uMobile)) { // if user already exist

                        AlertDialog.Builder otpDialog = new AlertDialog.Builder(RegisterActivity.this);
                        otpDialog.setMessage("Enter OTP");
                        otpDialog.setCancelable(true);

                        userOTP = new EditText(RegisterActivity.this);
                        userOTP.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

                        otpDialog.setView(userOTP);

                        otpDialog.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        otpDialog.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        otpDialog.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "User already registered. Please login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

//    checking if user already registered or not
    private boolean isUserAlreadyExist(String email, String mobile) {
        return false;
    }

//    validate user
    private boolean userValidate(String uFullName, String email, String mobile, String password, String reenterPassword) {
        boolean validate = true;

//        checking name
        if (TextUtils.isEmpty(uFullName)) {
            userFullName.setError("Enter your name");
            validate = false;
        }

        if (TextUtils.isEmpty(email)) {
            userEmail.setError("Email is required");
            validate = false;
        }
//      checking mobile
        if (TextUtils.isEmpty(mobile)) {
            userMobile.setError("Mobile is required");
            validate = false;
        }

        if (mobile.length() != 10) {
            userMobile.setError("Mobile no. should be 10 digits");
            validate = false;
        }

        if (TextUtils.isEmpty(password)) {
            userPassword.setError("Password is required");
            validate = false;
        }

        if (password.length() < 8) {
            userPassword.setError("Password should be >= 8");
            validate = false;
        }

        if (TextUtils.isEmpty(reenterPassword)) {
            userReenterPassword.setError("Password is required");
            validate = false;
        }

        if (!password.equals(reenterPassword)) {
            userReenterPassword.setError("Password not matching");
            userPassword.setError("Password not matching");
            validate = false;
        }

        return true;
    }
}