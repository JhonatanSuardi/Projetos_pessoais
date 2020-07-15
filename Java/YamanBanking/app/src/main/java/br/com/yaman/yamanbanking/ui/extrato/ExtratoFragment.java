package br.com.yaman.yamanbanking.ui.extrato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import br.com.yaman.yamanbanking.R;
import br.com.yaman.yamanbanking.ui.main.SectionsPagerAdapter;

public class ExtratoFragment extends Fragment {

    private ExtratoViewModel extratoViewModel;

    private TabLayout tabs;
    private ViewPager viewPager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        extratoViewModel =
                ViewModelProviders.of(this).get(ExtratoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_extrato, container, false);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getChildFragmentManager());
        viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//        SectionPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext())
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        extratoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}
