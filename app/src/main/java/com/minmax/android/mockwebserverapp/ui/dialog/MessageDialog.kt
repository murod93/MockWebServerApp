package com.minmax.android.mockwebserverapp.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.minmax.android.mockwebserverapp.R
import com.minmax.android.mockwebserverapp.databinding.DialogMessageBinding
import com.minmax.android.mockwebserverapp.util.ext.viewBinding

/**
 * Created by murodjon on 2021/01/02
 */
class MessageDialog:DialogFragment() {

    private val binding by viewBinding(DialogMessageBinding::bind)

    private var title:String=""
    private var description:String=""
    private var f:(()->Unit)?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleView.text = title
        binding.descriptionView.text = description
        binding.btnOk.setOnClickListener {
            dismiss()
            f?.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCanceledOnTouchOutside(true)
        }
    }

    fun setTitle(title: String){
        this.title = title
    }

    fun setDescription(desc:String){
        this.description = desc
    }

    fun setListener(f:()->Unit){
        this.f = f
    }

    companion object{
        fun show(childFragment: FragmentManager, title:String, description:String, f:()->Unit){
            val dialog = MessageDialog()
            dialog.setTitle(title)
            dialog.setDescription(description)
            dialog.setListener(f)
            dialog.show(childFragment, TAG)
        }

        const val TAG = "MessageDialog"
    }

}