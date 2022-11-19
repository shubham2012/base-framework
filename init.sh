#This file should be run once when you clone the repo to enable the git pre-commit hook to do google formate all the staged files and add again as staged
cat pre-commit >> .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit
