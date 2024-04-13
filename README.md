# ms-file
The microsservice responsible for the file management of the Beta+ project.

---

### How it works


The microservice will save the files at the directory "uploads", so it will save
files physically at the machine. After that, the micro will save the File Entity at
the database containing information of the physical file saved at the local machine.