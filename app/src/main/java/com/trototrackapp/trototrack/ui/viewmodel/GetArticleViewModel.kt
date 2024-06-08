package com.trototrackapp.trototrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.trototrackapp.trototrack.data.repository.ArticleRepository
import com.trototrackapp.trototrack.data.repository.ReportRepository

class GetArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    fun getArticle() =
        articleRepository.getArticle()
}