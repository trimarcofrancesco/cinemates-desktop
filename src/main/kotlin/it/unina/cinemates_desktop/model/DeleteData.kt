package it.unina.cinemates_desktop.model

data class DeleteData(
    var deleteFrom: String,
    var itemId: Int
)


data class DeletedDataResponse(
    var itemId: Int
)
