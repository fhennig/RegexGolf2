package regexgolf2.model.regexgenerator;

import regexgolf2.model.regexgenerator.AbstractGenerator.EmptyConfig;

public interface GeneratorConfigVisitor
{
	void visit(EmptyConfig emptyConfig);
}
