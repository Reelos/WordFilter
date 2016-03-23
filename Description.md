#WordFilter-Plugin Description
WordFilter is a Plugin to censor swear words and/or other words.
It also is able to show pre- & suffix provided by PermissionsEx.

##**1. Dependecies**
* **PermissionsEx** is needed for Prefix and Suffix configuration.

* **MySQL-Database** is needed to save the Blacklist


##**2. MySQL Connection**
This Plugin creates a ini File inside of the Plugins folder when first loaded.
In this file, you can set the MySQL Connetion configuration.
The File is Named WordFilter.ini
The Basic Configuration for the MySQL Server Connection is

	Server: localhost:3306 
	Username: root
	Password: <none>
	Database: WordFilter

After this, the plugin will automaticly create the given Schema,
create a table named "BadWords" and fill it with some basic Words.

##**3. Commands**
There are 4 commands prvoided with this plugin:
* /addbadword - to add a word to the blacklist
	* usage: /addbadword <word> <for_all>
		* the <for_all> flag can only be set with a special permission
	
* /changewordflag - to change the flag of a blacklisted word
	* usage: /changewordflag <word> <for_all>
	
* /removebadword - to remove a word from the blacklist
	* usage: /removebadword <word>
	
* /getbadwords - shows a list of blacklisted words

##**4. Permissions**
There 4 permissions for this plugin.
They all start with 'wordfilter.'
* canSwear - enables the user to use 0 flaged blacklisted words.

* canAdd - enables the user to add words to the blacklist.
	* user can use /addbadword <word>
	
* canFlag - enables the user to change the flag of blacklisted words, also to set them in the fist place.
	* user can use /changewordflag <word> <for_all>
	* user can use /addbadword <word> <for_all>
	
* canRemove - enables the user to remove words from the blacklist.
	* user can use /removebadword <word>
	
* canList - enables the user to read the blacklist.
	* user can use /getbadwords