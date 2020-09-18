package me.weiwen.blanktopia.sql

/*
 * Maintenance - https://git.io/maintenancemode
 * Copyright (C) 2018-2020 KennyTV (https://github.com/KennyTV)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.ResultSet
import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MySQL(
        private val logger: Logger,
        hostname: String,
        port: Int,
        username: String?,
        password: String?,
        database: String,
        useSSL: Boolean
) {
    private val hikariDataSource: HikariDataSource
    suspend fun executeUpdate(
            query: String,
            vararg objects: Any?
    ): Int {
        return suspendCoroutine<Int> { cont ->
            try {
                hikariDataSource.connection.use { connection ->
                    connection.prepareStatement(query).use { preparedStatement ->
                        var current = 1
                        for (`object` in objects) {
                            preparedStatement.setObject(current, `object`)
                            current++
                        }
                        val result = preparedStatement.executeUpdate()
                        cont.resume(result)
                    }
                }
            } catch (e: SQLException) {
                logger.log(Level.SEVERE, "Error while executing update method: $query")
                e.printStackTrace()
                cont.resumeWithException(e)
            }
        }
    }

    suspend fun executeQuery(
            query: String,
            vararg objects: Any?
    ): ResultSet {
        return suspendCoroutine<ResultSet> { cont ->
            try {
                hikariDataSource.connection.use { connection ->
                    connection.prepareStatement(query).use { preparedStatement ->
                        var current = 1
                        for (`object` in objects) {
                            preparedStatement.setObject(current, `object`)
                            current++
                        }
                        val resultSet: ResultSet = preparedStatement.executeQuery()
                        cont.resume(resultSet)
                    }
                }
            } catch (e: SQLException) {
                logger.log(Level.SEVERE, "Error while executing query method: $query")
                e.printStackTrace()
                cont.resumeWithException(e)
            }
        }
    }

    fun close() {
        hikariDataSource.close()
    }

    init {
        val hikariConfig = HikariConfig()
        hikariConfig.maximumPoolSize = 10
        hikariConfig.addDataSourceProperty("serverName", hostname)
        hikariConfig.addDataSourceProperty("user", username)
        hikariConfig.addDataSourceProperty("password", password)
        var urlProperty = "jdbc:mysql://$hostname:$port/$database"
        if (!useSSL) {
            urlProperty += "?useSSL=false"
        }
        hikariConfig.addDataSourceProperty("url", urlProperty)
        hikariConfig.jdbcUrl = urlProperty
        hikariConfig.dataSourceClassName = "org.mariadb.jdbc.MariaDbDataSource"
        hikariDataSource = HikariDataSource(hikariConfig)
    }
}