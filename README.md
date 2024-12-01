# SimpleCloud v2 to LuckPerms Migration Tool

This tool allows for seamless migration of permission data from the SimpleCloud v2 Permission System to LuckPerms. Designed for server administrators, it simplifies the transition to LuckPerms, a modern and robust permission management system.

## Features

- **Seamless Data Transfer:** Migrates users, roles, and permissions from SimpleCloud v2 to LuckPerms.
- **Straightforward Process:** Minimal setup required for a smooth migration.
- **Safe and Reliable:** Ensures data integrity during the migration.

---

## How to Perform the Migration

### Prerequisites

1. **Set the Minecraft server to maintenance mode:**  
   Ensure no players can join or play on the server during the migration.

2. **Perform the migration on a sub-server:**  
   Do not execute the migration on the Proxy server. Use a sub-server, such as the Lobby server.

3. **Install and configure LuckPerms:**  
   Ensure LuckPerms is properly installed and set up on the selected sub-server.

---

### Step-by-Step Guide

1. **Prepare the server:**  
   - Activate maintenance mode on the server.
   - Ensure LuckPerms is installed and configured correctly.

2. **Restart the server:**  
   - Restart the sub-server after installing LuckPerms to apply all changes.

3. **Start the migration:**  
   - Execute the command `/migrate` in the console or as a player.

4. **Complete the migration:**  
   - Wait for all permission data to be successfully transferred.
   - Verify that the migration process completed without errors.

---


## Contributing

Contributions are welcome! If you have ideas for improvements, bug fixes, or new features, feel free to open an issue or submit a pull request.

---

## License

This project is licensed under the [Apache 2](LICENSE).
