package cn.holelin.alipay.config;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "2016101000650885";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJ5rrVk6nh3kEDeIzBjKzrrjxQI4mJj+FK/8Zs+b5yHOGwNOxQW6lAku+M96mxgRly8/DNXpYDNvw1YIReuTqSYbMUzo7c4geuS63nbjTNTDd+w+8G6RBT/OF4lqwWZMbDsDVojTeXOT4/wZwnI2lul1RIO7DWRSZrCOAcOiJ8IokPkjeCpft59TF0Z3h2a1Hx1YhZLe2dOUSECuw1u5r3yQwduVAzxtFjNcjJ5Tcvn1Q05EcN2armQ9ffsECfGXEidvRAv3nDnqlRtWD/jaBanxrLPG+Gis/fJtyAQr/nRCI9iYEVgKtpxwrQUpzA4W0iVtAJjCOrSp5GtgoS9JYBAgMBAAECggEAdcXTPQJ+rQNm3PMAq3BcYnAM3JhtJ0n1LFtjeMCWcNjPuvCH/l06IIcdBXSivZE3TeCegJbOYXJ9VYzXoBprtoawLg33Hxf/XVEO9b8+YzM7lgz6QBmeM0yet6Fe2UU6J/h+tIsetg1SZ5sPpLFuot+lQIeSEH1AQVt0I0dXGlIW7uobw0WLhp1tR4F5gjp6UBPGJFPrafhcQXGOw2eccyWjIHelyzpw9lFZz0hHEG8dJQ5DQkh2yATZngrxfUB41sZnQtlLnc7dZ89HDYnVe4HDrOClaOvNayTeESYodPaNcNDCjATLMxShj35dTUKvkNnOaEImB+CE7lQx6EjCEQKBgQDG2j5b81Yfq9C7gBTbS32JIxQQtTxcyHNdB2kxOUEEguVq8PWOr3gMyfFDAfYN+G70tC1k/8BmH/tLhv6fyF0V1NX+yBKlLhn7M082huOi0AUQKKtv/vw8pQJBoCNlrhn6eFdcUH+ezLbL9k6A9e55/h6xRELtKd+39eaD+JW1fQKBgQCxiEAVABmyVqXKOOrFN5uDVpjcNDp0eDNQIbNxm+bDZS/a8QJQrDQvJqbFkHQjU2rpEpuFaQpwqsTkpOQIngxdnKPV65D80bwU4mIXXhA29GkrcJ+muzGysfFyM1wnWlx6n3mP0ym7cWOYcCILhVqrqg9/xqkAKNkaA5TdIrb51QKBgChst56jwk9f82gaqqLGHvghDHQ1E9dc3K790bGzDPeo0JDD+XePTl1zNDvGC0pdxdizREMzBt78A28gO2UqK1pD0VkhPUEdNSTvElxoYaB4n37gdbGn6Yk+lk3XCzshgKB9lGzs5HTY8xRVtzHaTSe5tG2fDef+yj8wmzMa9vxVAoGBAKN9Rw5P3lo2o0z8Z2+hSwyDdhfr3R0fCJo1vuvT14p/p7dnlWs3eoMlhBWSdvGFQ4JKSYBtgFjLWrD5DKrG6bpXoyVAX3ADscJFzHGahTImmKeReuTvpM/cQO6bT7VJ6LpgHHGP0/GzdTWPpjqX3y0RWl6gU0g7Vds4CyIucvTVAoGADMn3xaiA3/ze+pXCzg3/VSh9AgbcxtgHQ0j84+mQHbZf2c0ZHx6tiseqOLzCS5G1mgFUHX7yOLuBndD/uFr6nztcJWOvmYyGTYIGwy1sYE1zwzda1O5Tv7NUaeS1xpo4xiaKOgr8wgSeREjiuGyvyDxaTtUi/K/Dg42xXUgX/Ic=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "https://21i6864h95.goho.co/wappay/notify_url";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "https://21i6864h95.goho.co/wappay/return_url";
    // 请求网关地址
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiea61ZOp4d5BA3iMwYys6648UCOJiY/hSv/GbPm+chzhsDTsUFupQJLvjPepsYEZcvPwzV6WAzb8NWCEXrk6kmGzFM6O3OIHrkut5240zUw3fsPvBukQU/zheJasFmTGw7A1aI03lzk+P8GcJyNpbpdUSDuw1kUmawjgHDoifCKJD5I3gqX7efUxdGd4dmtR8dWIWS3tnTlEhArsNbua98kMHblQM8bRYzXIyeU3L59UNORHDdmq5kPX37BAnxlxInb0QL95w56pUbVg/42gWp8ayzxvhorP3ybcgEK/50QiPYmBFYCraccK0FKcwOFtIlbQCYwjq0qeRrYKEvSWAQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
}