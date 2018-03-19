package com.keyontech.woot1.Models

/**
 * Created by kot on 3/12/18.
 */

class JSONUrL (
    val mehurl: String
    , val allwooturl: String
    , val wwwwooturl: String
    , val electronicswooturl: String
    , val computerswooturl: String
    , val homewooturl: String
    , val toolswooturl: String
    , val sportwooturl: String
    , val accessorieswooturl: String
    , val kidswooturl: String
    , val shirtwooturl: String
    , val winewooturl: String
    , val selloutwooturl: String
)



class ModelWoot(
//        val Id: String
//        var items: List<ModelWootDeal>? = null
//        val modelWootDeals: ArrayList<ModelWootDeal> = arrayListOf<ModelWootDeal>()
        val items: ArrayList<ModelWootDeal> = arrayListOf<ModelWootDeal>()
)

class ModelWootDeal(
    val Type: String
    , val Id: String
    , val Title: String
    , val Site: String
    , val StartDate: String
    , val EndDate: String
    , val Offers: ArrayList<ModelWootOffer>
//    , val Offers: ArrayList<ModelWootOffer> = arrayListOf<ModelWootOffer>()
)

class ModelWootOffer (
    val Title: String
    , val PercentageRemaining: Int
    , val Features: String
    , val Specs: String
    , val SoldOut: Boolean
    , val Subtitle: String
    , val Teaser: String
    , val WriteUp: String
    , val DiscussionUrl: String
    , val Url: String
    , val Items: ArrayList<ModelWootOfferItem> = arrayListOf<ModelWootOfferItem>()
    , val Photos: ArrayList<ModelWootOfferPhoto> = arrayListOf<ModelWootOfferPhoto>()
)

class ModelWootOfferPhoto (
    val Url: String
    , val Width: Int
    , val Height: Int
    , val Tags: ArrayList<String> = arrayListOf<String>()
)

class ModelWootOfferItem (
    val SalePrice: String
    , val ListPrice: String
    , val PurchaseLimit: Int
    , val Attributes: ArrayList<ModelWootOfferItemAttributes> = arrayListOf<ModelWootOfferItemAttributes>()
)

class ModelWootOfferItemAttributes (
    val Key: String
    , val Value: String
)

class ModelMeh(
        val deal: ModelMehDeal
        , val poll: ModelMehPoll
        , val video: ModelMehVideo
)

class ModelMehDeal(
        val features: String = ""
        , val id: String = ""
        , val items: ArrayList<ModelMehItem> = arrayListOf<ModelMehItem>()
//    , val items: List<ModelMehItem>
        , val photos: ArrayList<String> = arrayListOf<String>()
//    , val photos: List<String>
        , val title: String = ""
        , val specifications: String = ""
//        , val soldOutAt: String
//        , val story: ArrayList<ModelMehStory>
        , val story: ModelMehStory
        , val theme: ModelMehTheme
        , val url: String = ""
        , val launches: ArrayList<ModelMehLaunches> = arrayListOf<ModelMehLaunches>()
//    , val launches: List<ModelMehLaunches>
        , val topic: ModelMehTopic
)






class ModelMehPoll (
//    companion object Factory {
//        fun create(): ModelMehPoll = ModelMehPoll()
//    }
//
//    fun create() = ModelMehPoll
//    {
//        return@ModelMehPoll
//    }

        val id: String = ""
        , val startDate: String = ""
        , val title: String = ""
        , val topic: ModelMehTopic
        , val answers: ArrayList<ModelMehPollAnswer> = arrayListOf<ModelMehPollAnswer>()
//    , val answers: List<ModelMehPollAnswer>


//        companion object {
//            val id: String
//            ,
//            val startDate: String
//            ,
//            val title: String
//            ,
//            val topic: ModelMehTopic
//            ,
//            val answers: ArrayList<ModelMehPollAnswer>
//        }
)

class ModelMehPollAnswer(
        val id: String
        , val text: String
        , val voteCount: Int
)








class ModelMehVideo (
        val id: String
        , val startDate: String
        , val title: String
        , val url: String
        , val topic: ModelMehTopic
)






class ModelMehTopic (
        val commentCount: Int
        , val createdAt: String
        , val id: String
        , val replyCount: Int
        , val url: String
        , val voteCount: Int
)





class ModelMehItem (
        val id: String
        , val condition: String
        , val photo: String
        , val price: Double
)






class ModelMehStory (
        val title: String
        , val body: String
)



class ModelMehTheme (
        val accentColor: String
        , val backgroundColor: String
        , val backgroundImage: String
        , val foreground: String
)

class ModelMehLaunches (
        val soldOutAt: String
)
