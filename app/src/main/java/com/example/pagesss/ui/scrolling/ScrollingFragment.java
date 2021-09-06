package com.example.pagesss.ui.scrolling;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pagesss.R;
import com.example.pagesss.databinding.FragmentScrollingBinding;

public class ScrollingFragment extends Fragment {

    private ScrollingViewModel scrollingViewModel;
    private FragmentScrollingBinding binding;
    TextView listkawasan;
    TextView linkkawasan;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scrollingViewModel =
                new ViewModelProvider(this).get(ScrollingViewModel.class);

        binding = FragmentScrollingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listkawasan = (TextView)root.findViewById(R.id.text_kawasan);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("Pemetaan Kawasan dengan drone");
        ssb.append("\n \n");
        ssb.append("Dilakukan pementaan udara dengan DJI Phantom 4 Pro pada tanggal 18 Juni 2021." +
                "Hasil citra diolah menggunakan Agisoft menghasilkan berbagai jenis pencitraan.");
        ssb.append("\n \n ");
        ssb.setSpan(new ImageSpan(getContext(),R.drawable.dji), ssb.length()-1,ssb.length(),0);
        ssb.append("\n \n ");
        ssb.append("Diperoleh pencitraan udara yang mencakup luas area seluas " +
                "0.44 km persegi. Dari hasil pengolahan data juga dapat dipetakan " +
                "variasi ketinggian tanah. Gambar dipetakan menggunakan koordinat WGS 84. ");
        ssb.append("\n \n ");
        ssb.append("\n \n ");

        listkawasan.setText(ssb);

        scrollingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}