package martinezruiz.javier.pmdm003;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.identity.CredentialSavingClient;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


    }


    private void signIn() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setLogo(R.drawable.loginicon)     // Set logo drawable
                .setTheme(R.style.Base_Theme_PMDM003)      // Set theme
                .build();
        signInLauncher.launch(signInIntent);
        //javiermaruiz@gmail.com

    }

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);

                }
            }
    );


    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            user = mAuth.getCurrentUser();
            goToMainActivity(user.getEmail());
        } else {


            Snackbar snackLogin = Snackbar.make(this.findViewById(result.getResultCode()),
                    "Error en el logado",
                    3000);
            snackLogin.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

        if (user!=null) {
            String email = user.getEmail();
            goToMainActivity(email);
        }
        else {
            signIn();
        }
    }

    /**
     * Vamos al main activity con el email del user logado
     * @param email
     */
    private void goToMainActivity(String email) {

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("email", email);
        startActivity(i);
        finish();
    }


    private FirebaseAuth mAuth;
    private FirebaseUser user;

}
