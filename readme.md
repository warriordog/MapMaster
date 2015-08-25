Map Master: A tool for creating minecraft maps
---
MapMaster is a lightweight tool for merging and annotating map tiles created by various mapping tools and mods.  It is currently in very early development, but supported importing JournemyMap and MapWriter tiles and can add shapes and text to predefined locations on the finished map.  MapMaster is controlled through configuration scripts, so there is no need to edit complex command line arguments to use it.  MM uses the pngj library for exporting the completed map, so arbitrarily large maps can be created with memory impact reduced to the size of a single strip of regions.

**Features:**
* Support for multiple tile formats (most in development)
* Minimal memory usage
* Configuration scripts
* Annotations - colored shaped and text (w/ font choice)
* Automatically pick terrain from most-recent data
* Fairly fast, about 90 seconds for a 5-source 20,000 x 20,000 block map w/ annotations
* Able to render binary data storage from VoxelMap (raw world saves coming soon)

**Weaknesses:**
* Many tile formats are not implemented
* Formats based on storage sizes other than standard MC regions don't work well
* Configuration scripts are not well documented
* Colors must be specified by a packed 4-byte ARGB integer value
* Different mappers have different renderers, which cause abrupt edges
* Single-threaded
* Bad big-O efficiency, compexity increases exponentially by number of tile sources

**Supported Tile Formats:**
* JourneyMap
* MapWriter
* VoxelMap (data mode)
* VoxelMap (tile mode)
* Anything that stores tiles in an x,y.png or x.y.png (if placed in a matching JM / MW directory tree)