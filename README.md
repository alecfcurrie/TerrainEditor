# Personal Project: Map Creator

For my personal project, I would like to create a program that allows users to open, edit, and save grid-based 2D maps.
This project would be used by me, and perhaps others, who want to plan out **simple** 2D maps for overhead strategy games. I've 
modded the GameBoy Advance Fire Emblem games before, and the existing tools can be daunting due to their complexity. This tool
would be much easier to use than those other tools, and ideal for the initial planning stages of a strategy game level.

Additionally, I have not found a tool that allows it's user to edit both terrain terrainTile and unit positions on the terrain at the same time easily.
I would like it if my program could do this as well.

## User Stories

- As a user, I want to create a new terrain, and add it to my terrain collection.
- As a user, I want to be able to load, edit, and save 2D terrain.
- As a user, I want to be able to resize maps without the program resetting all the terrain tiles.
- As a user, I want to be able to add player, ally and enemy characters on the terrain.
- As a user, I want to have the option to save my maps to file upon quitting the application
- As a user, I want the application to automatically load my terrain list on startup.

*NOTE:* The GUI saves and loads Terrain objects directly, as opposed to the console which saves a list of Terrain.

# Instructions for Grader

- You can generate the first required event related to adding Units to a Terrain by first clicking on the add unit button 
(the button that has a stickfigure and a plus button), then using the dialog that appears to set the unit's battle class and
faction, then clicking any unoccupied on the terrain to place them. You can confirm that the program added the unit successfully by looking
at the terrain render on the main GUI window to see their faction (Blue is player, Red is enemy, Green is ally) and position, or by using the inspect unit button
(the button with a stick figure and a magnifying glass) to view and edit their battle class and faction (The dialog defaults to selecting the unit's current
battle class and faction).
- You can generate the second required event related to adding Units to a Terrain by either clicking on a new unoccupied the terrain after adding 
the first unit, or by again clicking the add unit button, specifying (and optionally changing) the battle class and faction using the dialog, 
then clicking on an unoccupied space on the terrain.
- The visual component of my project is the terrain render, on the main GUI window. The render displays all the terrain tiles of
the current terrain, as well as all the units on the current terrain. It updates anytime there is a modification to the terrain, visible if 
you add a unit (as described above), use Edit -> Resize to resize the map, or select a terrain type from the button panel, then click on a terrain tile on the map
to set the given tile to the terrain type you selected.
- You can save the state of my application by clicking file in the menu bar, then save, then saving the file using the file chooser.
- You can reload the state of my application by clicking file in the menu bar, then load, then selecting the map you wish to load. Terrain name, all terrain tiles, as well as unit
faction, battle class, and position are all be preserved.

# Phase 4: Task 2

Example Event Log:

Fri Dec 02 18:35:48 PST 2022
Set all terrain to plain
Fri Dec 02 18:35:48 PST 2022
Instantiated new terrain with a width of 20, a height of 15, and name StartupMap
Fri Dec 02 18:35:55 PST 2022
Set terrain at (8,12) to WATER
Fri Dec 02 18:35:55 PST 2022
Set terrain at (8,13) to WATER
Fri Dec 02 18:35:56 PST 2022
Set terrain at (8,14) to WATER
Fri Dec 02 18:36:05 PST 2022
Set all terrain to plain
Fri Dec 02 18:36:05 PST 2022
Resized map to 15 x 10
Fri Dec 02 18:36:13 PST 2022
Renamed map to Prologue
Fri Dec 02 18:36:19 PST 2022
Instantiated a new unit of faction Enemy, battle class Myrmidon, and position (5,5)
Fri Dec 02 18:36:19 PST 2022
Added unit at (5,5)
Fri Dec 02 18:36:20 PST 2022
Instantiated a new unit of faction Enemy, battle class Myrmidon, and position (7,7)
Fri Dec 02 18:36:20 PST 2022
Added unit at (7,7)
Fri Dec 02 18:36:21 PST 2022
Instantiated a new unit of faction Enemy, battle class Myrmidon, and position (7,6)
Fri Dec 02 18:36:21 PST 2022
Added unit at (7,6)
Fri Dec 02 18:36:31 PST 2022
Changed faction of unit at (7,6) to Ally
Fri Dec 02 18:36:31 PST 2022
Changed battle class of unit at (7,6) to Archer
Fri Dec 02 18:36:34 PST 2022
Removed unit at (7,7)
Fri Dec 02 18:36:36 PST 2022
Set terrain at (7,7) to FOREST
Fri Dec 02 18:36:39 PST 2022
Set terrain at (6,7) to WALL
Fri Dec 02 18:36:39 PST 2022
Set terrain at (6,8) to WALL

# Phase 4: Task 3

I'm overall quite pleased with my project design. There's only a single bidirectional relationship (between TerrainPanel and TerrainEditorFrame),
and there's no methods that have to change a field in each class. Instead, the TerrainPanel gets all the images it needs to render
the terrain and the terrain itself from its owner TerrainEditorFrame, and delegates all the clicks on the map to the TerrainEditorFrame.
Meanwhile, the TerrainEditorFrame only initializes the TerrainPanel, adds it to the frame, and resizes the panel when necessary. No fields are altered
in two places, and thus the association, although bi-directional, is pretty minimal.

If I had the time to refactor the program, I would like to create an abstract class called CancellableDialog, a class that extends JDialog, and that would
be the superclass of TerrainResizeDialog and UnitEditorDialog. The reason for this is that there is a lot of duplicated code between these two classes, mainly regarding
the OK and Cancel buttons, and how they handle input from them.

I would also have liked to have the UnitList class to implement the Writable interface. I do write UnitLists in the JSON files my program creates. However, the 
Writable interface requires the toJson() method to return a JSONObject, instead of a JSONArray. Thus, it would likely be difficult to refactor the class without
breaking all preexisting saves. An alternative solution would be to completely remove the UnitList class, and just delegate the task of transforming the
unit list to JSONArray to its owner Terrain object, which already implements Writeable.