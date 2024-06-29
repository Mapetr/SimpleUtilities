package me.mapetr.uwuEssentials

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class Database {
    companion object {
        lateinit var dataSource: HikariDataSource

        fun connect() {
            val config = HikariConfig()
            if (Main().config.getBoolean("mysql")) {
                config.jdbcUrl = "jdbc:mysql://${Main().config.getString("mysql_host")}:${Main().config.getString("mysql_port")}/${Main().config.getString("mysql_database")}"
                config.username = Main().config.getString("mysql_username")
                config.password = Main().config.getString("mysql_password")
                config.dataSourceProperties["cachePrepStmts"] = "true"
                config.dataSourceProperties["prepStmtCacheSize"] = "250"
            } else {
                config.jdbcUrl = "jdbc:sqlite:${Main().dataFolder}/uwu.db"
            }
            dataSource = HikariDataSource(config)
        }
    }
}
