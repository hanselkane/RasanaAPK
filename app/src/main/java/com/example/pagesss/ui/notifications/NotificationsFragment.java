package com.example.pagesss.ui.notifications;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
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
import com.example.pagesss.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    TextView texttentang;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        texttentang = (TextView)root.findViewById(R.id.text_tentang);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("Rasana Rasyidah");
        ssb.append("\n \n");
        ssb.append("Ketua : Tantan Rustandi (08XX-XXXX-XXXX)");
        ssb.append("\n \n");
        ssb.append("Koordinator : Oci (08XX-XXXX-XXXX)");
        ssb.append("\n \n");
        ssb.append("Bekerjasama dengan Fisika ITB");
        ssb.append("\n \n");
        ssb.append("\n \n  ");
        ssb.setSpan(new ImageSpan(getContext(),R.drawable.rasanarasyidah), ssb.length()-1,ssb.length(),0);
        ssb.append("\n \n  ");
        ssb.setSpan(new ImageSpan(getContext(),R.drawable.itb), ssb.length()-1,ssb.length(),0);
        ssb.append("\n \n  ");
        ssb.append("\n \n  ");
        ssb.append("\n \n  ");
        ssb.append("\n \n  ");
        texttentang.setText(ssb);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}