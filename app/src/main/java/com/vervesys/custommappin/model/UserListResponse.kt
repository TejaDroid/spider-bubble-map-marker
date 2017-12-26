package com.vervesys.custommappin.model

import com.tejadroid.spiderview.model.User

/**
 * Created by Tejas on 12/20/17.
 */
class UserListResponse {
    var Status = ""
    var Message = ""
    var Data = mutableListOf<User>()
}