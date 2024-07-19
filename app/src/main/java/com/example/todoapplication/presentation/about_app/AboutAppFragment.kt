package com.example.todoapplication.presentation.about_app

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapplication.R
import com.example.todoapplication.core.util.AssetReader
import com.example.todoapplication.databinding.FragmentAboutAppBinding
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.json.expressions.ExpressionResolver

class AboutAppFragment : Fragment() {
    private var _binding: FragmentAboutAppBinding? = null
    private val binding: FragmentAboutAppBinding
        get() = _binding!!

    private lateinit var assetReader: AssetReader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assetReader = AssetReader(requireContext())

        val divJson = assetReader.read("about.json")
        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(
            baseContext = ContextThemeWrapper(requireContext(), R.style.Theme_TODOApplication),
            configuration = createDivConfiguration(),
        )

        val divView = DivViewFactory(divContext, templatesJson).createView(cardJson)
        binding.aboutAppMainBlock.addView(divView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createDivConfiguration(): DivConfiguration {
        val expressionResolver = ExpressionResolver.EMPTY

        return DivConfiguration.Builder(PicassoDivImageLoader(requireContext()))
            .actionHandler(SampleDivActionHandler(findNavController(), expressionResolver))
            .visualErrorsEnabled(true)
            .build()
    }
}