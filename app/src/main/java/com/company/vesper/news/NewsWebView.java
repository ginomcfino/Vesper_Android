package com.company.vesper.news;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.vesper.R;
import com.company.vesper.databinding.FragmentNewsWebViewBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NewsWebView extends Fragment {

    private final String url;

    public NewsWebView(String url) {
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewsWebViewBinding binding = FragmentNewsWebViewBinding.inflate(inflater);
        binding.webview.loadUrl(url);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}