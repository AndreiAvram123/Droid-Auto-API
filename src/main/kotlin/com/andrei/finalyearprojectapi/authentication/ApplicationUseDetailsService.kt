package com.andrei.finalyearprojectapi.authentication

import org.springframework.security.core.userdetails.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ApplicationUseDetailsService(
        private val userRepository: UserRepository
) : UserDetailsService {

    /**
     * Load the user by its "username"
     * However, the username is a term to describe that the key identifier which can
     * be phone number, email etc
     */
    override fun loadUserByUsername(username: String): UserDetails {
       val user = userRepository.findTopByUsername(username)
        user?.let{
           return User(it.username,it.password, emptyList())
        }
        throw UsernameNotFoundException(username)
    }
}
