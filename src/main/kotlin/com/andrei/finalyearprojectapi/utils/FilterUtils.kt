package com.andrei.finalyearprojectapi.utils

import com.andrei.finalyearprojectapi.controllers.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

object Controllers{
    private val  controllers:MutableList<KClass<out Any>> = mutableListOf()

    fun add(controller: KClass<out Any>){
        if(controller.isSubclassOf(Controller::class)){
            controllers.add(controller)
        }
    }

    fun all() = controllers
}


 fun KAnnotatedElement.getRequestMethodWithPath(controllerPrefixPath:String):RequestMethodWithPath{
    val getAnnotation = findAnnotation<GetMapping>()
    val postAnnotation = findAnnotation<PostMapping>()
    val deleteMapping = findAnnotation<DeleteMapping>()
    val putMapping = findAnnotation<PutMapping>()
    return when{
         getAnnotation != null -> RequestMethodWithPath(RequestMethod.GET,controllerPrefixPath + getAnnotation.value.first())
         postAnnotation != null ->RequestMethodWithPath(RequestMethod.POST,controllerPrefixPath + postAnnotation.value.first())
         deleteMapping != null -> RequestMethodWithPath(RequestMethod.DELETE,controllerPrefixPath + deleteMapping.value.first())
         putMapping != null -> RequestMethodWithPath(RequestMethod.PUT,controllerPrefixPath + putMapping.value.first())
        else->  RequestMethodWithPath(RequestMethod.OPTIONS,controllerPrefixPath + "")
    }
}


 data class RequestMethodWithPath(
    val requestMethod: RequestMethod,
    val path:String
)

/**
 * Return all endpoints that from the [controllers] that have a specific annotation
 *
 */
 inline fun <reified T : Annotation>findEndpointsWithAnnotation():List<RequestMethodWithPath>{
    val routes = mutableListOf<RequestMethodWithPath>()
    Controllers.all().forEach { kClassController->
        //for some controllers a default route can be declared such as /api

        val controllerPrefixPath = kClassController.findAnnotation<RequestMapping>()?.value?.firstOrNull() ?: ""

        kClassController.members.forEach { function->
            if(function.findAnnotation<T>() != null) {
                routes.add(function.getRequestMethodWithPath(controllerPrefixPath))

            }
        }
    }
    return routes.toList()
}

/**
 * Get rid of placeholders such as {id} as they can differ per request and will mess
 * with the endpoint logic
 */
fun String.endpointPathRegex():Regex = replace("\\{.+}".toRegex(),"[0-9]+").toRegex()

inline fun <reified T:Annotation> HttpServletRequest.endpointHasAnnotation():Boolean  = findEndpointsWithAnnotation<T>().find {
            route->
        servletPath.matches(route.path.endpointPathRegex()) && method == route.requestMethod.name
    } != null
