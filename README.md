# git-social
a social media based on git.

[中文说明](./README.cn.md)

# Instructions

## Initialize software
To use this software, you first need to have your own publicly available git repository, as well as an account and password to log in to the repository. The git repository is designed to store your social data, such as updates, comments, and likes. The account and password are for the software to successfully push data to your git repository.

## Post updates
After publishing the dynamic, the software will automatically push the data to the remote git repository. But comments and likes need to be manually pushed by clicking the push button.

## Pay attention to others
Add the follower's Git address to the following list

# IDEA

## Q&A

- Q: How social data is saved. 

A: Everyone's data is their private property and should be kept and maintained by themselves. So all personal data, such as updates, comments, likes, videos and images, and other files. They are all responsible for storage themselves. Corresponding to this project, personal data is saved in their own git warehouse.

- Q: How does the interaction between users proceed? 

A: The dynamic posted by user A is pushed to their own git warehouse, and user B has added user A's git warehouse address for attention. User B can pull updates from User A to their local location when pulling. This project will read and pull local data, and then integrate it to display to user B. User B's comments and likes on this dynamic are all submitted and saved to their own git repository. If User A also follows User B. You can see user B's comments and likes on themselves. If you don't follow, you won't see user B's comments and likes on you. If user C follows both user A and user B, then user C can see.