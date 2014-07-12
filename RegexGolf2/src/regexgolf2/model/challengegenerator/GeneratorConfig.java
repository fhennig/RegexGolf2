package regexgolf2.model.challengegenerator;

public interface GeneratorConfig
{
	void accept(GeneratorConfigVisitor visitor);
}
