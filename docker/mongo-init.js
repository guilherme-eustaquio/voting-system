db.createUser(
        {
            user: "voting",
            pwd: "voting",
            roles: [
                {
                    role: "readWrite",
                    db: "votingsystem"
                }
            ]
        }
);