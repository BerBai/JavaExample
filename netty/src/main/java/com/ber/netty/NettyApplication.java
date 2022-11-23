package com.ber.netty;

//import com.ber.netty.config.EnableNettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableNettyServer
public class NettyApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }

}
