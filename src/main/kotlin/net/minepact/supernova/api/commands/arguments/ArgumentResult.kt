package net.minepact.supernova.api.commands.arguments

sealed interface ArgumentResult {
    data class Success(val arguments: CommandArguments) : ArgumentResult
    data class Failure(val message: String) : ArgumentResult
}