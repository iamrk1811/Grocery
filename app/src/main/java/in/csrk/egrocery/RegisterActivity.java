package in.csrk.egrocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    TextView alreadyRegisterButton;
    EditText userFullName, userPassword, userReenterPassword, userMobile, userEmail, userOTP;
    Button userRegisterButton;
    ProgressBar userRegistrationProgressBar;
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
        userRegistrationProgressBar = findViewById(R.id.userRegistrationProgressBar);


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

                userRegistrationProgressBar.setVisibility(View.VISIBLE);
                userRegisterButton.setClickable(false);

                String uFullName = userFullName.getText().toString().trim();
                String uEmail = userEmail.getText().toString().trim();
                String uMobile = userMobile.getText().toString().trim();
                String uPassword = userPassword.getText().toString();
                String uReenterPassword = userReenterPassword.getText().toString();

//                user validation
//                if (userValidate(uFullName, uEmail, uMobile, uPassword, uReenterPassword)) {
                if (true) {
                    if (!isUserAlreadyExist(uEmail, uMobile)) { // if user already exist

                        String phoneNumber = "+91" + uMobile;

                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(fAuth)
                                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(RegisterActivity.this)                 // Activity (for callback binding)
                                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                userRegistrationProgressBar.setVisibility(View.INVISIBLE);
                                                userRegisterButton.setClickable(true);
                                                Toast.makeText(RegisterActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                                                super.onCodeAutoRetrievalTimeOut(s);
                                                userRegistrationProgressBar.setVisibility(View.INVISIBLE);
                                                userRegisterButton.setClickable(true);
                                                Toast.makeText(RegisterActivity.this, "time out" , Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                super.onCodeSent(verificationID, forceResendingToken);
                                                userRegistrationProgressBar.setVisibility(View.INVISIBLE);
                                                userRegisterButton.setClickable(true);
                                                Toast.makeText(RegisterActivity.this, "Code sent" , Toast.LENGTH_SHORT).show();

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
                                                                String userEnteredOTP = userOTP.getText().toString().trim();

                                                                PhoneAuthCredential userCredential = PhoneAuthProvider.getCredential(verificationID, userEnteredOTP);

                                                                fAuth.signInWithCredential(userCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if (task.isSuccessful()) {
                                                                            startActivity(new Intent(RegisterActivity.this, UserDashBoard.class));
                                                                            finish();
                                                                        }
                                                                    }
                                                                });
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
                                            }

                                            @Override
                                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                                userRegistrationProgressBar.setVisibility(View.INVISIBLE);
                                                userRegisterButton.setClickable(true);
                                                Toast.makeText(RegisterActivity.this, "Verification failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })          // OnVerificationStateChangedCallbacks
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);

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