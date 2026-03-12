package com.example.projectzeta.Repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RealtimeFirebaseHelper {
    companion object {
        private val database = FirebaseDatabase.getInstance().reference
        fun <T> readList(
            tableName: String,
            clazz: Class<T>,
            onData: (List<T>) -> Unit
        ) {
            database.child(tableName).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val genericList = mutableListOf<T>()

                    for (child in snapshot.children) {

                        val item = child.getValue(clazz)

                        if (item != null) {
                            genericList.add(item)
                        }

                    }
                    onData(genericList)
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error: Database fetch error, Message: ${error.message}")
                }

            })
        }

        fun writeItem(
            tableName: String,
            key: String,
            item: Any
        ) {
            database.child(tableName)
                .child(key)
                .setValue(item)
        }

        fun deleteItem(
            tableName: String,
            key: String
        ) {
            database.child(tableName)
                .child(key)
                .removeValue()
        }

        fun <T> readItem(
            tableName:String,
            key:String,
            clazz:Class<T>,
            onResult:(T?) -> Unit
        ){
            database.child(tableName)
                .child(key)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){

                            val item = snapshot.getValue(clazz)
                            onResult(item)

                        } else {
                            onResult(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onResult(null)
                    }

                })
        }

        fun <T> readItemUsingProperty(
            tableName: String,
            property: String,
            value: String,
            clazz: Class<T>,
            onResult: (T?) -> Unit
        ){
            database.child(tableName)
                .orderByChild(property)
                .equalTo(value)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){

                            for(child in snapshot.children){
                                val item = child.getValue(clazz)
                                onResult(item)
                                return
                            }

                        } else {
                            onResult(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onResult(null)
                    }

                })
        }

    }
}