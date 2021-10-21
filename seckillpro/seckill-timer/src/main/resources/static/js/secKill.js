/**
 * 模块化的JS,它是一种类似面向对象的JS
 * secKillObj 就是一个Json对象,这个对象中可以有若干个属性,这些属性可以是基本类型或Json对象类型或函数类型
 * url 是一个Json对象格式,这个对象中可以有若干个属性,这些属性可以是基本类型或Json对象类型或函数类型,我们这里
 *     会定义若干个函数,用于返回具体的url地址路径,起到了地址路径重用的特点
 * fun 是一个Json对象格式,这个对象中可以有若干个属性,这些属性可以是基本类型或Json对象类型或函数类型,我们这里
 *     会定义若干个函数,用于实现秒杀的具体业务逻辑实现
 */
var secKillObj = {
    url: {
        getSysTime: function () {
            return "/getSysTime"
        },
        getRandomName: function () {
            return "/getRandomName"
        },
        secKill:function(){
            return "/secKill"
        }
    }
    ,
    fun: {
        /**
         * 秒杀的初始化函数,主要用于控制抢购按钮是否可以点击的逻辑
         * @param goodId 商品Id
         * @param startTime 活动开始时间
         * @param endTime 活动的结束时间
         * @param price  商品的价格
         */
        initSecKill: function (goodsId, startTime, endTime, price) {
            $.ajax({
                url: secKillObj.url.getSysTime(),
                type: "get",
                dataType: "json",
                success: function (data) {
                    //统一错误逻辑控制
                    if (data.code != 'OK') {
                        alert(data.msg)
                        return
                    }

                    var sysTime = data.result
                    if (sysTime < startTime) {
                        $("#secKillBtn").attr("disabled", true)
                        secKillObj.fun.secKillCountdown(goodsId, startTime, price)
                        return
                    }
                    if (sysTime > endTime) {
                        $("#secKillBtn").attr("disabled", true)
                        $("#seckillTip").html('<span style="color: red">活动已经结束</span>')
                        return
                    }
                    secKillObj.fun.doSecKill(goodsId, price)

                },
                error: function () {
                    alert("网络异常!请稍后再试!")
                }
            })
        },
        /**
         * 秒杀的倒计时函数
         * @param goodsId 商品Id
         * @param startTime 活动开始时间
         * @param price 商品价格
         */
        secKillCountdown: function (goodsId, startTime, price) {
            /**
             * 秒杀倒计时的目标时间
             */
            var killTime = new Date(startTime + 1000);
            //使用任意jQuery对象调用jQuery的倒计时函数
            //参数 1 为倒计时的目标时间
            //参数 2 为回调方法这个方法将每秒钟被调用一次用于更新页面效果
            $("#seckillTip").countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('距秒杀开始还有: %D天 %H时 %M分 %S秒');
                $("#seckillTip").html("<span style='color:red;'>" + format + "</span>");
            }).on('finish.countdown', function () {
                //倒计时结束后回调事件，已经开始秒杀，用户可以进行秒杀了，有两种方式：
                //1、刷新当前页面
                // location.reload();
                //或者2、调用秒杀开始的函数
                $("#seckillTip").html("")
                secKillObj.fun.doSecKill(goodsId, price)
            });
        },
        /**
         * 准备开始秒杀的函数,用于为抢购按钮绑定点击事件
         * @param goodsId
         * @param price
         */
        doSecKill: function (goodsId, price) {
            $("#secKillBtn").attr("disabled", false)
            $("#secKillBtn").bind("click", function () {
                //设置抢购按钮不可点击,防止用户重复购买
                //但是这里不能100%拦截所有的用户请求,只能拦截住一部分用户请求
                $("#secKillBtn").attr("disabled", true)
                $.ajax({
                    url: secKillObj.url.getRandomName(),
                    type: "get",
                    dataType: "json",
                    data: {goodsId: goodsId},
                    success: function (data) {
                        //统一错误逻辑控制
                        if (data.code != 'OK') {
                            alert(data.msg)
                            return
                        }
                        var randomName = data.result
                        secKillObj.fun.secKill(goodsId, randomName, price)
                    },
                    error: function () {
                        alert("网络异常!请稍后再试!")
                    }
                })
            })
        },
        /**
         * 秒杀函数,用于发送秒杀请求到远程服务
         * @param goodsId
         * @param randomName
         * @param price
         */
        secKill: function (goodsId, randomName, price) {
            $.ajax({
                url: secKillObj.url.secKill(),
                type: "get",
                dataType: "json",
                data: {goodsId: goodsId,randomName:randomName,price:price},
                success: function (data) {
                    //统一错误逻辑控制
                    if (data.code != 'OK') {
                        alert(data.msg)
                        return
                    }
                   alert(data.code+"    "+data.msg+"     "+data.result)
                },
                error: function () {
                    alert("网络异常!请稍后再试!")
                }
            })
        }
    }
}