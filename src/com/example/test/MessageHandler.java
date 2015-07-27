package com.example.test;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.luna.anytime.AnyTimeApplication;

/**
 * Created by zhangxiaobo on 15/4/20.
 */
public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
  private static AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler;
  private Context context;
  private String TAG = MessageHandler.this.getClass().getSimpleName();

  public MessageHandler(Context context) {
    this.context = context;
  }

  @Override
  public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
    Log.d(TAG, "娑堟伅宸插埌杈惧鏂�" + message.getContent());
  }

  @Override
  public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
    if (client.getClientId().equals(AnyTimeApplication.getClientIdFromPre())) {
      if (activityMessageHandler != null) {
        // 姝ｅ湪鑱婂ぉ鏃讹紝鍒嗗彂娑堟伅锛屽埛鏂扮晫闈�
        activityMessageHandler.onMessage(message, conversation, client);
      } else {
        // 娌℃湁鎵撳紑鑱婂ぉ鐣岄潰锛岃繖閲岀畝鍗曞湴 Toast 涓�涓嬨�傚疄闄呬腑鍙互鍒锋柊鏈�杩戞秷鎭〉闈紝澧炲姞灏忕孩鐐�
        if (message instanceof AVIMTextMessage) {
          AVIMTextMessage textMessage = (AVIMTextMessage) message;
          Toast.makeText(context, "鏂版秷鎭� " + message.getFrom() + " : " + textMessage.getText(), Toast.LENGTH_SHORT).show();
        }
      }
    } else {
      client.close(null);
    }
  }

  public static AVIMTypedMessageHandler<AVIMTypedMessage> getActivityMessageHandler() {
    return activityMessageHandler;
  }

  public static void setActivityMessageHandler(AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler) {
    MessageHandler.activityMessageHandler = activityMessageHandler;
  }
}
