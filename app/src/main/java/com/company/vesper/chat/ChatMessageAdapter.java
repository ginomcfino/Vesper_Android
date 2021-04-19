package com.company.vesper.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.ChatMessageLayoutLeaderBinding;
import com.company.vesper.databinding.ChatMessageLayoutRegularBinding;
import com.company.vesper.lib.Helpers;
import com.company.vesper.dbModels.Signal;

import java.util.List;

/**
 * Adapter used for ListView in Chat to display messages
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private static final String TAG = ChatMessageAdapter.class.getName();

    public ChatMessageAdapter(@NonNull Context context, List<ChatMessage> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) { // Cannot reuse view because we are stacking from the bottom, adding items to both the top and bottom.
        ChatMessage message = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (message.type.equals("signal")) {
            Signal signal = message.signal;
            convertView = Helpers.createSignalMessage(inflater, signal, true);
        } else {
            TextView txtMessage = null;
            TextView txtSender = null;
            TextView txtTimestamp = null;
            ConstraintLayout layout = null;

            if (message.isLeader) {
                ChatMessageLayoutLeaderBinding binding = ChatMessageLayoutLeaderBinding.inflate(inflater);
                convertView = binding.getRoot();

                txtSender = binding.txtSender;
                txtMessage = binding.txtMessage;
                txtTimestamp = binding.txtTime;

                layout = binding.layout;
                // drawable.mutate create a mutatable copy so if we change it, it won't affect any of the other drawables here.
                layout.getBackground().mutate().setTint(ContextCompat.getColor(getContext(), R.color.signal_leader_message));
            } else {
                ChatMessageLayoutRegularBinding binding = ChatMessageLayoutRegularBinding.inflate(inflater);
                convertView = binding.getRoot();

                txtSender = binding.txtSender;
                txtMessage = binding.txtMessage;
                txtTimestamp = binding.txtTime;

                layout = binding.layout;
                if (!message.senderName.equals(State.getUser().getName())) { // We don't need to change tint color for our own message
                    layout.getBackground().mutate().setTint(ContextCompat.getColor(getContext(), R.color.other_message));
                }
            }
            txtSender.setText(message.senderName);
            txtMessage.setText(message.message);
            txtTimestamp.setText(message.timestamp);

            layout.setClipToOutline(true);
        }
//        }

        return convertView;
    }
}
