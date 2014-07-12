package regexgolf2.model.challengegenerator;

import regexgolf2.model.challengegenerator.Generator.EmptyConfig;

public interface GeneratorConfigVisitor
{
	void visit(EmptyConfig emptyConfig);

	void visit(RandomGeneratorConfig randomGeneratorConfig);
}
