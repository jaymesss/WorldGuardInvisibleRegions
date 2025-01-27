## Basic Spigot plugin which adds a region flag to WorldGuard to vanish players inside of a WorldGuard region.
### Tested on WorldGuard 7.0.0

**Region Flag:** invisibility (Allow/Deny)

**Permissions:**
> **invisibleregions.always-visible** - Stay visible while inside of an invisibility region (Recommended, prevents the plugin from messing with any vanish plugins you have installed. No plan of adding integrations for vanish plugins)

**Config Options:**
> **debug**: Enable this if you need support from me

> **bypass-permission**: Enable/disable the bypass permission

**For Developers:**
To check if a player is inside of an invisibility flagged region, you can check if they have the metadata key _in-invisibility-region_ via Player#hasMetadata
