package com.keyontech.woot1.Adapters

import android.content.Intent
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.keyontech.woot1.Activities.DetailsActivity
import com.keyontech.woot1.Models.ModelWootDeal
import com.keyontech.woot1.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_main_row.view.*
import com.keyontech.woot1.Utils.*


/**
 * Created by kot on 3/12/18.
 */

class AdapterRecyclerViewActivityMain(private val fragmentManager: FragmentManager, private val wootDeal: List<ModelWootDeal>, private val jsonResponse: String? = ""): RecyclerView.Adapter<CustomerViewHolderClass>() {
    override fun getItemCount(): Int {
        return wootDeal.count()
    }

    // users custom view holder class below
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomerViewHolderClass {
        /*** this method talks to the recyclerview_content layout file */
        val layoutInflator = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflator.inflate(R.layout.recyclerview_main_row, parent,false)
//        val cellForRow = layoutInflator.inflate(R.layout.recyclerview_main_row_2, parent,false)

        /*** return cannot use cellForRow requires return below */
        return CustomerViewHolderClass(cellForRow, jsonResponse)
    }

    override fun onBindViewHolder(holder: CustomerViewHolderClass?, position: Int) {
        val deal = wootDeal[position]

        if (deal != null) {
            if (deal.Offers.count() > 0) {
                val offer = deal.Offers[0]

                offer.Photos.forEach {
                    val xP = it

                    it.Tags.forEach {
                        if ( it == CONDITION_OFFER_PHOTO_TAG_KEY_FULLSIZE) {
                            val imageviewThumbNail = holder?.customViewHolder_View?.imageView_Offer_ThumbNail
                            Picasso.with( holder?.customViewHolder_View?.context).load(xP.Url).into(imageviewThumbNail)
                        }
                    }
                }

//                holder?.customViewHolder_View?.textView_Site_Title?.text =  deal.Site.replace(CONDITION_DEAL_SITE_TRIM_CONDITION,"",true)
                holder?.customViewHolder_View?.textView_Site_Title?.text =  deal.Site
                holder?.customViewHolder_View?.textView_Item_Title?.text =  offer.Title
                holder?.customViewHolder_View?.textView_Price?.text = wootPriceLowtoHigh(offer)

                if (offer.Items.count() > 0) {
                    if (offer.Items[0].Attributes.count() > 0) {
                        holder?.customViewHolder_View?.textView_Item_Condition?.text =
                                if (offer.Items[0].Attributes.count() > 0) {
                                    if (offer.Items[0].Attributes[0].Key.equals(CONDITION_OFFER_ITEM_ATTRIBUTE_KEY_CONDITION, true)) {
                                        offer.Items[0].Attributes[0].Value
                                    } else {
                                        ""
                                    }
                                } else {
                                    "-"
                                }
                    }
                }
            }

            /*** animate on start */
            holder?.customViewHolder_View?.textView_Site_Title?.startAnimation(AnimationUtils.loadAnimation(holder?.customViewHolder_View?.context, startRandomAnimation()))
            holder?.customViewHolder_View?.linearLayout_RecyclerView_Main_Row_1?.startAnimation(AnimationUtils.loadAnimation(holder?.customViewHolder_View?.context, startRandomAnimation()))
            holder?.customViewHolder_View?.cardView_Recyclerview_Main_Row_v2?.startAnimation(AnimationUtils.loadAnimation(holder?.customViewHolder_View?.context, startRandomAnimation()))
//        holder?.customViewHolder_View?.imageView_Offer_ThumbNail?.startAnimation(AnimationUtils.loadAnimation(holder?.customViewHolder_View?.context, startRandomAnimation()))
//        holder?.customViewHolder_View?.textView_Price?.startAnimation(AnimationUtils.loadAnimation(holder?.customViewHolder_View?.context, startRandomAnimation()))
//        holder?.customViewHolder_View?.textView_Item_Title?.startAnimation(AnimationUtils.loadAnimation(holder?.customViewHolder_View?.context, startRandomAnimation()))
//        holder?.customViewHolder_View?.textView_Item_Condition?.startAnimation(AnimationUtils.loadAnimation(holder?.customViewHolder_View?.context, startRandomAnimation()))

        }

    }

}

/*** the customViewHolder_View at the end is the same view defined as a parameter class CustomerViewHolderClass(val customViewHolder_View: View) */
class CustomerViewHolderClass(val customViewHolder_View: View, private val jsonResponse: String? = ""): RecyclerView.ViewHolder(customViewHolder_View) {
    init {
        if (!jsonResponse.isNullOrEmpty()) {
            customViewHolder_View.setOnClickListener {
                val intent = Intent(customViewHolder_View.context, DetailsActivity::class.java)
                intent.putExtra(ACT_FRAG_JSON_RESPONSE, jsonResponse)
                intent.putExtra(ACT_FRAG_MAIN_ITEM_SELECTED, layoutPosition)
                customViewHolder_View.context.startActivity(intent)
            }
        }
    }
}