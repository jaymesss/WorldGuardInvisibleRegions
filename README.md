## Basic Spigot plugin which adds a region flag to WorldGuard to vanish players inside of a WorldGuard region.

**Region Flag:** invisibility (Allow/Deny)

**Permissions:**
> **invisibleregions.always-visible** - Stay visible while inside of an invisibility region (Recommended, prevents the plugin from messing with any vanish plugins you have installed. No plan of adding integrations for vanish plugins)

**For Developers:**
To check if a player is inside of an invisibility flagged region, you can check if they have the metadata key _in-invisibility-region_ via Player#hasMetadata
