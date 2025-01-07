package martinezruiz.javier.pmdm003;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.util.GoogleApiUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import martinezruiz.javier.pmdm003.databinding.ActivityMainBinding;
import martinezruiz.javier.pmdm003.ui.pokedex.PokedexViewModel;
import martinezruiz.javier.pmdm003.ui.settings.SettingsViewModel;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAIN aCTIVITY ON  CREATE", "MAIN2W2");

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            userEmail = extras.getString("email");
            Log.d("ONCREATE: ", userEmail);
        }
        PokedexViewModel viewModel = new ViewModelProvider(this).get(PokedexViewModel.class);
        viewModel.setUserEmail(userEmail);

//        EdgeToEdge.enable(this);

        //antes de obtener una instancia de esta clase hay que manipular el gradle
        // y añadirle las buildFeatures { viewBinding = true}
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //obtenemos una instancia del controller de navegación
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        //bindeamos la barra de navegación con el controlador
        NavigationUI.setupWithNavController(binding.navViewBottom, navController);


        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.getLoginState().observe(this, state->{
            if(!state){
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainActivity.this, R.string.sesion_cerrada, Toast.LENGTH_SHORT).show();
                                goToLogin();
                            }
                        });
            }
        });

        setApplicationLocale();
        // Initialize Firebase Auth
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void goToLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    /**
     *
     */
    private void setApplicationLocale(){

        SharedPreferences sp = getSharedPreferences(getString(R.string.preference_file_settings), Context.MODE_PRIVATE);
        String prefLanguage = sp.getString(getString(R.string.language_preference_key),null);

        //Solo hacemos esto si no hay preferencias de lenguaje. Si las hay, ya usamos el setApplicationLocales en
        //el settings fragment. Cómo lo usamos allí, ya recreamos la actividad
        if(prefLanguage == null){ //si no hay preferencia de idioma  establecemos el que tenga configurado el sistema
            prefLanguage = Resources.getSystem().getConfiguration().getLocales().get(0).toLanguageTag();
            LocaleListCompat localeList = LocaleListCompat.forLanguageTags(prefLanguage);
            AppCompatDelegate.setApplicationLocales(localeList);
        }
    }




    NavController navController;
    private String userEmail;

}
