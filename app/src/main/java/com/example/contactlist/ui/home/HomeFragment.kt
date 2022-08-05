package com.example.contactlist.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactlist.data.model.Contact
import com.example.contactlist.databinding.FragmentHomeBinding
import com.example.contactlist.ui.ContactAdapter
import com.example.contactlist.ui.home.viewModel.HomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var contactAdapter: ContactAdapter
    val viewModel: HomeViewModelImpl by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setupAdapter()
        setupFragmentListener()
        onBindView()
    }

    fun onBindView() {

        binding.srlContacts.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.refreshFinished.asLiveData().observe(viewLifecycleOwner) {
            binding.srlContacts.isRefreshing = false
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.setModels(contacts)
        }

        binding.fabAdd.setOnClickListener {
            navigateToAddContact()
        }
    }

    fun setupAdapter() {
        contactAdapter = ContactAdapter(emptyList())
        contactAdapter.listener = object: ContactAdapter.Listener {
            override fun onItemClicked(item: Contact) {
                navigateToEditContact(item.id!!)
            }

            override fun onDeleteClicked(item: Contact) {
                viewModel.onDeleteClicked(item.id!!)
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvContacts.adapter = contactAdapter
        binding.rvContacts.layoutManager = layoutManager
    }

    fun navigateToAddContact() {
        val action = HomeFragmentDirections.actionHomeToAddContact()
        NavHostFragment.findNavController(this).navigate(action)
    }

    fun navigateToEditContact(id: Int) {
        val action = HomeFragmentDirections.actionHomeToEditContact(id)
        NavHostFragment.findNavController(this).navigate(action)
    }

    fun setupFragmentListener() {
        setFragmentResultListener("add_contact_finished") { _, result ->
            if(result.getBoolean("refresh")) {
                viewModel.refresh()
            }
        }

        setFragmentResultListener("edit_contact_finished") { _, result ->
            if(result.getBoolean("refresh")) {
                viewModel.refresh()
            }
        }
    }
 }
