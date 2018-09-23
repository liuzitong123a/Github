package com.kwunai.github.ext

import java.text.SimpleDateFormat
import java.util.*


fun Date.format(pattern: String): String = SimpleDateFormat(pattern, Locale.CHINA).format(this)