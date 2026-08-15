package com.harsh.visionx_signbridge.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.harsh.visionx_signbridge.OfflinePackFragment;
import com.harsh.visionx_signbridge.R;

public class HomeFragment extends Fragment {

    EditText etSearchSigns;
    TextView ivtextanim,tvQuickAction;

    MaterialCardView cardEmergency;
    MaterialCardView cardDailyPhrases;
    MaterialCardView cardLearnSign;
    MaterialCardView cardOfflinePack;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );



        etSearchSigns = view.findViewById(
                R.id.etSearchSigns
        );
        tvQuickAction = view.findViewById(
                R.id.tvQuickAction
        );





        ivtextanim = view.findViewById(
                R.id.ivtextanim
        );

        Animation animation = AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.luxaryanim
        );

        ivtextanim.startAnimation(animation);



        cardEmergency = view.findViewById(
                R.id.cardEmergency
        );

        cardDailyPhrases = view.findViewById(
                R.id.cardDailyPhrases
        );

        cardLearnSign = view.findViewById(
                R.id.cardLearnSign
        );

        cardOfflinePack = view.findViewById(
                R.id.cardOfflinePack
        );


        cardEmergency.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        SignFragment signFragment =
                                new SignFragment();

                        Bundle bundle =
                                new Bundle();

                        bundle.putString(
                                "category",
                                "Emergency"
                        );

                        signFragment.setArguments(
                                bundle
                        );

                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.homeFrameLayout,
                                        signFragment
                                )
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );




        cardDailyPhrases.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        SignFragment signFragment =
                                new SignFragment();

                        Bundle bundle =
                                new Bundle();

                        bundle.putString(
                                "category",
                                "Daily Phrases"
                        );

                        signFragment.setArguments(
                                bundle
                        );

                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.homeFrameLayout,
                                        signFragment
                                )
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );




        cardLearnSign.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        SignFragment
                                learnSignFragment =
                                new SignFragment();

                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.homeFrameLayout,
                                        learnSignFragment
                                )
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );


        // =========================
        // OFFLINE PACK
        // =========================

        cardOfflinePack.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        OfflinePackFragment
                                offlinePackFragment =
                                new OfflinePackFragment();

                        requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.homeFrameLayout,
                                        offlinePackFragment
                                )
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );


        // =========================
        // SEARCH SIGNS
        // =========================

        etSearchSigns.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(
                            TextView v,
                            int actionId,
                            KeyEvent event) {

                        if (actionId ==
                                EditorInfo.IME_ACTION_SEARCH
                                ||
                                (event != null
                                        && event.getKeyCode()
                                        == KeyEvent.KEYCODE_ENTER)) {

                            String search =
                                    etSearchSigns
                                            .getText()
                                            .toString()
                                            .trim();

                            if (!search.isEmpty()) {

                                SignFragment
                                        signFragment =
                                        new SignFragment();

                                Bundle bundle =
                                        new Bundle();

                                bundle.putString(
                                        "search",
                                        search
                                );

                                signFragment.setArguments(
                                        bundle
                                );

                                requireActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(
                                                R.id.homeFrameLayout,
                                                signFragment
                                        )
                                        .addToBackStack(null)
                                        .commit();
                            }

                            return true;
                        }

                        return false;
                    }
                }
        );


        return view;
    }
}