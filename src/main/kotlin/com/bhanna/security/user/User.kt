package com.bhanna.security.user

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(
    name = "_user",
    uniqueConstraints = [
        UniqueConstraint(name = "unique_user_email", columnNames = arrayOf("email"))
    ]
)
class User(
    private val firstName: String,
    private val lastName: String,
    private val email: String,
    private val password: String,

    @Enumerated(EnumType.STRING)
    private val role: Role,

    @Id
    @GeneratedValue
    private val id: Int? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}
