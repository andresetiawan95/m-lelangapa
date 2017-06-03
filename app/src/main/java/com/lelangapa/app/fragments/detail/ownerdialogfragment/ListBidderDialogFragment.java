package com.lelangapa.app.fragments.detail.ownerdialogfragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.lelangapa.app.R;

/**
 * Created by andre on 11/03/17.
 */

public class ListBidderDialogFragment extends DialogFragment {
    private EditText mEditText;
    private Button mButton;

    /*\
    * INI PERCOBAAN DIALOGFRAGMENT DAN SUDAH BERHASIL
    *
    * TIDAK PERLU BERURUSAN DENGAN SOCKET LAGI KARENA MASIH CONNECTED
    * */
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
    public static ListBidderDialogFragment newInstance(String title)
    {
        ListBidderDialogFragment bidderDialogFragment = new ListBidderDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        bidderDialogFragment.setArguments(args);
        return bidderDialogFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_list_bidder_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTryAlertDialog();
            }
        };
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        mButton = (Button) view.findViewById(R.id.buttonlala);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mButton.setOnClickListener(clickListener);
    }

    private void setTryAlertDialog()
    {
        AlertDialog.Builder winnerSelectedAlertDialogBuilder = new AlertDialog.Builder(getActivity());
        winnerSelectedAlertDialogBuilder.setTitle(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGTITLE)
                .setMessage(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGMSG)
                .setPositiveButton(R.string.DETAILFRAGMENT_WINNERSELECTED_ALERTDIALOGBUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog winnerSelectedDialog = winnerSelectedAlertDialogBuilder.create();
        winnerSelectedDialog.show();
    }
}
