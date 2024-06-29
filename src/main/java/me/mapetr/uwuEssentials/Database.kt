package me.mapetr.uwuEssentials

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class Database {
    companion object {
        lateinit var dataSource: HikariDataSource

        fun connect() {
            val config = HikariConfig()
            if (Main().config.getBoolean("use_mysql")) {
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

        fun executeAsync(query: String, vararg params: Any) {
            Main().server.asyncScheduler.runNow(Main.instance) {
                val connection = dataSource.connection
                val statement = connection.prepareStatement(query)
                for (i in params.indices) {
                    statement.setObject(i + 1, params[i])
                }
                statement.executeUpdate()
                statement.close()
                connection.close()
            }
        }
    }
}
