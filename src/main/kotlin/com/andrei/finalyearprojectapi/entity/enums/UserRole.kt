package com.andrei.finalyearprojectapi.entity.enums

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class UserRole(val value:String) {
        USER("USER"),
        ADMIN("ADMIN"),
        UNKNOWN("UNKNOWN")
    }

@Converter(autoApply = true)
class UserRoleConverter : AttributeConverter<UserRole, String>{
    override fun convertToDatabaseColumn(attribute: UserRole?): String {
          attribute?.let {
              return it.value
          }
        return ""
    }

    override fun convertToEntityAttribute(dbData: String?): UserRole {
          dbData?.let{
              return UserRole.valueOf(it)
          }
        return UserRole.UNKNOWN
    }
}