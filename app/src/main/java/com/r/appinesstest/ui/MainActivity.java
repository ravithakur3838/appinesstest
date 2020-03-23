package com.r.appinesstest.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.r.appinesstest.R;
import com.r.appinesstest.databinding.ActivityMainBinding;
import com.r.appinesstest.repo.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private ActivityMainBinding mBinding;
    private List<ResponseModel> taskList;
    private RecylerViewAdapter adapter;
    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
        setClickListeners();
        setAdapter();
        apiCall();
    }

    private void init() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    private void setClickListeners() {
        mBinding.imageViewSearch.setOnClickListener(this);
        mBinding.editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    search(mBinding.editTextSearch.getText().toString().trim());
                }
                return false;
            }
        });
    }

    private void search(String query) {
        List<ResponseModel> searchList = new ArrayList<>();
        query = query.toLowerCase();
        if (mainViewModel.getResponseListObservable().getValue() != null) {
            if (query.isEmpty()) {
                searchList.addAll(mainViewModel.getResponseListObservable().getValue());
            } else {
                for (ResponseModel data : mainViewModel.getResponseListObservable().getValue()) {
                    if (data.getTitle().toLowerCase().contains(query)) {
                        searchList.add(data);
                    }
                }
            }

            if (searchList.size() > 0) {
                adapter.addAll(searchList);
                mBinding.recyclerView.setVisibility(View.VISIBLE);
                mBinding.textViewNoDataFound.setVisibility(View.GONE);
            } else {
                mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
                mBinding.recyclerView.setVisibility(View.GONE);
            }
        }
    }


    private void apiCall() {
        if (isOnline()) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            mainViewModel.getResponseListObservable().observe(this, data -> {
                if (data.size() > 0) {
                    adapter.addAll(data);
                    mBinding.recyclerView.setVisibility(View.VISIBLE);
                    mBinding.textViewNoDataFound.setVisibility(View.GONE);
                } else {
                    mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
                    mBinding.recyclerView.setVisibility(View.GONE);
                }
                mBinding.progressBar.setVisibility(View.GONE);
            });
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Alert")
                .setMessage("Check network connection!")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                })
                .show();
    }

    private void setAdapter() {
        taskList = new ArrayList<>();
        final int defaultPadding = getResources().getDimensionPixelOffset(R.dimen.padding_16);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        mBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                outRect.top = position == 0 ? defaultPadding / 2 : defaultPadding / 4;
                if (parent.getAdapter() != null)
                    outRect.bottom = position == parent.getAdapter().getItemCount() - 1 ? defaultPadding / 2 : defaultPadding / 4;
                outRect.left = defaultPadding / 2;
                outRect.right = defaultPadding / 2;
            }
        });
        adapter = new RecylerViewAdapter(taskList);
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_view_search) {
            if (mBinding.editTextSearch.getVisibility() == View.VISIBLE) {
                mBinding.editTextSearch.setVisibility(View.GONE);
                mBinding.imageViewSearch.setImageResource(R.drawable.ic_search_white);
                mBinding.editTextSearch.setText("");
                resetList();
                hideKeyboard();
            } else {
                mBinding.imageViewSearch.setImageResource(R.drawable.ic_cross);
                mBinding.editTextSearch.setVisibility(View.VISIBLE);
                mBinding.editTextSearch.requestFocus();
            }
        }
    }

    private void resetList() {
        taskList = mainViewModel.getResponseListObservable().getValue();
        adapter.addAll(taskList);
        mBinding.textViewNoDataFound.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting() && netInfo.isAvailable() && netInfo.isConnected();
    }
}
