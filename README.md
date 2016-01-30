# SafeGuard

*By viveleroi*

SafeGuard is a land protection plugin for Sponge-based servers.

## Installation

Place into your `mods` directory. 


## Features

- Create zones which allow only you to make block changes.
- Add/remove access for other players.
- Protects land from creepers.

## Commands

- `/sg pos` - Add a position to the buffer. Two points form the boundary of your zone.
- `/sg pos fullheight` - Expand your buffer from the map floor to map ceiling.
- `/sg zone create (name)` - Create a new zone with the selected region.
- `/sg zone allow (user)` - Add a user to your zone.
- `/sg zone deny (user)` - Revoke access to your zone for a user.
- `/sg zone flag (flag) (true|false)` - Toggle a specific flag.
- `/sg zone delete` - Delete a zone.

## Flags

- `block.change` - Block changes (place, break, etc) are allowed.
- `block.use` - Containers may be opened.
- `damage.player` - Players may damage a player in their zone.
- `item.drop` - Players may drop items.
- `spawn.monster` - Monsters may spawn or enter the zone.

## Permissions

- `safeguard.create` - Create and manage your zone.
- `safeguard.admin` - Staff-level zone override.

You can limit zone sizes by defining a `zoneLengthLimit: (int)` option in your permissions file.

## IRC

Please follow development on `irc.esper.net` in `#prism`

## Credits

- viveleroi (*Creator, Lead Dev*)
