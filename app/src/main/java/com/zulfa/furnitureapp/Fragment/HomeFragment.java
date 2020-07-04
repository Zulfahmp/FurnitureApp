package com.zulfa.furnitureapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.zulfa.furnitureapp.List.ListKursiActivity;
import com.zulfa.furnitureapp.List.ListMejaActivity;
import com.zulfa.furnitureapp.R;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private ImageView cardView, cardView2, cardView3, cardView4;
    private ImageView mLemari, mMeja, mSofa, mKursi;
    private SliderLayout sliderLayout;
    ImageView kursi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        cardView = view.findViewById(R.id.ivLemari);
        cardView2 = view.findViewById(R.id.ivMeja);
        cardView3 = view.findViewById(R.id.ivSofa);
        cardView4 = view.findViewById(R.id.ivKursi);
        init(view);
        imageslider();
        onClick();
    }

    private void init(View view) {
        mLemari = view.findViewById(R.id.ivArLemari);
        mMeja = view.findViewById(R.id.ivArMeja);
        mSofa = view.findViewById(R.id.ivArSofa);
        mKursi = view.findViewById(R.id.ivArKursi);
    }

    private void onClick() {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListMejaActivity.class));
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ListKursiActivity.class));
            }
        });

        mKursi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void imageslider() {
        // Load image dari URL
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        // Load Image Dari res/drawable
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Diskon Besar Besaran",R.drawable.pertama);
        file_maps.put("Ayo Makan",R.drawable.kedua);
        file_maps.put("Ayo njoget",R.drawable.ketiga);
        file_maps.put("Mendem Anggur Lur?", R.drawable.anggur);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Toast.makeText(getContext(), "klik", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
    }
}
