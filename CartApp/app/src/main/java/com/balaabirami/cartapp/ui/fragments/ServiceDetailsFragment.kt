package com.balaabirami.cartapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.balaabirami.cartapp.viewmodels.CartViewModel
import com.balaabirami.cartapp.viewmodels.ServiceDetailsViewModel
import com.balaabirami.cartapp.R
import com.balaabirami.cartapp.utils.Utils
import com.balaabirami.cartapp.model.CartItem
import com.balaabirami.cartapp.model.Employee
import com.balaabirami.cartapp.model.Service
import com.balaabirami.cartapp.model.Status
import com.balaabirami.cartapp.databinding.FragmentServiceDetailsBinding
import com.balaabirami.cartapp.room.CartDatabase
import com.balaabirami.cartapp.ui.activity.HomeActivity
import com.balaabirami.cartapp.ui.adapter.EmployeeListAdapter
import com.bumptech.glide.Glide

class ServiceDetailsFragment : Fragment(), EmployeeListAdapter.EmployeeSelectedListener {
    private var cartItems: List<CartItem>? = null
    private var rootView: View? = null
    private lateinit var binding: FragmentServiceDetailsBinding
    private lateinit var adapter: EmployeeListAdapter
    private val serviceDetailsViewModel: ServiceDetailsViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    lateinit var service: Service
    lateinit var cartItem: CartItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        service = requireArguments().getParcelable("service")!!
        cartItem = CartItem(
            id = System.currentTimeMillis(),
            service = mutableListOf(service),
            employees = mutableListOf()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_service_details,
                container,
                false
            )
            rootView = binding.getRoot()
            initViews()
            setData(service)
        }
        return rootView
    }

    private fun setData(service: Service) {
        Glide.with(requireContext()).load(service.image).centerCrop().into(binding.image)
        binding.tvName.text = service.name
        binding.tvPrice.text = "$" + service.price
    }

    private fun initViews() {
        binding.rvEmployees.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        serviceDetailsViewModel.getAllEmployees()
        serviceDetailsViewModel.getEmployeesListData().observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        showProgress(true)
                    }
                    Status.SUCCESS -> {
                        showProgress(false)
                        setData(it.data)
                    }
                    Status.ERROR -> {
                        showProgress(true)
                    }
                }
            }
        }

        binding.btnAddCart.setOnClickListener {
            // findNavController().navigate(R.id.action_serviceDetailFragment_to_cartListFragment)
            cartViewModel.loadServices(CartDatabase.getDatabase(requireContext()).wordDao())
            cartViewModel.getCartListData().observe(viewLifecycleOwner) {
                cartItems = it

                cartItems?.let {
                    var isFound = false;
                    for (ci in cartItems!!) {
                        if (ci.employees.containsAll(cartItem.employees) && cartItem.employees.containsAll(
                                ci.employees
                            )
                        ) {
                            if (!ci.service.contains(cartItem.service[0])) {
                                ci.service.add(cartItem.service[0])
                                serviceDetailsViewModel.updateCartItem(
                                    CartDatabase.getDatabase(
                                        requireContext()
                                    ).wordDao(), ci
                                )
                                isFound = true
                            }
                        }
                    }
                    if (!isFound) {
                        serviceDetailsViewModel.insertCartItem(
                            CartDatabase.getDatabase(
                                requireContext()
                            ).wordDao(), cartItem
                        )
                    }
                }
                Utils.showSnack(
                    requireActivity(),
                    service.name + "Service and selected employees added to Cart!"
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun setData(data: List<Employee>) {
        adapter = EmployeeListAdapter(context, data, this)
        binding.rvEmployees.adapter = adapter
    }

    fun showProgress(show: Boolean) {
        (requireActivity() as HomeActivity).showProgress(show)
    }

    companion object {
        private var serviceListFragment: ServiceDetailsFragment? = null
        fun newInstance(): ServiceDetailsFragment? {
            if (serviceListFragment == null) {
                serviceListFragment = ServiceDetailsFragment()
            }
            return serviceListFragment
        }
    }

    override fun onEmployeeChecked(employee: Employee, checked: Boolean) {
        if (checked) {
            if (!cartItem.employees.contains(employee)) {
                cartItem.employees.add(employee)
            }
        } else {
            if (cartItem.employees.contains(employee)) {
                cartItem.employees.remove(employee)
            }
        }
        binding.btnAddCart.isEnabled = !cartItem.employees.isEmpty()
    }


}