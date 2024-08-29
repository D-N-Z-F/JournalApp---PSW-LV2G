package com.daryl.journalapp.core

object Constants {
    const val INFO = "Info"
    const val SUCCESS = "Success"
    const val ERROR = "Error"

    const val BLUE = "BLUE"
    const val WHITE = "WHITE"
    const val BLACK = "BLACK"

    const val LOGIN = "LOGIN"
    const val REGISTER = "REGISTER"
    const val PASSWORD = "PASSWORD"

    const val ADD = "ADD"
    const val EDIT = "EDIT"
    const val DELETE = "DELETE"

    const val JOURNAL_COLLECTION_PATH = "journals"

    const val DATE_TIME_FORMAT = "d/M/yyyy"

    const val UNEXPECTED_ERROR = "Unexpected error occurred."
    const val SOMETHING_WENT_WRONG = "Something went wrong, please retry later."
    const val NON_EXISTENT_USER = "User doesn't exist."
    const val REGISTRATION_FAILED = "Registration failed, please retry later."
    const val NON_EXISTENT_JOURNAL = "Journal doesn't exist."

    const val EMAIL_REGEX = "[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+"
    const val EMAIL_ERROR_MESSAGE = "Please enter a valid email. (e.g. johndoe123@gmail.com)"
    const val PASSWORD_REGEX = "[a-zA-Z0-9#$%]{8,20}"
    const val PASSWORD_ERROR_MESSAGE = "Password must have a length of 8 to 20, only (#$%) special characters are allowed."
    const val PASSWORD_MISMATCH = "Both passwords must match!"
    const val TITLE_REGEX = ".{2,20}"
    const val TITLE_ERROR_MESSAGE = "Title must have a length of 2 to 20."
    const val DESC_REGEX = ".{2,100}"
    const val DESC_ERROR_MESSAGE = "Description must have a length of 2 to 100."
}