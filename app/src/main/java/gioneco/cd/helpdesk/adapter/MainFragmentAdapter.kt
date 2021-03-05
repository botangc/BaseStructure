package gioneco.cd.helpdesk.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 主页VP适配器
 *
 * @since V1.0.0 tangbo 初建
 */
class MainFragmentAdapter(fm: FragmentManager, private val mFragments: ArrayList<Fragment>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size
}