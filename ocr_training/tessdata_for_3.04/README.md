# README on tessdata_for_3.04 dir

## dir contents

### fonts
This dir includes the font files used for training data.


### langdata
This dir includes tesseract-ocr langdata, imported from tesseract-ocr/langdata repo in github with following `git subtree` command.

```
$ git remote add -f tesseract-ocr/langdata https://github.com/tesseract-ocr/langdata.git
$ git subtree add --prefix ocr_training/tessdata_for_3.04/langdata tesseract-ocr/langdata master --squash
```

Use following `git subtree` command in GoIV project *root* dir when sync and update this dir from tesseract-ocr/langdata repo.

```
$ git subtree pull --prefix ocr_training/tessdata_for_3.04/langdata tesseract-ocr/langdata master --squash
```


### out
This dir temporarily is used by the scripts to generate tesseract-ocr trained data file.


### scripts
This dir includes the script files to generate tesseract-ocr trained data file.
These are imported and customized from training util scripts in tesseract-ocr v3.04.01-4(Ubuntu 16.04.3 package).


### traineddata
This dir includes tesseract-ocr trained data files.
