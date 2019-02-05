package es.iessaladillo.pedrojoya.pr08.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import es.iessaladillo.pedrojoya.pr08.R;
import es.iessaladillo.pedrojoya.pr08.databinding.FragmentMainBinding;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    private SharedPreferences.OnSharedPreferenceChangeListener onSharePreferencesChangeListener;
    private SharedPreferences settings;
    private TextView lblLorem;
    private NavController navController;
    private FragmentMainBinding b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        settings.registerOnSharedPreferenceChangeListener(onSharePreferencesChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        settings.unregisterOnSharedPreferenceChangeListener(onSharePreferencesChangeListener);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        onSharePreferencesChangeListener = (sharedPreferences, key) -> changeLorem();
        settings = PreferenceManager.getDefaultSharedPreferences(requireContext());
        changeLorem();
    }

    private void changeLorem() {
        String text;

        if (TextUtils.equals(settings.getString(getString(R.string.prefLoremType_key),
                getString(R.string.prefLoremType_defaultValue)),
                getString(R.string.prefLoremType_defaultValue))) {
            text = getString(R.string.main_latin_ipsum);
        } else {
            text = getString(R.string.main_chiquito_ipsum);
        }

        lblLorem.setText(text);
    }

    private void setupViews() {
        setupToolbar();
        FloatingActionButton fab = b.fab;
        lblLorem = b.lblLorem;
        fab.setOnClickListener(v -> navController.navigate(R.id.actionMainToDetail));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = b.toolbar;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
    }

}
