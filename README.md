# BottomSheetPicker

[![Core](https://api.bintray.com/packages/quanturium/maven/bottomsheetpicker/images/download.svg) ](https://bintray.com/quanturium/maven/bottomsheetpicker/_latestVersion)
[![Build Status](https://travis-ci.org/quanturium/BottomSheetPicker.svg?branch=master)](https://travis-ci.org/quanturium/BottomSheetPicker)
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/quanturium/BottomSheetPicker/blob/master/LICENSE.txt)

Picker | Taking photo / video | Multi-select of items
--- | --- | ---
![Picker](https://raw.githubusercontent.com/quanturium/BottomSheetPicker/master/assets/screenshot001.png) | ![Picker](https://raw.githubusercontent.com/quanturium/BottomSheetPicker/master/assets/screenshot003.png) | ![Picker](https://raw.githubusercontent.com/quanturium/BottomSheetPicker/master/assets/screenshot003.png)

## Table of Contents
1. [Demo](#demo)
1. [Setup](#setup)
2. [Getting Started](#getting-started)
3. [License](#license)

## Demo

[![Demo Video](http://img.youtube.com/vi/KVohfD_5FFk/0.jpg)](https://www.youtube.com/embed/KVohfD_5FFk/)

## Setup

The Gradle dependency is available via jcenter.

The minimum API level supported by this library is API 19.

The easiest way to add the Material DateTime Picker library to your project is by adding it as a dependency to your build.gradle

```groovy
dependencies {
  compile 'com.quanturium.android:bottomsheetpicker:1.0.0'
}
```

## Getting Started

### Configuration

BottomSheetPicker requires some additional configurations made to your app's string and xml resources. Bottomsheetpicker uses a [FileProvider](https://developer.android.com/reference/android/support/v4/content/FileProvider.html) which makes additional configuration necessary.

 * In your manifest.xml, add the following:
 ```
 <provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="@string/file_provider_authority"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths"/>
 </provider>
```

 * In your app's strings.xml file, add a string called 'file_provider_authority' and specify a value
 ```
<string name="file_provider_authority">FILE_PROVIDER_STRING_HERE</string>
```
 
 * In your app's xml directory, add a new file called 'file_paths.xml' (If the xml directory does not exist, create it under the app's res/ directory). The contents of this file should look like:
```
 <?xml version="1.0" encoding="utf-8"?>
 <paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="external_files" path="." />
 </paths>
```

### Usage



## License
    Copyright (c) 2018 Arnaud Frugier & Steven Hong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
