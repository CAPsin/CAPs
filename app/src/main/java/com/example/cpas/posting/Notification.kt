package com.example.cpas.posting

data class Notification(val comment : String,
                        val pTitle : String,
                        val pWho : String,
                        val pContent : String,
                        val pID : String,
                        val cWho : String,
                        val cID : String,
                        val time : String,
                        val writerUid : String,
                        val commentID : String,
                        val epoch : String)
