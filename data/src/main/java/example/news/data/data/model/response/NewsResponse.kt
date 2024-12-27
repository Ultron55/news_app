package example.news.data.data.model.response

data class NewsResponse(
    val status : String,
    val code : String?,
    val message : String?,
    val totalResults : Int?,
    val articles : List<Articles>?,
)

data class Articles(
    val source : Source,
    val author : String,
    val title : String,
    val description : String,
    val url : String,
    val urlToImage : String,
    val publishedAt : String
)

data class Source(
    val id : String?,
    val name : String,
)