package com.axionlabs.keyhive.utils

import android.app.assist.AssistStructure
import android.view.View

class AutofillParser {
    var usernameField: AssistStructure.ViewNode ?= null
    var passwordField: AssistStructure.ViewNode ?= null
    var domain: String? = null

    fun parse(structure: AssistStructure){
        val nodes = structure.windowNodeCount
        for (i in 0 until nodes){
            val windowNode = structure.getWindowNodeAt(i)
            traverseNode(windowNode.rootViewNode)
        }
    }
    private fun traverseNode(node: AssistStructure.ViewNode){
        if (node.webDomain != null){
            domain = node.webDomain
        }
        val hints = node.autofillHints
        if (hints != null){
            if(hints.contains(View.AUTOFILL_HINT_USERNAME)) usernameField = node
            if(hints.contains(View.AUTOFILL_HINT_PASSWORD)) passwordField = node
        }
        for(i in 0 until node.childCount){
            traverseNode(node.getChildAt(i))
        }

    }

}
