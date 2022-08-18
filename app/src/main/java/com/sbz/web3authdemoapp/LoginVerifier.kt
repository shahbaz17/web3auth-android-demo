package com.sbz.web3authdemoapp

import com.web3auth.core.types.Provider

data class LoginVerifier (
    val name : String,
    val loginProvider : Provider
)