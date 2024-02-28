# SecureBox
## Litle secure space, easy to use.

[![](https://jitpack.io/v/mithrundeal/SecureBox.svg)](https://jitpack.io/#mithrundeal/SecureBox)

##### SecureBox is Android Keystore-powered library that provides a reliable and secure space in Android projects.


## Features

- Android Keystore container powered
- Uses RealmDb for Database
- Easy to secure password
- Generic secure save function for custom objects
- Basic random pasword genererator come build-in


It is aimed to provide a solution for people who want to quickly secure their sensitive information.

> SecureBox is an Android library written in Kotlin that provides a convenient and secure way to encrypt and decrypt sensitive data within your Android applications. It leverages modern cryptographic techniques to ensure data privacy and integrity.


![Screenshot](https://github.com/mithrundeal/SecureBox/blob/master/SecureBox/docs/sb-diagram.png)

## Installation

Dont forget to ad Jitpack to root build.gradle
```kotlin
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
Add to project build.gradle
```kotlin
	dependencies {
	        implementation 'io.realm.kotlin:library-base:1.13.0'
	        implementation 'com.google.code.gson:gson:2.10.1'
	        implementation 'com.github.mithrundeal:SecureBox:Tag'
	}
```

## Usage

Get global instance of SecureBox like below;
```kotlin
 val secureBox = SecureBox.getInstance()
```

You can check your device is hardware-backed TEE
```kotlin
 val tee = secureBox.isHardwareBackedTEE()
```

You can save and get your password like below;
```kotlin
secureBox.savePassword("data1", "data1")
var pass = secureBox.getPassword("data1")
```

You can save and get custom data with SecureBox. Its and generic function so you can save anything you want. 
```kotlin
class DEVCLASS(var data: String, var data0: String) {}

val dummyData = DEVCLASS("asdasd", "123123")
secureBox.saveData("custom-data", dummyData)

val result = secureBox.getData("custom-data", DEVCLASS::class.java)
```

There is an util, which is you can generate random password with given parameters, like below;
```kotlin
val passwordGenerator = PasswordGeneratorBuilder().apply {
    enableUppercase(true)
    enableLowerCase(true)
    enableNumbers(true)
    enableSymbols(true)
    setMaxLength(16)
}.build()

    val psw = passwordGenerator.generatePassword()
```

## Development

First version released as an alpha. SecureBox is an open-source project. Contribution is very welcomed!

## Compatibility

**minSdk** version is **31**.

## Related Resources

If you want to read further about the technologies used in SecureBox, you can review the links.


 Keystore | [https://developer.android.com/privacy-and-security/keystore][PlDb]
 RealmDb | [https://www.mongodb.com/docs/realm/sdk/kotlin/][PlGh]
 Orm benchmark | [https://github.com/AlexeyZatsepin/Android-ORM-benchmark?tab=readme-ov-file#results-table][PlGd]
 Gson | [https://github.com/google/gson][PlGd]

## Disclaimer
You have your own ris while using this library. Security is a very broad scope and SecureBox does not guarantee the security of your data.

## License

