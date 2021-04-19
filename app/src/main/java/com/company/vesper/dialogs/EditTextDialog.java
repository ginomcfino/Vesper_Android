package com.company.vesper.dialogs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.company.vesper.R;
import com.company.vesper.databinding.FragmentEditTextDialogBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A dialog fragment that allows user to input some text. Can use a regex pattern to validate the input.
 */
public class EditTextDialog extends DialogFragment {
    private String title;
    private String hint;
    private Pattern pattern;
    private Matcher matcher;

    private okCallback okCallback;

    public EditTextDialog(String title, String hint, String pattern, okCallback okCallback) {
        this.title = title;
        this.hint = hint;
        if (pattern != null) {
            this.pattern = Pattern.compile(pattern);
        }

        this.okCallback = okCallback;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(this.title);

        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint(this.hint);
        input.setText(this.hint);
        input.setOnFocusChangeListener((f, b) -> {
            if (!b) {
                matcher = pattern.matcher(input.getText().toString());
                if (!matcher.matches()) {
                    input.setText(this.hint);
                    Toast.makeText(getContext(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            okCallback.callback(input.getText().toString());
            dialog.dismiss();

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {

            if (dialog != null) {
                dialog.dismiss();
            }

        });
        return builder.create();
    }

    public interface okCallback {
        void callback(String input);
    }
}