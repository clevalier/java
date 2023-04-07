/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.csj.in.spring.bean.lifecycle;

import org.csj.in.spring.ioc.overview.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

/**
 * Bean 初始化生命周期示例
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since
 */
public class BeanInitializationLifecycleDemo {

    public static void main(String[] args) {

       // executeBeanFactory();
        int[] weight = {1,3,4};
        int[] value = {15,20,30};
        int bagSize = 4;
        bagProblem(weight,value,bagSize);

    }

    private static void executeBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 添加 BeanPostProcessor 实现 MyInstantiationAwareBeanPostProcessor
        beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        // 添加 CommonAnnotationBeanPostProcessor 解决 @PostConstruct
        beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        String[] locations = {"META-INF/dependency-lookup-context.xml", "META-INF/bean-constructor-dependency-injection.xml"};
        int beanNumbers = beanDefinitionReader.loadBeanDefinitions(locations);
        System.out.println("已加载 BeanDefinition 数量：" + beanNumbers);
        // 显示地执行 preInstantiateSingletons()
        // SmartInitializingSingleton 通常在 Spring ApplicationContext 场景使用
        // preInstantiateSingletons 将已注册的 BeanDefinition 初始化成 Spring Bean
        beanFactory.preInstantiateSingletons();

        // 通过 Bean Id 和类型进行依赖查找
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);

        User superUser = beanFactory.getBean("superUser", User.class);
        System.out.println(superUser);

        // 构造器注入按照类型注入，resolveDependency
        UserHolder userHolder = beanFactory.getBean("userHolder", UserHolder.class);

        System.out.println(userHolder);

    }




        /**
         * 动态规划获得结果
         * @param weight  物品的重量
         * @param value   物品的价值
         * @param bagSize 背包的容量
         */
        public static void testWeightBagProblem(int[] weight, int[] value, int bagSize){

            // 创建dp数组
            int goods = weight.length;  // 获取物品的数量
            int[][] dp = new int[goods][bagSize + 1];

            // 初始化dp数组
            // 创建数组后，其中默认的值就是0
            for (int j = weight[0]; j <= bagSize; j++) {
                dp[0][j] = value[0];
            }

            // 填充dp数组
            for (int i = 1; i < weight.length; i++) {
                for (int j = 1; j <= bagSize; j++) {
                    if (j < weight[i]) {
                        /**
                         * 当前背包的容量都没有当前物品i大的时候，是不放物品i的
                         * 那么前i-1个物品能放下的最大价值就是当前情况的最大价值
                         */
                        dp[i][j] = dp[i-1][j];
                    } else {
                        /**
                         * 当前背包的容量可以放下物品i
                         * 那么此时分两种情况：
                         *    1、不放物品i
                         *    2、放物品i
                         * 比较这两种情况下，哪种背包中物品的最大价值最大
                         */
                        dp[i][j] = Math.max(dp[i-1][j] , dp[i-1][j-weight[i]] + value[i]);
                    }
                }
            }

            // 打印dp数组
            for (int i = 0; i < goods; i++) {
                for (int j = 0; j <= bagSize; j++) {
                    System.out.print(dp[i][j] + "\t");
                }
                System.out.println("\n");
            }
        }


        public static void bagProblem(int[] weight,int[] value,int bagSize){
            int goodsNumber = weight.length;
            int[][] dp = new int[goodsNumber][bagSize+1];
            //初始化
            for(int j=weight[0];j<=bagSize;j++){
                dp[0][j] = value[0];
            }
            for (int i=1;i<weight.length;i++){
                for(int j=1;j<=bagSize;j++){
                    if(j<weight[i]){
                        //如果背包放不下，还是上一个的东西
                        dp[i][j]=dp[i-1][j];
                    }else {
                        //如果背包可以放得下，那就要考虑放不放该物品
                        //不放该物品，还是dp[i-1][j]
                        //放置了该物品，（背包容量-物品i的容量）所能放的最大价值+物品i的价值
                        dp[i][j]= Math.max(dp[i-1][j-weight[i]]+value[i],dp[i-1][j]);
                    }
                }
            }
            // 打印dp数组
            for (int i = 0; i < goodsNumber; i++) {
                for (int j = 0; j <= bagSize; j++) {
                    System.out.print(dp[i][j] + "\t");
                }
                System.out.println("\n");
            }

        }


}

