package com.andrei.finalyearprojectapi.models

data class InquiryCompleteResponseWrapper(
   val data:InquiryCompleteResponse
   )

data class InquiryCompleteResponse(
    val type:String,
    val id :String,
    val attributes:InquiryCompleteResponseAttributes
)

data class InquiryCompleteResponseAttributes(
    val name:String,
    val payload:InquiryCompleteResponsePayloadWrapper

)
data class InquiryCompleteResponsePayloadWrapper(
    val data:InquiryCompleteResponsePayloadData,
)

data class InquiryCompleteResponsePayloadData(
    val attributes:InquiryCompleteResponsePayloadDataAttributes,
)

data class InquiryCompleteResponsePayloadDataAttributes(
    val referenceId:String,
)