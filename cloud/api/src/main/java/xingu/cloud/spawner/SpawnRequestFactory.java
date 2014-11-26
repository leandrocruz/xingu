package xingu.cloud.spawner;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.utils.NameValue;

public class SpawnRequestFactory
{
	public static SpawnRequestFactory builder()
	{
		return new SpawnRequestFactory();
	}

	private SpawnRequestImpl req = new SpawnRequestImpl();

	public SpawnRequest get(int count)
	{
		req.count = count;
		return req;
	}

	public SpawnRequestFactory withRegion(String region)
	{
		req.region = region;
		return this;
	}

	public SpawnRequestFactory withNamespace(String ns)
	{
		req.namespace = ns;
		return this;
	}

	public SpawnRequestFactory withGroup(String group)
	{
		req.group = group;
		return this;
	}
	
	public SpawnRequestFactory withNamePattern(String pattern)
	{
		req.namePattern = pattern;
		return this;
	}

	public SpawnRequestFactory withIdPattern(String pattern)
	{
		req.idPattern = pattern;
		return this;
	}

	public SpawnRequestFactory withMachineType(String machineType)
	{
		req.machineType = machineType;
		return this;
	}
	
	public SpawnRequestFactory withImage(String image)
	{
		req.image = image;
		return this;
	}

	public SpawnRequestFactory withMeta(String name, String value)
	{
		req.addMeta(name, value);
		return this;
	}

	class SpawnRequestImpl 
		implements SpawnRequest
	{
		String	region; // google : zone

		String	namespace; // google: project

		String	group;

		String	namePattern;
		
		String	idPattern;

		String	machineType;

		String	image;

		int		count;
		
		List<NameValue<String>> meta = new ArrayList<>();

		public void addMeta(String name, String value)
		{
			meta.add(new NameValue<String>(name, value));
		}

		@Override
		public List<NameValue<String>> getMeta()
		{
			return meta;
		}

		@Override
		public String getRegion()
		{
			return region;
		}

		@Override
		public String getNamespace()
		{
			return namespace;
		}

		@Override
		public String getGroup()
		{
			return group;
		}

		@Override
		public String getNamePattern()
		{
			return namePattern;
		}

		@Override
		public String getMachineType()
		{
			return machineType;
		}

		@Override
		public String getImage()
		{
			return image;
		}

		@Override
		public int getCount()
		{
			return count;
		}

		@Override
		public String getIdPattern()
		{
			return idPattern;
		}
	}
}