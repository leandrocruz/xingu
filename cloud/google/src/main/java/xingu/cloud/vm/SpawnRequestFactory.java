package xingu.cloud.vm;

public class SpawnRequestFactory
{
	public static SpawnRequestFactory builder()
	{
		return new SpawnRequestFactory();
	}

	private SpawnRequestImpl req = new SpawnRequestImpl();

	public SpawnRequest get()
	{
		return req;
	}

	public SpawnRequestFactory withZone(String zone)
	{
		req.zone = zone;
		return this;
	}

	public SpawnRequestFactory withProject(String project)
	{
		req.project = project;
		return this;
	}

	public SpawnRequestFactory withGroup(String group)
	{
		req.group = group;
		return this;
	}

	public SpawnRequestFactory withName(String name)
	{
		req.name = name;
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

	class SpawnRequestImpl 
		implements SpawnRequest
	{
		String	zone;

		String	project;

		String	group;

		String	name;

		String	machineType;

		String	image;
		
		@Override
		public String getZone()
		{
			return zone;
		}

		@Override
		public String getProject()
		{
			return project;
		}

		@Override
		public String getGroup()
		{
			return group;
		}

		@Override
		public String getName()
		{
			return name;
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
			return 0;
		}
	}
}
