package xingu.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.project.MavenProject;

public class MavenHelper
{
	private final MavenProject project;
	private final ArtifactRepository localRepository;
	
	public MavenHelper(MavenProject project, ArtifactRepository localRepository)
	{
		this.project = project;
		this.localRepository = localRepository;
	}

	public File getMainArtifactAsFile()
	{
		Artifact artifact = project.getArtifact();
		String path = localRepository.pathOf(artifact);
		String base = localRepository.getBasedir();
		return new File (base + File.separator + path);
	}

	public List<File> getRuntimeDependenciesAsFiles()
	{
        List<Artifact> artifacts = project.getRuntimeArtifacts();
        List<File> result = new ArrayList<File>();
        for (Artifact artifact : artifacts)
        {
            String path = localRepository.pathOf(artifact);
            String base = localRepository.getBasedir();
			result.add(new File(base + File.separator + path));
        }

		return result;
	}
}
