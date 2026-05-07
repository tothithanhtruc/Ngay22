package com.example.hitcapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rcvChatMessages;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    private EditText edtChatMessage;
    private ImageView btnSendMessage, btnBackChat;
    private TextView tvChatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tvChatName = findViewById(R.id.tvChatName);
        rcvChatMessages = findViewById(R.id.rcvChatMessages);
        edtChatMessage = findViewById(R.id.edtChatMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnBackChat = findViewById(R.id.btnBackChat);

        String shopName = getIntent().getStringExtra("shopName");
        if (shopName != null) {
            tvChatName.setText(shopName);
        }

        btnBackChat.setOnClickListener(v -> finish());

        // Mock messages
        messageList.add(new ChatMessage("Chào bạn! Shop có thể giúp gì cho bạn?", false));
        messageList.add(new ChatMessage("Mình muốn hỏi về bó hoa hồng đỏ ạ.", true));
        messageList.add(new ChatMessage("Dạ hoa hồng đỏ bên mình luôn có sẵn ạ.", false));

        chatAdapter = new ChatAdapter(messageList);
        rcvChatMessages.setLayoutManager(new LinearLayoutManager(this));
        rcvChatMessages.setAdapter(chatAdapter);

        btnSendMessage.setOnClickListener(v -> {
            String msg = edtChatMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                messageList.add(new ChatMessage(msg, true));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                rcvChatMessages.scrollToPosition(messageList.size() - 1);
                edtChatMessage.setText("");
            }
        });
    }
}

class ChatMessage {
    private String text;
    private boolean isUser;

    public ChatMessage(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }

    public String getText() { return text; }
    public boolean isUser() { return isUser; }
}

class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatMessage> messages;
    private static final int TYPE_USER = 1;
    private static final int TYPE_OTHER = 2;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser() ? TYPE_USER : TYPE_OTHER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new UserMsgViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new OtherMsgViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String text = messages.get(position).getText();
        if (holder instanceof UserMsgViewHolder) {
            ((UserMsgViewHolder) holder).tv.setText(text);
            ((UserMsgViewHolder) holder).tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            ((OtherMsgViewHolder) holder).tv.setText(text);
        }
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class UserMsgViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public UserMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(android.R.id.text1);
        }
    }

    static class OtherMsgViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public OtherMsgViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(android.R.id.text1);
        }
    }
}
