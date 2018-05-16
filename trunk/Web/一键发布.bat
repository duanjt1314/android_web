::发布，只会发布代码文件，不发布公共文件 copy复制文件 xcopy复制文件夹
copy index.html ..\..\LifeManager\assets\index.html
copy login.html ..\..\LifeManager\assets\login.html

xcopy system ..\..\LifeManager\assets\system /s/i/y
xcopy life ..\..\LifeManager\assets\life /s/i/y
xcopy script ..\..\LifeManager\assets\script /s/i/y
xcopy bank ..\..\LifeManager\assets\bank /s/i/y
xcopy income ..\..\LifeManager\assets\income /s/i/y

::删除不需要的文件
del ..\..\LifeManager\assets\script\contact.js /q