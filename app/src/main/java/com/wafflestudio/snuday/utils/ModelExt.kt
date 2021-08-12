package com.wafflestudio.snuday.utils

import com.wafflestudio.snuday.model.Channel

fun List<Channel>.filterPersonalChannel() = this.filter { !it.isPersonal }
