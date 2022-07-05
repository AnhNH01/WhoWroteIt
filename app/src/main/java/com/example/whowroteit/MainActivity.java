package com.example.whowroteit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

public class MainActivity extends AppCompatActivity {

    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton;
        mBookInput = findViewById(R.id.bookInput);
        mTitleText = findViewById(R.id.titleText);
        mAuthorText = findViewById(R.id.authorText);
        mButton = findViewById(R.id.searchButton);

        if (LoaderManager.getInstance(this).getLoader(0) != null) {
            LoaderManager.getInstance(this).initLoader(0, null, new Callbacks(this, mTitleText, mAuthorText));
        }

        mButton.setOnClickListener(view -> {
            String queryString = mBookInput.getText().toString();
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }

            if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
                //new FetchBook(mTitleText, mAuthorText).execute(queryString);
                Bundle queryBundle = new Bundle();
                queryBundle.putString("queryString", queryString);
                LoaderManager.getInstance(MainActivity.this).restartLoader(0, queryBundle, new Callbacks(MainActivity.this, mTitleText, mAuthorText));
                mAuthorText.setText("");
                mTitleText.setText(R.string.loading);
            } else {
                if (queryString.length() == 0) {
                    mAuthorText.setText("");
                    mTitleText.setText(R.string.no_search_term);
                } else {
                    mAuthorText.setText("");
                    mTitleText.setText(R.string.no_network);
                }
            }
        });

    }

}