package com.example.sedonortdd.ui.chatbot

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.sedonortdd.R
import com.example.sedonortdd.databinding.ActivityChatBotBinding
import com.example.sedonortdd.viewModel.ChatBotViewModel

class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding
    private lateinit var progressDialog: ProgressDialog
    private val chatbotViewModel: ChatBotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            val userInput = binding.editTextUserInput.text.toString()
            if (userInput.isNotBlank()) {
                addMessageToConversation(userInput, true)
                chatbotViewModel.sendMessageToChatbot(userInput)
                binding.editTextUserInput.text.clear()
            }
        }

        chatbotViewModel.conversation.observe(this) { conversation ->
            conversation?.let {
                binding.conversationLayout.removeAllViews()
                it.forEach { message ->
                    addMessageToConversation(message, false)
                }
            }
        }

        chatbotViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) showProcessingDialog()
            else dismissProcessingDialog()
        }

        chatbotViewModel.errorMessage.observe(this) { errorMessage ->
            Log.e("Chatbot", errorMessage)
        }
    }

    private fun showProcessingDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Sedang memproses pertanyaan...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun dismissProcessingDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun addMessageToConversation(message: String, isFromUser: Boolean) {
        val inflater = LayoutInflater.from(this)
        val chatBubbleLayout = inflater.inflate(R.layout.chat_bubble, null) as LinearLayout
        val messageTextView = chatBubbleLayout.findViewById<TextView>(R.id.messageTextView)

        messageTextView.text = message
        if (isFromUser) {
            messageTextView.setBackgroundResource(R.drawable.user_bubble)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.END
            chatBubbleLayout.layoutParams = params
        } else {
            messageTextView.setBackgroundResource(R.drawable.chatbot_bubble)
        }

        binding.conversationLayout.addView(chatBubbleLayout)
    }
}
