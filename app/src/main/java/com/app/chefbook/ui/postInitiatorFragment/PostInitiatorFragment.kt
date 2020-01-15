package com.app.chefbook.ui.postInitiatorFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.app.chefbook.DI.DataManager.componentFragment
import com.app.chefbook.Data.DataManager
import com.app.chefbook.model.AdapterModel.PostInitiatorMedia
import com.app.chefbook.R
import com.app.chefbook.ui.adapters.postInitiatorMedia.PostInitiatorMediaAdapter
import com.app.chefbook.ui.adapters.RecyclerViewOnClickListener
import com.app.chefbook.ui.CameraActivity.CameraActivity
import com.app.chefbook.utility.PostList
import com.app.chefbook.utility.Utility
import com.app.chefbook.utility.getInflateLayout
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_post_initiator.*

import javax.inject.Inject

class PostInitiatorFragment : Fragment(), RecyclerViewOnClickListener {

    @Inject
    lateinit var dataManager: DataManager
    private lateinit var viewModel: PostInitiatorViewModel
    lateinit var postInitiatorMediaAdapter: PostInitiatorMediaAdapter
    private val addImageUri: Uri =
        Uri.parse("android.resource://com.example.peeple/drawable/ic_add_a_photo_white_24dp")
    var toolbar: Toolbar? = null
    private var toolbarPostInitiator: View? = null
    private var imgToolbarSend: ImageView? = null
    private lateinit var lnIngredientsMap: MutableMap<Int, RelativeLayout>
    private lateinit var lnStepMap: MutableMap<Int, RelativeLayout>
    private lateinit var postIngredientsMap: MutableMap<Int, TextInputEditText>
    private lateinit var postStepMap: MutableMap<Int, TextInputEditText>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_post_initiator, container, false)
        componentFragment.inject(this)
        viewModel = ViewModelProviders.of(this, PostInitiatorViewModelFactory(dataManager))
            .get(PostInitiatorViewModel::class.java)

        initToolbar()



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val spanCount = Utility.calculateNoOfColumns(context!!, 125f)
        postInitiatorMediaAdapter = PostInitiatorMediaAdapter(PostList.instance, this)
        recViewPostMedia.layoutManager = GridLayoutManager(context, spanCount)
        recViewPostMedia.adapter = postInitiatorMediaAdapter
        postInitiatorMediaAdapter.notifyDataSetChanged()
        initMap()


        imgToolbarSend?.setOnClickListener {
            Toast.makeText(context, "Send", Toast.LENGTH_SHORT).show()

            var changeState = true

            if (PostList.instance?.size!! < 2) {
                txtMediaNotFound.visibility = View.VISIBLE
                var changeState = true
            } else {
                txtMediaNotFound.visibility = View.GONE

                if (postTitle.length() < 10 || postTitle.length() > 50) {
                    layoutTitle.error = "En az 10, en fazla 50 karakter olmalı"
                    changeState = false
                }
                if (postDescription.length() < 10 || postDescription.length() > 150) {
                    layoutDescripton.error = "En az 10, en fazla 150 karakter olmalı"
                    changeState = false
                }
                //-----------
                if (postStepOne.length() < 10 || postDescription.length() > 50) {
                    layoutStepOne.error = "En az 10, en fazla 50 karakter olmalı"
                    changeState = false
                }
            }
            if (changeState) {
                Toast.makeText(context, "Send", Toast.LENGTH_SHORT).show()
            }
        }

        imgAddIngredients.setOnClickListener {
            when {
                lnIngredientsSecond.visibility == View.GONE -> {
                    lnIngredientsSecond.visibility = View.VISIBLE
                    txtIngredientsCount.text = "2"
                }
                lnIngredientsThird.visibility == View.GONE -> {
                    lnIngredientsThird.visibility = View.VISIBLE
                    txtIngredientsCount.text = "3"
                }
                lnIngredientsFourth.visibility == View.GONE -> {
                    lnIngredientsFourth.visibility = View.VISIBLE
                    txtIngredientsCount.text = "4"
                }
                lnIngredientsFifth.visibility == View.GONE -> {
                    lnIngredientsFifth.visibility = View.VISIBLE
                    txtIngredientsCount.text = "5"
                }
                lnIngredientsSixth.visibility == View.GONE -> {
                    lnIngredientsSixth.visibility = View.VISIBLE
                    txtIngredientsCount.text = "6"
                }
                lnIngredientsSeventh.visibility == View.GONE -> {
                    lnIngredientsSeventh.visibility = View.VISIBLE
                    txtIngredientsCount.text = "7"
                }
                lnIngredientsEight.visibility == View.GONE -> {
                    lnIngredientsEight.visibility = View.VISIBLE
                    txtIngredientsCount.text = "8"
                }
                lnIngredientsNinth.visibility == View.GONE -> {
                    lnIngredientsNinth.visibility = View.VISIBLE
                    txtIngredientsCount.text = "9"
                }
                lnIngredientsTenth.visibility == View.GONE -> {
                    lnIngredientsTenth.visibility = View.VISIBLE
                    txtIngredientsCount.text = "10"
                    lnAddIngredients.visibility = View.GONE
                }
            }
        }

        imgAddStep.setOnClickListener {
            when {
                lnStepSecond.visibility == View.GONE -> {
                    lnStepSecond.visibility = View.VISIBLE
                    //txtStepCount.text = ((txtStepCount.text.toString().toInt())+1).toString()
                    txtStepCount.text = "2"
                }
                lnStepThird.visibility == View.GONE -> {
                    lnStepThird.visibility = View.VISIBLE
                    txtStepCount.text = "3"
                }
                lnStepFourth.visibility == View.GONE -> {
                    lnStepFourth.visibility = View.VISIBLE
                    txtStepCount.text = "4"
                }
                lnStepFifth.visibility == View.GONE -> {
                    lnStepFifth.visibility = View.VISIBLE
                    txtStepCount.text = "5"
                    lnAddStep.visibility = View.GONE
                }
            }
        }
        imgRemoveIngredientsSecond.setOnClickListener {
            if (postIngredientsSecond.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(2)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(2)
            }
        }
        imgRemoveIngredientsThird.setOnClickListener {
            if (postIngredientsThird.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(3)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(3)
            }
        }
        imgRemoveIngredientsFourth.setOnClickListener {
            if (postIngredientsFourth.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(4)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(4)
            }
        }
        imgRemoveIngredientsFifth.setOnClickListener {
            if (postIngredientsFifth.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(5)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(5)
            }
        }
        imgRemoveIngredientsSixth.setOnClickListener {
            if (postIngredientsSixth.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(6)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(6)
            }
        }
        imgRemoveIngredientsSeventh.setOnClickListener {
            if (postIngredientsSeventh.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(7)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(7)
            }
        }
        imgRemoveIngredientsEight.setOnClickListener {
            if (postIngredientsEight.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(8)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(8)
            }
        }
        imgRemoveIngredientsNinth.setOnClickListener {
            if (postIngredientsNinth.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(9)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(9)
            }
        }
        imgRemoveIngredientsTenth.setOnClickListener {
            if (postIngredientsTenth.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleIngredients(10)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleIngredients(10)
            }
        }

        imgRemoveStepSecond.setOnClickListener {
            if (postStepSecond.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleStep(2)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleStep(2)
            }
        }
        imgRemoveStepThird.setOnClickListener {
            if (postStepThird.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleStep(3)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleStep(3)
            }
        }
        imgRemoveStepFourth.setOnClickListener {
            if (postStepFourth.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleStep(4)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleStep(4)
            }
        }
        imgRemoveStepFifth.setOnClickListener {
            if (postStepFifth.text!!.isNotEmpty()) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("İçerik dolu. Silmek istediğinize emin misiniz?")
                    .setConfirmButton("Sil") {
                        it.dismissWithAnimation()
                        handleStep(5)
                    }
                    .showCancelButton(true)
                    .show()
            } else {
                handleStep(5)
            }
        }
    }

    private fun initMap() {
        lnIngredientsMap = mutableMapOf(
            1 to lnIngredientsOne,
            2 to lnIngredientsSecond,
            3 to lnIngredientsThird,
            4 to lnIngredientsFourth,
            5 to lnIngredientsFifth,
            6 to lnIngredientsSixth,
            7 to lnIngredientsSeventh,
            8 to lnIngredientsEight,
            9 to lnIngredientsNinth,
            10 to lnIngredientsTenth
        )
        postIngredientsMap = mutableMapOf(
            1 to postIngredientsOne,
            2 to postIngredientsSecond,
            3 to postIngredientsThird,
            4 to postIngredientsFourth,
            5 to postIngredientsFifth,
            6 to postIngredientsSixth,
            7 to postIngredientsSeventh,
            8 to postIngredientsEight,
            9 to postIngredientsNinth,
            10 to postIngredientsTenth
        )
        lnStepMap = mutableMapOf(
            1 to lnStepOne,
            2 to lnStepSecond,
            3 to lnStepThird,
            4 to lnStepFourth,
            5 to lnStepFifth
        )
        postStepMap = mutableMapOf(
            1 to postStepOne,
            2 to postStepSecond,
            3 to postStepThird,
            4 to postStepFourth,
            5 to postStepFifth
        )

    }

    //Silinmek istenen satırdan, her bir sonraki satırın değerini bir öncekine ata ve bir sonrakinin visibility'sini GONE yap.
    private fun handleIngredients(line: Int) {

        var state: Boolean = false

        if (lnIngredientsMap[line+1]?.visibility == View.GONE || lnIngredientsMap.size == line) {
            postIngredientsMap[line]?.setText("")
            lnIngredientsMap[line]?.visibility = View.GONE
            txtIngredientsCount.text = (txtIngredientsCount.text.toString().toInt() - 1).toString()
            state = true
        } else {
            for (i in line..9) {
                if (lnIngredientsMap[i + 1]?.visibility == View.VISIBLE) {
                    postIngredientsMap[i]?.text = postIngredientsMap[i + 1]?.text
                    postIngredientsMap[i + 1]?.setText("")
                    lnIngredientsMap[i]?.visibility = View.VISIBLE
                    lnIngredientsMap[i + 1]?.visibility = View.GONE
                    state = true
                }
            }
            txtIngredientsCount.text = (txtIngredientsCount.text.toString().toInt() - 1).toString()
        }
        if (state && lnIngredientsTenth.visibility == View.GONE) {
            lnAddIngredients.visibility = View.VISIBLE
        }
    }

    //Silinmek istenen satırdan, her bir sonraki satırın değerini bir öncekine ata ve bir sonrakinin visibility'sini GONE yap.
    private fun handleStep(line: Int) {

        var state: Boolean = false

        if (lnStepMap[line+1]?.visibility == View.GONE || lnStepMap.size == line) {
            postStepMap[line]?.setText("")
            lnStepMap[line]?.visibility = View.GONE
            txtStepCount.text = (txtStepCount.text.toString().toInt() - 1).toString()
            state = true
        } else {
            for (i in line..4) {
                if (lnStepMap[i + 1]?.visibility == View.VISIBLE) {
                    postStepMap[i]?.text = postStepMap[i + 1]?.text
                    postStepMap[i + 1]?.setText("")
                    lnStepMap[i]?.visibility = View.VISIBLE
                    lnStepMap[i + 1]?.visibility = View.GONE
                    state = true
                }
            }
            txtStepCount.text = (txtStepCount.text.toString().toInt() - 1).toString()
        }
        if (state && lnStepFifth.visibility == View.GONE) {
            lnAddStep.visibility = View.VISIBLE
        }
    }

    override fun onClick(item: String) {
        if (PostList.instance!![item.toInt()].isAddPost) {
            startActivity(Intent(context!!, CameraActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val postList = PostList.instance!!
        val iterator = postList.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().isAddPost) {
                iterator.remove()
            }
        }
        if (postList.size < 6) {
            postList.add(PostInitiatorMedia(addImageUri, isImage = true, isAddPost = true))
        }
        postInitiatorMediaAdapter.notifyDataSetChanged()
    }

    private fun initToolbar() {
        toolbar = requireActivity().findViewById(R.id.toolbar_addPost_content)
        toolbar?.menu?.clear()
        toolbar?.title = null
        toolbarPostInitiator = context?.let { getInflateLayout(it, R.layout.toolbar_postinitiator) }

        imgToolbarSend = toolbarPostInitiator?.findViewById(R.id.imgSend)

        toolbar?.addView(toolbarPostInitiator)
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar?.removeView(toolbarPostInitiator)
    }

    interface MutableEntry<K, V> : Map.Entry<K, V>
}
