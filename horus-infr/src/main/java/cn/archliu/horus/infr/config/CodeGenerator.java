package cn.archliu.horus.infr.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;

/**
 * @Author: Arch
 * @Date: 2022-04-22 23:00:12
 * @Description: mybatis-plus 代码生成器
 */
public class CodeGenerator {

	/**
	 * 数据源配置
	 */
	private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig.Builder(
			"jdbc:mysql://192.168.31.106:3306/horus?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true",
			"root", "Qqxqq13579.");

	@SuppressWarnings({ "squid:S1874", "deprecation" })
	public static void main(String[] args) {
		FastAutoGenerator.create(DATA_SOURCE_CONFIG)
				// 全局配置
				.globalConfig((scanner, builder) -> builder.author(scanner.apply("请输入作者名称？"))
						.outputDir(System.getProperty("user.dir") + "/horus-infr/src/main/java").fileOverride())
				// 包配置
				.packageConfig((scanner, builder) -> builder
						.parent("cn.archliu.horus.infr.domain." + scanner.apply("请输入包名？")))
				// 策略配置
				.strategyConfig(
						(scanner, builder) -> builder
								.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
								.controllerBuilder().enableRestStyle()
								.enableHyphenStyle()
								.entityBuilder()
								.enableLombok()
								.enableChainModel()
								.build())
				/*
				 * 模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
				 * 
				 * .templateEngine(new FreemarkerTemplateEngine())
				 */
				.templateEngine(new BeetlTemplateEngine())
				// 不生成 controller、service
				.templateConfig((scanner, builder) -> builder.controller(null).service(null)
						.serviceImpl(null).build())
				.execute();
	}

	// 处理 all 情况
	protected static List<String> getTables(String tables) {
		return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
	}

}
