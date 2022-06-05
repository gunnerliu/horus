import groovy.sql.Sql;

def checkAlive() {
    def sql = new Sql(master);
    def alertInfo = [];
    // 捞出心跳超过一分钟、还是在线的服务
    sql.eachRow("select * from horus_application_info where online_state = 'ONLINE' and last_heat_time < DATE_SUB(NOW(),INTERVAL 1 MINUTE)") {
        row -> {
            alertInfo.add([row['application_name'], row['app_ip'], row['app_port']])
        }
    }
    // 将心跳停止一分钟的服务进行下线
    sql.executeUpdate "update horus_application_info set online_state = 'OFFLINE' where online_state = 'ONLINE' and last_heat_time < DATE_SUB(NOW(),INTERVAL 1 MINUTE)"
    
    if (alertInfo.size() > 0) {
        log.info("下线服务： {}", alertInfo);
    } else {
        log.info("无服务下线！");
    }
}

checkAlive();