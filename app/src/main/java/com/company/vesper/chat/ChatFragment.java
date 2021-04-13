package com.company.vesper.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.company.vesper.MainActivity;
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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment

        adapter = new ChatMessageAdapter(getContext(), messages);
        binding.listMessages.setAdapter(adapter);

        binding.btnSend.setOnClickListener(v -> sendMessage());
        binding.txtGroupName.setOnClickListener(v -> showSwitchGroupsMenu(v));

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

    /**
     * Event handle for display switch group menu. When clicked will reload fragment to the new group
     *
     * @param v The view that is clicked to display this menu.
     */
    private void showSwitchGroupsMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);

        List<State.GroupInfo> groups = new ArrayList<>();
        groups.addAll(State.getUser().getGroups());
        if (groups.size() == 0) {
            // if the user is only in one single group, do not do anything
            return;
        }

        for (int i = 0; i < groups.size(); ++i) {
            // remove current group from the list
            if (groups.get(i).getID().equals(State.getGroup().getID())) {
                groups.remove(i);
                break;
            }
        }

        popup.setOnMenuItemClickListener(menuItem -> {
            int index = menuItem.getItemId();
            State.getDatabase().collection("groups").document(groups.get(index).getID()).get().addOnCompleteListener(task -> {
                // swap groups
                State.setGroup(task.getResult());
                ((MainActivity) getActivity()).setCurrentFragment(new ChatFragment());
            });
            return true;
        });

        for (int i = 0; i < groups.size(); i++) {
            popup.getMenu().add(0, i, i, groups.get(i).getName());
        }

        popup.getMenuInflater().inflate(R.menu.blank_menu, popup.getMenu());
        popup.show();
    }

    /**
     * Event handler for sending message. Bound to btnSendMessage;
     */
    private void sendMessage() {
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

        Long time = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000L);
        params.put("time", time.toString());

        HttpConnectionLibrary.sendPOST("http://128.31.25.3/send-message", params);

        messages.add(new ChatMessage(State.getUser().getName(), State.getUser().getUid(), isSignaler, message, time));
        adapter.notifyDataSetChanged();

        binding.edtMessage.setText("");
    }
}