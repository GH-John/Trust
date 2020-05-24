package com.application.arenda.mainWorkspace.fragments.proposals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import butterknife.Unbinder
import com.application.arenda.R
import com.application.arenda.databinding.FragmentUserProposalsBinding
import com.application.arenda.entities.announcements.proposalsAnnouncement.pager.ProposalsPagerAdapter
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar
import com.application.arenda.ui.widgets.sideBar.ItemSideBar
import com.application.arenda.ui.widgets.sideBar.SideBar

class FragmentUserProposals private constructor(): Fragment(), AdapterActionBar, ItemSideBar {
    private lateinit var bind: FragmentUserProposalsBinding

    private var unbinder: Unbinder? = null
    private var pagerAdapter: ProposalsPagerAdapter? = null
    private var sideBar: SideBar? = null
    private var itemBurgerMenu: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserProposalsBinding.inflate(inflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
    }

    private fun initComponents() {
        pagerAdapter = ProposalsPagerAdapter(requireActivity().supportFragmentManager)

        pagerAdapter?.setPagerItem(FragmentIncomingProposals.instance, getString(R.string.title_pager_incoming_proposals))
        pagerAdapter?.setPagerItem(FragmentOutgoingProposals.instance, getString(R.string.title_pager_outgoing_proposals))
        pagerAdapter?.setPagerItem(FragmentActiveProposals.instance, getString(R.string.title_pager_active_proposals))
        pagerAdapter?.setPagerItem(FragmentHistoryProposals.instance, getString(R.string.title_pager_history_proposals))

        bind.pagerProposals.adapter = pagerAdapter
        bind.pagerTabLayout.setupWithViewPager(bind.pagerProposals)

        bind.pagerTabLayout.getTabAt(0)?.setIcon(R.drawable.indicator_incoming_proposals)
        bind.pagerTabLayout.getTabAt(1)?.setIcon(R.drawable.indicator_outgoing_proposals)
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_user_proposals
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup) {
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu)
    }

    override fun initListenersActionBar(viewGroup: ViewGroup) {
        itemBurgerMenu?.setOnClickListener { sideBar?.openLeftMenu() }
    }

    override fun setSideBar(sideBar: SideBar) {
        this.sideBar = sideBar
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }

    companion object {
        private var fragmentUserProposals: FragmentUserProposals? = null

        val instance: FragmentUserProposals?
            get() {
                if (fragmentUserProposals == null) fragmentUserProposals = FragmentUserProposals()
                return fragmentUserProposals
            }
    }
}