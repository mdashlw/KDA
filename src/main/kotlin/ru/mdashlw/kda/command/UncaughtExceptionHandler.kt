package ru.mdashlw.kda.command

abstract class UncaughtExceptionHandler : ExceptionHandler<Throwable>(Throwable::class)
