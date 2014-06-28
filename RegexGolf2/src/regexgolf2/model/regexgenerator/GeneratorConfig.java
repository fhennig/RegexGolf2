package regexgolf2.model.regexgenerator;

public interface GeneratorConfig
{
	void accept(GeneratorConfigVisitor visitor);
}
