package com.lelangapa.android.activities.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.fragments.search.MainSearchTextChangeFragment;
import com.lelangapa.android.fragments.search.MainSearchTextSubmitFragment;

/**
 * Created by andre on 15/12/16.
 */

public class MainSearchActivity extends AppCompatActivity {
    private TextView textView;
    private MainSearchTextChangeFragment textChangeFragment;
    private MainSearchTextSubmitFragment textSubmitFragment;
    private SearchView searchView;
    private Fragment currentFragment;
    private boolean switchToTextChangeFragment;
    private boolean switchToTextQuerySubmitFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initializeConstants();
        setContentView(R.layout.activity_main_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textChangeFragment = new MainSearchTextChangeFragment();
        textSubmitFragment = new MainSearchTextSubmitFragment();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        handleIntent(getIntent());
    }
    /*@Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            textView.setText(intent.getStringExtra(SearchManager.QUERY));
        }
    }*/
    private void initializeConstants()
    {
        currentFragment = null;
        switchToTextChangeFragment = false;
        switchToTextQuerySubmitFragment = false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!switchToTextQuerySubmitFragment){
                    currentFragment = getFragmentManager().findFragmentByTag("FRAGMENT_TEXT_SUBMIT");
                    if (currentFragment==null)
                    {
                        textSubmitFragment.submitQuery(query);
                        textSubmitFragment.clearListBarang();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_search_textchange, textSubmitFragment, "FRAGMENT_TEXT_SUBMIT")
                                .commit();
                    }
                    switchToTextQuerySubmitFragment=true;
                    switchToTextChangeFragment=false;
                }
                else
                {

                }

                /*String done = query + " - submitted";
                textView.setText(done);*/
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!switchToTextChangeFragment){
                    currentFragment = getFragmentManager().findFragmentByTag("FRAGMENT_TEXT_CHANGE");
                    if (currentFragment==null)
                    {
                        textChangeFragment.setTextInit(newText);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_search_textchange, textChangeFragment, "FRAGMENT_TEXT_CHANGE")
                                .commit();
                    }
                    switchToTextChangeFragment = true;
                    switchToTextQuerySubmitFragment = false;
                }
                else {
                    textChangeFragment.changeText(newText);
                }
                //Toast.makeText(MainSearchActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //searchView.setIconifiedByDefault(false);
        return super.onCreateOptionsMenu(menu);
    }
}
