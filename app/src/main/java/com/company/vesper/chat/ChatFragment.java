package com.company.vesper.chat;

import android.annotation.SuppressLint;
import android.icu.number.NumberFormatter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.FragmentChatBinding;
import com.company.vesper.lib.HttpConnectionLibrary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private List<ChatMessage> messages;
    private ChatMessageAdapter adapter;

    private boolean isLoadingMessage;

    public ChatFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        if (messages == null) {
            messages = new ArrayList<>();
            ChatLoader.loadMessages(State.getGroup().getID(), m -> {
                messages.addAll(m);
                adapter.notifyDataSetChanged();
            });

        }
    }

    public void switchGroups(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);

        List<State.GroupInfo> groups = State.getUser().getGroups();
        popup.setOnMenuItemClickListener(menuItem -> {
            int index = menuItem.getItemId();
            Toast.makeText(getContext(), "" + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
            return true;
        });

        for (int i = 0; i < groups.size(); i++) {
            popup.getMenu().add(0, i, i, groups.get(i).getName());
        }

        popup.getMenuInflater().inflate(R.menu.blank_menu, popup.getMenu());
        popup.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment

        adapter = new ChatMessageAdapter(getContext(), messages);
        binding.listMessages.setAdapter(adapter);

        binding.btnSend.setOnClickListener(v -> sendMessage(v));
        binding.txtGroupName.setOnClickListener(v -> switchGroups(v));

        binding.txtGroupName.setText(State.getGroup().getName());

        binding.listMessages.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // see if it top is at Zero, and first visible position is at 0
                    if (binding.listMessages.getFirstVisiblePosition() == 0) {
                        // TODO load messages here. Also set message load boolean to true.
                        Toast.makeText(ChatFragment.this.getContext(), "Header Item Visible",
                                Toast.LENGTH_SHORT).show();
                    }
            }
            return false;
        });
        return binding.getRoot();
    }

    public void sendMessage(View v) {
        if (binding.edtMessage.getText().length() == 0) {
            return;
        }

        String message = binding.edtMessage.getText().toString();

        HashMap<String, Object> params = new HashMap<>();
        Boolean isSignaler = State.getUser().getUid().equals(State.getGroup().getSignaler());

        params.put("sender", State.getUser().getUid());
        params.put("isSignaler", isSignaler.toString());
        params.put("chatID", State.getGroup().getID());
        params.put("message", message);
        params.put("sourceDevice", State.getDeviceFCMToken());

        Long time = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()/ 1000L);
        params.put("time", time.toString());

        HttpConnectionLibrary.sendPOST("http://128.31.25.3/send-message", params);

        messages.add(new ChatMessage(State.getUser().getName(), State.getUser().getUid(), isSignaler, message, time));
        adapter.notifyDataSetChanged();

        binding.edtMessage.setText("");
    }
}