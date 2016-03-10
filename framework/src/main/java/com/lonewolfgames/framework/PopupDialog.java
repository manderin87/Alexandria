package com.lonewolfgames.framework;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jhyde on 7/14/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class PopupDialog extends DialogFragment {

    private AbstractMainApplication mApp;
    private View mView;

    private String mTitle = "";
    private String mMessage = "";
    private String mOkButtonName = "";

    private String mCancelButtonName = "";

    private TextView mTitleTextView;
    private TextView mMessageTextView;
    private Button mOkButton;
    private Button mCancelButton;

    private PopupDialogListener mListener;


//    public static final PopupDialog newInstance(String title, String message, String okButtonName) {
//        PopupDialog fragment = new PopupDialog();
//
//        Bundle bundle = new Bundle();
//        bundle.putString("title", title);
//        bundle.putString("message", message);
//        bundle.putString("ok_button_name", okButtonName);
//        fragment.setArguments(bundle);
//
//        return fragment;
//    }

    public static final PopupDialog newInstance(String title, String message, String okButtonName, PopupDialogListener listener) {
        PopupDialog fragment = new PopupDialog();
        fragment.setListener(listener);

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("ok_button_name", okButtonName);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static final PopupDialog newInstance(String title, String message, String okButtonName,
                                                String cancelButtonName, PopupDialogListener listener) {
        PopupDialog fragment = new PopupDialog();
        fragment.setListener(listener);

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("ok_button_name", okButtonName);
        bundle.putString("cancel_button_name", cancelButtonName);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.popup_view, container);

        mApp = (AbstractMainApplication) getActivity().getApplicationContext();

        mTitleTextView = (TextView) mView.findViewById(R.id.textView_title);
        mMessageTextView = (TextView) mView.findViewById(R.id.textView_message);
        mOkButton = (Button) mView.findViewById(R.id.button_ok);
        mCancelButton = (Button) mView.findViewById(R.id.button_cancel);

        if(getArguments() != null) {
            mTitle = getArguments().getString("title");
            mMessage = getArguments().getString("message");

            if(getArguments().containsKey("ok_button_name")) {
                mOkButtonName = getArguments().getString("ok_button_name");
            }

            if(getArguments().containsKey("cancel_button_name")) {
                mCancelButtonName = getArguments().getString("cancel_button_name");
                mCancelButton.setVisibility(View.VISIBLE);
            } else {
                mCancelButton.setVisibility(View.GONE);
            }
        }


        mTitleTextView.setText(mTitle);

        mMessageTextView.setText(mMessage);

        mOkButton.setText(mOkButtonName);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.OnOkClicked();
                }
                dismiss();
            }
        });

        mCancelButton.setText(mCancelButtonName);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.OnCancelClicked(PopupDialog.this);
                }
                dismiss();
            }
        });

        return mView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.PrintDialogAnimation;

        return dialog;
    }

    public void setListener(PopupDialogListener listener) {
        mListener = listener;
    }

    public interface PopupDialogListener {
        void OnOkClicked();
        void OnCancelClicked(DialogFragment dialog);
    }
}
