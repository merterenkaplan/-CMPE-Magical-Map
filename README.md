# Magical Map
## Project Overview
In this project, students will navigate a dynamically changing enchanted map in the magical land of Oz. The goal is to efficiently reach designated magical sites while handling hidden obstacles, recalculating routes, and making strategic choices to optimize travel.

## Key Features
Graph-based shortest path algorithms: Required for efficient navigation.
Dynamic map updates: Revealing hidden obstacles and adjusting routes accordingly.
Decision-making: Selecting the best obstacle type to unlock when prompted.

## Project Scope
The land is represented as a rectangular grid of nodes, each with a type determining passability.
Travel times between nodes are floating point values.
Some obstacles are hidden and only revealed when within a defined radius.
The route must be recalculated when encountering impassable nodes.
At certain sites, the wizard will offer choices to unlock specific types of obstacles across the entire map, requiring an optimal selection strategy.

## Game Rules
Objective:

Navigate through the enchanted map to visit designated magical sites as instructed by the wizard.
Follow the shortest path while handling hidden obstacles and making strategic decisions.
Movement Mechanics:

The land is a rectangular grid, where each node (x, y coordinate) has a specific type affecting movement.
Movement is only allowed between adjacent nodes.
Travel time between nodes is a floating-point number (0 to 99999 with six decimal places).
Node Types & Visibility:

Type 0: Freely passable nodes.
Type 1: Always impassable; initially visible.
Type ≥ 2: Hidden obstacles that seem passable but are revealed as impassable when close enough.
Line of Sight & Route Recalculation:

The radius of visibility reveals unknown node types as you move.
If an obstacle makes the current path infeasible, the route must be recalculated immediately.
Paths are deterministic, meaning there is always one unique fastest route at any moment.
Wizard’s Help Mechanic:

At specific objective points, the wizard may offer a choice to unlock a specific node type.
Selecting an option will convert all nodes of that type to Type 0 across the entire map.
The choice must be made based on which option enables the fastest travel to the next objective.
Input & Output Requirements:

The game will receive three input files:
Land File (grid and node types).
Travel Time File (movement costs between nodes).
Mission File (objectives, starting position, and visibility radius).
The output must include:
Every move made.
When a path becomes impassable.
When an objective is reached.
The selected number when wizard assistance is offered.

## How to Run
Compile the code:
  
  javac *.java

Run the program with input/output files:
  
  java Main <land_file> <travel_time_file> <mission_file>

## Technical Requirements
Java

File I/O Handling

Graph Algorithms (Dijkstra’s Algorithm for shortest path)

Custom Data Structures (Priority Queue, HashMap, and 2D ArrayLists)

