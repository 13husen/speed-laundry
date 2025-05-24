package com.speedlaundryapp.userapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.speedlaundryapp.userapp.R;
import com.speedlaundryapp.userapp.adapter.laundry.TypeLaundryAdapter;
import com.speedlaundryapp.userapp.databinding.DialogInfoLaundryBinding;
import com.speedlaundryapp.userapp.model.laundry.category.CategoryItem;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryItem;

import java.util.ArrayList;

public class DialogInfoLaundry extends DialogFragment {
    DialogInfoLaundryBinding mBinding;
    ArrayList<TypeLaundryItem> typelaundry;
    CategoryItem category;
    public DialogInfoLaundry(ArrayList<TypeLaundryItem> typelaundry, CategoryItem category
    ) {
        this.category = category;
        this.typelaundry = typelaundry;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_info_laundry,container,false);
        mBinding.close.setOnClickListener(view -> {
            getDialog().dismiss();
        });
        mBinding.txtDescription.setText(category.getDescription());
        if (typelaundry.size() > 0){
            TypeLaundryAdapter typeAdapter = new TypeLaundryAdapter(typelaundry, getActivity());
            mBinding.listType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mBinding.listType.setAdapter(typeAdapter);
            typeAdapter.setListener(categoryItem -> {});
            mBinding.listType.setVisibility(View.VISIBLE);
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mBinding.getRoot();
    }
}
