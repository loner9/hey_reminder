package info.project.hey.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import info.project.hey.LoginActivity;
import info.project.hey.R;
import info.project.hey.StuActivity;
import info.project.hey.TentangActivity;

public class MenuFragment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Button feedback = v.findViewById(R.id.feedback);
        Button tentang = v.findViewById(R.id.tentang);
        Button keluar = v.findViewById(R.id.keluar);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"aksack.09@gmail.com"});
                intent.putExtra(Intent.EXTRA_CC, new String[] {"ahmadkoirulanwar.rpla@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Pesan dari app!");
                intent.putExtra(Intent.EXTRA_TEXT, "Masukkan feedback(saran/kritik) anda");

                try {
                    startActivity(Intent.createChooser(intent, "Bagaimana untuk mengirim?"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "Terjadi Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TentangActivity.class);
                startActivity(intent);
            }
        });
        keluar.setOnClickListener(v1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setMessage("Keluar juga akan menghapus seluruh data pengingat anda,Lanjut?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", (dialog, which) -> {
                        progressDialog.setMessage("Memproses . . .");
                        auth.signOut();

                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    })
                    .setNegativeButton("Tidak",((dialog, which) -> {
                        Toast.makeText(getActivity()," ",Toast.LENGTH_SHORT).show();
                    }));
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
}
