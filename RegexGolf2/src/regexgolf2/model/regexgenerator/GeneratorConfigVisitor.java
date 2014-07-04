package regexgolf2.model.regexgenerator;

import regexgolf2.model.regexgenerator.Generator.EmptyConfig;

public interface GeneratorConfigVisitor
{
	void visit(EmptyConfig emptyConfig);

	void visit(RandomGeneratorConfig randomGeneratorConfig);
}
