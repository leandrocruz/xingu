package xingu.cloud.spawner;

import java.util.ArrayList;
import java.util.List;

import xingu.utils.NameValue;

public class SpawnRequestFactory
{
	public static SpawnRequestFactory builder()
	{
		return new SpawnRequestFactory();
	}

	private SpawnRequestImpl req = new SpawnRequestImpl();

	public SpawnRequestFactory copy()
	{
		SpawnRequestFactory result = new SpawnRequestFactory();
		result.req.count       = req.count;
		result.req.region      = req.region;
		result.req.namespace   = req.namespace;
		result.req.group       = req.group;
		result.req.machineType = req.machineType;
		result.req.image       = req.image;
		result.req.idPattern   = req.idPattern;
		result.req.tag		   = req.tag;
		List<NameValue<String>> meta = req.getMeta();
		for(NameValue<String> nv : meta)
		{
			result.req.addMeta(nv.name, nv.value);
		}
		return result;
	}

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

	public SpawnRequestFactory withTag(String tag)
	{
		req.tag = tag;
		return this;
	}

	class SpawnRequestImpl 
		implements SpawnRequest
	{
		String	region; // google : zone

		String	namespace; // google: project

		String	group;

		String	idPattern;

		String	machineType;

		String	image;

		String 	tag;
		
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

		@Override
		public String getTag()
		{
			return tag;
		}
	}
}