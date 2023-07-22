package com.example.subway


import com.squareup.moshi.Json

data class station(
    @field:Json(name = "SearchInfoBySubwayNameService")
    val searchInfoBySubwayNameService: SearchInfoBySubwayNameService?
)
data class SearchInfoBySubwayNameService(
    @field:Json(name = "list_total_count")
    val listTotalCount: Int?,
    @field:Json(name = "RESULT")
    val rESULT: RESULT?,
    @field:Json(name = "row")
    val row: List<Row?>?
)

data class Row(
    @field:Json(name = "FR_CODE")
    val fRCODE: String?,
    @field:Json(name = "LINE_NUM")
    val lINENUM: String?,
    @field:Json(name = "STATION_CD")
    val sTATIONCD: String?,
    @field:Json(name = "STATION_NM")
    val sTATIONNM: String?
)
data class RESULT(
    @field:Json(name = "CODE")
    val cODE: String?,
    @field:Json(name = "MESSAGE")
    val mESSAGE: String?
)