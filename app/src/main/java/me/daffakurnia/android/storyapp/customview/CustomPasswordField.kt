package me.daffakurnia.android.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import me.daffakurnia.android.storyapp.R

class CustomPasswordField : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.isNotEmpty()) {
                    if (s.count() < 6) showError(true) else showError(false)
                }
            }

            override fun afterTextChanged(p0: Editable?) { }

        })
    }

    private fun showError(isError: Boolean) {
        error = if (isError) resources.getString(R.string.password_error) else null
    }
}