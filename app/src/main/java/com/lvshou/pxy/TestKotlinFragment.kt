package com.lvshou.pxy


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * A fragment with a Google +1 button.
 */
class TestKotlinFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plus_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val array = arrayListOf("str1-", "str2-", "str3")
        var stringBuilder = StringBuilder()
        array.forEach {
            stringBuilder.append(it)
        }
        textView.apply {
            text = stringBuilder.toString()
            textSize = 22f
        }*/

      /*  var sb = StringBuilder()
        fun Int.isOdd(): Boolean = this % 2 == 0
        val ints = intArrayOf(1, 3, 4, 5, 8)
        val result = ints.filter(Int::isOdd)
        result.forEach {
            sb.append(it)
        }
        textView.text = sb.toString()*/

        val list = listOf(
                1..10,
                2..5,
                100..110
        )
        val flatList = list.flatMap { it }
        System.out.println(flatList)


        val evens = (1..100).filter {
            it % 2 == 0
        }
        System.out.println(evens)


        /*val br = BufferedReader(FileReader("hello.txt"))
        with(br){
            var line: String?
            while (true){ line = readLine()?: break }
            close()
        }*/

    }


    companion object {

        // The request code must be 0 or greater.
        private val PLUS_ONE_REQUEST_CODE = 0
    }


}
