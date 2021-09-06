package com.example.pagesss.ui.dashboard;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.pagesss.R;
import com.example.pagesss.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {
//Dashboard == komoditas
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    ExpandableListView listView;
    List<String> listGroup;
    HashMap<String,List<SpannableStringBuilder>> listItem;
    MainAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView=(ExpandableListView)root.findViewById(R.id.expandable_listview);

        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(getContext(),listGroup,listItem);
        listView.setAdapter((ExpandableListAdapter)adapter);
        initListdata();

//        ArrayList<String> arrayList = new ArrayList<>();
//
//        arrayList.add("Tomat Beef");
//        arrayList.add("Buah Tin");
//        arrayList.add("Apel");
//
//        ArrayAdapter arrayAdapter =new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1,arrayList);
//        listView.setAdapter(arrayAdapter);

        return root;
    }

    private void initListdata() {
        listGroup.add(getString((R.string.group1)));
        listGroup.add(getString((R.string.group2)));
        listGroup.add(getString((R.string.group3)));
        listGroup.add(getString((R.string.group4)));
        listGroup.add(getString((R.string.group5)));
        listGroup.add(getString((R.string.group6)));
        listGroup.add(getString((R.string.group7)));
        listGroup.add(getString((R.string.group8)));
        listGroup.add(getString((R.string.group9)));

//        SpannableString[] array;

        List<SpannableStringBuilder> list1 = new ArrayList<>();
        List<SpannableStringBuilder> list2 = new ArrayList<>();
        List<SpannableStringBuilder> list3 = new ArrayList<>();
        List<SpannableStringBuilder> list4 = new ArrayList<>();
        List<SpannableStringBuilder> list5 = new ArrayList<>();
        List<SpannableStringBuilder> list6 = new ArrayList<>();
        List<SpannableStringBuilder> list7 = new ArrayList<>();
        List<SpannableStringBuilder> list8 = new ArrayList<>();
        List<SpannableStringBuilder> list9 = new ArrayList<>();

        SpannableStringBuilder ssb1 = new SpannableStringBuilder();
        SpannableStringBuilder ssb2 = new SpannableStringBuilder();
        SpannableStringBuilder ssb3 = new SpannableStringBuilder();
        SpannableStringBuilder ssb4 = new SpannableStringBuilder();
        SpannableStringBuilder ssb5 = new SpannableStringBuilder();
        SpannableStringBuilder ssb6 = new SpannableStringBuilder();
        SpannableStringBuilder ssb7 = new SpannableStringBuilder();
        SpannableStringBuilder ssb8 = new SpannableStringBuilder();
        SpannableStringBuilder ssb9 = new SpannableStringBuilder();

        ssb1.append("Paprika Rasana Rasyidah Garut");
        ssb1.append("\n  \n");
        ssb1.append("Rasana Rasyidah melihat peluang usaha paprika masih dapat" +
                " dikembangkan karena belum banyak petani yang memproduksi paprika " +
                "dan harganya yang relatif stabil bahkan cenderung meningkat pada bulan tertentu. " +
                "Dengan waktu budidaya sekitar 6 bulan, paprika juga memiliki potensi pasar yang besar, baik lokal maupun internasional.");
        ssb1.append("\n  \n ");
        ssb1.setSpan(new ImageSpan(getContext(),R.drawable.paprika), ssb1.length()-1,ssb1.length(),0);
        ssb1.append("\n \n ");
        ssb1.append("Grade paprika yang diproduksi di Rasana Rasyidah adalah paprika grade A. " +
                "Jenis meliputi paprika merah, hijau, kuning. Ketersediaan barang mengikuti musim tertentu");
        ssb1.append("\n  \n ");
        ssb1.setSpan(new ImageSpan(getContext(),R.drawable.paprikahilir), ssb1.length()-1,ssb1.length(),0);
        ssb1.append("\n  \n ");
        ssb1.append("Pengolahan juga dilakukan pada hasil panen paprika, meliputi paprika bar dan paprika bubuk " +
                "telah dikemas dan siap jual (ketersediaan bergantung musim)");
        ssb1.append("\n  \n ");
        list1.add(ssb1);

        ssb2.append("Tomat Beef Rasana Rasyidah");
        ssb2.append("\n  \n ");
        ssb2.append("Produksi Tomat Beef juga merupakan salah satu komoditas unggulan" +
                " di Rasana Rasyidah, hanya sedikit petani yang melakukan budidaya tomat beef." +
                "Harga komoditas ini relatif stabil dan memiliki potensi pasar yang besar baik lokal " +
                "maupun ekspor. Tomat Beef memiliki waktu budidaya sekitar 6 bulan, dan penanamannya " +
                "bergantung musim.");
        ssb2.append("\n  \n ");
        ssb2.setSpan(new ImageSpan(getContext(),R.drawable.tomat), ssb2.length()-1,ssb2.length(),0);
        ssb2.append("\n  \n ");
        list2.add(ssb2);

        ssb3.append("Kiuri Rasana Rasyidah");
        ssb3.append("\n  \n ");
        ssb3.setSpan(new ImageSpan(getContext(),R.drawable.kiuritanah), ssb3.length()-1,ssb3.length(),0);
        ssb3.append("\n  \n ");
        ssb3.append("Kiuri atau timun jepang juga dibudidayakan di Rasana Rasyidah. Potensi pasar dari" +
                " kiuri ini juga terbilang besar dengan permintaan di pulau Jawa dan Bali sekitar 2 ton per hari. " +
                "Belum banyak petani yang membudidayakan timun jepang ini. Biasanya dikonsumsi oleh katering, restoran, maupun hotel. " +
                "Memiliki potensi ekspor ke timur tengah, Singapore, Taiwan, Jepang, dll.");
        ssb3.append("\n  \n ");
        ssb3.setSpan(new ImageSpan(getContext(),R.drawable.kiuri), ssb3.length()-1,ssb3.length(),0);
        ssb3.append("\n  \n ");
        list3.add(ssb3);

        ssb4.append("Sayur Hidroponik Rasana Rasyidah");
        ssb4.append("\n  \n ");
        ssb4.setSpan(new ImageSpan(getContext(),R.drawable.sayurantanah), ssb4.length()-1,ssb4.length(),0);
        ssb4.append("\n  \n ");
        ssb4.append("Sayuran hidroponik atau rakit apung juga menjadi komoditas unggulan yang dibudidayakan di " +
                "Rasana Rasyidah. Sayur mayur tersebut meliputi, selada keriting, ceisim, kangkung, pakcoy dengan musim - musim penanaman" +
                " tertentu. Proses penanaman dan pemeliharaan juga menjadi sarana belajar bagi murid - murid di SMK ini." +
                " Potensi pasar dari" + " sayur mayur ini juga terbilang besar.");
        ssb4.append("\n  \n ");
        ssb4.setSpan(new ImageSpan(getContext(),R.drawable.sayuran), ssb4.length()-1,ssb4.length(),0);
        ssb4.append("\n  \n ");
        list4.add(ssb4);

        ssb5.append("Bawang Merah Rasana Rasyidah");
        ssb5.append("\n  \n ");
        ssb5.setSpan(new ImageSpan(getContext(),R.drawable.bawangtanah), ssb5.length()-1,ssb5.length(),0);
        ssb5.append("\n  \n ");
        ssb5.append("Rasana Rasyidah juga membudidayakan bawang merah, baik untuk kebutuhan lokal maupun ekspor. Permintaan " +
                "bawang merah yang cenderung konsisten menjadi pilihan dikala pandemik Covid-19 ini. Digunakan 2 cara " +
                "penanaman yang berbeda untuk membudidayakan bawang merah, yaitu cara polybag dan penanaman di tanah secara langsung. " +
                "Proses pembudidayaan bawang merah ini juga dapat menjadi sarana belajar murid - murid SMK Rasana Rasyidah.");
        ssb5.append("\n  \n ");
        ssb5.setSpan(new ImageSpan(getContext(),R.drawable.bawang), ssb5.length()-1,ssb5.length(),0);
        ssb5.append("\n  \n ");
        ssb5.append("Potensi ekonomi bawang merah juga terbilang besar, baik lokal maupun ekspor.");
        ssb5.append("\n  \n ");
        list5.add(ssb5);

        ssb6.append("Padi Rasana Rasyidah");
        ssb6.append("\n  \n ");
        ssb6.setSpan(new ImageSpan(getContext(),R.drawable.padi), ssb6.length()-1,ssb6.length(),0);
        ssb6.append("\n  \n ");
        ssb6.append("Padi polybag juga merupakan salah satu dari komoditas yang dibudidayakan di Rasana Rasyidah. Padi memiliki potensi " +
                "ekonomi yang besar baik dalam maupun luar negri. Waktu budidaya tanamana padi terbilang singkat, sekitar 3 hingga 4 bulan. " +
                "Proses budidaya padi juga menjadi sarana pembelajaran bagi murid - murid di Rasana Rasyidah. Permintaan yang cukup konsisten " +
                "dikala pandemik Covid-19 ini membuat Rasana Rasyidah membudidayakan padi polybag.");
        ssb6.append("\n  \n ");
        list6.add(ssb6);

        ssb7.append("Buah Tin Rasana Rasyidah");
        ssb7.append("\n  \n ");
        ssb7.append("Salah satu budidaya yang menarik di Rasana Rasyidah adalah budidaya buah Tin. Belum banyak yang mengetahui " +
                "keberadaan buah tin ini. Buah tin atau disebut juga buah ara memiliki berbagai fungsi " +
                "salah satunya melancarkan pencernaan dan membantu mengatur gula darah.");
        ssb7.append("\n  \n ");
        ssb7.setSpan(new ImageSpan(getContext(),R.drawable.tin), ssb7.length()-1,ssb7.length(),0);
        ssb7.append("\n  \n ");
        ssb7.append("Ketersediaan buah tin di Rasana Rasyidah bergantung musim.");
        list7.add(ssb7);

        ssb8.append("Anggur Brazil Rasana Rasyidah");
        ssb8.append("\n  \n ");
        ssb8.setSpan(new ImageSpan(getContext(),R.drawable.anggur), ssb8.length()-1,ssb8.length(),0);
        ssb8.append("\n  \n ");
        ssb8.append("Anggur Brazil juga dibudidayakan di Rasana Rasyidah. Waktu penanaman anggur brazil ini sangat lama yakni " +
                "selama 8 tahun hingga berbuah, jika ditanam dari biji. Olehkarena waktu penanamannya yang sangat lama " +
                "Rasana Rasyidah tidak memfokuskan produksi anggur brazil ini, walaupun harga jualnya tinggi. " +
                "Buah ini juga terkadang disebut kupa Balanda.");
        ssb8.append("\n  \n ");
        list8.add(ssb8);

        ssb9.append("Produk Hilir Rasana Rasyidah");
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.triseka), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.trisekabatang), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.trisekapropolis), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        ssb9.append("Hasil olahan berbagai komoditas pertanian kemudian diolah menghasilkan berbagi produk siap jual. Produk - produk" +
                " kebersihan meliputi, sabun (cair dan batang) dan pasta gigi Triseka. Maupun makanan olahan seperti abon, nugget, bakso, " +
                "telur asin, dan lain - lain. Produk yang dihasilkan sangat variatif dengan berbagai proses pengolahan yang berbeda - beda. " +
                "Program agroindustri ini selain menghasilkan berbagai produk juga merupakan sarana pembelajaran bagi SMK Rasana Rasyidah.");
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.seladakedelai), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.jamurmerah), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.jahebawang), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.baksotelur), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        ssb9.setSpan(new ImageSpan(getContext(),R.drawable.abonnuget), ssb9.length()-1,ssb9.length(),0);
        ssb9.append("\n  \n ");
        list9.add(ssb9);

        listItem.put(listGroup.get(0),list1);
        listItem.put(listGroup.get(1),list2);
        listItem.put(listGroup.get(2),list3);
        listItem.put(listGroup.get(3),list4);
        listItem.put(listGroup.get(4),list5);
        listItem.put(listGroup.get(5),list6);
        listItem.put(listGroup.get(6),list7);
        listItem.put(listGroup.get(7),list8);
        listItem.put(listGroup.get(8),list9);

        adapter.notifyDataSetChanged();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}