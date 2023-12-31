package com.example.e_key

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View


class MyInputMethodService : InputMethodService(), KeyboardView.OnKeyboardActionListener {
    private var keyboardView: KeyboardView? = null
    private var keyboard: Keyboard? = null
    private var caps = false
    private var isCodeMode=false

    override fun onPress(p0: Int) {
    }

    override fun onRelease(p0: Int) {
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {

        val inputConnection = currentInputConnection
        if (inputConnection != null) {
            when (primaryCode) {
                1002 -> {
                    // Toggle between the default layout and the numeric keypad layout


                    isCodeMode = !isCodeMode

                    // Update the keyboard layout based on the mode
                    if (isCodeMode) {
                        // Switch to the numeric keypad layout
                        keyboard = Keyboard(this, R.xml.keyboard_code)
                    } else {
                        // Switch back to the default keyboard layout
                        keyboard = Keyboard(this, R.xml.keys_layout)
                    }

                    // Set the new keyboard layout for the keyboard view
                    keyboardView?.keyboard = keyboard
                    keyboardView?.invalidateAllKeys()
                }

                Keyboard.KEYCODE_DELETE -> {
                    val selectedText = inputConnection.getSelectedText(0)
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }

                }
                Keyboard.KEYCODE_SHIFT -> {
                    caps = !caps
                    keyboard!!.isShifted = caps
                    keyboardView!!.invalidateAllKeys()
                }
                Keyboard.KEYCODE_DONE -> inputConnection.sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER
                    )
                )
                else -> {
                    var code = primaryCode.toChar()
                    if (Character.isLetter(code) && caps) {
                        code = Character.toUpperCase(code)
                    }
                    inputConnection.commitText(code.toString(), 1)
                }
            }
        }

    }

    override fun onText(p0: CharSequence?) {
    }

    override fun swipeLeft() {
    }

    override fun swipeRight() {
    }

    override fun swipeDown() {
    }

    override fun swipeUp() {
    }

    override fun onCreateInputView(): View {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView
        keyboard = Keyboard(this, R.xml.keys_layout) // Load the custom layout
        keyboardView?.isPreviewEnabled = false
        keyboardView!!.apply {
            keyboard = this@MyInputMethodService.keyboard
            setOnKeyboardActionListener(this@MyInputMethodService)
        }
        return keyboardView!!
    }

}