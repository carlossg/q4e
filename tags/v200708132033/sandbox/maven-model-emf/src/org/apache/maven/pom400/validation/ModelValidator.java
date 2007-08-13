/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.maven.pom400.validation;

import org.apache.maven.pom400.Build;
import org.apache.maven.pom400.CiManagement;
import org.apache.maven.pom400.ContributorsType;
import org.apache.maven.pom400.DependenciesType;
import org.apache.maven.pom400.DependencyManagement;
import org.apache.maven.pom400.DevelopersType;
import org.apache.maven.pom400.DistributionManagement;
import org.apache.maven.pom400.IssueManagement;
import org.apache.maven.pom400.LicensesType;
import org.apache.maven.pom400.MailingListsType;
import org.apache.maven.pom400.ModulesType;
import org.apache.maven.pom400.Organization;
import org.apache.maven.pom400.Parent;
import org.apache.maven.pom400.PluginRepositoriesType;
import org.apache.maven.pom400.Prerequisites;
import org.apache.maven.pom400.ProfilesType;
import org.apache.maven.pom400.PropertiesType2;
import org.apache.maven.pom400.Reporting;
import org.apache.maven.pom400.ReportsType1;
import org.apache.maven.pom400.RepositoriesType;
import org.apache.maven.pom400.Scm;

/**
 * A sample validator interface for {@link org.apache.maven.pom400.Model}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface ModelValidator {
	boolean validate();

	boolean validateParent(Parent value);
	boolean validateModelVersion(String value);
	boolean validateGroupId(String value);
	boolean validateArtifactId(String value);
	boolean validatePackaging(String value);
	boolean validateName(String value);
	boolean validateVersion(String value);
	boolean validateDescription(String value);
	boolean validateUrl(String value);
	boolean validatePrerequisites(Prerequisites value);
	boolean validateIssueManagement(IssueManagement value);
	boolean validateCiManagement(CiManagement value);
	boolean validateInceptionYear(String value);
	boolean validateMailingLists(MailingListsType value);
	boolean validateDevelopers(DevelopersType value);
	boolean validateContributors(ContributorsType value);
	boolean validateLicenses(LicensesType value);
	boolean validateScm(Scm value);
	boolean validateOrganization(Organization value);
	boolean validateBuild(Build value);
	boolean validateProfiles(ProfilesType value);
	boolean validateModules(ModulesType value);
	boolean validateRepositories(RepositoriesType value);
	boolean validatePluginRepositories(PluginRepositoriesType value);
	boolean validateDependencies(DependenciesType value);
	boolean validateReports(ReportsType1 value);
	boolean validateReporting(Reporting value);
	boolean validateDependencyManagement(DependencyManagement value);
	boolean validateDistributionManagement(DistributionManagement value);
	boolean validateProperties(PropertiesType2 value);
}
