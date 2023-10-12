package com.shegs.artreasurehunt.ui.events

interface SignUpUIEvents {
    object OnSuccessFulSignUp : SignUpUIEvents
    data class showSnackBar(val message:String) : SignUpUIEvents
}