# README on tessdata_for_3.04 dir

## dir contents

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

### traineddata

This dir includes tesseract-ocr trained data files.
